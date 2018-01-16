package com.example.shailendrathakur.textme;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

/**
 * Created by Shailendra Thakur on 16-01-2018.
 */

public class Draft extends Activity {

    ListView inbx;
    SimpleCursorAdapter adapter;
    @Override
    public void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
        setContentView(R.layout.inbox);
        inbx = (ListView)findViewById(R.id.lvMsg);
    }
    private void loadInbox()
    {
        Uri inboxURI = Uri.parse("content://sms/draft");

        String[] reqCols = new String[] { "_id", "address", "body" };

        ContentResolver cr = getContentResolver();

        Cursor c = cr.query(inboxURI, reqCols, null, null, null);

        adapter = new SimpleCursorAdapter(this, R.layout.row, c,
                new String[] { "body", "address" }, new int[] {
                R.id.msg, R.id.lbl });
        inbx.setAdapter(adapter);
    }
}
