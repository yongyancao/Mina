package com.cznne.m;

import android.content.Context;
import android.util.Log;

import org.apache.mina.core.filterchain.IoFilterAdapter;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.service.IoService;
import org.apache.mina.core.service.IoServiceListener;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.serialization.ObjectSerializationCodecFactory;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import java.lang.ref.WeakReference;
import java.net.InetSocketAddress;

public class ConnectManager {
    private String TAG="cyy";

    private ConnectConfig mConfig;//配置文件
    private WeakReference<Context> mContext;
    private NioSocketConnector mConnection;
    private IoSession mSessioin;
    private InetSocketAddress mAddress;

    public ConnectManager(ConnectConfig mConfig) {
        this.mConfig = mConfig;
        this.mContext = new WeakReference<Context>(mConfig.getContext());
        init();
    }

    private void init() {
        mAddress = new InetSocketAddress(mConfig.getIp(),mConfig.getPort());
        //创建连接对象
        mConnection = new NioSocketConnector();
        //设置连接地址
        mConnection.setDefaultRemoteAddress(mAddress);
        mConnection.getSessionConfig().setReadBufferSize(mConfig.getReadBufferSize());
        //设置过滤
//        mConnection.getFilterChain().addLast("logger",new LoggingFilter());
//        mConnection.getFilterChain().addLast("codec",new ProtocolCodecFilter(new ObjectSerializationCodecFactory()));
        //设置连接监听
        mConnection.setHandler(new MinHandler(mContext.get()));
        //      断线重连回调拦截器
        mConnection.getFilterChain().addFirst("reconnection", new IoFilterAdapter() {
            @Override
            public void sessionClosed(NextFilter nextFilter, IoSession ioSession) throws Exception {
               while (true){
                    try{
                        Log.i(TAG, "sessionClosed: 连接已断开");
                        Thread.sleep(1500);
                        ConnectFuture future = mConnection.connect();
                        future.awaitUninterruptibly();// 等待连接创建成功
                        IoSession session = future.getSession();// 获取会话
                        if(session.isConnected()){
                            Log.i(TAG, "sessionClosed: 断线重连成功");
                            break;
                        }else {
                            Log.i(TAG, "sessionClosed: 重连失败，3秒后重试..");
                        }
                    }catch(Exception ex){
                        Log.i(TAG, "sessionClosed: 重连失败，3秒后重试..");
                    }
                }
            }
        });
    }


    /**
     * 与服务器连接的方法
     * @return
     */
    public boolean connect(){
        try{
            ConnectFuture future =mConnection.connect();
            future.awaitUninterruptibly();
            mSessioin = future.getSession();
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return mSessioin==null?false:true;
    }

    /**
     * 断开连接的方法
     */
    public void disConnect(){
        mConnection.dispose();
        mConnection=null;
        mSessioin=null;
        mAddress=null;
        mContext=null;
    }
}
