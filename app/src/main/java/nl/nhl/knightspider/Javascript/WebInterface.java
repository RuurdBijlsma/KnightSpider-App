package nl.nhl.knightspider.Javascript;

import android.content.Context;
import android.util.Log;
import android.webkit.JavascriptInterface;

public abstract class WebInterface {
    @JavascriptInterface
    public void send(String message){
        onMessage(message);
    }

    public abstract void onMessage(String message);
}