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

import nl.nhl.knightspider.Communication.Connection;
import nl.nhl.knightspider.Communication.SpiderInfo;
import nl.nhl.knightspider.Pages.DiagnosticsScreen;
import nl.nhl.knightspider.Pages.SpiderView;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView navigation;
    private LinearLayout spiderLayout;

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

    WebView spiderViewer;
    public WebView streamViewer;
    public String streamUrl = "http://141.252.240.37:5000/index.html";
    public String blogUrl = "https://ruurdbijlsma.github.io/KnightSpider/blog.html";
    public boolean streamSuccess = false;

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
//        diagnosticsScreen.setVolt(3.3f);
//        diagnosticsScreen.setLoad(40);

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

        showLayout(R.id.navigation_diagnostics);
        int Ĳ = 4;
        SpiderInfo spinfo = SpiderInfo.fromJson("{\"slope\": 20, \"cpuTemperature\": 46.7, \"battery\": 200, \"cpuUsage\": 5.0}");
        Log.d("JSON", spinfo.toString());

        MainActivity that = this;

        //Connect socket client to server
        Thread t = new Thread(() -> {
            String ip = "141.252.240.37";
            int port = 4980;
            try {
                Log.d("SOCKET", "creating connection");
                Connection c = new Connection(ip, port, that);
            } catch (Exception e) {
                Log.d("SOCKET", e.getMessage());
            }
        });
        t.start();
    }
}
