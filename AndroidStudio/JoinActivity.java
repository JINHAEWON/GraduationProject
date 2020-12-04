package com.example.test;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class JoinActivity extends AppCompatActivity {

    public static final int REQUEST_CODE_MENU=101;

    EditText et_id;
    EditText et_pw;
    EditText et_pwc;
    EditText et_name;
    EditText et_email;
    EditText et_phone;

    Toolbar toolbar;
    ActionBar actionBar;

    //toolbar를 위한 코드
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.join_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                //select back button
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.joinlayout);

        //toolbar설정
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);//기본 제목을 없애줍니다.
        actionBar.setDisplayHomeAsUpEnabled(true); //뒤로가기 버튼


        Button button = findViewById(R.id.btn_register);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_id = findViewById(R.id.et_id);
                et_pw = findViewById(R.id.et_pass);
                //et_pwc = findViewById(R.id.et_passck);
                et_name = findViewById(R.id.et_name);
                et_email = findViewById(R.id.et_email);
                et_phone = findViewById(R.id.et_phone);

                String joinActivity_id = et_id.getText().toString();
                String joinActivity_pw = et_pw.getText().toString();
                String joinActivity_name = et_name.getText().toString();
                String joinActivity_email = et_email.getText().toString();
                String joinActivity_phone = et_phone.getText().toString();
                //String joinActivity_pwc = et_pwc.getText().toString();
                String result = "none";

                //이부분이 이클립스랑 연동하는 것
                try {
                    JoinRegisterActivity join = new JoinRegisterActivity();
                    result = join.execute(joinActivity_id, joinActivity_pw, joinActivity_name, joinActivity_email, joinActivity_phone).get();
                }
                catch(Exception e) {
                    System.out.println("회원가입 실패");
                }

                if(result.equals("success")) {
                    // 다음 화면으로 넘기기
                    Toast.makeText( getApplicationContext(), "회원가입 완료", Toast.LENGTH_SHORT ).show();
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivityForResult(intent, REQUEST_CODE_MENU);
                }
                else if(result.equals("fail")){
                    Toast.makeText( getApplicationContext(), " 가입실패. 중복아이디 존재 ", Toast.LENGTH_SHORT ).show();
                }
            }
        });
    }
}