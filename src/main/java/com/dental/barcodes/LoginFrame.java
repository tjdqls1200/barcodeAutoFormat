package com.dental.barcodes;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import static javax.swing.JOptionPane.PLAIN_MESSAGE;
import static javax.swing.JOptionPane.showMessageDialog;

public class LoginFrame extends JFrame implements ActionListener {
    MainService mainService;
    MainFrame mainFrame;

    JPanel loginPanel;
    JLabel idLabel, passwordLabel;
    JTextField idInput, passwordInput;
    JButton loginButton, exitButton;


    public LoginFrame(MainService mainService, MainFrame mainFrame) {
        this.mainService = mainService;
        this.mainFrame = mainFrame;
    }

    public void showLoginFrame() {
        createFrame();
        createLoginPanel();
        setVisible(true);
    }

    private void createFrame() {
        setLocation(1200, 0);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("바코드 변환기");
        setResizable(true);
        setPreferredSize(new Dimension(400, 100));
        setLayout(new BorderLayout());
        pack();
    }

    private void createLoginPanel() {
        loginPanel = new JPanel(new GridLayout(3, 2));
        createIdField();
        createPasswordField();
        createLoginButton();
        add(loginPanel);
    }

    private void createIdField() {
        idLabel = new JLabel("아이디");
        idLabel.setHorizontalAlignment(JLabel.CENTER);
        idInput = new JTextField(20);
        idInput.setFocusTraversalKeysEnabled(true);
        idInput.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_TAB)
                    passwordInput.requestFocus();
            }
        });
        loginPanel.add(idLabel);
        loginPanel.add(idInput);
    }

    private void createPasswordField() {
        passwordLabel = new JLabel("비밀번호");
        passwordLabel.setHorizontalAlignment(JLabel.CENTER);
        passwordInput = new JTextField(20);
        passwordInput.setFocusTraversalKeysEnabled(true);
        passwordInput.addActionListener(this);
        loginPanel.add(passwordLabel);
        loginPanel.add(passwordInput);
    }

    private void createLoginButton() {
        loginButton = new JButton("로그인");
        loginButton.addActionListener(this);
        exitButton = new JButton("종료");
        exitButton.addActionListener(this);

        loginPanel.add(loginButton);
        loginPanel.add(exitButton);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == exitButton) {
            System.exit(0);
        }
        if (idInput.getText().equals("") || passwordInput.getText().equals("")) {
            showMessageDialog(null, "로그인 또는 비밀번호를 입력해주세요.", "알림창", PLAIN_MESSAGE);
            return;
        }
        mainService.login(idInput.getText(), passwordInput.getText());
        setVisible(false);
        mainFrame.showMainFrame();

    }
}