package com.together.kimdog91.myapplication;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class BaseActivity extends AppCompatActivity {

    public void progressON(Activity activity) {
        BaseApplication.getInstance().progressON(activity, null);
    }

    public void progressON(Activity activity, String message) {
        BaseApplication.getInstance().progressON(activity, message);
    }

    public void progressOFF() {
        BaseApplication.getInstance().progressOFF();
    }

}
