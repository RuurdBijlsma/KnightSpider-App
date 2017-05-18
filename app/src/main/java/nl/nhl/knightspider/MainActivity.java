package nl.nhl.knightspider;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.JsonReader;
import android.util.JsonToken;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import java.io.IOException;
import java.io.StringReader;
///// TODO: 17-May-17 Info panel in spider tab zetten als je op link klikt in javascript
public class MainActivity extends AppCompatActivity {
    BottomNavigationView navigation;
    private View spiderLayout;
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
                spiderLayout.setVisibility(View.VISIBLE);
                return true;
            case R.id.navigation_diagnostics:
                diagnosticsLayout.setVisibility(View.VISIBLE);
                return true;
            case R.id.navigation_live_stream:
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

        spiderLayout = findViewById(R.id.spider_layout);
        diagnosticsLayout = (ScrollView) findViewById(R.id.diagnostics_container);
        liveStreamLayout = (LinearLayout) findViewById(R.id.live_stream_layout);

        //Diagnostics screen
        DiagnosticsScreen diagnostics = new DiagnosticsScreen(getApplicationContext(), 2);
        diagnosticsLayout.addView(diagnostics);
        diagnostics.setBattery(97);
        diagnostics.setGyro(20);
        diagnostics.setTemp(32);
        diagnostics.setVolt(3.3f);
        diagnostics.setLoad(40);

        //Live stream screen
        WebView streamViewer = (WebView) findViewById(R.id.stream_viewer);
//        streamViewer.getSettings().setJavaScriptEnabled(true);
        streamViewer.loadUrl("http://141.252.208.61:5000");
        WebView redditViewer = (WebView) findViewById(R.id.reddit_viewer);
        redditViewer.getSettings().setJavaScriptEnabled(true);
        redditViewer.loadUrl("https://ruurdbijlsma.github.io/KnightSpider/blog.html");

        //Spider 3D stream screen
        spiderViewer = (WebView) findViewById(R.id.spider_viewer);
        spiderViewer.getSettings().setJavaScriptEnabled(true);
        spiderViewer.getSettings().setAppCacheEnabled(false);
        spiderViewer.getSettings().setAllowContentAccess(true);
        spiderViewer.getSettings().setAllowFileAccess(true);
        spiderViewer.getSettings().setAllowFileAccessFromFileURLs(true);
        spiderViewer.getSettings().setAllowUniversalAccessFromFileURLs(true);
        spiderViewer.loadUrl("file:///android_asset/web/index.html");
        navigation.setOnNavigationItemReselectedListener(new BottomNavigationView.OnNavigationItemReselectedListener() {
            @Override
            public void onNavigationItemReselected(@NonNull MenuItem item) {
                evaluateJavascript("java('ping')");
            }
        });

        navigation.getMenu().getItem(1).setChecked(true);
        showLayout(R.id.navigation_diagnostics);
    }

    public void evaluateJavascript(String javascript) {
        spiderViewer.evaluateJavascript(javascript, new ValueCallback<String>() {
            @TargetApi(Build.VERSION_CODES.HONEYCOMB)
            @Override
            public void onReceiveValue(String s) {
                JsonReader reader = new JsonReader(new StringReader(s));

                // Must set lenient to parse single values
                reader.setLenient(true);

                try {
                    if (reader.peek() != JsonToken.NULL) {
                        if (reader.peek() == JsonToken.STRING) {
                            String msg = reader.nextString();
                            if (msg != null) {
                                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                } catch (IOException e) {
                    Log.e("TAG", "MainActivity: IOException", e);
                } finally {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        // NOOP
                    }
                }
            }
        });
    }
}
