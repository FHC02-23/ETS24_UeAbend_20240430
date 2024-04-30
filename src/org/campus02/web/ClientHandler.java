package org.campus02.web;

import java.io.*;
import java.net.Socket;

public class ClientHandler implements Runnable {

    private Socket client;
    private WebProxy webProxy;

    public ClientHandler(Socket client, WebProxy webProxy) {
        this.client = client;
        this.webProxy = webProxy;
    }

    private void handleClientCommunication() {
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(client.getInputStream()));
             BufferedWriter bw = new BufferedWriter(
                     new OutputStreamWriter(client.getOutputStream())
             )
        ) {

            // server -> Lese Modus
            // client -> Schreibe Modus

            // server -> Schreibe Modus
            // client -> Lese Modus
            String input;
            while ((input = br.readLine()) != null) {
                if (input.equalsIgnoreCase("bye")) {
                    System.out.println("client wants to exit");
                    client.close(); // verbindung beendet
                }

                String[] cmds = input.split(" ");
//                String[] cmds = input.split("\\s");
                // zB: input = fetch <<url>>
                // ["fetch", "<<url">>]
                if (cmds.length != 2) {
                    bw.write("error: command invalid");
                    bw.newLine();
                    bw.flush();
                    continue; // gehe wieder direkt in den lesemodus
                }

                switch (cmds[0]) {
                    case "fetch":
                        try {
                            WebPage webPage = webProxy.fetch(cmds[1]);
                            bw.write(webPage.getContent());
                        } catch (UrlLoaderException e) {
                            e.printStackTrace();
                            bw.write("error: loading url failed");
                        }
                        break;
                    case "stats":
                        if (cmds[1].equals("hits")) {
                            bw.write(webProxy.statsHits());
                        } else if (cmds[1].equals("misses")) {
                            bw.write(webProxy.statsMisses());
                        } else {
                            bw.write("error: command invalid");
                        }
                        break;
                    default:
                        bw.write("error: command invalid");
                }
                bw.newLine();
                bw.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        // logik
        handleClientCommunication();
    }
}
