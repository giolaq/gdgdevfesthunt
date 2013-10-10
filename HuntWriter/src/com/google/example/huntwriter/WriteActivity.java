package com.google.example.huntwriter;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.*;
import android.nfc.tech.Ndef;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import com.google.wolff.androidhunt.AHTag;
import com.google.wolff.androidhunt.HuntLite;
import com.google.wolff.androidhunt.TagListAdapter;

public class WriteActivity extends Activity implements OnItemClickListener {
    private static NfcAdapter mAdapter;
    private static PendingIntent mForegroundDispatchIntent;
    private static IntentFilter[] mForegroundDispatchIntentFilters;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Prepare foreground dispatch intents. All NFC tags will be allowed while this activity is on screen.
        mAdapter = NfcAdapter.getDefaultAdapter(this);
        mForegroundDispatchIntent = PendingIntent.getActivity(
                this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        IntentFilter intentFilter = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
        mForegroundDispatchIntentFilters = new IntentFilter[] {intentFilter};

        // Setup UI
        setContentView(R.layout.write_tag);
    }

    @Override
    public void onResume() {
        super.onResume();

		HuntLite hunt = HuntLite.getHunt(getResources(), getApplicationContext());

		ListView lv = (ListView)findViewById(R.id.tagsList);

		lv.setOnItemClickListener(this);
		lv.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		ArrayAdapter<AHTag> adapter = new TagListAdapter(getApplicationContext(), hunt);

		lv.setAdapter(adapter);

        mAdapter.enableForegroundDispatch(
                this, mForegroundDispatchIntent,mForegroundDispatchIntentFilters, null);
    }

    @Override
    public void onPause() {
        super.onPause();
        mAdapter.disableForegroundDispatch(this);
    }

    @Override
    public void onNewIntent(Intent intent) {
        Tag tagFromIntent = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        writeTag(tagFromIntent);
    }

    public void writeTag(Tag t) {
        Ndef ndef = Ndef.get(t);

		ListView lv = (ListView)findViewById(R.id.tagsList);
		if (lastChecked == -1) {
			Toast.makeText(getApplicationContext(), "No tag selected", Toast.LENGTH_SHORT).show();
			return;
		}
		AHTag aht = (AHTag) lv.getItemAtPosition(lastChecked);

        try {
            ndef.connect();
            ndef.writeNdefMessage(createTagPayload(aht));
           // ndef.makeReadOnly();
            ndef.close();
            Toast.makeText(this, "Tag created", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error creating tag", Toast.LENGTH_SHORT).show();
        } catch (FormatException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error creating tag", Toast.LENGTH_SHORT).show();
        }
    }

    private static NdefMessage createTagPayload(AHTag aht) throws UnsupportedEncodingException {
        return new NdefMessage(
                new NdefRecord[] {
                        // We're writing a URI whose intent will be intercepted by your app.
                        NdefRecord.createUri("https://nfchunt.appspot.com/f?c=" + aht.id )});
    }

    int lastChecked = -1;

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {
		CheckedTextView tv = (CheckedTextView) arg1;
		tv.setChecked(true);

		lastChecked = position;
	}

}