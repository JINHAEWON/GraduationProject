package com.example.test;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class BuyingActivity  extends AppCompatActivity {
    TextView Final_Price;
    Bitmap bitmap;
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


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buying);



        //toolbar설정
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);//기본 제목을 없애줍니다.
        actionBar.setDisplayHomeAsUpEnabled(true); //뒤로가기 버튼


        //값 받기
        Intent intent = getIntent();
        final String name = intent.getExtras().getString("inputid_key");
        final String buying_item=intent.getExtras().getString("buying_list");
        System.out.println("구매하기에서 나온 "+buying_item);
        String BuyingItem[] = buying_item.split(",");

        //DB랑 연결하기
        //URL(IMAGE)이랑 가격
        String return_andDB5 = "none";
        try {
            BuyingReturn shop = new BuyingReturn();
            return_andDB5 = shop.execute(buying_item).get();
        }
        catch(Exception e) {
            System.out.println(e);
        }

        //imageView 동적 생성 (part 1)
       // LinearLayout layout = findViewById(R.id.shopping_image);
       // LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1);


        String example[] = return_andDB5.split("-");

        ArrayList<ImageButton> ar=null;

        String image_URL = example[0]; // 이미지 경로 받아오기
        String item_price = example[1]; // 가격 받아오기


        String image_list[] = image_URL.split(","); //이미지 split으로 자르기
        final String item_Price[] = item_price.split(","); // 제품가격 split으로 자르기


        //list view 생성
        final ListView listview;
        BuyingViewAdapter adapter = new BuyingViewAdapter();

        listview=(ListView) findViewById(R.id.buying_listview);
        listview.setAdapter(adapter);

        int itemSize = BuyingItem.length;
        for(int i=0; i<itemSize;i++) {
            final String url_basket=image_list[i];
            final String itemPrice=item_Price[i];

            Thread mThread = new Thread() {
                public void run() {
                    try {
                        URL url = new URL(url_basket);
                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                        conn.setDoInput(true);
                        conn.connect();

                        InputStream is = conn.getInputStream();
                        bitmap = BitmapFactory.decodeStream(is);

                    } catch (Exception e) {
                        System.out.println(e);
                    }
                }
            };
            mThread.start();


            try {
                //메인 스레드는 별도의 스레드 작업을 완료할때까지 대기
                //join을 호출하여 별도의 작업 스레드가 종료될때까지 메인 스레드는 기다림
                mThread.join();

                //imageView 동적 생성 (part 2)
                ImageView iv= new ImageView(this); //새로 추가할 imageView
                iv.setImageBitmap(bitmap); //imageview에 drawable객체 추가

                adapter.addItem(iv,BuyingItem[i],item_Price[i]);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        Integer price=0;
        for(int j=0;j<item_Price.length;j++) {
            price+=Integer.parseInt(item_Price[j]);
        }




        String pri = Integer.toString(price);
        Final_Price = findViewById(R.id.final_price);
        Final_Price.setText("총 결제 가격은 : "+pri);

        Button Buy_button = findViewById(R.id.buying);
        Button Cancel_button = findViewById(R.id.cancel);

        Buy_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // DB 에서 결제한 품목 삭제하기
                //DB랑 연결하기
                //URL(IMAGE)이랑 가격
                String return_andDB6 = "none";
                try {
                    BuyingReturn shop = new BuyingReturn();
                    return_andDB6 = shop.execute(buying_item).get();
                }
                catch(Exception e) {
                    System.out.println(e);
                }


                // 결제창으로 화면 넘어가면 됨
                Intent intent = new Intent(getApplicationContext(), OrderActivity.class);
                // 다음창으로 보내줄 값
                intent.putExtra("inputid_key",name);
                intent.putExtra("buying_list",buying_item);

                //액티비티 시작
                startActivity(intent);
            }
        });

        Cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // 결제창으로 화면 넘어가면 됨
                Intent intent = new Intent(getApplicationContext(), ShoppingBasket.class);
                // 다음창으로 보내줄 값
                intent.putExtra("inputid_key",name);

                //액티비티 시작
                startActivity(intent);
            }
        });



    }


}
