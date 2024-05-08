package Client;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.PrintWriter;

public class Composer extends JFrame {
    JTextField recipientField = new JTextField(20); // Reduced the width
    JTextField subjectField = new JTextField(20); // Reduced the width
    JTextArea bodyArea = new JTextArea(12, 40); // Increased the height
    JButton sendButton = new JButton("Send");
    JButton attachmentButton = new JButton("Attach");
    JFileChooser fileChooser = new JFileChooser();

    PrintWriter out;

    public Composer(PrintWriter out) {
        this.out = out;

        // Set a custom font
        Font font = new Font("Arial", Font.PLAIN, 14);
        Font labelFont = new Font("Arial", Font.BOLD, 14); // Font for labels
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
        UIManager.put("Button.background", Color.ORANGE); // Changed to orange
        UIManager.put("Button.foreground", Color.WHITE);
        UIManager.put("TextField.background", Color.LIGHT_GRAY);
        UIManager.put("TextField.foreground", Color.BLACK);

        setLayout(new BorderLayout());

        // Panel for the first two rows (small)
        JPanel topPanel = new JPanel(new GridLayout(2, 2)); // 2 rows, 2 columns
        JLabel recipientLabel = new JLabel("Mailto:");
        recipientLabel.setFont(labelFont);
        topPanel.add(recipientLabel);
        topPanel.add(recipientField);
        JLabel subjectLabel = new JLabel("Subject:");
        subjectLabel.setFont(labelFont);
        topPanel.add(subjectLabel);
        topPanel.add(subjectField);
        add(topPanel, BorderLayout.NORTH);
        
        // Panel for the third row (large)
        JPanel bodyPanel = new JPanel(new BorderLayout());
        JLabel bodyLabel = new JLabel("Body:");
        bodyLabel.setFont(labelFont);
        bodyPanel.add(bodyLabel, BorderLayout.NORTH);
        bodyPanel.add(new JScrollPane(bodyArea), BorderLayout.CENTER);
        add(bodyPanel, BorderLayout.CENTER);
        
        // Panel for the buttons at the bottom
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER)); // Center-aligned
        bottomPanel.add(attachmentButton);
        bottomPanel.add(sendButton);
        add(bottomPanel, BorderLayout.SOUTH);

        sendButton.addActionListener(e -> {
            out.println(recipientField.getText() + ":" + subjectField.getText() + ":" + bodyArea.getText());
            setVisible(false);
        });

        pack();
        attachmentButton.addActionListener(e -> {
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
        File selectedFile = fileChooser.getSelectedFile();
        // Process the selected file (e.g., store the file path or upload the file)
        // You can customize this part based on how you want to handle file attachments
        String attachmentFilePath = selectedFile.getAbsolutePath();
        // Update UI or store attachment information
        // For simplicity, let's display the file path in the body area
        bodyArea.append("\nAttachment: " + attachmentFilePath);
    }
});

    }
}
