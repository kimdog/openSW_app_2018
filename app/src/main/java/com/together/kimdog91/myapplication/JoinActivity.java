package com.together.kimdog91.myapplication;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class JoinActivity extends AppCompatActivity {

    EditText idEditText;
    EditText pwEditText;
    EditText pwChkEditText;
    EditText phoneEditText;

    Button submitBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        idEditText = (EditText) findViewById(R.id.id);
        pwEditText = (EditText) findViewById(R.id.pw);
        pwChkEditText = (EditText) findViewById(R.id.pw_chk);
        phoneEditText = (EditText) findViewById(R.id.phone);

        submitBtn = (Button)findViewById(R.id.submitBtn);

        final Context context = getApplicationContext();

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = idEditText.getText().toString();
                String pw = pwEditText.getText().toString();
                String pwChk = pwChkEditText.getText().toString();
                String phone = phoneEditText.getText().toString();

                if( id.isEmpty() || pw.isEmpty() || pwChk.isEmpty() || phone.isEmpty() ) {
                    Toast toast = Toast.makeText(context, "빈칸을 모두 채워주세요", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }

                if( !pw.equals(pwChk.toString())) {
                    Toast toast = Toast.makeText(context, "비밀번호를 다시 한번 확인해주세요.", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }

                String recieve = "Y";
                if(recieve.equals("Y")) {
                    Toast toast = Toast.makeText(context, "등록되었습니다.", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();

                    // 액티비티 종료
                    finish();
                }

            }
        });
    }
}
