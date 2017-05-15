package nl.nhl.knightspider;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.LinearLayout;

/**
 * Created by ruurd on 15-May-17.
 */

public class InfoCard extends CardView {
    public InfoCard(Context context) {
        super(context);
        setForegroundGravity(Gravity.CENTER);
        setRadius(dpToPx(4));
        int padding = dpToPx(15);
        setContentPadding(padding, padding, padding, padding);
    }

    private Color backgroundColor;
    private String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Color getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
    }
    private DisplayMetrics getDisplayMetrics() {
        return getResources().getDisplayMetrics();
    }

    private int dpToPx(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getDisplayMetrics());
    }
}
