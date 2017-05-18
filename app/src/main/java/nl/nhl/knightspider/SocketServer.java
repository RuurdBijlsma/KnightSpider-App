package nl.nhl.knightspider;

import android.util.Log;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;

/**
 * Created by ruurd on 18-May-17
 */

public class SocketServer extends WebSocketServer {
    public SocketServer(InetSocketAddress inetSocketAddress) {
        super(inetSocketAddress);
    }

    @Override
    public void onOpen(WebSocket webSocket, ClientHandshake clientHandshake) {
        Log.d("OPEN", "open");
    }

    @Override
    public void onClose(WebSocket webSocket, int i, String s, boolean b) {
        Log.d("close", "open");
    }

    @Override
    public void onMessage(WebSocket webSocket, String s) {
        Log.d("msg", "open");
    }

    @Override
    public void onError(WebSocket webSocket, Exception e) {
        Log.d("err", "open");
    }

    @Override
    public void onStart() {
        Log.d("SOCKET", "Started server on " + getAddress().getHostString());

//        Thread t = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                String ip = Utils.getIPAddress(true);
//                ip = "localhost";
//                URI uri = null;
//                int port = 4000;
//                try {
//                    uri = new URI("ws://" + ip + ":" + port);
//                } catch (URISyntaxException e) {
//                    e.printStackTrace();
//                }
//
//                SocketClient client = new SocketClient(uri);
//                client.run();
//            }
//        });
//        t.start();
    }
}
