package nl.nhl.knightspider;

import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Objects;

/**
 * Created by bouke on 18-5-2017.
 */

public abstract class Connection {
    private Socket clientSocket;

    public Connection(String ip, int port) throws IOException {
        clientSocket = new Socket(ip, port);
        while (true) {
            String out = readFromServer();
            onMessage(out);
            if (Objects.equals(out, "")) {
                clientSocket.close();
                break;
            }
        }
    }

    public abstract void onMessage(String message);

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