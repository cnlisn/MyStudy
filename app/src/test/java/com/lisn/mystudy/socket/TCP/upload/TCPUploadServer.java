package com.lisn.mystudy.socket.TCP.upload;

import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPUploadServer {

    @Test
    public void main() throws IOException {

        //1.创建客户端Socket
        ServerSocket serverSocket = new ServerSocket(10001);

        //2.获取到客户端Socket
        Socket socket = serverSocket.accept();

        //3.通过客户端Socket获取到输入流
        InputStream is = socket.getInputStream();

        //4.将流以文件的形式写入到磁盘
        File dir = new File("./tcp");
        //如果文件夹不存在就创建文件夹
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File file = new File(dir, "girl.jpg");
        FileOutputStream fos = new FileOutputStream(file);
        byte[] bytes = new byte[1024];
        int len = 0;
        while ((len = is.read(bytes)) != -1) {
            fos.write(bytes, 0, len);
        }

        //5.通知客户端文件保存完毕
        OutputStream os = socket.getOutputStream();
        os.write("success".getBytes());

        //6.关闭流
        fos.close();
        os.close();
        socket.close();
    }
}
