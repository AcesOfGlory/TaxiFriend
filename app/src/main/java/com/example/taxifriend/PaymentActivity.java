package com.example.taxifriend;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.text.SimpleDateFormat;
import java.util.Date;

public class PaymentActivity extends AppCompatActivity {

    String strContent,strContent2,strContent3,strContent4,strContent5;
    EditText content,content2,content3,content4,content5;
    Spinner spinnerContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        Button btn = (Button)findViewById(R.id.buttonOver);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                content = findViewById(R.id.fromPayment);
                content2 = findViewById(R.id.toPayment);
                content3 = findViewById(R.id.bookingTimePayment);
                spinnerContent = findViewById(R.id.priorityPayment);
                content5 = findViewById(R.id.promotionPayment);
                strContent = content.getText().toString();
                strContent2 = content2.getText().toString();
                strContent3 = content3.getText().toString();
                strContent4 = spinnerContent.getSelectedItem().toString();
                strContent5 = content5.getText().toString();
                Intent intent = new Intent(PaymentActivity.this, OverviewActivity.class);
                intent.putExtra("fromPayment",strContent);
                intent.putExtra("toPayment",strContent2);
                intent.putExtra("bookingTimePayment",strContent3);
                intent.putExtra("priorityPayment",strContent4);
                intent.putExtra("promotionPayment",strContent5);
                startActivity(intent);
            }
        });

        Spinner dropdown = findViewById(R.id.priorityPayment);
        String[] items = new String[]{"Normal", "VIP"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);

        Date d=new Date();
        SimpleDateFormat sdf=new SimpleDateFormat("hh:mm a");
        String currentDateTimeString = sdf.format(d);
        content = findViewById(R.id.fromPayment);
        content2 = findViewById(R.id.toPayment);
        content3 = findViewById(R.id.bookingTimePayment);
        Intent intent = getIntent();
        strContent = intent.getStringExtra("fromLocation");
        strContent2 = intent.getStringExtra("toLocation");
        content.setText(strContent);
        content2.setText(strContent2);
        content3.setText(currentDateTimeString);


    }
}
