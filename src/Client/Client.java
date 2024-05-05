package Client;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

public class Client {
    BufferedReader in;
    PrintWriter out;
    public JFrame frame = new JFrame("RMail");
    JTextField textField = new JTextField(40);
    JTextArea messageArea = new JTextArea(8, 40);
    DefaultTableModel tableModel = new DefaultTableModel(new String[]{"Sender Email", "Subject", "Body", "Attachments"}, 0);
    JTable table = new JTable(tableModel);
    JButton composeButton = new JButton("Compose");
    JTableHeader header = table.getTableHeader();
    DefaultTableCellRenderer headerRenderer = (DefaultTableCellRenderer) header.getDefaultRenderer();

    public Client() {
        // Set a custom font
        Font font = new Font("Arial", Font.PLAIN, 14);
        Font headerFont = new Font("Arial", Font.BOLD, 14);
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
        frame.setLayout(new BorderLayout());

        // Set a custom color for buttons and text fields
        Color darkOrange = new Color(255, 140, 0);
        UIManager.put("Button.background", darkOrange);
        UIManager.put("Button.foreground", Color.WHITE);
        UIManager.put("TextField.background", Color.LIGHT_GRAY);
        UIManager.put("TextField.foreground", Color.BLACK);

        textField.setEditable(false);
        messageArea.setEditable(false);
        frame.getContentPane().add(textField, BorderLayout.NORTH);
        frame.getContentPane().add(new JScrollPane(messageArea), BorderLayout.CENTER);
        frame.getContentPane().add(new JScrollPane(table), BorderLayout.CENTER);
        frame.getContentPane().add(composeButton, BorderLayout.SOUTH);
        composeButton.addActionListener(e -> {
            Composer composer = new Composer(out);
            composer.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            composer.setVisible(true);
        });
        composeButton.setBackground(darkOrange);
        composeButton.setForeground(Color.WHITE);
        composeButton.setFont(font);

        // Adjust the appearance of the table header
        headerRenderer.setBackground(darkOrange);
        headerRenderer.setForeground(Color.WHITE);
        headerRenderer.setFont(headerFont);
        headerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        header.setDefaultRenderer(headerRenderer);

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

        // Allow users to delete rows
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    int row = table.rowAtPoint(e.getPoint());
                    int column = table.columnAtPoint(e.getPoint());
                    if (row >= 0 && column >= 0) {
                        int option = JOptionPane.showConfirmDialog(frame, "Are you sure you want to delete this message?", "Delete Message", JOptionPane.YES_NO_OPTION);
                        if (option == JOptionPane.YES_OPTION) {
                            tableModel.removeRow(row);
                        }
                    }
                }
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
                            String attachments = parts.length > 3 ? parts[3] : ""; // Check if attachments exist
                            tableModel.addRow(new Object[]{email, subject, body, attachments});
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
