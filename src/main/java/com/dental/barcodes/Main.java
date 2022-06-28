package com.dental.barcodes;

public class Main {
    public static void main(String[] args) {
        init();
    }
    private static void init() {
        MainService mainService = new MainService();
        MainFrame mainFrame = new MainFrame(mainService);
        LoginFrame loginFrame = new LoginFrame(mainService, mainFrame);

        loginFrame.showLoginFrame();
    }
}
