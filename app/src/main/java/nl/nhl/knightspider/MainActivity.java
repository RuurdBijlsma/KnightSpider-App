package nl.nhl.knightspider;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
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
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            return showLayout(item.getItemId());
        }
    };

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

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        spiderLayout = (LinearLayout) findViewById(R.id.spider_layout);
        diagnosticsLayout = (ScrollView) findViewById(R.id.diagnostics_container);
        liveStreamLayout = (LinearLayout) findViewById(R.id.live_stream_layout);

        //Diagnostics screen
        diagnosticsScreen = new DiagnosticsScreen(getApplicationContext(), 2);
        diagnosticsLayout.addView(diagnosticsScreen);
        diagnosticsScreen.setBattery(97);
        diagnosticsScreen.setGyro(20);
        diagnosticsScreen.setTemp(32);
//        diagnosticsScreen.setVolt(3.3f);
//        diagnosticsScreen.setLoad(40);
        diagnosticsScreen.setCpu(1.1f);

        //Live stream screen
        WebView streamViewer = (WebView) findViewById(R.id.stream_viewer);
        streamViewer.loadUrl("http://141.252.240.172:5000");
        //Blog screen
        WebView blogViewer = (WebView) findViewById(R.id.blog_viewer);
        blogViewer.getSettings().setJavaScriptEnabled(true);
        blogViewer.loadUrl("https://ruurdbijlsma.github.io/KnightSpider/blog.html");

        //Spider 3D stream screen
        spiderView = new SpiderView(getApplicationContext(),
                (TextView) findViewById(R.id.servo_id),
                (TextView) findViewById(R.id.servo_temp),
                (TextView) findViewById(R.id.servo_angle),
                (TextView) findViewById(R.id.servo_load),
                (TextView) findViewById(R.id.servo_voltage));

        spiderLayout.addView(spiderView, 0);
        spiderView.setServoId(18);
        spiderView.setTemp(32);
        spiderView.setAngle(23);
        spiderView.setLoad(50);

        spiderView.setOnServoInfoRequestedCallback((a) -> Log.d("CONSOLE", String.valueOf(a)));


        showLayout(R.id.navigation_diagnostics);
        int Ĳ = 4;
        SpiderInfo spinfo = SpiderInfo.fromJson("{\"slope\": 20, \"cpuTemperature\": 46.7, \"battery\": 200, \"cpuUsage\": 5.0}");
        Log.d("JSON", spinfo.toString());

        MainActivity that = this;

        //Connect socket client to server
        Thread t = new Thread(() -> {
            String jornLaptop = "141.252.229.227";
            String spin = "141.252.240.172";
            String ip = spin;
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
