package com.cznne.m;

import android.content.Context;

public class ConnectConfig {
    private Context context;
    private String ip;
    private int port;
    private int readBufferSize; //缓存大小
    private long connectionTimeout;//连接超时时间

    public Context getContext() {
        return context;
    }



    public String getIp() {
        return ip;
    }



    public int getPort() {
        return port;
    }


    public int getReadBufferSize() {
        return readBufferSize;
    }



    public long getConnectionTimeout() {
        return connectionTimeout;
    }



    public static class Builder{
        private Context mContext;
        private String ip="127.0.0.1";
        private int port=8888;
        private int readBufferSize=10240; //缓存大小
        private long connectionTimeout=15000;//连接超时时间


        public Builder(Context mContext) {
            this.mContext = mContext;
        }

        public Builder setConnectionTimeout(long connectionTimeout) {
            this.connectionTimeout = connectionTimeout;
            return this;
        }

        public Builder setIp(String ip) {
            this.ip = ip;
            return this;
        }

        public Builder setmContext(Context mContext) {
            this.mContext = mContext;
            return this;
        }

        public Builder setPort(int port) {
            this.port = port;
            return this;
        }

        public Builder setReadBufferSize(int readBufferSize) {
            this.readBufferSize = readBufferSize;
            return this;
        }

        public ConnectConfig build(){
            ConnectConfig connectConfig = new ConnectConfig();
            connectConfig.connectionTimeout = this.connectionTimeout;
            connectConfig.ip = this.ip;
            connectConfig.port = this.port;
            connectConfig.context = this.mContext;
            connectConfig.readBufferSize = this.readBufferSize;
            return  connectConfig;
        }
    }
}
