package com.together.kimdog91.myapplication;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    EditText idEditText;
    EditText pwEditText;
    Button joinBtn;
    Button loginBtn;

    NetworkManager nm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Context context = getApplicationContext();

        idEditText = (EditText) findViewById(R.id.id);
        pwEditText = (EditText) findViewById(R.id.pw);
        joinBtn = (Button) findViewById(R.id.joinBtn);
        loginBtn = (Button) findViewById(R.id.loginBtn);

        nm = new NetworkManager();
        nm.setActivity(this);

        joinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent( MainActivity.this, JoinActivity.class);
                startActivity(intent);
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = idEditText.getText().toString();
                String pw = pwEditText.getText().toString();

                // 비어있음
                if( id.isEmpty() || pw.isEmpty() ) {
                    Toast toast = Toast.makeText(context, context.getResources().getString(R.string.error_account_0001), Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    return;
                }

                // 데이터 세팅
                String[] colums = { "id", "pw" };
                String[] data = { id, pw };


                try {
                    // 서버와 통신
                    JSONArray result = nm.executePost("/api/signin", colums, data);

                    JSONObject resultObject = result.getJSONObject(0);
                    if ( resultObject.get("errorCode").toString().equals("Y") ) {
                        // 로그인 성공
                        Toast toast = Toast.makeText(context, context.getResources().getString(R.string.success_account_0001), Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();

                        Intent intent = new Intent(MainActivity.this, MoimActivity.class);
                        startActivity(intent);

                        // 액티비티 종료
                        finish();
                    }
                    else {
                        // 계정 생성 실패
                        // 서버 오류
                        Toast toast = Toast.makeText(context, context.getResources().getString(R.string.error_account_0004), Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    }

                } catch ( Exception e ) {
                    e.printStackTrace();
                }

            }
        });

    }
}

