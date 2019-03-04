package com.example.taxifriend;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class PaymentActivity extends AppCompatActivity {

    String strContent,strContent2;
    EditText content,content2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        Button btn = (Button)findViewById(R.id.buttonOver);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PaymentActivity.this, OverviewActivity.class));
            }
        });
        content = findViewById(R.id.fromPayment);
        content2 = findViewById(R.id.toPayment);
        Intent intent = getIntent();
        strContent = intent.getStringExtra("fromLocation");
        strContent2 = intent.getStringExtra("toLocation");
        content.setText(strContent);
        content2.setText(strContent2);


    }
}
