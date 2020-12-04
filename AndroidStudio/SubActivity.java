package com.example.test;
import android.app.Activity;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

import static java.security.AccessController.getContext;



public class SubActivity extends  AppCompatActivity {

    String nameID;
    String Numbering="TESTING!@!@";

    //view Objects
    Button buttonScan;
    Button cancel_button,adding_button;
    TextView textViewName, textViewAddress, textViewResult;

    //qr code scanner object
    private IntentIntegrator qrScan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout2);



        //넘겨온 값 받기
        Intent intent = getIntent();
        final String name = intent.getExtras().getString("inputid_key");
        nameID=name;


        System.out.println("받아온값:"+name);

        //View Objects
        buttonScan = (Button) findViewById(R.id.buttonScan);
        textViewName = (TextView) findViewById(R.id.textViewName);
        textViewAddress = (TextView) findViewById(R.id.textViewAddress);
        textViewResult = (TextView)  findViewById(R.id.textViewResult);
        adding_button=(Button)findViewById(R.id.adding_button);
        cancel_button=(Button)findViewById(R.id.cancle_button);


        //intializing scan object
        qrScan = new IntentIntegrator(this);

        //button onClick
        buttonScan.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //scan option
                qrScan.setPrompt("Scanning...");
                //qrScan.setOrientationLocked(false);
                qrScan.initiateScan();
            }
        });



        cancel_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ShoppingBasket.class);
                intent.putExtra("inputid_key",nameID);
                startActivity(intent);
            }
        });

        adding_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String andDB3_result;
                try {
                    ReturnQRCode QRCode = new ReturnQRCode();
                    Numbering = (String)textViewResult.getText();
                    andDB3_result = QRCode.execute(nameID, Numbering).get();
                    System.out.println("QRCode의 정보를 DB로 전달 완료");
                }
                catch(Exception e) {
                    System.out.println("DB로 전달 실패!!!");
                }



                Intent intent = new Intent(getApplicationContext(), ShoppingBasket.class);
                intent.putExtra("inputid_key",nameID);
                startActivity(intent);
            }
        });

    }

    //Getting the scan results
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            //qrcode 가 없으면
            if (result.getContents() == null) {
                Toast.makeText(SubActivity.this, "취소!", Toast.LENGTH_SHORT).show();
            } else {
                //qrcode 결과가 있으면
                    Toast.makeText(SubActivity.this, "스캔완료!", Toast.LENGTH_SHORT).show();
                    try {
                    //data를 json으로 변환
                    JSONObject obj = new JSONObject(result.getContents());
                    textViewName.setText(obj.getString("name"));
                    textViewAddress.setText(obj.getString("address"));

                    Numbering=obj.getString("name");
                    ////////////////////////////////////////////////////////
                        /*
                    //장바구니 담기 눌렀을 경우
                    adding_button.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            String andDB3_result;
                            try {
                                ReturnQRCode QRCode = new ReturnQRCode();
                                andDB3_result = QRCode.execute(nameID, Numbering).get();
                                System.out.println("QRCode의 정보를 DB로 전달 완료");
                            }
                            catch(Exception e) {
                                System.out.println("DB로 전달 실패!!!");
                            }



                            Intent intent = new Intent(getApplicationContext(), ShoppingBasket.class);
                            intent.putExtra("inputid_key",nameID);
                            startActivity(intent);
                        }
                    });

                    /// *//////////////////////////////////////////////////////


                } catch (JSONException e) {
                    e.printStackTrace();
                    //Toast.makeText(MainActivity.this, result.getContents(), Toast.LENGTH_LONG).show();
                    textViewResult.setText(result.getContents());
                }
            }

        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}


