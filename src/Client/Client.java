package Client;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import javax.swing.*;
import javax.swing.table.*;

public class Client {
    BufferedReader in;
    PrintWriter out;
    public JFrame frame = new JFrame("RMail");
    JTextField textField = new JTextField(40);
    JTextArea messageArea = new JTextArea(8, 40);
    DefaultTableModel tableModel = new DefaultTableModel(new String[]{"Email", "Subject", "Body"}, 0);
    JTable table = new JTable(tableModel);
    JButton composeButton = new JButton("Compose");

    public Client() {
        // Set a custom font
        Font font = new Font("Arial", Font.PLAIN, 14);
        textField.setFont(font);
        messageArea.setFont(font);

        // Add padding
        textField.setBorder(BorderFactory.createCompoundBorder(
                textField.getBorder(), 
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        messageArea.setBorder(BorderFactory.createCompoundBorder(
                messageArea.getBorder(), 
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));

        // Set a custom background color
        frame.getContentPane().setBackground(Color.LIGHT_GRAY);

        // Set a custom color for buttons and text fields
        UIManager.put("Button.background", Color.DARK_GRAY);
        UIManager.put("Button.foreground", Color.WHITE);
        UIManager.put("TextField.background", Color.LIGHT_GRAY);
        UIManager.put("TextField.foreground", Color.BLACK);

        textField.setEditable(false);
        messageArea.setEditable(false);
        frame.getContentPane().add(textField, "North");
        frame.getContentPane().add(new JScrollPane(messageArea), "Center");

        frame.getContentPane().add(new JScrollPane(table), "Center");
        frame.getContentPane().add(composeButton, "South");
        composeButton.addActionListener(e -> new Composer(out).setVisible(true));

        frame.pack();

        textField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String recipient = JOptionPane.showInputDialog(
                        frame,
                        "Enter the recipient's name:",
                        "Recipient",
                        JOptionPane.PLAIN_MESSAGE
                );
                out.println(recipient + ":" + textField.getText());
                textField.setText("");
            }
        });
    }

    public void run(User user) {
        String serverAddress = "localhost";

        new Thread(() -> {
            try {
                Socket socket = new Socket(serverAddress, 9002);

                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                out = new PrintWriter(socket.getOutputStream(), true);

                while (true) {
                    String line = in.readLine();

                    if (line.startsWith("SUBMITEMAIL")) {
                        out.println(user.getEmail());
                    } else if (line.startsWith("EMAILACCEPTED")) {
                        SwingUtilities.invokeLater(() -> textField.setEditable(true));
                    } else if (line.startsWith("MESSAGE")) {
                        SwingUtilities.invokeLater(() -> {
                            String[] parts = line.substring(8).split(":");
                            String email = parts[0];
                            String subject = parts[1];
                            String body = parts[2];
                            tableModel.addRow(new Object[]{email, subject, body});
                        });
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public static void main(String args[]) throws Exception {
        User user = new User("user1", "user@rmail.com");
        Client client = new Client();
        client.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        client.frame.setVisible(true);
        client.run(user);
    }
}