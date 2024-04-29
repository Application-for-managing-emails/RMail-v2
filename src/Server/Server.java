package Server;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Handler;

public class Server {
    private static final int PORT = 9002;

    private static Map<String, String> emails = new HashMap<>();

    private static Map<String, PrintWriter> senders = new HashMap<>();

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            System.out.println("Server started on port " + PORT);

            while (true) {
                new Handler(serverSocket.accept()).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class Handler extends Thread{
        private String email;
        private Socket socket;
        private BufferedReader in;
        private PrintWriter out;

        public Handler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);

                while (true) {
                    out.println("SUBMITEMAIL");
                    email = in.readLine();
                    if (email == null) {
                        return;
                    }
                    synchronized (emails) {
                        if (!emails.containsKey(email)) {
                            emails.put(email, "");
                            break;
                        }
                    }
                }
                out.println("EMAILACCEPTED");
                senders.put(email, out);

                while (true) {
                    String input = in.readLine();
                    if (input == null) {
                        return;
                    }
                    String recipient = input.split(":")[0];
                    String subject = input.split(":")[1];
                    String body = input.split(":")[2];
                    PrintWriter sender = senders.get(recipient);
                    if (sender != null) {
                        sender.println("MESSAGE " + email + ": " + subject + ": " + body);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (email != null) {
                    emails.remove(email);
                }
                if (out != null) {
                    senders.remove(email);
                }
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
