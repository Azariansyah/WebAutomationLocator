import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.Set;

public class EndToEndLocator {
    static WebDriver driver;
    static WebDriverWait wait;
    static Actions actions;// Declare at class level

    public static void main(String[] args) throws InterruptedException {
        System.setProperty("webdriver.chrome.driver", "src/main/resources/chromedriver.exe");
        driver = new ChromeDriver();
        actions = new Actions(driver); // TAMBAHKAN INI
        wait = new WebDriverWait(driver, Duration.ofSeconds(10)); // TAMBAHKAN INI
        driver.get("https://rahulshettyacademy.com/AutomationPractice/");
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));

        Thread.sleep(3000);
        radioButtons();
        SuggestionExample();
        dropdownExample();
        checkBoxExample();
        SwitchWindowExample();
        SwitchTabExample();
        testAlertsAndConfirmations();
        testMouseHover();// Panggil method checkbox
        driver.quit();
    }

    public static void radioButtons() {
        for (int i = 1; i <= 3; i++) {
            String selector = "input[value='radio" + i + "']";
            WebElement radio = driver.findElement(By.cssSelector(selector));
            radio.click();
        }
    }

    public static void SuggestionExample() {
        WebElement countryInput = driver.findElement(By.id("autocomplete"));
        countryInput.sendKeys("Indo"); // Partial text to trigger suggestions


    /// / Wait for dropdown to appear and get options
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        List<WebElement> countryOptions = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy
                (
                        By.xpath("//div[@class='ui-menu-item-wrapper' and text()='Indonesia']")
                ));
        for (WebElement option : countryOptions) {
            if (option.getText().equals("Indonesia")) {
                option.click();
                break;
            }
        }
    }

    public static void dropdownExample() throws InterruptedException {
        WebElement dropdownElement = driver.findElement(By.id("dropdown-class-example"));
        Select dropdown = new Select(dropdownElement);

        // Dapatkan semua opsi
        var options = dropdown.getOptions();

        // Loop melalui semua opsi (skip opsi pertama "Select")
        for (int i = 1; i < options.size(); i++) {
            // Dapatkan nilai opsi
            String value = options.get(i).getAttribute("value");
            String text = options.get(i).getText();

            // Pilih opsi
            dropdown.selectByIndex(i);

            // Verifikasi opsi terpilih
            String selectedText = dropdown.getFirstSelectedOption().getText();
            System.out.println("Memilih opsi: " + text +
                    " | Value: " + value +
                    " | Status: " + selectedText.equals(text));

            Thread.sleep(1500); // Hanya untuk demo
        }
    }

    public static void checkBoxExample() throws InterruptedException {
        // Gunakan ID yang benar dengan huruf kapital 'O' di Option
        String[] checkboxIds = {
                "checkBoxOption1",
                "checkBoxOption2",
                "checkBoxOption3"
        };

        for (String id : checkboxIds) {
            WebElement checkbox = driver.findElement(By.id(id));
            checkbox.click();
            Thread.sleep(1000);
            System.out.println("Checkbox " + id + " status: " + checkbox.isSelected());
        }
    }
    public static void SwitchWindowExample() throws InterruptedException {
        // Click button to open new window
        WebElement openWindowBtn = driver.findElement(By.id("openwindow"));
        openWindowBtn.click();
        Thread.sleep(2000);  // Tunggu setelah klik


        // Wait for new window to open and switch to it
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        String originalWindowHandle = driver.getWindowHandle();

//// Wait until there are 2 window handles
        wait.until(ExpectedConditions.numberOfWindowsToBe(2));
        Thread.sleep(2000);  // Tunggu setelah window baru terbuka

        // Get all window handles and switch to new window
        Set<String> allWindowHandles = driver.getWindowHandles();
        for (String windowHandle : allWindowHandles) {
            if (!windowHandle.equals(originalWindowHandle)) {
                driver.switchTo().window(windowHandle);
                Thread.sleep(2000);  // Tunggu setelah switch window
                break;
            }
        }

        // Now on the new window, verify the email element
        WebElement emailElement = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//span[text()='info@qaclickacademy.com']")
        ));
        Thread.sleep(2000);  // Tunggu setelah switch window



     /// Optional: Close new window and return to original
        driver.close();
        Thread.sleep(2000);  // Tunggu setelah switch window
        driver.switchTo().window(originalWindowHandle);
        System.out.println("SwitchWindowExample completed successfully!");
        Thread.sleep(2000);  // Tunggu setelah switch window

    }

    public static void SwitchTabExample() throws InterruptedException {
        System.out.println("Starting SwitchTabExample...");

        // Return to main window explicitly
        driver.switchTo().defaultContent();

        // Wait for Open Tab element to be clickable
        WebElement openTab = wait.until(ExpectedConditions.elementToBeClickable(
                By.id("opentab")
        ));
        System.out.println("Clicking Open Tab...");
        openTab.click();

        // Handle new tab
        String parentHandle = driver.getWindowHandle();
        wait.until(ExpectedConditions.numberOfWindowsToBe(2));
        Thread.sleep(2000);
        for (String handle : driver.getWindowHandles()) {
            if (!handle.equals(parentHandle)) {
                System.out.println("Switching to new tab...");
                driver.switchTo().window(handle);
                Thread.sleep(2000);
                break;
            }
        }

        // Verify content in new tab
        System.out.println("Verifying email...");
        WebElement emailElement = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//span[text()='info@qaclickacademy.com']")
        ));
        Thread.sleep(2000);

        // Close tab and return
        System.out.println("Closing tab and returning...");
        driver.close();
        driver.switchTo().window(parentHandle);
        System.out.println("SwitchTabExample completed successfully!");
        Thread.sleep(2000);
    }

    public static void testAlertsAndConfirmations() throws InterruptedException {
        System.out.println("Memulai test skenario alerts...");

        // 1. Input Azhari
        WebElement nameInput = driver.findElement(By.id("name"));
        nameInput.sendKeys("Azhari");
        Thread.sleep(1000);

        // 2. Click Alert Button
        WebElement alertButton = driver.findElement(By.id("alertbtn"));
        alertButton.click();
        Thread.sleep(1000);

        // Handle Alert
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        Alert alert = wait.until(ExpectedConditions.alertIsPresent());

        // Verify Alert Text
        String alertText = alert.getText();
        System.out.println("Alert text: " + alertText);

        // 3. Click OK pada Alert
        alert.accept();
        Thread.sleep(1000);

        // 4. Input ulang Azhari
        nameInput.clear();
        nameInput.sendKeys("Azhari");
        Thread.sleep(1000);

        // 5. Click Confirm Button
        WebElement confirmButton = driver.findElement(By.id("confirmbtn"));
        confirmButton.click();
        Thread.sleep(1000);

        // Handle Confirm Alert
        Alert confirmAlert = wait.until(ExpectedConditions.alertIsPresent());

        // 6. Verify Confirm Text
        String confirmText = confirmAlert.getText();
        System.out.println("Confirm text: " + confirmText);

        // 7. Click Cancel pada Confirm
        confirmAlert.dismiss();
        Thread.sleep(1000);

        System.out.println("Test skenario alerts berhasil!");
    }
    public static void testMouseHover() {
        WebElement hoverButton = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("mousehover")));

        // Handle Hover Top
        handleHoverAction(hoverButton, "Top", EndToEndLocator::verifyPageScrollToTop);

        // Handle Hover Reload
        handleHoverAction(hoverButton, "Reload", EndToEndLocator::verifyPageReload);
    }

    private static void handleHoverAction(WebElement hoverButton, String linkText, Runnable verification) {
        try {
            // Scroll ke element dan beri margin 100px dari atas
            ((JavascriptExecutor) driver).executeScript(
                    "window.scrollTo(0, arguments[0].getBoundingClientRect().top - 100);",
                    hoverButton
            );

            // Hover dengan action chain yang lebih stabil
            actions.moveToElement(hoverButton)
                    .pause(1500)
                    .perform();

            // Temukan link dengan text yang sesuai
            WebElement link = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath(String.format("//div[@class='mouse-hover-content']/a[normalize-space()='%s']", linkText))
            ));

            // Klik dengan JavaScript sebagai fallback
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", link);

            // Jalankan verifikasi
            verification.run();

        } catch (Exception e) {
            System.out.println("Gagal memproses " + linkText + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void verifyPageScrollToTop() {
        try {
            new WebDriverWait(driver, Duration.ofSeconds(5))
                    .until(d -> (Long) ((JavascriptExecutor) d)
                            .executeScript("return window.pageYOffset;") == 0);

            System.out.println("✅ Scroll ke top berhasil");
        } catch (Exception e) {
            System.out.println("❌ Gagal scroll ke top");
            System.out.println("Posisi scroll saat ini: " +
                    ((JavascriptExecutor) driver).executeScript("return window.pageYOffset;"));
        }
    }

    private static void verifyPageReload() {
        try {
            // Verifikasi dengan kombinasi URL dan element kunci
            wait.until(ExpectedConditions.and(
                    d -> d.getCurrentUrl().equals("https://rahulshettyacademy.com/AutomationPractice/"),
                    ExpectedConditions.presenceOfElementLocated(By.id("mousehover"))
            ));

            System.out.println("✅ Reload berhasil - Halaman aktif");
        } catch (Exception e) {
            System.out.println("❌ Gagal reload");
            System.out.println("URL saat ini: " + driver.getCurrentUrl());
        }
    }
}