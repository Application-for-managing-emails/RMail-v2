package Client;
import java.io.*;
import java.net.Socket;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;





public class MailComposer extends JFrame {
    BufferedReader in;
    PrintWriter out;
    private JTextField recipientField;
    private JTextField subjectField;
    private JTextArea messageArea;
    
    private BlockingQueue<Message> messageQueue;
    private User currentUser;

    public MailComposer(User user) {
    	this.currentUser = user;
        setTitle("RMail "+user.getUsername());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        initComponents();
        setSize(1000, 800);
        setVisible(true);
        messageQueue = new LinkedBlockingQueue<>();
        startInboxListener();
    }
    

    private void initComponents() {
        setBackground(new Color(255, 255, 255));

        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBackground(new Color(240, 240, 240));
        leftPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JLabel inboxLabel = new JLabel("Inbox");
        inboxLabel.setFont(new Font("Arial", Font.BOLD, 16));
        leftPanel.add(inboxLabel, BorderLayout.NORTH);
        JPanel receivedMailsPanel = new JPanel();
        leftPanel.add(new JScrollPane(receivedMailsPanel), BorderLayout.CENTER);

        JPanel composePanel = new JPanel(new BorderLayout());
        composePanel.setBackground(new Color(255, 255, 255));


        JPanel topicPanel = new JPanel();
        topicPanel.setBackground(new Color(255, 87, 51));
        JLabel topicLabel = new JLabel("New Mail");
        topicLabel.setForeground(Color.WHITE);
        topicPanel.add(topicLabel);


        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBackground(new Color(255, 255, 255));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);

        JLabel recipientLabel = new JLabel("Recipient:");
        inputPanel.add(recipientLabel, gbc);

        gbc.gridy++;
        JLabel subjectLabel = new JLabel("Subject:");
        inputPanel.add(subjectLabel, gbc);

        gbc.gridy++;
        JLabel messageLabel = new JLabel("Message:");
        inputPanel.add(messageLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        recipientField = new JTextField();
        recipientField.setPreferredSize(new Dimension(200, 20));
        inputPanel.add(recipientField, gbc);

        gbc.gridy++;
        subjectField = new JTextField();
        subjectField.setPreferredSize(new Dimension(200, 20));
        inputPanel.add(subjectField, gbc);

        gbc.gridy++;
        gbc.weighty = 1.0;
        messageArea = new JTextArea();
        messageArea.setLineWrap(true);
        JScrollPane scrollPane = new JScrollPane(messageArea);
        scrollPane.setPreferredSize(new Dimension(400, 200));
        inputPanel.add(scrollPane, gbc);

        JButton sendButton = new JButton("Send");
        sendButton.setBackground(new Color(255, 87, 51));
        sendButton.setForeground(Color.WHITE);
        sendButton.setFocusPainted(false);
        sendButton.setPreferredSize(new Dimension(100, 30));
        sendButton.setBorder(BorderFactory.createBevelBorder(10));

        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMail();
            }
        });


        composePanel.add(topicPanel, BorderLayout.NORTH);
        composePanel.add(inputPanel, BorderLayout.CENTER);
        composePanel.add(sendButton, BorderLayout.SOUTH);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, composePanel);
        splitPane.setResizeWeight(0.5);

        add(splitPane);
    }

    public void run(User user) throws IOException{
        // String serverAddress = getServerAddress();
        String serverAddress = "localhost";
        Socket socket = new Socket(serverAddress,9002);

        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        out = new PrintWriter(socket.getOutputStream(),true);

        while (true){
            //String line = in.readLine();
            out.println(user.getEmail());
            // if (line.startsWith("MESSAGE")){
            //     messageArea.append(line.substring(8)+"\n");
            // }
        }
    }

    private void sendMail() {
        String recipient = recipientField.getText();
        String subject = subjectField.getText();
        String message = messageArea.getText();

        if (currentUser != null) {
            Message newMessage = new Message(currentUser, recipient, subject, message);
            try {
                messageQueue.put(newMessage);
                System.out.println("Message sent successfully.");
            } catch (InterruptedException e) {
                System.out.println("Failed to send message.");
                e.printStackTrace();
            }
        } else {
            System.out.println("No user logged in.");
        }

        recipientField.setText("");
        subjectField.setText("");
        messageArea.setText("");
    }

    private void startInboxListener() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Message message = messageQueue.take();
                        if (currentUser.equals(message.getRecipient())) {
                            // Add message to the inbox
                            // Example: receivedMailsPanel.add(new JLabel(message.getSubject()));
                            System.out.println("New message received: " + message.getSubject());
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    // Assuming you have a class User and a class Message defined elsewhere
    // You need to implement these classes according to your requirements
    // static class User {
    //     private String username;
    //     // Other user properties

    //     public User(String username) {
    //         this.username = username;
    //     }

    //     public String getUsername() {
    //         return username;
    //     }
    // }

    private static class Message {
        private User sender;
        private String recipient;
        private String subject;
        private String body;

        public Message(User sender, String recipient, String subject, String body) {
            this.sender = sender;
            this.recipient = recipient;
            this.subject = subject;
            this.body = body;
        }

        public User getSender() {
            return sender;
        }

        public String getRecipient() {
            return recipient;
        }

        public String getSubject() {
            return subject;
        }

        public String getBody() {
            return body;
        }
    }
   


	

    

    
}
