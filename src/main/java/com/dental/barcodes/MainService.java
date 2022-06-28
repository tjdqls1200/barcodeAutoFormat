package com.dental.barcodes;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MainService {
    public static ChromeDriver driver;
    private static final String URL = "https://careliveudi.com/UDI/login.jsp";
    public static final String[] values = new String[4];


    public void login(String id, String password) {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        driver = new ChromeDriver(options);
        driver.get(URL);
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        List<WebElement> loginFormList = driver.findElements(By.tagName("input"));
        loginFormList.get(0).sendKeys(id);
        loginFormList.get(1).sendKeys(password);
        loginFormList.get(2).click();
    }


    public void sendBarcode(String udi) throws Exception {
        WebElement barcode = driver.findElement(By.id("barcode"));
        barcode.clear();
        barcode.sendKeys(udi + Keys.ENTER);
    }


    public String format(String barcodeLine) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            values[i] = "";
        }
        for (int i = 0; i < 4; i++) {
            if (barcodeLine.equals(""))
                break;
            int size = barcodeSplit(sb, barcodeLine);
            barcodeLine = barcodeLine.substring(size + 2);
        }
        return sb.toString();
    }

    private int barcodeSplit(StringBuilder sb, String barcodeLine) {
        // (01) 관리코드, (11) 생산일자, (17) 유통기한, (10) 로트번호
        switch (barcodeLine.substring(0, 2)) {
            case "01": {
                String manageCode = barcodeLine.substring(2, 16);
                values[0] = manageCode;
                sb.append("(01)").append(manageCode);
                return manageCode.length();
            }
            case "30" : {
                if (barcodeLine.charAt(4) == '1' && barcodeLine.charAt(5) == '0') {
                    sb.append("(30)").append(barcodeLine.charAt(2)).append(barcodeLine.charAt(3));
                    return 2;
                } else if (barcodeLine.charAt(3) == '1' && barcodeLine.charAt(4) == '0') {
                    sb.append("(30)").append(barcodeLine.charAt(2));
                    return 1;
                }
            }
            case "11": {
                String manufacturingYear = barcodeLine.substring(2, 8);
                values[1] = manufacturingYear;
                sb.append("(11)").append(manufacturingYear);
                return manufacturingYear.length();
            }
            case "17": {
                String expirationDate = barcodeLine.substring(2, 8);
                values[2] = expirationDate;
                sb.append("(17)").append(expirationDate);
                return expirationDate.length();
            }
            case "10": {
                String lotNumber = barcodeLine.substring(2);
                if (values[1].equals("") && values[2].equals("")) {
                    lotNumber = lotNumberCheck(barcodeLine, lotNumber);
                }
                values[3] = lotNumber;
                sb.append("(10)").append(lotNumber);
                return lotNumber.length();
            }
            default: {
                return -2;
            }
        }
    }

    private String lotNumberCheck(String barcodeLine, String lotNumber) {
        int lastIndex = barcodeLine.length() - 1;
        if (lastIndex > 7 && barcodeLine.charAt(lastIndex - 7) == '1') {
            if (barcodeLine.charAt(lastIndex - 6) == '7' || barcodeLine.charAt(lastIndex - 6) == '1') {
                if (lastIndex > 15 && barcodeLine.charAt(lastIndex - 15) == '1' &&
                        (barcodeLine.charAt(lastIndex - 14) == '1' || barcodeLine.charAt(lastIndex - 14) == '7')) {
                    lotNumber = barcodeLine.substring(2, lastIndex - 15);
                } else {
                    lotNumber = barcodeLine.substring(2, lastIndex - 7);
                }
            }
        }
        return lotNumber;
    }
}
