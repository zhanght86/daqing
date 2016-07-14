package com.midas.uitls.socket;




import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

/**
 * SSH工具类
 * @author 赵聪慧
 * 2013-4-7
 */
public class SSHHelper {
  
  /**
   * 远程 执行命令并返回结果调用过程 是同步的（执行完才会返回）
   * @param host	主机名
   * @param user	用户名
   * @param psw	密码
   * @param port	端口
   * @param command	命令
   * @return
   */
  public static String exec(String host,String user,String psw,int port,String command){
    String result="";
    Session session =null;
    ChannelExec openChannel =null;
    BufferedReader reader=null;
    try {
      JSch jsch=new JSch();
      session = jsch.getSession(user, host, port);
      java.util.Properties config = new java.util.Properties();
      config.put("StrictHostKeyChecking", "no");
      session.setConfig(config);
      session.setPassword(psw);
      session.connect();
      openChannel = (ChannelExec) session.openChannel("exec");      
      openChannel.setCommand(command);
      openChannel.setInputStream(null);
      int exitStatus = openChannel.getExitStatus();
      System.out.println(exitStatus);
      openChannel.connect();  
            InputStream in = openChannel.getInputStream();  
             reader = new BufferedReader(new InputStreamReader(in));  
            String buf = null;
            while ((buf = reader.readLine()) != null) {
            	result+= new String(buf.getBytes("UTF-8"),"UTF-8")+"    <br>\r\n";  
            }  
           
    } catch (JSchException | IOException e) {
      result+=e.getMessage();
    }finally{
    	 try {
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
      if(openChannel!=null&&!openChannel.isClosed()){
        openChannel.disconnect();
      }
      if(session!=null&&session.isConnected()){
        session.disconnect();
      }
    }
    return result;
  }
  
  
  
  public static void main(String args[]){
    String exec = exec("172.43.1.107", "root", "jvcnas", 22, "cp /groups/0233_W20160512000001\\(84-9\\)/芳38/W116331.001.SGY_midas_6_002.split /leofsdata3/test/W116331");
    System.out.println(exec);	
  }
}