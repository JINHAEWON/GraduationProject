package com.example.test;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ReturnQRCode extends AsyncTask<String, Void, String> {
    String sendMsg, receiveMsg;

    public ReturnQRCode() {
        System.out.println("QRCode에 대한 생성자가 만들어졌음 ");
    }

    @Override
    protected String doInBackground(String... strings) {
        try {
            String str;

            // 접속할 서버 주소 (이클립스에서 android.jsp 실행시 웹브라우저 주소)
            URL url = new URL("http://192.168.8.59:31231/shop/andDB3.jsp");

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestMethod("POST");
            OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());

            // 전송할 데이터. GET 방식으로 작성
            sendMsg = "et_id=" + strings[0] + "&ModelNumber=" + strings[1];

            osw.write(sendMsg);
            osw.flush();


            //jsp와 통신 성공 시 수행
            if (conn.getResponseCode() == conn.HTTP_OK) {
                System.out.println("jsp와 통신 성공 시 수행");
                InputStreamReader tmp = new InputStreamReader(conn.getInputStream(), "UTF-8");
                BufferedReader reader = new BufferedReader(tmp);
                StringBuffer buffer = new StringBuffer();

                // jsp에서 보낸 값을 받는 부분
                while ((str = reader.readLine()) != null) {
                    buffer.append(str);
                    if(str.equals("<body>")) receiveMsg = reader.readLine();
                    System.out.println("str:"+str);
                }
                //receiveMsg = buffer.toString();

            } else {
                receiveMsg="통신 실패";
                System.out.println("통신 실패");
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //jsp로부터 받은 리턴 값
        return receiveMsg;
    }

}