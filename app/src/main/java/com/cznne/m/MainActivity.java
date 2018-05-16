package com.cznne.m;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private MyBroadcastReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //注册广播
        IntentFilter intentFilter=new IntentFilter();
        intentFilter.addAction("com.cznne.m.MyBroadcastReceiver");
        receiver = new MyBroadcastReceiver();
        registerReceiver(receiver,intentFilter);
    }

    public void onClick(View view){
        Intent intent=new Intent(this, MyService.class);
        startService(intent);
    }

    class MyBroadcastReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent!=null){
                String msg=intent.getStringExtra("msg");
                Toast.makeText(context, ""+msg, Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }
}
