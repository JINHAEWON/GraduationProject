package com.example.test;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Queue;

public class ShoppingBasket extends AppCompatActivity {
    TextView value_txt;
    TextView value_txt2;
    ImageView imgView;
    Bitmap bitmap;
    String name_toolbar;

    Toolbar toolbar;
    ActionBar actionBar;


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shopping);

        value_txt=(TextView)findViewById(R.id.textView);


        //toolbar설정
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);//기본 제목을 없애줍니다.
        actionBar.setDisplayHomeAsUpEnabled(true); //뒤로가기 버튼


        //mainactivity에서 ID값을 넘겨받음
        Intent intent = getIntent();
        final String name = intent.getExtras().getString("inputid_key"); //mainActivity에서 input_key로 보낸 값을 받아와랑 !
        System.out.println(name);
        value_txt.setText(name.concat("님 환영합니다."));

        name_toolbar=name;

        ////////////////////////////////////////////////////////
        //여기에서 DB 조회한 다음에 DB에 있는 이미지 파일 불러오기//

        //이미지 받아오는 것
        // image_URL = https://naver.com,https://daum.com,https://google.com~~~~~~이렇게
        String return_andDB4 = "none";
        try {
            ShoppingBasketReturn shop = new ShoppingBasketReturn();
            return_andDB4 = shop.execute(name).get();
        }
        catch(Exception e) {
            System.out.println(e);
        }


        if(Objects.equals(return_andDB4,"null-null"))
        {
            System.out.println("******빈 장바구니");
            value_txt2=(TextView)findViewById(R.id.textView2);
            value_txt2.setText(("장바구니가 비어있습니다."));

        }

        else {
            //imageView 동적 생성 (part 1)
         //   LinearLayout layout = findViewById(R.id.shopping_image);
         //   LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1);


            String example[] = return_andDB4.split("-");

            ArrayList<ImageButton> ar = null;

            String item_name = example[0]; // 상표명 값 받아오기
            System.out.println("여기에서 값을 찍어봆시다"+item_name);
            String image_URL = example[1]; // 이미지 경로 받아오기

            String image_list[] = image_URL.split(","); //이미지 split으로 자르기
            final String item_list[] = item_name.split(","); // 제품명 split으로 자르기


            //리스트뷰 여기서 생성
            final ListView listview;
            ListViewAdapter adapter = new ListViewAdapter();

            listview = (ListView) findViewById(R.id.listView);
            listview.setAdapter(adapter);


            for (int i = 0; i < image_list.length; i++) {
                final String url_basket = image_list[i];
                final String itemName = item_list[i];

                Thread mThread = new Thread() {
                    public void run() {
                        try {
                            //URL url = new URL("https://ifh.cc/g/GiewmX.jpg");
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

                    // 기존 라인 제거
                    // imgView.setImageBitmap(bitmap);

                    //imageView 동적 생성 (part 2)
                    ImageView iv = new ImageView(this); //새로 추가할 imageView
                    iv.setImageBitmap(bitmap); //imageview에 drawable객체 추가

                    ///////////////////////////////////////
                    //여기에서
                    adapter.addItem(iv, item_list[i]);


                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            ////////////////////////////////////////////////////////
            listview.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);


            // 여기부턴 버튼
            ////https://blog.naver.com/cosmosjs/221347564725 참조한 사이트
            Button B_button = findViewById(R.id.buying_button);

            //구매하기 버튼을 눌렀을 경우에
            B_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    SparseBooleanArray checkedItems = listview.getCheckedItemPositions();
                    Queue<String> queue = new LinkedList<>();
                    for (int i = 0; i < item_list.length; i++) {
                        if (checkedItems.get(i)) {
                            queue.add(item_list[i]); // i번째의 item이 체크 되어있음
                        }
                    }

                    String buying = null;
                    int num = queue.size();
                    for (int i = 0; i < num; i++) {
                        String s = queue.poll();
                        if (i == 0) {
                            buying = s;
                            continue;
                        }
                        buying += "," + s;

                    }

                    final String buying_item = buying;


                    // 결제창으로 화면 넘어가면 됨
                    Intent intent = new Intent(getApplicationContext(), BuyingActivity.class);
                    // 그때 구매했던 item과 회원 id를 보내주면됨
                    intent.putExtra("inputid_key", name);
                    intent.putExtra("buying_list", buying_item);

                    //액티비티 시작
                    startActivity(intent);
                }
            });

        }
        /*
        //스캔하기 버튼을 눌렀을 경우에
        Button S_button = findViewById(R.id.scan_button);

        S_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 스캔창으로 화면 넘어가면 됨
                Intent intent = new Intent(getApplicationContext(), SubActivity.class);
                // DB에서 조회하기 위해서 회원 id를 보내주면됨
                intent.putExtra("inputid_key",name);

                //액티비티 시작
                startActivity(intent);
            }
        });
        */

    }
    //toolbar를 위한 코드
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.scan_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.logout:
                Intent intent_logout = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent_logout);
                break;
            case R.id.action_scan:
                Intent intent_scan = new Intent(getApplicationContext(), SubActivity.class);
                intent_scan.putExtra("inputid_key",name_toolbar);
                startActivity(intent_scan);
                break;
            case android.R.id.home:
                //select back button
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


}