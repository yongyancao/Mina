package com.cznne.m;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

public class MinHandler extends IoHandlerAdapter{
    private Context context;
    private String TAG="cyy";
    private Intent intent=new Intent();

    public MinHandler(Context context) {
        this.context = context;
        intent.setAction("com.cznne.m.MyBroadcastReceiver");
    }

    @Override
    public void sessionCreated(IoSession session) throws Exception {
        super.sessionCreated(session);
        Log.i(TAG, "sessionCreated: 创建session:");
    }

    @Override
    public void sessionOpened(IoSession session) throws Exception {
        super.sessionOpened(session);
        Log.i(TAG, "sessionOpened: 打开session:");
    }

    @Override
    public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
        super.exceptionCaught(session, cause);
        Log.i(TAG, "exceptionCaught: 出现异常：");
    }

    @Override
    public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
        super.sessionIdle(session, status);
        Log.i(TAG, "sessionIdle: 连接断开..：");
    }

    @Override
    public void sessionClosed(IoSession session) throws Exception {
        super.sessionClosed(session);
        Log.i(TAG, "sessionClosed: 关闭session:");
    }

    @Override
    public void messageReceived(IoSession session, Object message) throws Exception {
        super.messageReceived(session, message);
        IoBuffer buffer = (IoBuffer)message;
        byte[] bytes=buffer.array();
        String msg=new String(bytes,"GBK").trim();
        Log.i(TAG, "messageReceived: 收到消息："+msg);
        intent.putExtra("msg",msg);
        context.sendBroadcast(intent);
    }

}
