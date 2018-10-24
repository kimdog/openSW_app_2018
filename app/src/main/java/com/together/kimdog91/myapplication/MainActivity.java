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

    public static int SIGN_UP_ID = 10;

    EditText idEditText;
    EditText pwEditText;
    Button joinBtn;
    Button loginBtn;

    private NetworkManager nm;

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
                startActivityForResult(intent, SIGN_UP_ID);
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
                    if ( resultObject.get("uid") != null ) {
                        // 허가되지 않은 사용자
                        if( !resultObject.get("passYn").equals("Y") ) {
                            Toast toast = Toast.makeText(context, context.getResources().getString(R.string.error_login_0002), Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                            return;
                        }

                        // 로그인 성공
                        Toast toast = Toast.makeText(context, context.getResources().getString(R.string.success_login_0001), Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();

                        Intent intent = new Intent(MainActivity.this, MoimActivity.class);
                        intent.putExtra("uid", Integer.parseInt(resultObject.get("uid").toString()));
                        intent.putExtra("name", resultObject.get("name").toString());
                        intent.putExtra("phone", resultObject.get("phone").toString());
                        // pos_id,
                        // profile_id 처리

                        startActivity(intent);

                        // 액티비티 종료
                        finish();
                    }
                    else {
                        // 로그인 실패
                        Toast toast = Toast.makeText(context, context.getResources().getString(R.string.error_login_0001), Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    }

                } catch ( Exception e ) {
                    e.printStackTrace();
                }

            }
        });

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // 결과반환 액티비티 식별
        if( requestCode == SIGN_UP_ID ) {
            if(resultCode == RESULT_OK) {
                String new_id = data.getStringExtra(JoinActivity.EXTRA_SIGN_UP_ID);
                idEditText.setText(new_id);
            }
        }
    }
}

