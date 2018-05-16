package com.cznne.m;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.HandlerThread;
import android.os.IBinder;

public class MyService extends Service {

    private ConnectManager mManager;

    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        //使用子线程开启连接
        ConnectThread connectThread=new ConnectThread("connectThread",getApplicationContext());
        connectThread.start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mManager.disConnect();
    }

    private class ConnectThread extends HandlerThread{

        private boolean isConnection;

        public ConnectThread(String name, Context context) {
            super(name);
            //创建配置文件类
            ConnectConfig config = new ConnectConfig.Builder(context)
                    .setIp("192.168.191.1")
                    .setPort(8888)
                    .setReadBufferSize(10240)
                    .setConnectionTimeout(15000)
                    .build();
            //创建连接的管理类
            mManager = new ConnectManager(config);
        }

        @Override
        protected void onLooperPrepared() {
            //利用循环请求连接
            while (true) {
                isConnection = mManager.connect();
                if (isConnection) {
                    //当请求成功的时候,跳出循环
                    break;
                }
                try {
                    Thread.sleep(3000);
                } catch (Exception e) {

                }
            }
        }
    }
}
