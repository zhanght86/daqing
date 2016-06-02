package com.midas;
import org.apache.commons.net.telnet.TelnetClient;
import java.io.*;
import java.nio.ByteBuffer;
public class TelnetUtil {
    String charset = null;
    byte[] buff = new byte[2048];
    TelnetClient telnetClient = new TelnetClient();
    BufferedReader telnetReader = null;
    BufferedWriter telnetWirter = null;
    InputStream telnetIn = null;
    OutputStream telnetOut = null;

    public TelnetUtil() {
        telnetClient = new TelnetClient();
    }

    /**
     * 连接至服务器
     * @param ip
     * @param port
     * @throws UnsupportedEncodingException
     * @throws IOException
     */
    public void connect(String ip, int port) throws UnsupportedEncodingException,IOException {
        telnetClient.connect(ip,port);
        setIOStream();
    }

    /**
     * 连接至服务器
      * @param ip
     * @throws UnsupportedEncodingException
     * @throws IOException
     */
    public void connect(String ip) throws UnsupportedEncodingException,IOException {
        telnetClient.connect(ip);
        setIOStream();
    }

    void setIOStream() throws UnsupportedEncodingException {
        telnetIn = telnetClient.getInputStream();
        telnetOut = telnetClient.getOutputStream();
        if(null==charset){
            telnetReader = new BufferedReader(new InputStreamReader(telnetIn));
            telnetWirter = new BufferedWriter(new OutputStreamWriter(telnetOut));
        }else{
            telnetReader = new BufferedReader(new InputStreamReader(telnetIn, charset));
            telnetWirter = new BufferedWriter(new OutputStreamWriter(telnetOut, charset));
        }
    }

    /**
     * 登录
     * @param user
     * @param passwd
     * @return 是否登录成功.
     * @throws IOException
     */
    public boolean login(String user,String passwd) throws IOException {
        String read = readString();
        for(int i=0; ; i++){
            if(-1==read.indexOf("login")){
                read = readString();
            }else{
                break;
            }
        }
        writeText(user);

        read = readString();
        for(int i=0; ; i++){
            if(-1==read.indexOf("Password")){
                read = readString();
            }else{
                break;
            }
        }
        writeText(passwd);

        for(;;){
            read = readString();
            //System.out.println("last:"+read);
            if(-1!=read.indexOf("Last")){
                return true;
            }else if(-1!=read.indexOf("incorrect")){
                return false;
            }
        }
    }

    /**
     * 这是一个测试方法,随便写。
     * @throws IOException
     */
    public void show() throws IOException {
//        System.out.println(readString());
//        System.out.println(readString());
//        ByteBuffer tmp = ByteBuffer.allocate(1024);
//        byte[] buff = new byte[1024];
//        while(telnetIn.available()>0){
//            int readLen = readBytes(buff,0,1024);
//            tmp.put(buff,0,readLen);
//        }

//        System.out.println(new String(tmp.array()));
        System.out.println("1 "+readString());
        System.out.println("2 "+readString());
        System.out.println("3 "+readString());
        writeText("root");
        System.out.println("4 " + readString());
        writeText("123456");
        System.out.println("5 "+readString());
//        System.out.println("6 "+readString());
//        System.out.println("7 "+readString());

    }

    public int readBytes(byte[] buff, int offset, int len) throws IOException {
        return telnetIn.read(buff,offset,len);
    }

    /**
     * 读取字符串<br/>
     * 相当于readByte()转为字符串
     * @return
     * @throws IOException
     */
    public String readString() throws IOException {
        int readLen = readBytes(this.buff, 0, this.buff.length);
        if(0<readLen)
            return new String(buff,0,readLen).trim();
        else
            return "";
    }

    /**
     * 读取一行<br/>
     * 如果服务器与客户端不是同一种操作系统，可能导致此方法计行失败。
     * @return
     * @throws IOException
     */
    public String readLine() throws IOException {
        String read = telnetReader.readLine();
        return null==read?"":read.trim();
    }

    public void writeBytes(byte[] buff, int offset, int len) throws IOException {
        telnetOut.write(buff,offset,len);
    }

    /**
     * 向服务器写字符串
     * @param text
     * @throws IOException
     */
    public void writeText(String text) throws IOException {
        telnetWirter.write(text);
        telnetWirter.write('r');
        telnetWirter.write('n');
        telnetWirter.flush();
    }

    /**
     * 执行命令，并返回结果<br/>
     * 相当于: <br>
     * writeText();  <br/>
     * return readString();
     * @param cmd
     * @return
     * @throws IOException
     */
    public String exec(String cmd) throws IOException {
        writeText(cmd);
        return readString();
    }

    String login1(String user,String passwd) throws IOException {
        String read = null;
        readString();
        readString();
        read = readString();

        if(-1!=read.indexOf("login")){
            writeText(user);
        }

        read = readString();
        if(-1!=read.indexOf("Password")){
            writeText(passwd);
        }

        read  = readString();
        read += readString();
        return read;

//        StringBuffer sb = new StringBuffer();
//        while(null!= (read = readString())){
//            sb.append(read);
//        }
//
//        return sb.toString();
    }

    /**
     * 关闭
     */
    public void close(){
        try{
            writeText("exit");
            writeText("exit");
            writeText("exit");
        }catch(Exception ex){
        }

        try {
            if(null!=telnetIn) telnetIn.close();
        } catch (Exception e) {
        }

        try {
            if(null!=telnetOut) telnetOut.close();
        } catch (Exception e) {
        }

        try {
            if(null!=telnetClient)telnetClient.disconnect();
        } catch (Exception e) {
        }
    }

    /**
     * 设置telnet通信时的字符集<br/>
     * 注:此字符集与服务器端字符集没有必然关系<br/>
     * 此方法需在connect()前调用
     * @param charset
     */
    public void setCharset(String charset ){
        this.charset = charset;
    }

    /**
     * 重新设置buff大小,默认为2048字节.
     * @param size
     */
    public void setBufferSize(int size){
        this.buff = new byte[size];
    }
    /** * 写操作 * * @param value */  
    
  
    
    public static void main(String[] args){
        TelnetUtil client=new TelnetUtil();
        try {
            client.connect("192.168.0.233", 2021);
           
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        
    }
}