package login_signup;

import Client.Client;
// import Client.Dashboard;
import Client.MailComposer;
import Client.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class Login extends javax.swing. JFrame {

    public Login() {
        initComponents();

        // Resize the RMail.png logo
        ImageIcon icon = new ImageIcon(getClass().getResource("../Assets/RMail.png"));
        Image image = icon.getImage().getScaledInstance(232, 142, Image.SCALE_AREA_AVERAGING);
        ImageIcon scaledIcon = new ImageIcon(image);
        jLabel5.setIcon(scaledIcon);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        Right = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        Left = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jEmailLabel = new javax.swing.JLabel();
        jEmailText = new javax.swing.JTextField();
        jPwdLabel = new javax.swing.JLabel();
        jPasswordField1 = new javax.swing.JPasswordField();
        jLogInBtn = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jSignUp = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("LOGIN");
        setPreferredSize(new java.awt.Dimension(810, 500));

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setPreferredSize(new java.awt.Dimension(800, 500));
        jPanel1.setLayout(null);

        Right.setBackground(new java.awt.Color(255, 87, 51));
        Right.setPreferredSize(new java.awt.Dimension(400, 500));

        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Assets/RMail.png"))); // NOI18N

        jLabel6.setFont(new java.awt.Font("Showcard Gothic", 1, 24)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        //jLabel6.setText("Company Name");

        jLabel7.setFont(new java.awt.Font("Segoe UI Light", 0, 14)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(204, 204, 204));

        javax.swing.GroupLayout RightLayout = new javax.swing.GroupLayout(Right);
        Right.setLayout(RightLayout);
        RightLayout.setHorizontalGroup(
                RightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, RightLayout.createSequentialGroup()
                                .addGap(0, 81, Short.MAX_VALUE)
                                .addComponent(jLabel7)
                                .addGap(40, 40, 40))
                        .addGroup(RightLayout.createSequentialGroup()
                                .addGroup(RightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(RightLayout.createSequentialGroup()
                                                .addGap(103, 103, 103)
                                                .addComponent(jLabel6))
                                        .addGroup(RightLayout.createSequentialGroup()
                                                .addGap(70, 80, 90)
                                                .addComponent(jLabel5)))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        RightLayout.setVerticalGroup(
                RightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(RightLayout.createSequentialGroup()
                                .addGap(136, 136, 136)
                                .addComponent(jLabel5)
                                .addGap(26, 26, 26)
                                .addComponent(jLabel6)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 91, Short.MAX_VALUE)
                                .addComponent(jLabel7)
                                .addGap(78, 78, 78))
        );

        jPanel1.add(Right);
        Right.setBounds(0, 0, 400, 500);

        Left.setBackground(new java.awt.Color(255, 255, 255));
        Left.setMinimumSize(new java.awt.Dimension(400, 500));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 87, 51));
        jLabel1.setText("LOGIN");

        jEmailLabel.setBackground(new java.awt.Color(102, 102, 102));
        jEmailLabel.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jEmailLabel.setText("Email");

        jEmailText.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jEmailText.setForeground(new java.awt.Color(102, 102, 102));
        jEmailText.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

        jPwdLabel.setBackground(new java.awt.Color(102, 102, 102));
        jPwdLabel.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jPwdLabel.setText("Password");

        jLogInBtn.setBackground(new java.awt.Color(255, 87, 51));
        jLogInBtn.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLogInBtn.setForeground(new java.awt.Color(255, 255, 255));
        jLogInBtn.setText("Log In");
        jLogInBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jLogInBtnActionPerformed(evt);
            }

            private void jLogInBtnActionPerformed(ActionEvent evt) {
                String email = jEmailText.getText();
                String password = String.valueOf(jPasswordField1.getPassword());
                String SUrl, SUser, SPass;
                SUrl = "jdbc:mysql://localhost:3306/rmail";
                SUser = "root";
                SPass = "";

                try {
                    Class.forName("com.mysql.cj.jdbc.Driver");
                    Connection con = DriverManager.getConnection(SUrl, SUser, SPass);
                    Statement stmt = con.createStatement();
                    String query = "SELECT * FROM users WHERE email = '" + email + "' AND password = '" + password + "'";
                    ResultSet rs = stmt.executeQuery(query);
                    if (rs.next()) {
                        JOptionPane.showMessageDialog(null, "Login Successful");
                        // Open the compose page
                        String userName = rs.getString("userName");
                        User user = new User(userName, email);
                        Client client = new Client();
                        client.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                        client.frame.setVisible(true);
                        client.run(user);

                        // MailComposer composer = new MailComposer(user);
                        // composer.pack();
                        // composer.setVisible(true);
                        // composer.setLocationRelativeTo(null);
                        // Dispose of the current login frame
                        dispose();
                        //composer.run(user);
                    } else {
                        JOptionPane.showMessageDialog(null, "Invalid Email or Password");
                    }
                } catch (Exception e) {
                    System.out.println(e);
                }
            }


        
        });

        jLabel4.setText("I don't have an account");

        jSignUp.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jSignUp.setForeground(new java.awt.Color(58, 51, 255));
        jSignUp.setBackground(new java.awt.Color(255, 255, 255));
        jSignUp.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        jSignUp.setText("Sign Up");
        jSignUp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jSignUpActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout LeftLayout = new javax.swing.GroupLayout(Left);
        Left.setLayout(LeftLayout);
        LeftLayout.setHorizontalGroup(
                LeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(LeftLayout.createSequentialGroup()
                                .addGroup(LeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(LeftLayout.createSequentialGroup()
                                                .addGap(138, 138, 138)
                                                .addComponent(jLabel1))
                                        .addGroup(LeftLayout.createSequentialGroup()
                                                .addGap(30, 30, 30)
                                                .addGroup(LeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addGroup(LeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                                .addComponent(jEmailLabel)
                                                                .addComponent(jEmailText)
                                                                .addComponent(jPwdLabel)
                                                                .addComponent(jPasswordField1, javax.swing.GroupLayout.DEFAULT_SIZE, 343, Short.MAX_VALUE)
                                                                .addComponent(jLogInBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                        .addGroup(LeftLayout.createSequentialGroup()
                                                                .addComponent(jLabel4)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(jSignUp)))))
                                .addContainerGap(27, Short.MAX_VALUE))
        );
        LeftLayout.setVerticalGroup(
                LeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(LeftLayout.createSequentialGroup()
                                .addGap(51, 51, 51)
                                .addComponent(jLabel1)
                                .addGap(40, 40, 40)
                                .addComponent(jEmailLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jEmailText, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jPwdLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jPasswordField1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(26, 26, 26)
                                .addComponent(jLogInBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(33, 33, 33)
                                .addGroup(LeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel4)
                                        .addComponent(jSignUp))
                                .addContainerGap(77, Short.MAX_VALUE))
        );

        jPanel1.add(Left);
        Left.setBounds(400, 0, 400, 500);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 129, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 149, Short.MAX_VALUE))
        );

        getAccessibleContext().setAccessibleName("LOGIN");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jSignUpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jSignUpActionPerformed

        SignUp SignUpFrame = new SignUp();
        SignUpFrame.setVisible(true);
        SignUpFrame.pack();
        SignUpFrame.setLocationRelativeTo(null);
        this.dispose();
    }//GEN-LAST:event_jSignUpActionPerformed
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Login().setVisible(true);
            }
        });
    }

    /**
     * @param args the command line arguments
     */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel Left;
    private javax.swing.JPanel Right;
    private javax.swing.JButton jLogInBtn;
    private javax.swing.JButton jSignUp;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jEmailLabel;
    private javax.swing.JLabel jPwdLabel;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPasswordField jPasswordField1;
    private javax.swing.JTextField jEmailText;
    // End of variables declaration//GEN-END:variables
}
