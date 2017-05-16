package nl.nhl.knightspider;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ScrollView;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView navigation;
    private View spiderLayout;
    private ScrollView diagnosticsLayout;
    private View liveStreamLayout;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            return showLayout(item.getItemId());
        }

    };
    private GridLayout gridLayout;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.getMenu().getItem(1).setChecked(true);

        spiderLayout = findViewById(R.id.spider_layout);
        diagnosticsLayout = (ScrollView) findViewById(R.id.diagnostics_container);
        liveStreamLayout = findViewById(R.id.live_stream_layout);

        DiagnosticsScreen diagnostics = new DiagnosticsScreen(getApplicationContext(), 2);
        diagnosticsLayout.addView(diagnostics);
        diagnostics.setBattery(97);
        diagnostics.setGyro(20);
        diagnostics.setTemp(32);
        diagnostics.setVolt(3.3f);
        diagnostics.setLoad(40);

        showLayout(R.id.navigation_diagnostics);
    }
}
