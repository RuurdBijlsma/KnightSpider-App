package nl.nhl.knightspider.Communication;

import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.HashMap;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import nl.nhl.knightspider.MainActivity;
import nl.nhl.knightspider.Pages.DiagnosticsScreen;
import nl.nhl.knightspider.Pages.SpiderView;
import nl.nhl.knightspider.R;
import nl.nhl.knightspider.Utils;

/**
 * Created by bouke on 18-5-2017.
 */

public class Connection {
    private int connectionTryCount = 0;
    private int connectionCountDown = -1;
    private boolean connecting = false;
    private Snackbar retrySnackbar;
    private Timer retryTimer;
    private final String ip;
    private final int port;
    private final MainActivity activity;
    private Socket clientSocket;

    public Connection(String ip, int port, MainActivity activity) throws IOException {
        this.ip = ip;
        this.port = port;
        this.activity = activity;
        this.activity.getSpiderView().setOnServoInfoRequestedCallback((id) -> {
            send(new Message(Identifiers.GET_SERVO, String.valueOf(id)));
        });
        if (!initSocket()) {
            startRetry();
        }
        for (; ; ) {
            if (connecting) continue;
            try {
                String out = readFromServer();
                onMessage(out);
            } catch (Exception e) {
                startRetry();
            }
        }
    }

    private boolean initSocket() {
        Log.d("SOCKET", "Creating new socket");
        clientSocket = new Socket();
        try {
            clientSocket.connect(new InetSocketAddress(ip, port), 1000);
            connecting = false;
            return true;
        } catch (IOException e) {
            Log.d("SOCKET", "Socket init failed");
            return false;
        }

    }

    private void onMessage(String data) {
        Message message = Message.fromString(data);
        if (message == null) {
            return;
        }

        switch (message.getIdentifier()) {
            case Identifiers.ANGLES:

                activity.runOnUiThread(() -> {
                    SpiderView spiderView = this.activity.getSpiderView();
                    spiderView.setSpiderStanceFromJson(message.getPayload());
                });

                break;
            case Identifiers.SERVO:
                Log.d("SOCKET", message.getPayload());
                ServoReadings servoReadings = ServoReadings.fromJson(message.getPayload());
                if (servoReadings == null) {
                    return;
                }

                activity.runOnUiThread(() -> {
                    SpiderView spiderView = activity.getSpiderView();
                    spiderView.setServoId(servoReadings.getId());
                    spiderView.setAngle(servoReadings.getPosition());
                    spiderView.setLoad((int) servoReadings.getLoad());
                    spiderView.setTemp(servoReadings.getTemperature());
                    spiderView.setVoltage(servoReadings.getVoltage());
                });

                break;
            case Identifiers.SPIDER:
                SpiderInfo spiderInfo = SpiderInfo.fromJson(message.getPayload());
                if (spiderInfo == null) {
                    return;
                }
                activity.runOnUiThread(() -> {
                    DiagnosticsScreen diagnosticsScreen = this.activity.getDiagnosticsScreen();
                    diagnosticsScreen.setBattery(spiderInfo.getBattery());
                    diagnosticsScreen.setGyro(spiderInfo.getSlope());
                    diagnosticsScreen.setCpu(spiderInfo.getCpuUsage());
                    diagnosticsScreen.setTemp(spiderInfo.getCpuTemperature());
                });

                break;
            default:
                Log.d("SOCKET", "Unknown message identifier found. This is bad.");
        }
    }

    private String readFromServer() throws IOException {
        if (connecting) return null;
        String modifiedSentence;
        BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        modifiedSentence = inFromServer.readLine();
        return modifiedSentence;
    }

    private void send(Message message) {
        String stringMessage = message.toString();
        try {
            Log.d("SOCKET", "SENT: " + stringMessage);
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
            bufferedWriter.write(stringMessage);
            bufferedWriter.flush();
        } catch (IOException e) {
            startRetry();
        }
    }

    private void startRetry() {
        Log.d("SOCKET", "Retrying connection...");
        connecting = true;
        clientSocket = null;
        connectionCountDown = Utils.fibonacci(++connectionTryCount);

        retryTimer = new Timer();

        retryTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                updateCountDown();
            }
            // tick every second
        }, 1000, 1000);

        if (retrySnackbar == null) {
            retrySnackbar = Snackbar.make(activity.findViewById(R.id.content),
                    getCountDownString(),
                    Snackbar.LENGTH_INDEFINITE);

            retrySnackbar.show();
            retrySnackbar.setAction("Retry now", v -> new Thread(this::retryConnection).start());
        } else {
            activity.runOnUiThread(() -> {
                retrySnackbar.setAction("Retry now", v -> new Thread(this::retryConnection).start());
                retrySnackbar.setText(getCountDownString());
            });
        }


    }

    private void updateCountDown() {
        if (connectionCountDown <= 0) {
            retryConnection();
        } else {
            --connectionCountDown;
            activity.runOnUiThread(() -> retrySnackbar.setText(getCountDownString()));
        }
    }

    private String getCountDownString() {
        if (connectionCountDown == 0) {
            retrySnackbar.setAction("", null);
            return "Retrying connection now...";
        }
        return String.format("Retrying connection in %s sec", connectionCountDown);
    }

    private void retryConnection() {
        if (retryTimer != null) {
            retryTimer.cancel();
            retryTimer = null;
        }
        if (initSocket()) {
            connectionTryCount = 0;
            retrySnackbar.dismiss();
            retryTimer.cancel();
            retryTimer = null;
        } else {
            startRetry();
        }

    }
}