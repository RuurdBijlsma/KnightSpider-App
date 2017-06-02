package nl.nhl.knightspider;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by ruurd on 15-May-17
 */

public class InfoCard extends CardView {
    private TextView textView;
    private ImageView imageView;
    private int backgroundColor;
    public InfoCard(Context context) {
        super(context);
    }

    public InfoCard(Context context, int colorId, int iconId, int width, int height) {
        super(context);

        int margin = dpToPx(10);
        int textPadding = dpToPx(10);
        int imagePadding = dpToPx(20);

        setForegroundGravity(Gravity.CENTER);
        setRadius(dpToPx(4));

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(width, height);
        lp.setMargins(margin, margin, 0, margin);
        setLayoutParams(lp);

        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);

        textView = new TextView(context);
        textView.setTextAlignment(TEXT_ALIGNMENT_CENTER);
        textView.setTextSize(18);
        textView.setPadding(textPadding, textPadding, textPadding, textPadding);

        imageView = new ImageView(context);
        imageView.setPadding(imagePadding, imagePadding, imagePadding, imagePadding);
        imageView.setBackgroundColor(colorId);
        imageView.setImageResource(iconId);
        imageView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height - dpToPx(45)));

        layout.addView(imageView);
        layout.addView(textView);
        addView(layout);
    }

    public String getText() {
        return (String) textView.getText();
    }

    public void setText(String text) {
        textView.setText(text);
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
        imageView.setBackgroundColor(backgroundColor);
    }

    private DisplayMetrics getDisplayMetrics() {
        return getResources().getDisplayMetrics();
    }

    private int dpToPx(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getDisplayMetrics());
    }
}
