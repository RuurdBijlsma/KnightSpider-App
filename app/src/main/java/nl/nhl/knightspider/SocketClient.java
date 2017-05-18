package nl.nhl.knightspider;
import android.util.Log;

import org.java_websocket.client.*;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;

/**
 * Created by ruurd on 18-May-17
 */

public class SocketClient extends  WebSocketClient {
    public SocketClient(URI uri) {
        super(uri);
    }

    @Override
    public void onOpen(ServerHandshake serverHandshake) {
        Log.d("OPEN","open");
        send("Hoi hoe gaat ie");
    }

    @Override
    public void onMessage(String s) {
        Log.d("MESSAGE",s);

        send("hoi ik ben ruurd");
    }

    @Override
    public void onClose(int i, String s, boolean b) {
        Log.d("CLOSE","s");
    }

    @Override
    public void onError(Exception e) {
        Log.d("ERROR","open");
    }
}
