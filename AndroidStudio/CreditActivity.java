package com.example.test;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class CreditActivity extends AppCompatActivity {
    public static final int REQUEST_CODE_MENU = 101;

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


    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.paybycredit);

        //toolbar설정
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);//기본 제목을 없애줍니다.
        actionBar.setDisplayHomeAsUpEnabled(true); //뒤로가기 버튼



        Intent intent = getIntent();
        final String name = intent.getExtras().getString("inputid_key");
        final String buying_item = intent.getExtras().getString("buying_list");

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitDiskReads().permitDiskWrites().permitNetwork().build());

        Button button = findViewById(R.id.credit);
        button.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){

                String phoneNo;
                String sms=name+"님의 구매한 아이템 목록 : "+buying_item;


                //<--DB연결하는 부분 작성 -->
                //////////////////////////////

                String return_andDB7 = "none";
                try {
                    ConnectSMS shop = new ConnectSMS();
                    return_andDB7 = shop.execute(name).get();

                }
                catch(Exception e) {
                    System.out.println(e);
                }
                phoneNo=return_andDB7;


                ActivityCompat.requestPermissions(CreditActivity.this, new String[]{Manifest.permission.SEND_SMS}, 1001);

                try {
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(phoneNo, null, sms, null, null);
                    System.out.println("구매정보 "+phoneNo+"로 메세지 보냈습니다.");
                } catch (Exception e) {
                    System.out.println(e);
                }



                //<!---이메일 보내는 부분 -->
                String return_andDB8 = "none";
                try {
                    ConnectEmail shop = new ConnectEmail();
                    return_andDB8 = shop.execute(name).get();

                }
                catch(Exception e) {
                    System.out.println(e);
                }

                String emailSend;
                emailSend=return_andDB8;

                SendMail mailServer = new SendMail();
                mailServer.sendSecurityCode(getApplicationContext(), emailSend,name,buying_item);



                String result = "none";
                //이클립스랑 연동
                try {
                    OrderRegisterActivity order = new OrderRegisterActivity();
                    result = order.execute(name, buying_item).get();
                } catch (Exception e) {
                    System.out.println("구매실패");
                }

                if (result.equals("ok")) {
                    // 다음 화면으로 넘기기
                    Toast.makeText(getApplicationContext(), "구매완료", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), ShoppingBasket.class);
                    intent.putExtra("inputid_key", name);
                    startActivityForResult(intent, REQUEST_CODE_MENU);
                } else {
                    Toast.makeText(getApplicationContext(), " 구매실패. 다시 시도하세요 ", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
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