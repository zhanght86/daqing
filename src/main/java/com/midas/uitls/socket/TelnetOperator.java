package com.midas.uitls.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.SocketException;

import org.apache.commons.net.telnet.EchoOptionHandler;
import org.apache.commons.net.telnet.InvalidTelnetOptionException;
import org.apache.commons.net.telnet.SuppressGAOptionHandler;
import org.apache.commons.net.telnet.TelnetClient;
import org.apache.commons.net.telnet.TerminalTypeOptionHandler;

import com.midas.constant.ErrorConstant;
import com.midas.exception.ServiceException;

/**
 * Telnet操作器,基于commons-net-2.0.jar
 * 
 * @author JiangKunpeng
 *
 */
public class TelnetOperator {

    private String       prompt = "END"; // 结束标识字符
    private TelnetClient telnet;
    private InputStream  in;             // 输入流,接收返回信息
    private PrintStream  out;            // 向服务器写入 命令

    public TelnetOperator() {
        telnet = new TelnetClient();
        telnet.setConnectTimeout(60 * 1000);
        setPrompt("END");
    }

    /**
     * @param prompt
     *            结果结束标识
     */
    public void ConnTelnetUtil() throws InvalidTelnetOptionException, IOException{
        telnet=new TelnetClient();
        TerminalTypeOptionHandler ttopt = new TerminalTypeOptionHandler(
        "VT320", false, false, true, false);
        EchoOptionHandler echoopt = new EchoOptionHandler(true, true, true,
        true);
        SuppressGAOptionHandler gaopt = new SuppressGAOptionHandler(true,
        true, true, true);
        telnet.addOptionHandler(ttopt);
        telnet.addOptionHandler(echoopt);
        telnet.addOptionHandler(gaopt);
        }
    
    
    public TelnetOperator(String prompt) 
 // telnet有VT100 VT52  
    // VT220 VTNT  
    // ANSI等协议。  
    //VT320
    {
        telnet = new TelnetClient("VT220");
        telnet.setConnectTimeout(60 * 1000);
        setPrompt(prompt);
    }

    /**
     * 登录到目标主机
     * 
     * @param ip
     * @param port
     * @param username
     * @param password
     */
    public void login(String ip, int port, String command) {
        try {
            telnet.connect(ip, port);
            telnet.setSoTimeout(2 * 60 * 1000);
            in = telnet.getInputStream();
            out = new PrintStream(telnet.getOutputStream());
            readLine();
            write(command);
            readLine();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 登录到目标主机
     * 
     * @param ip
     * @param port
     * @param username
     * @param password
     */
    public void login(String ip, int port) {
        try {
            telnet.connect(ip, port);
            telnet.setSoTimeout(2 * 60 * 1000);
            in = telnet.getInputStream();
            out = new PrintStream(telnet.getOutputStream());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 读取分析结果
     * 
     * @return
     */
    public String readUntil() {
        StringBuffer sb = new StringBuffer();
        BufferedReader br=null;
        try {
            br= new BufferedReader(new InputStreamReader(in, "utf-8"));
            String line = null;
            int readCount=0;
            while ((line = br.readLine()) != null) {
            	readCount++;
                if (prompt.equals(line)||line.indexOf("results in")>0) {
                    break;
                }
                sb.append(line + "\n");
               
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
        	try {
				br.close();
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
        return sb.toString();
    }
    
    //TODO sullivan
    public String readUntilLimit(int limtNum) {
        StringBuffer sb = new StringBuffer();
        BufferedReader br=null;
        try {
        	br  = new BufferedReader(new InputStreamReader(in, "utf-8"));
            String line = null;
            int readCount=0;
            while ((line = br.readLine()) != null) {
            	if(readCount>=limtNum){//最大读取行数据
            		break;
            	}            		
                if (prompt.equals(line)||line.indexOf("match results in offline")>0||line.indexOf("match results in online")>0) {
                    break;
                }
                sb.append(line + "\n");
            	readCount++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
        	try {
				br.close();
				in.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
        return sb.toString();
    }

    private String readLine() {
    	  BufferedReader br=null; 
        try {
        	br = new BufferedReader(new InputStreamReader(in, "utf-8"));
            return br.readLine();
        } catch (Exception e) {
            throw new ServiceException(ErrorConstant.CODE3000, "离线柜登录失败");
        }
        finally {
        	try {
				br.close();
				in.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
    }

    /**
     * 发送命令
     * 
     * @param value
     */
    public void write(String value) {
        try {
            out.println(value);
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 发送命令,返回执行结果
     * 
     * @param command
     * @return
     */
    public String sendCommand(String command) {
        try {
            write(command);
            return readUntil();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void sendCommandOnly(String command) {
        try {
            write(command);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 关闭连接
     */
    public void distinct() {
        try {
            if (telnet != null && telnet.isConnected())
                telnet.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setPrompt(String prompt) {
        if (prompt != null) {
            this.prompt = prompt;
        }
    }

    public static String commandBurnMachine(String ip, int port, String command) throws Exception {
        TelnetOperator telnet = null;
        try {
            telnet = new TelnetOperator(); // Windows,用VT220,否则会乱码
            telnet.login(ip, port);
            return telnet.sendCommand(command);
        } catch (Exception e) {
            throw e;
        } finally {
            telnet.distinct();
        }
    }
    //TODO sullivan
    public static String commandFileSearch(String ip, int port, String command,int limitNum) throws Exception {
        TelnetOperator telnet = null;
        try {
            telnet = new TelnetOperator(); // Windows,用VT220,否则会乱码
            telnet.login(ip, port);            
            telnet. write(command);
            return telnet.readUntilLimit(limitNum);
        } catch (Exception e) {
            throw e;
        } finally {
            telnet.distinct();
        }
    }

    public static void commandOnly(String ip, int port, String command) throws Exception {
        TelnetOperator telnet = null;
        try {
            telnet = new TelnetOperator();
            telnet.login(ip, port);
            telnet.sendCommandOnly(command);
        } catch (Exception e) {
            throw e;
        } finally {
            telnet.distinct();
        }
    }

    public static String commandOffline(String ip, int port, String command) throws Exception {
        TelnetOperator telnet = null;
        try {
            telnet = new TelnetOperator(); // Windows,用VT220,否则会乱码
            telnet.login(ip, port, "RESP,");
            return telnet.sendCommand(command);
        } catch (Exception e) {
            throw e;
        } finally {
            if (null != telnet) {
                telnet.distinct();
            }
        }
    }

    public static void main(String[] args) throws Exception {
        String aa=commandBurnMachine("192.168.1.222", 22, "ls,");
        System.out.println(aa);
        // System.out.println(commandBurnMachine("192.168.2.222", 2021,
        // "QUERYSTATION,"));
        // System.out.println(commandBurnMachine("192.168.0.222", 2021,
        // "QOFFLINEJOB,开发人员目录,"));
        // System.out.println(commandOffline("192.168.0.231", 2020,
        // "QUERYSTATION,"));
    }

}