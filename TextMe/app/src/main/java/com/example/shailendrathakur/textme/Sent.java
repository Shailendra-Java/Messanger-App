package com.example.shailendrathakur.textme;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

/**
 * Created by Shailendra Thakur on 16-01-2018.
 */

public class Sent extends Activity {

    public static final int MY_PERMISSIONS_REQUEST_READ_SMS = 0;
    ListView inbx;
    SimpleCursorAdapter adapter;
    @Override
    public void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
        setContentView(R.layout.inbox);
        inbx = (ListView)findViewById(R.id.lvMsg);

        if (ContextCompat.checkSelfPermission(Sent.this,
                Manifest.permission.READ_SMS)
                != PackageManager.PERMISSION_GRANTED)
        {
            if (ActivityCompat.shouldShowRequestPermissionRationale(Sent.this,
                    Manifest.permission.READ_SMS))
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setPositiveButton("App wants permission for Read Draft", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Toast.makeText(Sent.this,"Thank You",Toast.LENGTH_LONG).show();
                    }
                });
            }
            else
            {
                ActivityCompat.requestPermissions(Sent.this,
                        new String[]{Manifest.permission.READ_SMS},
                        MY_PERMISSIONS_REQUEST_READ_SMS);

            }
        }
        loadSent();
    }
    private void loadSent()
    {
        Uri inboxURI = Uri.parse("content://sms/sent");

        String[] reqCols = new String[] { "_id", "address", "body" };

        ContentResolver cr = getContentResolver();

        Cursor c = cr.query(inboxURI, reqCols, null, null, null);

        adapter = new SimpleCursorAdapter(this, R.layout.row, c,
                new String[] { "body", "address" }, new int[] {
                R.id.msg, R.id.lbl });
        inbx.setAdapter(adapter);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_SMS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    try {
                        loadSent();
                    }catch(Exception exp){
                        Toast.makeText(Sent.this, "Some problem occurs", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(Sent.this,"Permission Denied!",Toast.LENGTH_LONG).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }
}
