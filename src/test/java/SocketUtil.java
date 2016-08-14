//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.io.OutputStream;
//import java.io.PrintWriter;
//import java.net.Socket;
//
//public class SocketUtil {
//
//
//        public static final String SERVER_IP = "192.168.0.233";
//
//        public static final int SERVER_PORT = 2021;
//
//         
//
//        public static void main(String[] args){
//
//            SocketUtil socketUtil=new SocketUtil();
//            
//        String command = "FINDFILE,D0001,";
//
//        String serverRet = socketUtil.callServerMethod(command);
//
//        System.out.println("客户端发送命令："+command);
//
//        System.out.println("服务器端返回："+serverRet);
//
//        }
//
//
//        public  String callServerMethod(String server,int port, String command) {
//
//        String ret = "";
//
//        try {
//
//        Socket socket = new Socket(SERVER_IP, SERVER_PORT);
//
//        // 发送命令
//
//        OutputStream socketOut = socket.getOutputStream();
//
//        command += "\r\n";
//
//        socketOut.write(command.getBytes("UTF-8"));
//
//
//        // 接收服务器的反馈
//
//        BufferedReader br = new BufferedReader(new InputStreamReader(
//
//        socket.getInputStream(), "UTF-8"));
//        PrintWriter pw=getWriter(socket);
//        
//        String msg=null;
//        while((msg=br.readLine())!=null){
//            pw.println(msg);
//            System.out.println(br.readLine());            
//           // ret+=br.readLine();
//            if(msg.equals("bye"))
//                break;
//        }
//        
//
//
//        System.out.println("Server return : " + ret);
//
//        // 发送关闭命令
//
//        socketOut.write("sb\r\n".getBytes("UTF-8"));// 服务器端接收到“sb”会终止本次连接
//        
//        socket.close();
//
//        } catch (IOException e) {
//
//        //e.printStackTrace();
//            
//        System.err.println("同服务器交互时"+e.getMessage());
//
//        }
//
//        return ret;
//
//        }
//        
//        private   PrintWriter getWriter(Socket socket)throws IOException{
//            OutputStream socketOut=socket.getOutputStream();
//            return new PrintWriter(socketOut,true);
//        }
//        private BufferedReader getReader(Socket socket)throws IOException{
//            InputStream socketIn=socket.getInputStream();
//            return new BufferedReader(new InputStreamReader(socketIn));
//        }
//
//}
