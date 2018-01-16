package com.example.shailendrathakur.textme;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Shailendra Thakur on 16-01-2018.
 */

public class Create extends Activity implements View.OnClickListener{

    public static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 0;
    EditText mobileno, message;
    Button sendsms;
    TextView stat;
    BroadcastReceiver sendBroadcastReceiver = new SentReceiver();
    BroadcastReceiver deliveryBroadcastReciever = new DeliverReceiver();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create);

        mobileno = (EditText) findViewById(R.id.editText1);
        message = (EditText) findViewById(R.id.editText2);
        stat = (TextView)findViewById(R.id.status);
        sendsms = (Button) findViewById(R.id.button1);
        sendsms.setOnClickListener(this);

        if (ContextCompat.checkSelfPermission(Create.this,
                Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED)
        {
            if (ActivityCompat.shouldShowRequestPermissionRationale(Create.this,
                    Manifest.permission.SEND_SMS))
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setPositiveButton("App wants permission for Sending SMS", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Toast.makeText(Create.this,"Thank You",Toast.LENGTH_LONG).show();
                    }
                });
            }
            else
            {
                ActivityCompat.requestPermissions(Create.this,
                        new String[]{Manifest.permission.SEND_SMS},
                        MY_PERMISSIONS_REQUEST_SEND_SMS);

            }
        }

    }
    @Override
    public void onClick(View arg0) {
        String SENT = "SMS_SENT";
        String DELIVERED = "SMS_DELIVERED";

        String no = mobileno.getText().toString();
        String msg = message.getText().toString();

        PendingIntent sentPI = PendingIntent.getBroadcast(Create.this, 0, new Intent(SENT), 0);
        PendingIntent deliveredPI = PendingIntent.getBroadcast(Create.this, 0, new Intent(DELIVERED), 0);

        registerReceiver(sendBroadcastReceiver, new IntentFilter(SENT));
        registerReceiver(deliveryBroadcastReciever, new IntentFilter(DELIVERED));

        if (ContextCompat.checkSelfPermission(Create.this,
                Manifest.permission.SEND_SMS)
                == PackageManager.PERMISSION_GRANTED) {
            SmsManager sms = SmsManager.getDefault();
            sms.sendTextMessage(no, null, msg, sentPI, deliveredPI);
        }

    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_SEND_SMS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    try {
                        onClick(sendsms);
                    }catch(Exception exp){
                        Toast.makeText(Create.this, "Some problem occurs", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(Create.this,"Permission Denied!",Toast.LENGTH_LONG).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }
    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        try {
            unregisterReceiver(sendBroadcastReceiver);
            unregisterReceiver(deliveryBroadcastReciever);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    class DeliverReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent arg1) {
            switch (getResultCode()) {
                case Activity.RESULT_OK:
                    stat.setText("SMS Delivered");
                    break;
                case Activity.RESULT_CANCELED:
                    stat.setText("SMS not delivered");
                    break;
            }

        }
    }

    class SentReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent arg1) {
            switch (getResultCode()) {
                case Activity.RESULT_OK:
                    stat.setText("SMS Sent");
                    break;
                case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                    stat.setText("Generic failure");
                    break;
                case SmsManager.RESULT_ERROR_NO_SERVICE:
                    stat.setText("No service");
                    break;
                case SmsManager.RESULT_ERROR_NULL_PDU:
                    stat.setText("Null PDU");
                    break;
                case SmsManager.RESULT_ERROR_RADIO_OFF:
                    stat.setText("Radio off");
                    break;
            }

        }

    }
}
