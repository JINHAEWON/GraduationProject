package com.example.test;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity;

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
import android.widget.Toast;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    public static final int REQUEST_CODE_MENU=101;
    EditText etid;
    EditText etpw;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==REQUEST_CODE_MENU){
            Toast.makeText(getApplicationContext(),
                    "onActivityResult 메서드 호출됨.요청코드 : "+ requestCode + ", 결과코드"+resultCode, Toast.LENGTH_LONG).show();

            if(resultCode == RESULT_OK){
                String name=data.getStringExtra("name");
                Toast.makeText(getApplicationContext(),"응답으로 전달된 name :"+name,Toast.LENGTH_LONG).show();
            }
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = findViewById(R.id.btn1);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                etid = findViewById(R.id.inputid);
                etpw = findViewById(R.id.inputpw);

                String mainActivity_id = etid.getText().toString();
                String mainActivity_pw=etpw.getText().toString();
                String result="null";


                //이부분이 이클립스랑 연동하는 것
                try {
                   RegisterActivity task = new RegisterActivity();
                   result = task.execute(mainActivity_id, mainActivity_pw).get();
                }
                catch(Exception e) {
                    System.out.println(e);
                }
                /////////////////////////////////////////////////////


                if(result.equals("loginSuccess")) {
                    // 다음 화면으로 넘기기
                    Intent intent = new Intent(getApplicationContext(), ShoppingBasket.class);

                    // 아이디 값을 넘기기
                    intent.putExtra("inputid_key",mainActivity_id);
                    startActivity(intent);

                    //startActivityForResult는 시작한 액티비티를 통해 결과값을 받기 위해 사용
                    //여기에서 request_code_menu를 통해서 잘 받아왔는지 확인하는 작업을 거침
                    //참고한 사이트 : https://blog.naver.com/eunjiamy/221853888036
                    //startActivityForResult(intent, REQUEST_CODE_MENU);
                }

            }
        });

        Button button2 = findViewById(R.id.btn2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(), JoinActivity.class); // 화면공유 객체 생성(MainActivity.this에서 SubActivity.class로 이동)
                startActivityForResult(intent, REQUEST_CODE_MENU);
            }
        });



    }


}