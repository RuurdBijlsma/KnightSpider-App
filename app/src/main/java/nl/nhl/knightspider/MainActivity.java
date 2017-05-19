package nl.nhl.knightspider;

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

import java.util.Timer;
import java.util.TimerTask;

import nl.nhl.knightspider.Pages.DiagnosticsScreen;
import nl.nhl.knightspider.Pages.SpiderView;

///// TODO: 17-May-17 Info panel in spider tab zetten als je op link klikt in javascript
public class MainActivity extends AppCompatActivity {
    BottomNavigationView navigation;
    private LinearLayout spiderLayout;
    private ScrollView diagnosticsLayout;
    private LinearLayout liveStreamLayout;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            return showLayout(item.getItemId());
        }
    };

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
        final DiagnosticsScreen diagnostics = new DiagnosticsScreen(getApplicationContext(), 2);
        diagnosticsLayout.addView(diagnostics);
        diagnostics.setBattery(97);
        diagnostics.setGyro(20);
        diagnostics.setTemp(32);
        diagnostics.setVolt(3.3f);
        diagnostics.setLoad(40);

        //Live stream screen
        WebView streamViewer = (WebView) findViewById(R.id.stream_viewer);
        streamViewer.loadUrl("http://141.252.208.61:5000");
        //Blog screen
        WebView blogViewer = (WebView) findViewById(R.id.blog_viewer);
        blogViewer.getSettings().setJavaScriptEnabled(true);
        blogViewer.loadUrl("https://ruurdbijlsma.github.io/KnightSpider/blog.html");

        //Spider 3D stream screen
        final SpiderView spiderView = new SpiderView(getApplicationContext(),
                (TextView) findViewById(R.id.servo_id),
                (TextView) findViewById(R.id.servo_temp),
                (TextView) findViewById(R.id.servo_angle),
                (TextView) findViewById(R.id.servo_load));
        spiderLayout.addView(spiderView, 0);
        spiderView.setServoId(18);
        spiderView.setTemp(32);
        spiderView.setAngle(23);
        spiderView.setLoad(50);

        showLayout(R.id.navigation_diagnostics);

        //Connect socket client to server
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                String ip = "141.252.228.164";
                int port = 7894;
                final int updateInterval = 1000;
                try {
                    Connection c = new Connection(ip, port) {
                        @Override
                        public void onMessage(String message) {
                            Log.d("SOCKET", "RECEIVED: " + message);
                            Timer t = new Timer();
                            t.schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    send("servo");
                                }
                            }, updateInterval);
                        }
                    };
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        t.start();
    }
}
