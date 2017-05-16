package nl.nhl.knightspider;

import android.content.Context;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

/**
 * Created by ruurd on 16-May-17
 */

public class WebInterface {
    Context mContext;

    /**
     * Instantiate the interface and set the context
     */
    WebInterface(Context c) {
        mContext = c;
    }

    /**
     * Show a toast from the web page
     */
    @JavascriptInterface
    public void showToast(String toast) {
        Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show();
    }
}
