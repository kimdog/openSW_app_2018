package com.together.kimdog91.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.together.kimdog91.myapplication.Adaptor.ListViewAdaptor;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MoimActivity extends BaseActivity {

    // 유저 정보
    private int uid;
    private String name;
    private String phone;

    TextView moimTitleText;
    Button moimAddBtn;

    private NetworkManager nm;

    private ListView listView;
    private ListViewAdaptor adaptor;

    Bitmap tempBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moim);

        // 네트워크 매니저
        nm = new NetworkManager();
        nm.setActivity(this);

        // 유저 정보 세팅
        Intent intent = getIntent();
        uid = intent.getIntExtra("uid", -1);
        name = intent.getStringExtra("name");
        phone = intent.getStringExtra("phone");

        // 변수 초기화
        adaptor = new ListViewAdaptor();
        listView = (ListView) findViewById(R.id.List_view);

        // 어뎁터 할당
        listView.setAdapter(adaptor);

        moimTitleText = (TextView) findViewById(R.id.MoimTitle);
        moimAddBtn = (Button) findViewById(R.id.addMoimBtn);

        getMoimList(this);

    }

    private void getMoimList(Activity activity) {
        // 데이터 세팅
        String[] colums = { "uid" };
        String[] data = { String.valueOf(uid) };

        try {
            // 서버와 통신
            JSONArray result = nm.executePost("/api/moims", colums, data);
            progressON(activity, "loading...");

            for(int i=0; i<result.length(); i++) {
                JSONObject resultObject = result.getJSONObject(i);

                String mname = resultObject.getString("mname");
                int pid = resultObject.getInt("pid");
                int range = resultObject.getInt("range");
                int cate_id = resultObject.getInt("cate_id");
                int pic_id = resultObject.getInt("pic_id");
                String pname = resultObject.getString("pname");
                String cate_name = resultObject.getString("cate_name");
                String img_url = resultObject.getString("url");

                String content = "["+ pname +"] 반경: " + range +"m, " + cate_name;
                // 어뎁터를 통한 값 전달
                adaptor.addVO(img_url, getImageFromUrl(activity, img_url), mname, content);

            }

            moimTitleText.setText(name +"의 Moim ("+ adaptor.getCount() + ")");

        } catch ( Exception e ) {
            e.printStackTrace();
        } finally {
            progressOFF();
        }
    }


    private Bitmap getImageFromUrl(final Activity activity, final String _url) {

        Thread mThread = new Thread() {
            @Override
            public void run() {
                try{

                    URL url = new URL(_url);

                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setDoInput(true); // 서버로부터 응답 수신
                    conn.connect();

                    InputStream is = conn.getInputStream();
                    tempBitmap = BitmapFactory.decodeStream(is); // Bitmap으로 변환

                } catch ( MalformedURLException e ) {
                    e.printStackTrace();
                } catch ( IOException e ) {
                    e.printStackTrace();
                }
            }
        };

        mThread.start();
        try {
            mThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return tempBitmap;
    }

}
