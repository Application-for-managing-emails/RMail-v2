package Client;

import javax.swing.*;
import java.awt.*;
import java.io.PrintWriter;

public class Composer extends JFrame {
    JTextField recipientField = new JTextField(40);
    JTextField subjectField = new JTextField(40);
    JTextArea bodyArea = new JTextArea(8, 40);
    JButton sendButton = new JButton("Send");

    PrintWriter out;

    public Composer(PrintWriter out) {
        this.out = out;

        // Set a custom font
        Font font = new Font("Arial", Font.PLAIN, 14);
        recipientField.setFont(font);
        subjectField.setFont(font);
        bodyArea.setFont(font);

        // Add padding
        recipientField.setBorder(BorderFactory.createCompoundBorder(
                recipientField.getBorder(), 
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        subjectField.setBorder(BorderFactory.createCompoundBorder(
                subjectField.getBorder(), 
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        bodyArea.setBorder(BorderFactory.createCompoundBorder(
                bodyArea.getBorder(), 
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));

        // Set a custom background color
        getContentPane().setBackground(Color.LIGHT_GRAY);

        // Set a custom color for buttons and text fields
        UIManager.put("Button.background", Color.DARK_GRAY);
        UIManager.put("Button.foreground", Color.WHITE);
        UIManager.put("TextField.background", Color.LIGHT_GRAY);
        UIManager.put("TextField.foreground", Color.BLACK);

        setLayout(new BorderLayout());
        add(recipientField, BorderLayout.NORTH);
        add(subjectField, BorderLayout.CENTER);
        add(new JScrollPane(bodyArea), BorderLayout.SOUTH);
        add(sendButton, BorderLayout.EAST);

        sendButton.addActionListener(e -> {
            out.println(recipientField.getText() + ":" + subjectField.getText() + ":" + bodyArea.getText());
            setVisible(false);
        });

        pack();
    }
}