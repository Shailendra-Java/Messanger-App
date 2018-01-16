package com.example.shailendrathakur.textme;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    Button inbox,sent,draft,create;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inbox = (Button)findViewById(R.id.inbx);
        sent = (Button)findViewById(R.id.sent);
        draft = (Button)findViewById(R.id.draft);
        create = (Button)findViewById(R.id.create);

        inbox.setOnClickListener(this);
        sent.setOnClickListener(this);
        draft.setOnClickListener(this);
        create.setOnClickListener(this);
    }

    @Override
    public void onClick(View view)
    {
        switch(view.getId())
        {
            case R.id.inbx:
                intent = new Intent(getApplicationContext(),Inbox.class);
                startActivity(intent);
                break;
            case R.id.sent:
                intent = new Intent(getApplicationContext(),Sent.class);
                startActivity(intent);
                break;
            case R.id.draft:
                intent = new Intent(getApplicationContext(),Draft.class);
                startActivity(intent);
                break;
            case R.id.create:
                intent = new Intent(getApplicationContext(),Create.class);
                startActivity(intent);
                break;
        }
    }
}
