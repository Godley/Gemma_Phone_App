package mobiledev.charlottegodley.gemmaphoneapp;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ColorPickerActivity extends Activity implements
        ColorPicker.OnColorChangedListener {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color_picker);
        this.activity = this;
    }

    @Override
    public void colorChanged(String str,int color, int view_id) {
        ColorPickerActivity.this.findViewById(view_id)
                .setBackgroundColor(color);
    }

    Activity activity;

    public void getColor(View v) {
        new ColorPicker(activity, this, v.getId(), "", Color.BLACK, Color.WHITE).show();
    }

    public void nfcButtonClick(View v) {
        StringBuilder nfcString = new StringBuilder();
        if(v.getId() == R.id.nfcButton) {

            int leds[] = {R.id.led1color,R.id.led2color,R.id.led3color,R.id.led4color,
                                    R.id.led5color,R.id.led6color, R.id.led7color,R.id.led8color,
                                    R.id.led9color,R.id.led10color,R.id.led11color,R.id.led12color,
                                    R.id.led13color,R.id.led14color, R.id.led15color,R.id.led16color};
            int seconds[] = {R.id.ledSeconds1,R.id.ledSeconds2,R.id.ledSeconds3,R.id.ledSeconds4,
                    R.id.ledSeconds5,R.id.ledSeconds6, R.id.ledSeconds7,R.id.ledSeconds8,
                    R.id.ledSeconds9,R.id.ledSeconds10,R.id.ledSeconds11,R.id.ledSeconds12,
                    R.id.ledSeconds13,R.id.ledSeconds14, R.id.ledSeconds15,R.id.ledSeconds16};
            for(int i=0;i<seconds.length;i++) {
                View btn = ColorPickerActivity.this.findViewById(leds[i]);
                String colString = "";
                try {
                    ColorDrawable buttonColor = (ColorDrawable) btn.getBackground();
                    if(buttonColor != null) {
                        int colorId = buttonColor.getColor();
                        colString = Integer.toHexString(colorId).substring(2);
                    }
                }
                catch(ClassCastException e) {
                    colString = "000000";
                }
                finally {
                    EditText secondsInput = (EditText)ColorPickerActivity.this.findViewById(seconds[i]);
                    int secondsInt = 0;
                    String secondsString = secondsInput.getText().toString();
                    try {
                        secondsInt = Integer.parseInt(secondsString);
                    }
                    catch(NumberFormatException e) {
                        secondsInt = 0;
                    }
                    finally {
                        nfcString.append(colString);
                        nfcString.append(secondsInt);
                        nfcString.append(".");
                    }
                }


            }
            Log.i("output", nfcString.toString());
        }
    }
}
