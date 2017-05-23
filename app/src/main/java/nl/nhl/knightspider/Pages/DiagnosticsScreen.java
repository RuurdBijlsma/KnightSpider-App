package nl.nhl.knightspider.Pages;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.widget.LinearLayout;
import android.widget.TextView;

import nl.nhl.knightspider.InfoCard;
import nl.nhl.knightspider.R;

/**
 * Created by ruurd on 16-May-17.
 */

public class DiagnosticsScreen extends LinearLayout {
    public DiagnosticsScreen(Context context) {
        super(context);
    }

    public DiagnosticsScreen(Context context, int cardsPerRow) {
        super(context);
        addContent(cardsPerRow);
    }

    private InfoCard batteryCard;
    private InfoCard gyroCard;
    private InfoCard tempCard;
    private InfoCard voltCard;
    private InfoCard loadCard;
    private InfoCard cpuCard;

    public void addContent(int cardsPerRow) {
        DisplayMetrics metrics = getDisplayMetrics();
        int halfWidth = metrics.widthPixels / cardsPerRow - dpToPx(15);
        int darkColor = Color.argb(255, 40, 40, 40);
        batteryCard = new InfoCard(this.getContext(), darkColor, R.drawable.batteryicon, halfWidth, dpToPx(150));
        gyroCard = new InfoCard(this.getContext(), darkColor, R.drawable.gyroicon, halfWidth, dpToPx(150));
        tempCard = new InfoCard(this.getContext(), darkColor, R.drawable.tempicon, halfWidth, dpToPx(150));
        tempCard = new InfoCard(this.getContext(), darkColor, R.drawable.tempicon, halfWidth, dpToPx(150));
        cpuCard = new InfoCard(this.getContext(), darkColor, R.drawable.cpuicon, halfWidth, dpToPx(150));
//        voltCard = new InfoCard(this.getContext(), darkColor, R.drawable.volticon, halfWidth, dpToPx(150));
//        loadCard = new InfoCard(this.getContext(), darkColor, R.drawable.loadicon, halfWidth, dpToPx(150));


        setOrientation(LinearLayout.VERTICAL);

        addView(getBigText("Spin diagnostiek"));

        InfoCard[] spiderCards = {
                batteryCard,
                gyroCard,
                cpuCard,
                tempCard
        };
        addCards(spiderCards, cardsPerRow);

//        addView(getBigText("Servo diagnostiek"));

        InfoCard[] servoCards = {
                tempCard,
                voltCard,
                loadCard
        };
//        addCards(servoCards, cardsPerRow);
    }

    private void addCards(InfoCard[] cards, int cardsPerRow) {
        LinearLayout currentLayout = null;
        for (int i = 0; i < cards.length; i++) {
            if (i % cardsPerRow == 0) {
                currentLayout = new LinearLayout(this.getContext());
                currentLayout.setOrientation(LinearLayout.HORIZONTAL);
                addView(currentLayout);
            }
            currentLayout.addView(cards[i]);
        }
    }

    private DisplayMetrics getDisplayMetrics() {
        return getResources().getDisplayMetrics();
    }

    private int dpToPx(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getDisplayMetrics());
    }

    private TextView getBigText(String text) {
        TextView textView = new TextView(this.getContext());
        textView.setText(text);
        textView.setTypeface(Typeface.MONOSPACE);
        textView.setTextSize(32);
        textView.setTextColor(Color.argb(240, 0, 0, 0));
        int padding = dpToPx(15);
        textView.setPadding(padding, padding, padding, padding);
        return textView;
    }

    public void setBattery(int value) {
        getBatteryCard().setText("Batterij " + value + "%");
    }

    public void setGyro(int value) {
        getGyroCard().setText("Helling " + value + "°");
    }

    public void setTemp(int value) {
        getTempCard().setText("Temperatuur " + value + "°");
    }

    public void setVolt(float value) {
        getVoltCard().setText("Voltage " + value + "V");
    }

    public void setLoad(int value) {
        getLoadCard().setText("Belasting " + value + "g");
    }

    public void setCpu(float value) {
        getCpuCard().setText("CPU " + value + "%");
    }

    public InfoCard getBatteryCard() {
        return batteryCard;
    }

    public InfoCard getGyroCard() {
        return gyroCard;
    }

    public InfoCard getTempCard() {
        return tempCard;
    }

    public InfoCard getVoltCard() {
        return voltCard;
    }

    public InfoCard getLoadCard() {
        return loadCard;
    }

    public InfoCard getCpuCard() {
        return cpuCard;
    }
}
