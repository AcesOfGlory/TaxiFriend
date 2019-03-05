package com.example.taxifriend;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class OverviewActivity extends AppCompatActivity {

    String strContent,strContent2,strContent3,strContent4,strContent5;
    EditText content,content2,content3,content4,content5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);

        Button btn = (Button)findViewById(R.id.buttonBooking);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(OverviewActivity.this, MapsActivity.class));
            }
        });

        content = findViewById(R.id.fromOverview);
        content2 = findViewById(R.id.toOverview);
        content3 = findViewById(R.id.bookingTimeOverview);
        content4 = findViewById(R.id.priorityOverview);
        content5 = findViewById(R.id.promotionOverview);
        Intent intent = getIntent();
        strContent = intent.getStringExtra("fromPayment");
        strContent2 = intent.getStringExtra("toPayment");
        strContent3 = intent.getStringExtra("bookingTimePayment");
        strContent4 = intent.getStringExtra("priorityPayment");
        strContent5 = intent.getStringExtra("promotionPayment");
        content.setText(strContent);
        content2.setText(strContent2);
        content3.setText(strContent3);
        content4.setText(strContent4);
        content5.setText(strContent5);

    }
}

