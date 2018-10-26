package com.xiaopeng.testtts.testttsapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.xiaopeng.speech.protocol.SpeechUtils;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        TextView tvClickme =  (TextView) findViewById(R.id.tv_clickme);
        tvClickme.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_clickme:
                String speak = SpeechUtils.speak("欢迎参加AI奇葩说！U看UBB");
//                Log.d(TAG,"看看打印什么 ="+speak);
                Toast.makeText(this,"speak"+speak,Toast.LENGTH_LONG).show();
                break;

            default:break;
        }
    }
}
