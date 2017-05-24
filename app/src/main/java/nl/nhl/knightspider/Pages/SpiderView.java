package nl.nhl.knightspider.Pages;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;

import nl.nhl.knightspider.Javascript.Javascript;
import nl.nhl.knightspider.Javascript.JavascriptCallback;

/**
 * Created by ruurd on 19-May-17.
 */

@SuppressLint("ViewConstructor")
public class SpiderView extends WebView {
    private Javascript javascript;
    private TextView idText;
    private TextView tempText;
    private TextView angleText;
    private TextView loadText;

    public SpiderView(Context context, TextView servoId, TextView servoTemp, TextView servoAngle, TextView servoLoad) {
        super(context);

        idText = servoId;
        tempText = servoTemp;
        angleText = servoAngle;
        loadText = servoLoad;

        setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dpToPx(400)));

        getSettings().setAppCacheEnabled(false);
        getSettings().setAllowContentAccess(true);
        getSettings().setAllowFileAccess(true);
        getSettings().setAllowFileAccessFromFileURLs(true);
        getSettings().setAllowUniversalAccessFromFileURLs(true);
        javascript = new Javascript(this) {
            @Override
            public void onMessage(String message) {
                showServoInfo(Integer.parseInt(message));
            }
        };
        loadUrl("file:///android_asset/web/index.html#engage");
    }

    private void showServoInfo(int servoId) {
        setServoId(servoId);
    }

    public void send(String message, JavascriptCallback callback) {
        javascript.send(message, callback);
    }

    private DisplayMetrics getDisplayMetrics() {
        return getResources().getDisplayMetrics();
    }

    private int dpToPx(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getDisplayMetrics());
    }

    public void setAngle(int angle) {
        setAngleText("Hoek " + angle + "°");
    }

    public void setTemp(int temperature) {
        setTempText("Temperatuur " + temperature + "°");
    }

    public void setLoad(int load) {
        setLoadText("Belasting " + load + "g");
    }

    public void setServoId(int id) {
        setIdText("Servo id: " + id);
    }

    public void setIdText(String text) {
        idText.setText(text);
    }

    public void setTempText(String text) {
        tempText.setText(text);
    }

    public void setAngleText(String text) {
        angleText.setText(text);
    }

    public void setLoadText(String text) {
        loadText.setText(text);
    }
}
