package org.campus02.web;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerMT {

    public static void main(String[] args) throws IOException {
//        WebProxy wp = new WebProxy();

        PageCache pc = new PageCache();
        pc.warmUp("data/demo_urls.txt");
        WebProxy wp = new WebProxy(pc);

        System.out.println("start server on port 1111");
        try (ServerSocket server = new ServerSocket(1111)) {
            while (true) {
                System.out.println("server is waiting for clients");
                Socket client = server.accept();
                System.out.println("client connection established");
                new Thread(new ClientHandler(client, wp)).start();
            }
        }
    }
}
