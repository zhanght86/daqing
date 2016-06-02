package com.midas.uitls.socket;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.UnknownHostException;

public class TcpLongConnection {

    // IP地址
    private String  serverIp;
    // 端口号
    private int     port;
    // 连接
    private Socket  socket;
    // 是否启动
    private boolean running   = false;
    // 最后一次发送数据时间
    private long    lastSendTime;
    // 支付编码
    private String  charset   = "utf-8";
    // 结束标志
    private String  endString = "END";

    public TcpLongConnection(String serverIp, int port) {
        this.serverIp = serverIp;
        this.port = port;
    }

    public static void main(String[] args) throws Exception {
        String serverIp = "127.0.0.1";
        int port = 2021;
        TcpLongConnection client = new TcpLongConnection(serverIp, port);
        client.start();

        System.out.println("输入内容操作：");

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        while (true) {
            String content = br.readLine();
            if ("BYE".equals(content)) {
                break;
            }
            client.sendObject(content);
        }

    }

    public void start() throws UnknownHostException, IOException {
        if (running) {
            return;
        }
        socket = new Socket(serverIp, port);
        System.out.println("本地端口：" + socket.getLocalPort());
        lastSendTime = System.currentTimeMillis();
        running = true;
        new Thread(new KeepAlive()).start();
        new Thread(new Receive()).start();
    }

    public boolean sendObject(String obj) {
        boolean isSendOK = false;
        try {
            OutputStream os = socket.getOutputStream();
            
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os));
            bw.write(obj);
            System.out.println("发送内容为： " + obj);
            os.flush();
            isSendOK = true;
            lastSendTime = System.currentTimeMillis();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            isSendOK = false;
        } catch (IOException e) {
            e.printStackTrace();
            isSendOK = false;
        }
        return isSendOK;
    }

    public void stop() {
        if (running) {
            running = false;
        }
    }

    /**
     * 心跳包
     * 
     * @author arron
     *
     */
    class KeepAlive implements Runnable {

        long checkDelay     = 10;
        long keepAliveDelay = 10 * 1000;

        @Override
        public void run() {
            while (running) {
                if (System.currentTimeMillis() - lastSendTime > keepAliveDelay) {
                    TcpLongConnection.this.sendObject("test");
                    lastSendTime = System.currentTimeMillis();
                } else {
                    try {
                        Thread.sleep(checkDelay);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        TcpLongConnection.this.stop();
                    }
                }
            }
        }
    }

    class Receive implements Runnable {
        @Override
        public void run() {
            while (running) {
                try {
                    InputStream in = socket.getInputStream();
                    BufferedReader br = new BufferedReader(new InputStreamReader(in));
                    System.out.println("-------------" + br.readLine());
                } catch (Exception e) {
                    e.printStackTrace();
                    TcpLongConnection.this.stop();
                }
            }
        }
    }

}
