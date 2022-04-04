package tcpip;

import java.io.*;
import java.net.*;

public class SmtpToTelegram {
    public static void main(String[] args) throws IOException {
        int port = 25;
        String ya_telegramId = "277750634"; //chatID YA
        String token = "5210238463:AAGSBxmw0eck61h_s5rJBIkexhZxRWn91eA"; //Токен бота yand1960bot

        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("SMTP receiver listening...");
        while (true) {
            System.out.println("Waiting for messages...");
            Socket socket = serverSocket.accept();
            System.out.println("Client connected");

            BufferedWriter writer
                    = new BufferedWriter(
                        new OutputStreamWriter(socket.getOutputStream())
            );

            BufferedReader reader
                    = new BufferedReader(
                    new InputStreamReader(socket.getInputStream())
            );

            writer.write("220 127.0.0.1\r\n");
            writer.flush();

            //EHL0
            String data = reader.readLine();
            System.out.println(data);

            writer.write("250 127.0.0.1\r\n");
            writer.flush();

            //MAIL FROM
            data = reader.readLine();
            System.out.println(data);

            writer.write("250 Ok\r\n");
            writer.flush();

            //RCPT TO
            data = reader.readLine();
            System.out.println(data);

            writer.write("250 Ok\r\n");
            writer.flush();

            //DATA
            data = reader.readLine();
            System.out.println(data);

            writer.write("354 End with <CRLF>.<CRLF>\r\n");
            writer.flush();

            //GET message
            data = "";
            String line = "";

            while (!line.equals(".") && line != null) {
                data += line + "\n";
                line = reader.readLine();
            }
            System.out.println("************Data received***************");
            // TODO Здесь из data нужно вычленить message и chatID.
            // То, как реализовано сейчас - просто заглушка:
            String message =data.substring(0);
            String chatID = ya_telegramId; //чат YA
            //chatID = "1354314310"; //чат СШ
            //chatID = "1234567"; //несуществующий чат
            System.out.println(message);
            System.out.println("****************************************");

            //Отправка сообщения боту
            String urlString = "https://api.telegram.org/bot%s/sendMessage?chat_id=%s&text=%s";
            urlString = String.format(urlString, token, chatID, URLEncoder.encode(message));

            try {
                URL url = new URL(urlString);
                URLConnection conn = url.openConnection();
                InputStream is = new BufferedInputStream(conn.getInputStream());
                writer.write("250 Ok\r\n");
                writer.flush();

            } catch (IOException e) {
                e.printStackTrace();
                writer.write("550 5.5.1 Address not found in Telegram\r\n");
                writer.flush();

            }

            //QUIT
            data= reader.readLine();
            System.out.println(data);

            writer.write("221 Bye\r\n");
            writer.flush();

            socket.close();

        }
    }

}
