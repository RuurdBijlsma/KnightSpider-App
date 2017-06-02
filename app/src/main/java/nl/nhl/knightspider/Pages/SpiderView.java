package nl.nhl.knightspider.Pages;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.webkit.ValueCallback;
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
    private TextView voltageText;

    public SpiderView(Context context, TextView servoId, TextView servoTemp, TextView servoAngle, TextView servoLoad, TextView servoVoltage) {
        super(context);

        idText = servoId;
        tempText = servoTemp;
        angleText = servoAngle;
        loadText = servoLoad;
        voltageText = servoVoltage;

        setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dpToPx(395)));

        getSettings().setAppCacheEnabled(false);
        getSettings().setAllowContentAccess(true);
        getSettings().setAllowFileAccess(true);
        getSettings().setAllowFileAccessFromFileURLs(true);
        getSettings().setAllowUniversalAccessFromFileURLs(true);
        SpiderView view = this;
        javascript = new Javascript(this) {
            @Override
            public void onMessage(String message) {
                view.callback.onReceiveValue(Integer.parseInt(message));
            }
        };
        loadUrl("file:///android_asset/web/index.html");
    }

    private ValueCallback<Integer> callback;

    public void setOnServoInfoRequestedCallback(ValueCallback<Integer> callback) {
        this.callback = callback;
    }

    public void setSpiderStanceFromJson(String json) {
        javascript.send(json, new JavascriptCallback() {
            @Override
            public void onMessage(String result) {
                Log.d("CONSOLE", result);
            }
        });
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

    public void setAngle(float angle) {
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

    public void setVoltage(float voltage) {
        setVoltageText("Voltage " + voltage + "V");
    }

    private void setIdText(String text) {
        idText.setText(text);
    }

    private void setTempText(String text) {
        tempText.setText(text);
    }

    private void setAngleText(String text) {
        angleText.setText(text);
    }

    private void setLoadText(String text) {
        loadText.setText(text);
    }

    private void setVoltageText(String text) {
        voltageText.setText(text);
    }
}
