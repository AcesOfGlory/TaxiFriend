package com.example.taxifriend;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class PaymentActivity extends AppCompatActivity {

    String strContent,strContent2,strContent3,strContent4,strContent5,strContent6,strContent7;
    EditText content,content2,content3,content4,content5;
    TextView textContent,textContent2,textContent3,textContent4;
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
                content4 = findViewById(R.id.bookingDatePayment);
                spinnerContent = findViewById(R.id.priorityPayment);
                content5 = findViewById(R.id.promotionPayment);
                strContent = content.getText().toString();
                strContent2 = content2.getText().toString();
                strContent3 = content3.getText().toString();
                strContent4 = content4.getText().toString();
                strContent6 = spinnerContent.getSelectedItem().toString();
                strContent5 = content5.getText().toString();
                Intent intent = getIntent();
                strContent7 = intent.getStringExtra("payment");
                intent = new Intent(PaymentActivity.this, OverviewActivity.class);
                intent.putExtra("fromPayment",strContent);
                intent.putExtra("toPayment",strContent2);
                intent.putExtra("bookingTimePayment",strContent3);
                intent.putExtra("priorityPayment",strContent6);
                intent.putExtra("promotionPayment",strContent5);
                intent.putExtra("bookingDatePayment",strContent4);
                intent.putExtra("payment",strContent7);
                startActivity(intent);
            }
        });

        Spinner dropdown = findViewById(R.id.priorityPayment);
        String[] items = new String[]{"Normal", "VIP"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);

        Date d=new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm");
        String currentTimeString = sdf.format(d);
        SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy");
        String currentDateString = sdf1.format(d);
        content = findViewById(R.id.fromPayment);
        content2 = findViewById(R.id.toPayment);
        content3 = findViewById(R.id.bookingTimePayment);
        content3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar currTime = Calendar.getInstance();
                int hour = currTime.get(Calendar.HOUR_OF_DAY);
                int minute = currTime.get(Calendar.MINUTE);
                TimePickerDialog timePicker;
                timePicker = new TimePickerDialog(PaymentActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minuteOfHour) {
                        content3.setText( hourOfDay + ":" + minuteOfHour);
                    }
                },hour,minute,true);
                timePicker.setTitle("Select Time");
                timePicker.show();
            }
        });
        content4 = findViewById(R.id.bookingDatePayment);
        content4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar currTime = Calendar.getInstance();
                final int year = currTime.get(Calendar.YEAR);
                final int month = currTime.get(Calendar.MONTH);
                final int day = currTime.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePicker;
                datePicker = new DatePickerDialog(PaymentActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDayOfMonth) {
                        currTime.set(Calendar.YEAR,selectedYear);
                        currTime.set(Calendar.MONTH,selectedMonth);
                        currTime.set(Calendar.DAY_OF_MONTH,selectedDayOfMonth);
                        selectedMonth++;
                        content4.setText(selectedDayOfMonth + "/" + selectedMonth + "/" + selectedYear);
                    }
                }, day,month,year);
                datePicker.getDatePicker().setMinDate(System.currentTimeMillis());
                datePicker.setTitle("Select date");
                datePicker.show();
            }
        });

        TabLayout tabLayout = findViewById(R.id.tabLayout);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                textContent = findViewById(R.id.cardNumberTextField);
                content = findViewById(R.id.cardNumberPayment);
                textContent2 = findViewById(R.id.endDateTextField);
                content2 = findViewById(R.id.endDatePayment);
                textContent3 = findViewById(R.id.CWTextField);
                content3 = findViewById(R.id.CWPayment);
                int position = tab.getPosition();
                if(position == 1){
                    textContent.setVisibility(View.INVISIBLE);
                    content.setVisibility(View.INVISIBLE);
                    textContent2.setVisibility(View.INVISIBLE);
                    content2.setVisibility(View.INVISIBLE);
                    textContent3.setVisibility(View.INVISIBLE);
                    content3.setVisibility(View.INVISIBLE);
                }else {
                    textContent.setVisibility(View.VISIBLE);
                    content.setVisibility(View.VISIBLE);
                    textContent2.setVisibility(View.VISIBLE);
                    content2.setVisibility(View.VISIBLE);
                    textContent3.setVisibility(View.VISIBLE);
                    content3.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        Intent intent = getIntent();
        strContent = intent.getStringExtra("fromLocation");
        strContent2 = intent.getStringExtra("toLocation");
        content.setText(strContent);
        content2.setText(strContent2);
        content3.setText(currentTimeString);
        content4.setText(currentDateString);

    }
}
