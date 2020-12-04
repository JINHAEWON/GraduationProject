package com.example.test;


import android.content.Context;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import javax.mail.MessagingException;
import javax.mail.SendFailedException;
public class SendMail extends AppCompatActivity {
    String user = "bluemintjoa@gmail.com"; // 보내는 계정의 id
    String password = "krap1021!*"; // 보내는 계정의 pw
    public void sendSecurityCode(Context context, String sendTo,String name,String buying_item) {
        try {
            GmailSender gMailSender = new GmailSender(user, password);

            String title = name+"님의 주문내역입니다";
            String body = name+"님이 주문하신 물품 :"+buying_item;

            gMailSender.sendMail(title, body, sendTo);
            Toast.makeText(context, "이메일을 성공적으로 보냈습니다.", Toast.LENGTH_SHORT).show();
        }
        catch (SendFailedException e) {
            Toast.makeText(context, "이메일 형식이 잘못되었습니다.", Toast.LENGTH_SHORT).show();
        }
        catch (MessagingException e) {
            e.printStackTrace();
            Toast.makeText(context, "인터넷 연결을 확인해주십시오", Toast.LENGTH_SHORT).show();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}