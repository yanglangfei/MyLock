package com.yf.applock;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.yf.applock.service.MyService;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

public class MainActivity extends Activity implements View.OnClickListener {
    private EditText account;
    private  EditText password;
    private Button login;
    private  String path="http://192.168.1.134:8080/MicroMessage/addUser";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        Intent intent=new Intent(this, MyService.class);
        startService(intent);

        login= (Button) findViewById(R.id.login);
        account= (EditText) findViewById(R.id.account);
        password= (EditText) findViewById(R.id.password);
        login.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        String accountStr = account.getText().toString();
        String passwordStr = password.getText().toString();

        RequestParams param=new RequestParams(path);
        param.addParameter("name",accountStr);
        param.addParameter("pwd",passwordStr);
        x.http().get(param, new Callback.CacheCallback<String>() {
            @Override
            public boolean onCache(String result) {
                return false;
            }

            @Override
            public void onSuccess(String result) {
                MainActivity.this.finish();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
            }

            @Override
            public void onCancelled(CancelledException cex) {
            }

            @Override
            public void onFinished() {
            }
        });


    }
}
