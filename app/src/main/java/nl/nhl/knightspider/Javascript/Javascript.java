package nl.nhl.knightspider.Javascript;

import android.annotation.TargetApi;
import android.os.Build;
import android.webkit.ValueCallback;
import android.webkit.WebView;

/**
 * Created by ruurd on 19-May-17.
 */

public abstract class Javascript {
    private WebView webView;

    public Javascript(WebView webView) {
        this.webView = webView;
        this.webView.getSettings().setJavaScriptEnabled(true);
        this.webView.addJavascriptInterface(new WebInterface() {
            @Override
            public void onMessage(String message) {
                Javascript.this.onMessage(message);
            }
        }, "android");
    }

    public abstract void onMessage(String message);

    public void send(String message, final JavascriptCallback callback) {
        String javascript = "send('" + message + "')";
        webView.evaluateJavascript(javascript, callback::onMessage);
    }
}
