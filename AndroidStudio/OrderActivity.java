package com.example.test;

import android.Manifest;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class OrderActivity extends AppCompatActivity {

    Toolbar toolbar;
    ActionBar actionBar;
    //toolbar를 위한 코드
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.basic_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.logout:
                Intent intent_logout = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent_logout);
                break;
            case android.R.id.home:
                //select back button
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.orderinfo);

        //toolbar설정
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);//기본 제목을 없애줍니다.
        actionBar.setDisplayHomeAsUpEnabled(true); //뒤로가기 버튼



        Intent intent = getIntent();
        final String name = intent.getExtras().getString("inputid_key");
        final String buying_item=intent.getExtras().getString("buying_list");

        Button cbutton = (Button)findViewById(R.id.credit);
        cbutton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){

                Intent intent = new Intent(getApplicationContext(),CreditActivity.class);
                intent.putExtra("inputid_key",name);
                intent.putExtra("buying_list",buying_item);

                startActivity(intent);
            }
        });

        Button cashbutton = (Button)findViewById(R.id.cash);
        cashbutton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){

                Intent intent = new Intent(getApplicationContext(),CashActivity.class);
                intent.putExtra("inputid_key",name);
                intent.putExtra("buying_list",buying_item);

                startActivity(intent);
            }
        });

    }


}

    /*
        String phoneNo = "650-555-1212";
        String sms = "안녕해원이야";

        @Override
        protected void onCreate (Bundle savedInstanceState){
            super.onCreate(savedInstanceState);
            setContentView(R.layout.paybycredit);

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(OrderActivity.this, new String[]{Manifest.permission.SEND_SMS}, 1001);
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, 1001);
            }

            Button button = findViewById(R.id.credit);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        SmsManager smsManager = SmsManager.getDefault();
                        smsManager.sendTextMessage(phoneNo, null, sms, null, null);
                        System.out.println("메세지 보냈당");
                    } catch (Exception e) {
                        System.out.println(e);
                    }


                }

            });


        }
        @Override
        public void onRequestPermissionsResult ( int requestCode, @NonNull String[] permissions,
        @NonNull int[] grantResults){
            switch (requestCode) {
                case 1001: {
                    // If request is cancelled, the result arrays are empty.
                    if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        System.out.println("sms 허가");
                    }
                }
            }
        }

    }
*/


