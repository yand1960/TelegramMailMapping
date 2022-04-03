package tcpip;

import java.io.*;
import java.net.*;

public class TelegramToPop3 {
    public static void main(String[] args) throws IOException {
        int port = 110;
        String ya_telegramId = "277750634";
        String token = "5210238463:AAGSBxmw0eck61h_s5rJBIkexhZxRWn91eA";

        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("POP3 Receiver listening...");
        while (true) {
            System.out.println("Waiting for queries...");
            Socket socket = serverSocket.accept();
            System.out.println("Client connected");

            //получение новых сообщений от бота
            String urlString = "https://api.telegram.org/bot%s/getUpdates";
            urlString = String.format(urlString, token);

            try {
                //TODO Преобразовать полученный от бота текст к формату почтовых сообщений
                URL url = new URL(urlString);
                URLConnection conn = url.openConnection();
                InputStream is = new BufferedInputStream(conn.getInputStream());
                String messages = new String(is.readAllBytes());
                System.out.println(messages);
            } catch (IOException e) {
                e.printStackTrace();
            }

            //получение потоков чтения и записи
            BufferedWriter writer
                    = new BufferedWriter(
                    new OutputStreamWriter(socket.getOutputStream())
            );

            BufferedReader reader
                    = new BufferedReader(
                    new InputStreamReader(socket.getInputStream())
            );

            //начало диалога

            writer.write("+OK POP3 127.0.0.1 server ready\r\n");
            writer.flush();

            String data = "";
            // Обработка сообщений в диалоге
            while (data == null || !data.equals("QUIT")) {

                data = reader.readLine();
                if (data != null) {

                    data = data.trim();
                    System.out.println(data);

                    if (data.equals("CAPA")) {
                        writer.write("-ERR\r\n");
                        writer.flush();
                    }

                    if (data.equals("AUTH")) {
                        writer.write("-ERR\r\n");
                        writer.flush();
                    }

                    if (data.equals("UIDL")) {
                        writer.write("-ERR\r\n");
                        writer.flush();
                    }

                    if (data.contains("USER")) {
                        writer.write("+OK\r\n");
                        writer.flush();
                    }

                    if (data.contains("PASS")) {
                        writer.write("+OK\r\n");
                        writer.flush();
                    }

                    if (data.equals("STAT")) {
                        //TODO передать реальную статистику сообщений
                        writer.write("+OK 2 2000\r\n");
                        writer.flush();
                    }

                    if (data.equals("LIST")) {
                        //TODO передать список реальных сообщений
                        writer.write("+OK 1 messages\r\n");
                        writer.write("1 1000\r\n");
                        writer.write("2 1000\r\n");
                        writer.write(".\r\n");
                        writer.flush();
                    }

                    if (data.contains("TOP")) {
                        //TODO Вычленить параметры команды TOP и передать данные соответственно
                        writer.write("-ERR\r\n");
                        writer.flush();
                    }

                    if (data.contains("RETR")) {
                        //TODO Вычленить параметры команды RET и передать соответствующее сообщение
                        writer.write("+OK 1000\r\n");
                        writer.write(".\r\n");
                        writer.flush();
                    }

                    if (data.contains("DELE")) {
                        //Тут ничего менять не надо. Не надо удалять сообщений фактически
                        writer.write("+OK deleted\r\n");
                        writer.write(".\r\n");
                        writer.flush();
                    }

                    if (data.equals("QUIT")) {
                        writer.write("+OK server signing off\r\n");
                        writer.flush();
                    }

                }
            }

            socket.close();

        }
    }

}
