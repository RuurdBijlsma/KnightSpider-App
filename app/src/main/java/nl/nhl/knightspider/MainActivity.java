package nl.nhl.knightspider;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.HashMap;

import nl.nhl.knightspider.Communication.Connection;
import nl.nhl.knightspider.Communication.ServoReadings;
import nl.nhl.knightspider.Pages.DiagnosticsScreen;
import nl.nhl.knightspider.Pages.SpiderView;

public class MainActivity extends AppCompatActivity {
    public static String RASPBERRY_IP = "141.252.207.87";

    public WebView streamViewer;
    public String streamUrl = "http://" + RASPBERRY_IP + ":5000/index.html";
    public String blogUrl = "https://ruurdbijlsma.github.io/KnightSpider/blog.html";
    public boolean streamSuccess = false;
    BottomNavigationView navigation;
    WebView spiderViewer;
    private LinearLayout spiderLayout;
    private HashMap<Integer, ServoReadings> servoReadingsCache;
    private ScrollView diagnosticsLayout;
    private DiagnosticsScreen diagnosticsScreen;
    private SpiderView spiderView;
    private LinearLayout liveStreamLayout;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = item -> showLayout(item.getItemId());

    public SpiderView getSpiderView() {
        return spiderView;
    }

    public DiagnosticsScreen getDiagnosticsScreen() {
        return diagnosticsScreen;
    }

    private void hideLayouts() {
        spiderLayout.setVisibility(View.GONE);
        diagnosticsLayout.setVisibility(View.GONE);
        liveStreamLayout.setVisibility(View.GONE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        spiderView.resumeTimers();
        spiderView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        spiderView.pauseTimers();
        spiderView.onPause();
    }

    @Override
    protected void onDestroy() {
        spiderView.destroy();
        spiderView = null;
        super.onDestroy();
    }

    private boolean showLayout(int id) {
        hideLayouts();
        switch (id) {
            case R.id.navigation_spider:
                navigation.getMenu().getItem(0).setChecked(true);
                spiderLayout.setVisibility(View.VISIBLE);
                return true;
            case R.id.navigation_diagnostics:
                navigation.getMenu().getItem(1).setChecked(true);
                diagnosticsLayout.setVisibility(View.VISIBLE);
                return true;
            case R.id.navigation_live_stream:
                navigation.getMenu().getItem(2).setChecked(true);
                liveStreamLayout.setVisibility(View.VISIBLE);
                return true;
        }
        return false;
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setNavigationBarColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));

        setContentView(R.layout.activity_main);

        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        spiderLayout = (LinearLayout) findViewById(R.id.spider_layout);
        diagnosticsLayout = (ScrollView) findViewById(R.id.diagnostics_container);
        liveStreamLayout = (LinearLayout) findViewById(R.id.live_stream_layout);

        //Diagnostics screen
        diagnosticsScreen = new DiagnosticsScreen(getApplicationContext(), 2);
        diagnosticsLayout.addView(diagnosticsScreen);
        diagnosticsScreen.setBattery(0);
        diagnosticsScreen.setGyro(0);
        diagnosticsScreen.setTemp(0);
        diagnosticsScreen.setCpu(0);
        diagnosticsScreen.setVolt(0);
        diagnosticsScreen.setLoad(0);

        //Live stream screen
        streamViewer = (WebView) findViewById(R.id.stream_viewer);
        WebViewClient client = new WebViewClient() {
            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                streamSuccess = false;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                streamSuccess = true;
            }
        };
        streamViewer.setWebViewClient(client);
        WebSettings settings = streamViewer.getSettings();
        settings.setJavaScriptEnabled(true);
        //Blog screen
        WebView blogViewer = (WebView) findViewById(R.id.blog_viewer);
        blogViewer.getSettings().setJavaScriptEnabled(true);
        blogViewer.loadUrl(blogUrl);

        //Spider 3D stream screen
        spiderView = new SpiderView(getApplicationContext(),
                (TextView) findViewById(R.id.servo_id),
                (TextView) findViewById(R.id.servo_temp),
                (TextView) findViewById(R.id.servo_angle),
                (TextView) findViewById(R.id.servo_load),
                (TextView) findViewById(R.id.servo_voltage));

        spiderLayout.addView(spiderView, 0);
        spiderView.setServoId(0);
        spiderView.setTemp(0);
        spiderView.setAngle(0);
        spiderView.setLoad(0);
        spiderView.setVoltage(0);
        spiderView.setOnServoInfoRequestedCallback(i -> {
            HashMap<Integer, ServoReadings> cache = getServoReadingsCache();
            if (!cache.containsKey(i)) return;

            ServoReadings servoReadings = cache.get(i);

            runOnUiThread(() -> {
                SpiderView spiderView = getSpiderView();
                spiderView.setServoId(servoReadings.getId());
                spiderView.setAngle(servoReadings.getPosition());
                spiderView.setLoad((int) servoReadings.getLoad());
                spiderView.setTemp(servoReadings.getTemperature());
                spiderView.setVoltage(servoReadings.getVoltage());
            });
        });

        showLayout(R.id.navigation_diagnostics);

        //Connect socket client to server
        Thread t = new Thread(() -> {
            int port = 4980;
            try {
                Log.d("SOCKET", "creating connection");
                Connection c = new Connection(RASPBERRY_IP, port, this);
            } catch (Exception e) {
                Log.d("SOCKET", e.getMessage());
            }
        });
        t.start();
    }

    public HashMap<Integer, ServoReadings> getServoReadingsCache() {
        return servoReadingsCache;
    }

    public void setServoReadingsCache(HashMap<Integer, ServoReadings> servoReadingsCache) {
        this.servoReadingsCache = servoReadingsCache;
    }

    public float getAverageLoad() {
        float value = 0;
        int nServos = 0;
        for (ServoReadings servoReadings : getServoReadingsCache().values()) {
            float load = servoReadings.getLoad();
            if (Math.abs(load - -500) > 0.1) {
                value += load;
                nServos++;
            }
        }
        return value / nServos;
    }

    public float getAverageVoltage() {
        float value = 0;
        int nServos = 0;
        for (ServoReadings servoReadings : getServoReadingsCache().values()) {
            float voltage = servoReadings.getVoltage();
            if (Math.abs(voltage - -500) > 0.1) {
                value += voltage;
                nServos++;
            }
        }
        return value / nServos;
    }

    public float getAverageTemperature() {
        float value = 0;
        int nServos = 0;
        for (ServoReadings servoReadings : getServoReadingsCache().values()) {
            float temperature = servoReadings.getTemperature();
            if (Math.abs(temperature - -500) > 0.1) {
                value += temperature;
                nServos++;
            }
        }
        return value / nServos;
    }
}
