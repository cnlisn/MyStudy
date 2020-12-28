package com.lisn.mystudy.socket.TCP.Socket多线程应用;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

//服务端
public class TCPServer {
    public static void main(String[] args) throws IOException {
        receive();
    }

//    private static void receive() throws IOException {
//        System.out.println("Server Start...");
//        //创建服务端Socket并明确端口号
//        ServerSocket serverSocket = new ServerSocket(10004);
//
//        while (true) {
//            //获取到客户端的Socket
//            Socket socket = serverSocket.accept();
//            //通过客户端的Socket获取到输入流
//            InputStream is = socket.getInputStream();
//
//            //通过输入流获取到客户端传递的数据
//            byte[] bytes = new byte[1024];
//            int len = is.read(bytes);
//            System.out.println(new String(bytes, 0, len));
//
//            //将客户端发来的数据原封不动返回
//            OutputStream os = socket.getOutputStream();
//            os.write(new String(bytes, 0, len).getBytes());
//            //关闭连接
//            socket.close();
//        }
//    }

    /**
     * 细心的同学可能又发现了，上面的写法是存在问题的，由于服务端始终都在主线程中处理请求，
     * 所以客户端的请求需要被服务端排队处理，举个例子：Client1对服务端进行了一次请求，
     * 服务端在响应Client1之前是不会接受其他请求的，显然这是不符合逻辑的，真正的服务器是要具备并发处理的。
     * 而多线程恰好能解决这个问题，我们来看修改之后的服务端代码
     * <p>
     * 作者：zskingking
     * 链接：https://www.jianshu.com/p/b04930d2b85e
     * 来源：简书
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     */
    private static void receive() throws IOException {
        System.out.println("Server Start...");

        //创建服务端Socket并明确端口号
        ServerSocket serverSocket = new ServerSocket(10004);
        while (true) {
            //获取到客户端的Socket
            final Socket socket = serverSocket.accept();
            //通过线程执行客户端请求
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        //通过客户端的Socket获取到输入流
                        InputStream is = socket.getInputStream();

                        //通过输入流获取到客户端传递的数据
                        byte[] bytes = new byte[1024];
                        int len = is.read(bytes);
                        System.out.println(new String(bytes, 0, len));

                        //将客户端发来的数据原封不动返回
                        OutputStream os = socket.getOutputStream();
                        os.write(new String(bytes, 0, len).getBytes());
                        //关闭连接
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();

        }
    }


}