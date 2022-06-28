package com.dental.barcodes;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.TimeUnit;

import static com.dental.barcodes.MainService.*;
import static com.dental.barcodes.MainService.driver;
import static javax.swing.JOptionPane.*;

public class MainFrame extends JFrame implements ActionListener {
    public static final String[] labels= {"관리코드(01)","생산일자(11)","유통기한(17)","로트번호(10)"};
    public static final JTextField[] fields = new JTextField[4];

    MainService mainService;
    JTextField barcodeInput;
    JPanel barcodePanel, buttonPanel, classifyPanel;
    JButton submitButton, exitButton;
    JLabel barcodeLabel;

    public MainFrame(MainService mainService) {
        this.mainService = mainService;
    }

    public void showMainFrame() {
        createFrame();
        createBarcodePanel();
        createClassifyPanel();
        createButtonPanel();

        setVisible(true);
    }

    private void createFrame() {
        setLocation(1200, 0);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("바코드 변환기");
        setResizable(true);
        setPreferredSize(new Dimension(400, 200));
        setLayout(new BorderLayout());
        pack();
    }

    private void createBarcodePanel() {
        barcodePanel = new JPanel(new GridLayout(1, 2, 5, 20));

        barcodeLabel = new JLabel("바코드 스캔");
        barcodeLabel.setHorizontalAlignment(JLabel.CENTER);
        barcodeInput = new JTextField(20);
        barcodeInput.addActionListener(this);

        barcodePanel.add(barcodeLabel);
        barcodePanel.add(barcodeInput);
        add(barcodePanel, BorderLayout.NORTH);
    }

    private void createClassifyPanel() {
        classifyPanel = new JPanel(new GridLayout(4, 2, 5, 5));

        for(int i = 0; i < 4; i++) {
            JLabel label = new JLabel(labels[i]);
            label.setHorizontalAlignment(JLabel.CENTER);
            fields[i] = new JTextField(20);
            fields[i].setEditable(false);
            classifyPanel.add(label);
            classifyPanel.add(fields[i]);
        }

        add(classifyPanel, BorderLayout.CENTER);
    }

    private void createButtonPanel() {
        buttonPanel = new JPanel(new GridLayout(1, 2));

        submitButton = new JButton("전송");
        submitButton.addActionListener(this);

        exitButton = new JButton("종료");
        exitButton.addActionListener(e -> {
            driver.quit();
            System.exit(0);
        });

        buttonPanel.add(submitButton);
        buttonPanel.add(exitButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String barcodeLine = barcodeInput.getText().toUpperCase();
        if (barcodeLine.equals("")) {
            showMessageDialog(null, "바코드를 먼저 스캔해주세요.", "알림창", PLAIN_MESSAGE);
            return;
        }
        try {
            String udi = mainService.format(barcodeLine);
            if (values[3].length() > 15 && values[3].indexOf("21") > 5) {
                showMessageDialog(null, "로트번호에 일련번호가 포함되었을 수 있습니다.", "알림창", WARNING_MESSAGE);
                return;
            }
            mainService.sendBarcode(udi);
            for (int i = 0; i < 4; i++)
                fields[i].setText(values[i]);
            barcodeInput.setText("");
        } catch (Exception ex) {
            showMessageDialog(null, "제대로 전송이 안 됐을 수 있습니다.\n" + ex.getMessage(), "알림창", WARNING_MESSAGE);
        }
    }
}
