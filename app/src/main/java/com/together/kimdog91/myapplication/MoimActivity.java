package com.together.kimdog91.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
;import com.together.kimdog91.myapplication.Adaptor.ListViewAdaptor;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.InputStream;
import java.net.URL;

public class MoimActivity extends Activity {

    // 유저 정보
    private int uid;
    private String name;
    private String phone;

    TextView moimTitleText;
    Button moimAddBtn;

    private NetworkManager nm;

    private ListView listView;
    private ListViewAdaptor adaptor;

    private int[] img = {R.drawable.art, R.drawable.basketball, R.drawable.guitar};
    private String[] title = { "미술 모임", "농구 모임", "기타치는 사람들" };
    private String[] content = { "미술을 하는 사람들입니다.", "아자아자!", "띵가띵~" };

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

        getMoimList();

    }

    private void getMoimList() {
        // 데이터 세팅
        String[] colums = { "uid" };
        String[] data = { String.valueOf(uid) };

        try {
            // 서버와 통신
            JSONArray result = nm.executePost("/api/moims", colums, data);
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
                adaptor.addVO(LoadImageFromWebOperations(img_url), mname, content);

            }

            moimTitleText.setText(name +"의 Moim ("+ adaptor.getCount() + ")");

        } catch ( Exception e ) {
            e.printStackTrace();
        }
    }

    private Drawable LoadImageFromWebOperations(String url) {
        try {
            InputStream is = (InputStream) new URL(url).getContent();
            Drawable d = Drawable.createFromStream(is, "srcName");
            return d;

        } catch ( Exception e ) {
            e.printStackTrace();
            return null;
        }
    }
}
