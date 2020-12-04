package com.example.test;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.InputStream;
import java.util.ArrayList;

public class BuyingViewAdapter extends BaseAdapter {

    // Adapter에 추가된 데이터를 저장하기 위한 ArrayList
    private ArrayList<BuyingViewItem> listViewItemList = new ArrayList<BuyingViewItem>() ;

    // ListViewAdapter의 생성자
    public BuyingViewAdapter() {

    }

    // Adapter에 사용되는 데이터의 개수를 리턴. : 필수 구현
    @Override
    public int getCount() {
        return listViewItemList.size() ;
    }

    // position에 위치한 데이터를 화면에 출력하는데 사용될 View를 리턴. : 필수 구현
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        // "listview_item" Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.buying_item, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        ImageView iconImageView = (ImageView) convertView.findViewById(R.id.imageView1) ;
        TextView textTextView = (TextView) convertView.findViewById(R.id.textView1) ;
        TextView textTextView2 = (TextView) convertView.findViewById(R.id.textView2) ;


        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        BuyingViewItem listViewItem = listViewItemList.get(position);

        // 아이템 내 각 위젯에 데이터 반영
        ImageView image = listViewItem.getIcon();
        BitmapDrawable preBitmap=(BitmapDrawable)image.getDrawable();
           /*
            if(preBitmap!=null) {
                Bitmap bm = preBitmap.getBitmap();
                if(bm!=null) {
                    bm.recycle();
                }
            }
*/
        iconImageView.setImageDrawable(preBitmap);
        textTextView.setText(listViewItem.getText());
        textTextView2.setText(listViewItem.getText2());

        return convertView;
    }

    // 지정한 위치(position)에 있는 데이터와 관계된 아이템(row)의 ID를 리턴. : 필수 구현
    @Override
    public long getItemId(int position) {
        return position ;
    }

    // 지정한 위치(position)에 있는 데이터 리턴 : 필수 구현
    @Override
    public Object getItem(int position) {
        return listViewItemList.get(position) ;
    }

    // 아이템 데이터 추가를 위한 함수. 개발자가 원하는대로 작성 가능.
    public void addItem(ImageView icon, String text,String text2) {
        BuyingViewItem item = new BuyingViewItem();

        item.setIcon(icon);
        item.setText(text);
        item.setText2(text2);


        listViewItemList.add(item);
    }
}