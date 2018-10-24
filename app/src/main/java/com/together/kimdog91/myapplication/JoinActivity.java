package com.together.kimdog91.myapplication;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

public class JoinActivity extends AppCompatActivity {

    public static final String EXTRA_SIGN_UP_ID = "extra.signup.id";
    private boolean checkId = false; // ID 중복여부

    EditText idEditText;
    EditText pwEditText;
    EditText pwChkEditText;
    EditText nameEditText;
    EditText phoneEditText;

    Button submitBtn;
    Button checkIdBtn;

    NetworkManager nm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        idEditText = (EditText) findViewById(R.id.id);
        pwEditText = (EditText) findViewById(R.id.pw);
        pwChkEditText = (EditText) findViewById(R.id.pw_chk);
        nameEditText = (EditText) findViewById(R.id.name);
        phoneEditText = (EditText) findViewById(R.id.phone);

        submitBtn = (Button)findViewById(R.id.submitBtn);
        checkIdBtn = (Button)findViewById(R.id.checkIdBtn);

        final Context context = getApplicationContext();

        nm = new NetworkManager();
        nm.setActivity(this);

        // 제출
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = idEditText.getText().toString();
                String pw = pwEditText.getText().toString();
                String pwChk = pwChkEditText.getText().toString();
                String name = nameEditText.getText().toString();
                String phone = phoneEditText.getText().toString();

                if( id.isEmpty() || pw.isEmpty() || pwChk.isEmpty() || phone.isEmpty() ) {
                    Toast toast = Toast.makeText(context, context.getResources().getString(R.string.error_account_0001), Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    return;
                }

                if( !pw.equals(pwChk.toString())) {
                    Toast toast = Toast.makeText(context, context.getResources().getString(R.string.error_account_0002), Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    return;
                }

                if( !checkId ) {
                    Toast toast = Toast.makeText(context, context.getResources().getString(R.string.error_account_0006), Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    return;
                }

                // 데이터 세팅
                String[] colums = { "id", "pw", "pwChk", "name", "phone" };
                String[] data = { id, pw, pwChk, name, phone };

                try {
                    // 서버와 통신
                    JSONArray result = nm.executePost("/api/signup", colums, data);

                    JSONObject resultObject = result.getJSONObject(0);
                    if ( resultObject.get("errorCode").toString().equals("Y") ) {
                        // 계정 생성 성공
                        Toast toast = Toast.makeText(context, context.getResources().getString(R.string.success_account_0001), Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();

                        // 액티비티 종료
                        Intent newData = new Intent();
                        newData.putExtra(EXTRA_SIGN_UP_ID, id);
                        setResult(RESULT_OK, newData);
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

        // 중복여부 확인
        checkIdBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = idEditText.getText().toString();

                if( id.isEmpty() ) {
                    Toast toast = Toast.makeText(context, context.getResources().getString(R.string.error_account_0001), Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    return;
                }

                // 데이터 세팅
                String[] colums = { "id" };
                String[] data = { id };

                try {
                    // 서버와 통신
                    JSONArray result = nm.executePost("/api/checkId", colums, data);

                    JSONObject resultObject = result.getJSONObject(0);
                    String _errorCode = resultObject.get("errorCode").toString();
                    if ( _errorCode.equals("Y") ) {
                        // 중복되지 않음
                        Toast toast = Toast.makeText(context, context.getResources().getString(R.string.success_account_0002), Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();

                        checkId = true;
                    }
                    else {
                        // 중복
                        if( _errorCode.equals("0003")) {
                            Toast toast = Toast.makeText(context, context.getResources().getString(R.string.error_account_0003), Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                        }
                        else if ( _errorCode.equals("0005")) {
                            Toast toast = Toast.makeText(context, context.getResources().getString(R.string.error_account_0005), Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                        }
                        // 알 수 없는 오류
                        else {
                            Toast toast = Toast.makeText(context, context.getResources().getString(R.string.error_unknown), Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                        }
                    }

                } catch ( Exception e ) {
                    e.printStackTrace();
                }

            }
        });

        // 아이디 변화 시 중복 여부 초기화
        idEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // 입력되는 텍스트에 변화가 있을 때
                checkId = false;
            }
            @Override
            public void afterTextChanged(Editable arg0) {
                // 입력이 끝났을 때
                checkId = false;
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // 입력하기 전에
            }
        });

    }


}
