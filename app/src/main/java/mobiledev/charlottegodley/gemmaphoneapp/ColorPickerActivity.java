package mobiledev.charlottegodley.gemmaphoneapp;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class ColorPickerActivity extends Activity implements
        ColorPicker.OnColorChangedListener {
    NfcAdapter adapter;
    PendingIntent pendingIntent;
    IntentFilter writeTagFilters[];
    boolean writeMode;
    Tag mytag;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color_picker);
        this.activity = this;

        adapter = NfcAdapter.getDefaultAdapter(this);
        pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
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
                    }
                }


            }
            Log.i("output", nfcString.toString());
            enableWriteMode();
            try {
                if(mytag==null){
                    Toast.makeText(this, "ERROR detected", Toast.LENGTH_LONG).show();

                }else{
                    write(nfcString.toString(),mytag);
                    Toast.makeText(this, "NFC write ok", Toast.LENGTH_LONG ).show();
                }
            } catch (IOException e) {
                Toast.makeText(this, "Error writing NFC", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            } catch (FormatException e) {
                Toast.makeText(this, "Error writing NFC", Toast.LENGTH_LONG ).show();
                e.printStackTrace();
            }

        }

    }

    private NdefRecord createRecord(String text) throws UnsupportedEncodingException {

        //create the message in according with the standard
        String lang = "en";
        byte[] textBytes = text.getBytes();
        byte[] langBytes = lang.getBytes("US-ASCII");
        int langLength = langBytes.length;
        int textLength = textBytes.length;

        byte[] payload = new byte[1 + langLength + textLength];
        payload[0] = (byte) langLength;

        // copy langbytes and textbytes into payload
        System.arraycopy(langBytes, 0, payload, 1, langLength);
        System.arraycopy(textBytes, 0, payload, 1 + langLength, textLength);

        NdefRecord recordNFC = new NdefRecord(NdefRecord.TNF_WELL_KNOWN, NdefRecord.RTD_TEXT, new byte[0], payload);
        return recordNFC;
    }

    private void write(String text, Tag tag) throws IOException, FormatException {

        NdefRecord[] records = { createRecord(text) };
        NdefMessage message = new NdefMessage(records);
        Ndef ndef = Ndef.get(tag);
        ndef.connect();
        ndef.writeNdefMessage(message);
        ndef.close();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(intent.getAction())) {
            mytag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            Toast.makeText(this, "Tag detected " + mytag.toString(), Toast.LENGTH_LONG).show();
        }
    }

    private void enableWriteMode(){
        writeMode = true;
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);

        IntentFilter tagDetected = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
        IntentFilter[] filters = new IntentFilter[] { tagDetected };

        adapter.enableForegroundDispatch(this, pendingIntent, filters, null);
    }
}
