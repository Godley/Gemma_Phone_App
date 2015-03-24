package mobiledev.charlottegodley.gemmaphoneapp;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

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
}
