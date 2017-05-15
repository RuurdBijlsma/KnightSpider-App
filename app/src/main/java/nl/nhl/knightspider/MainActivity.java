package nl.nhl.knightspider;

import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

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

    private DisplayMetrics getDisplayMetrics() {
        return getResources().getDisplayMetrics();
    }

    private int dpToPx(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getDisplayMetrics());
    }

    private TextView getBigText(String text) {
        TextView textView = new TextView(getApplicationContext());
        textView.setText(text);
        textView.setTypeface(Typeface.MONOSPACE);
        textView.setTextSize(32);
        int padding = dpToPx(15);
        textView.setPadding(padding, padding, padding, padding);
        return textView;
    }

    private CardView getDiagCard(@Nullable View child) {
        CardView card = new CardView(getApplicationContext());
        card.setForegroundGravity(Gravity.CENTER);

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        int margin = dpToPx(10);
        lp.setMargins(margin, margin, margin, margin);

        card.setLayoutParams(lp);
        card.setRadius(dpToPx(4));
        int padding = dpToPx(15);
        card.setContentPadding(padding, padding, padding, padding);

        if (child != null)
            card.addView(child);

        DisplayMetrics metrics = getDisplayMetrics();
        card.setMinimumWidth(metrics.widthPixels/2);
        card.setMinimumHeight(dpToPx(120));

        return card;
    }

    private void addCardsToLayout(LinearLayout layout, int number, int cardsPerRow) {
        LinearLayout currentLayout = null;
        for (int i = 0; i < number; i++) {
            if (i % cardsPerRow == 0) {
                currentLayout = new LinearLayout(getApplicationContext());
                currentLayout.setOrientation(LinearLayout.HORIZONTAL);
                layout.addView(currentLayout);
            }
            TextView tv = new TextView(getApplicationContext());
            tv.setText("Spin" + i);
            currentLayout.addView(getDiagCard(tv));
        }
    }

    private void fillDiagnosticsLayout() {
        LinearLayout contentLayout = new LinearLayout(getApplicationContext());
        contentLayout.setOrientation(LinearLayout.VERTICAL);

        contentLayout.addView(getBigText("Spin diagnostiek"));

        addCardsToLayout(contentLayout, 2, 2);

        contentLayout.addView(getBigText("Servo diagnostiek"));

        addCardsToLayout(contentLayout, 18, 2);

        diagnosticsLayout.addView(contentLayout);
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

//        fillDiagnosticsLayout();

        showLayout(R.id.navigation_diagnostics);
    }
}
