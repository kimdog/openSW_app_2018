package com.together.kimdog91.myapplication;

import android.app.Activity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
;import com.together.kimdog91.myapplication.Adaptor.ListViewAdaptor;

import org.w3c.dom.Text;

public class MoimActivity extends Activity {

    TextView moimTitleText;
    Button moimAddBtn;

    private ListView listView;
    private ListViewAdaptor adaptor;

    private int[] img = {R.drawable.art, R.drawable.basketball, R.drawable.guitar};
    private String[] title = { "미술 모임", "농구 모임", "기타치는 사람들" };
    private String[] content = { "미술을 하는 사람들입니다.", "아자아자!", "띵가띵~" };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moim);

        // 변수 초기화
        adaptor = new ListViewAdaptor();
        listView = (ListView) findViewById(R.id.List_view);

        // 어뎁터 할당
        listView.setAdapter(adaptor);

        // 어뎁터를 통한 값 전달
        for(int i=0; i<img.length; i++) {
            adaptor.addVO(ContextCompat.getDrawable(this, img[i]), title[i], content[i]);
        }

        moimTitleText = (TextView) findViewById(R.id.MoimTitle);
        moimAddBtn = (Button) findViewById(R.id.addMoimBtn);

        moimTitleText.setText("Moim ("+ adaptor.getCount() + ")");

    }
}
