
import org.apache.commons.io.FileUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.*;

public class MyJUnit {
    WebDriver driver;

    @Before
    public void setup() {
        System.setProperty("webdriver.gecko.driver", "./src/test/resources/geckodriver.exe");
        FirefoxOptions ops = new FirefoxOptions();

//        System.setProperty("webdriver.chrome.driver", "./src/test/resources/chromedriver.exe");
//        ChromeOptions ops =new ChromeOptions();

        ops.addArguments("--headed"); // if it's headless means browser won't open but in backend automation will be run
        driver = new FirefoxDriver(ops);
//        driver=new ChromeDriver(ops);
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));

    }

    @Test
    public void getTitle() {
        driver.get("https://demoqa.com/");
        String title = driver.getTitle();
        System.out.println(title);
        Assert.assertEquals("ToolsQA", title);
    }

    @Test
    public void checkIfImageExist() {
        driver.get("https://demoqa.com/");
        WebElement image1 = driver.findElement(By.cssSelector("img"));
        Assert.assertTrue(String.valueOf(image1.isDisplayed()), true);
//        WebElement image2 = driver.findElement(By.xpath("//header/a[1]/img[1]"));
//        Assert.assertTrue(String.valueOf(image2.isDisplayed()),true);

    }

    @Test
    public void writeSomething() {
        driver.get("https://demoqa.com/text-box");
        WebElement txtUsername = driver.findElement(By.id("userName"));
//        WebElement txtUsername = driver.findElement(By.cssSelector("[type=text]"));
//        WebElement txtUsername = driver.findElement(By.cssSelector("[placeholder='Full Name']"));
//        WebElement txtUsername = driver.findElement(By.tagName("input"));
//        List<WebElement> elements = driver.findElements(By.tagName("input"));
//        elements.get(0).sendKeys("Smrity");
//        elements.get(1).sendKeys("Smrity@test.com");

        txtUsername.sendKeys("Smrity");
        driver.findElement((By.id("userEmail"))).sendKeys("farhana@maildrop.com");
        Actions actions = new Actions(driver);
        WebElement btnSubmit = driver.findElement(By.id("submit"));
        actions.moveToElement(btnSubmit).click().perform();

    }

    @Test
    public void clickButton() {
//        driver.get("https://demoqa.com/buttons");
//        driver.findElement(By.id("8rGHi"));
        List<WebElement> buttons = driver.findElements(By.tagName("button"));
        buttons.get(3).click();
        Actions actions = new Actions(driver);
        actions.doubleClick(buttons.get(1)).perform();
        actions.contextClick(buttons.get(2)).perform();

        String doubleClickMassage = driver.findElement(By.id("doubleClickMessage")).getText();
        String rightClickMassage = driver.findElement(By.id("rightClickMessage")).getText();
        String dynamicClickMassage = driver.findElement(By.id("dynamicClickMessage")).getText();

        Assert.assertTrue(doubleClickMassage.contains("You have done a double click"));
        Assert.assertTrue(rightClickMassage.contains("You have done a right click"));
        Assert.assertTrue(dynamicClickMassage.contains("You have done a dynamic click"));
    }

    @Test
    public void alertHandle() throws InterruptedException {
        driver.get("https://demoqa.com/alerts");
        driver.findElement(By.id("alertButton")).click();
        Thread.sleep(2000);
        driver.switchTo().alert().accept();
    }

    @Test
    public void alertHandleWithDelay() throws InterruptedException {
        driver.get("https://demoqa.com/alerts");
        driver.findElement(By.id("timerAlertButton")).click();
        Thread.sleep(7000);
        driver.switchTo().alert().accept();
    }

    @Test
    public void dialogBoxHandle() {
        driver.get("https://demoqa.com/alerts");
        driver.findElement(By.id("confirmButton")).click();
        driver.switchTo().alert().dismiss();
    }

    @Test
    public void promptHandle() {
        driver.get("https://demoqa.com/alerts");
        driver.findElement(By.id("promtButton")).click();
        driver.switchTo().alert().sendKeys("Smrity");
        driver.switchTo().alert().accept();
    }

    @Test
    public void selectDate() {
        driver.get("https://demoqa.com/date-picker");
        driver.findElement(By.id("datePickerMonthYearInput")).click();
        driver.findElement(By.id("datePickerMonthYearInput")).clear();
        driver.findElement(By.id("datePickerMonthYearInput")).sendKeys("05/06/2022");
        driver.switchTo().alert().sendKeys(String.valueOf(Keys.ENTER));
    }

    @Test
    public void selectDropdown() {
        driver.get("https://demoqa.com/select-menu");
        Select options = new Select(driver.findElement(By.id("oldSelectMenu")));
        options.selectByValue("6");
        options.selectByVisibleText("White");
    }

    @Test
    public void selectMultipleDropdown() {
        driver.get("https://demoqa.com/select-menu");
        Select options = new Select(driver.findElement(By.id("cars")));
        if (options.isMultiple()) {
            options.selectByValue("audi");
        }
    }

    @Test
    public void handleMultipleTab() throws InterruptedException {
        driver.get("https://demoqa.com/browser-windows");
        driver.findElement(By.id("tabButton")).click();
        Thread.sleep(5000);
        ArrayList<String> w = new ArrayList(driver.getWindowHandles());
//switch to open tab
        driver.switchTo().window(w.get(1));
        System.out.println("New tab title: " + driver.getTitle());
        String text = driver.findElement(By.id("sampleHeading")).getText();
        Assert.assertEquals(text, "This is a sample page");
        driver.close();
        driver.switchTo().window(w.get(0));

    }

    @Test
    public void handleWindow() {
        driver.get("https://demoqa.com/browser-windows");

        driver.findElement(By.id(("windowButton"))).click();
        String mainWindowHandle = driver.getWindowHandle();
        Set<String> allWindowHandles = driver.getWindowHandles();
        Iterator<String> iterator = allWindowHandles.iterator();

        while (iterator.hasNext()) {
            String ChildWindow = iterator.next();
            if (!mainWindowHandle.equalsIgnoreCase(ChildWindow)) {
                driver.switchTo().window(ChildWindow);
                String text = driver.findElement(By.id("sampleHeading")).getText();
                Assert.assertTrue(text.contains("This is a sample page"));
            }
        }
    }

    @Test
    public void modalDialog() {
        driver.get("https://demoqa.com/modal-dialogs");
        driver.findElement(By.id("showSmallModal")).click();
        String text = driver.findElement(By.className("modal-body")).getText();
        System.out.println(text);
        driver.findElement(By.id("closeSmallModal")).click();
    }

    @Test
    public void webTables() {
        driver.get("https://demoqa.com/webtables");
        driver.findElement(By.xpath("//span[@id='edit-record-1']//*[@stroke='currentColor']")).click();
        driver.findElement(By.id("submit")).click();

    }

    @Test
    public void scrapData() {
        driver.get("https://demoqa.com/webtables");
        WebElement table = driver.findElement(By.className("rt-tbody"));
        List<WebElement> allRows = table.findElements(By.className("rt-tr"));
        int i = 0;
        for (WebElement row : allRows) {
            List<WebElement> cells = row.findElements(By.className("rt-td"));
            for (WebElement cell : cells) {
                i++;
                System.out.println("num[" + i + "] " + cell.getText());

            }
        }
    }

    @Test
    public void uploadImage() {
        driver.get("https://demoqa.com/upload-download");
        WebElement uploadElement = driver.findElement(By.id("uploadFile"));
        uploadElement.sendKeys("C:\\Users\\Jotno\\Downloads\\images.png");

        String text = driver.findElement(By.id("uploadedFilePath")).getText();
        Assert.assertTrue(text.contains("images.png"));
    }

    @Test
    public void handleIframe() {
        driver.get("https://demoqa.com/frames");
        driver.switchTo().frame("frame2");
        String text = driver.findElement(By.id("sampleHeading")).getText();
        Assert.assertTrue(text.contains("This is a sample page"));
        driver.switchTo().defaultContent();
    }

    @Test
    public void mouseHover() throws InterruptedException {
        driver.get("https://green.edu.bd/");
        List<WebElement> menuAboutElement = driver.findElements(By.xpath("//a[contains(text(),\"About Us\")]"));
        Actions actions = new Actions(driver);
        actions.moveToElement(menuAboutElement.get(2)).perform();
        Thread.sleep(3000);
    }

    @Test
    public void keyboardEvents() throws InterruptedException {
        driver.get("https://www.google.com/");
        WebElement searchElement = driver.findElement(By.name("q"));
        Actions action = new Actions(driver);
        action.moveToElement(searchElement);
        action.keyDown(Keys.SHIFT);
        action.sendKeys("Selenium Webdriver")
                .keyUp(Keys.SHIFT)
                .doubleClick()
                .contextClick()
                .perform();

        Thread.sleep(5000);
    }

    @Test
    public void takeScreenShot() throws IOException {
        driver.get("https://demoqa.com");
        File screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        String time = new SimpleDateFormat("dd-MM-yyyy-hh-mm-ss-aa").format(new Date());
        String fileWithPath = "./src/test/resources/screenshots/" + time + ".png";
        File DestFile = new File(fileWithPath);
        FileUtils.copyFile(screenshotFile, DestFile);
    }
    

    public static void readFromExcel(String filePath, String fileName, String sheetName) throws IOException {
        File file = new File(filePath + "\\" + fileName);
        FileInputStream inputStream = new FileInputStream(file);
        Workbook workbook = null;
        String fileExtensionName = fileName.substring(fileName.indexOf("."));
        if (fileExtensionName.equals(".xlsx")) {
            workbook = new HSSFWorkbook(inputStream);
        }
        Sheet sheet = workbook.getSheet(sheetName);
        int rowCount = sheet.getLastRowNum() - sheet.getFirstRowNum();
        for (int i = 0; i < rowCount + 1; i++) {
            Row row = sheet.getRow(i);
            for (int j = 0; j < row.getLastCellNum(); j++) {
                DataFormatter formatter = new DataFormatter();
                System.out.print(formatter.formatCellValue((row.getCell(j))) + "|| ");
            }
            System.out.println();

        }
    }
    @Test
    public void readExcelFile()throws IOException {
        String filePath = "./src/test/resources";

        readFromExcel(filePath,"book1.xlsx","Sheet1");
    }

    //@After
        // public void closeDriver(){
        // driver.close(); // only tab will be closed
        // driver.quit();  whole browser will be closed
    //}
}


