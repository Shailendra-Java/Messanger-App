package com.example.shailendrathakur.textme;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Shailendra Thakur on 16-01-2018.
 */

public class Create extends Activity {

    EditText mobileno, message;
    Button sendsms;
    TextView stat;
    BroadcastReceiver sendBroadcastReceiver = new SentReceiver();
    BroadcastReceiver deliveryBroadcastReciever = new DeliverReceiver();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mobileno = (EditText) findViewById(R.id.editText1);
        message = (EditText) findViewById(R.id.editText2);
        stat = (TextView)findViewById(R.id.status);
        sendsms = (Button) findViewById(R.id.button1);
        sendsms.setOnClickListener(new View.OnClickListener() {

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

                SmsManager sms = SmsManager.getDefault();
                sms.sendTextMessage(no, null, msg, sentPI, deliveredPI);

                Toast.makeText(Create.this, "Message sent successfully", Toast.LENGTH_SHORT).show();
            }
        });
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
