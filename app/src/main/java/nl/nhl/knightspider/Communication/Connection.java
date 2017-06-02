package nl.nhl.knightspider.Communication;

import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Objects;

import nl.nhl.knightspider.MainActivity;
import nl.nhl.knightspider.Pages.DiagnosticsScreen;
import nl.nhl.knightspider.Pages.SpiderView;
import nl.nhl.knightspider.Utils;

/**
 * Created by bouke on 18-5-2017.
 */

public class Connection {
    private final MainActivity activity;
    private Socket clientSocket;

    public Connection(String ip, int port, MainActivity activity) throws IOException {
        this.activity = activity;
        clientSocket = new Socket(ip, port);
        while (true) {
            String out = readFromServer();
            if (out != null) {
                onMessage(out);
                if (Objects.equals(out, "")) {
                    clientSocket.close();
                    break;
                }
            }
        }
    }

    public void onMessage(String data) {
        Message message = Message.fromString(data);
        if (message == null) {
            return;
        }

        // TODO: Implement
        switch (message.getIdentifier()) {
            case Identifiers.ANGLES:
                HashMap<Integer, Float> angles = Utils.parseAngles(message.getPayload());
                if (angles == null) {
                    return;
                }

                activity.runOnUiThread(() -> {
                    SpiderView spiderView = this.activity.getSpiderView();
                });

                break;
            case Identifiers.SERVO:

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
        String modifiedSentence;
        BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        modifiedSentence = inFromServer.readLine();
        return modifiedSentence;
    }

    public void send(String message) {
        try {
            Log.d("SOCKET", "SENT: " + message);
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
            bufferedWriter.write(message);
            bufferedWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}