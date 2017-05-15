package nl.nhl.knightspider;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {
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

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            return showLayout(item.getItemId());
        }

    };

    private View spiderLayout;
    private ScrollView diagnosticsLayout;
    private View liveStreamLayout;

    private GridLayout gridLayout;
    BottomNavigationView navigation;

    private CardView getDiagCard(@Nullable View child) {
        CardView card = new CardView(getApplicationContext());
        card.setMinimumWidth(165);
        card.setMinimumHeight(120);
        card.setForegroundGravity(Gravity.CENTER);

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        int margin = 10;
        lp.setMargins(margin, margin, margin, margin);

        card.setLayoutParams(lp);
        card.setRadius(4);
        int padding = 15;
        card.setContentPadding(padding, padding, padding, padding);

        if (child != null)
            card.addView(child);

        return card;
    }

    private void fillDiagnosticsLayout() {
        LinearLayout ll = new LinearLayout(getApplicationContext());
        ll.setOrientation(LinearLayout.VERTICAL);

        TextView spinText = new TextView(getApplicationContext());
        spinText.setText("Spin diagnostiek");
        spinText.setTypeface(Typeface.MONOSPACE);
        spinText.setTextSize(32);
        diagnosticsLayout.addView(spinText);

        for (int i = 0; i < 2; i++) {
            TextView tv = new TextView(getApplicationContext());
            tv.setText("Spin" + i);
            diagnosticsLayout.addView(getDiagCard(tv));
        }

        TextView servoText = new TextView(getApplicationContext());
        servoText.setText("Servo diagnostiek");
        servoText.setTypeface(Typeface.MONOSPACE);
        servoText.setTextSize(32);
        diagnosticsLayout.addView(servoText);

        for (int i = 0; i < 18; i++) {
            TextView tv = new TextView(getApplicationContext());
            tv.setText("Servo" + i);
            diagnosticsLayout.addView(getDiagCard(tv));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.getMenu().getItem(1).setChecked(true);

        spiderLayout = findViewById(R.id.spider_layout);
        diagnosticsLayout = (ScrollView) findViewById(R.id.diagnotsics_layout);
        liveStreamLayout = findViewById(R.id.live_stream_layout);

        showLayout(R.id.navigation_diagnostics);
    }
}
