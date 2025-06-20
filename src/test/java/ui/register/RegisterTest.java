package ui.register;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.time.Duration;

public class RegisterTest {

    WebDriver driver;
    WebDriverWait wait;

    @BeforeTest
    public void setUp() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("https://practice.expandtesting.com/register");

        wait = new WebDriverWait(driver, Duration.ofSeconds(10L));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.tagName("h1")));
    }

    @AfterTest
    public void tearDown() {
        driver.quit();
    }

    @Test
    public void testSuccessfulRegister() {
        fillFieldsAndClickRegister("username", "password123", "password123");

        Assert.assertEquals(driver.getCurrentUrl(), "https://practice.expandtesting.com/login");

        WebElement message = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("flash")));
        Assert.assertEquals(message.getText(), "Successfully registered, you can log in now.");
    }

    @Test
    public void testMissingUsername() {
        fillFieldsAndClickRegister("", "password123", "password123");

        WebElement message = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("flash")));
        Assert.assertEquals(message.getText(), "All fields are required.");
    }

    @Test
    public void testMissingPassword() {
        fillFieldsAndClickRegister("username", "", "");

        WebElement message = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("flash")));
        Assert.assertEquals(message.getText(), "All fields are required.");
    }

    @Test
    public void testNonMatchingPasswords() {
        fillFieldsAndClickRegister("username", "password123", "pw123");

        WebElement message = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("flash")));
        Assert.assertEquals(message.getText(), "Passwords do not match.");
    }

    private void fillFieldsAndClickRegister(String username, String pw, String confirmPw) {
        WebElement usernameField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("username")));
        WebElement passwordField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("password")));
        WebElement confirmPasswordField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("confirmPassword")));

        usernameField.click();
        usernameField.sendKeys(username);

        passwordField.click();
        passwordField.sendKeys(pw);

        confirmPasswordField.click();
        confirmPasswordField.sendKeys(confirmPw);

        var button = driver.findElement(By.cssSelector("button.btn.btn-primary"));
        button.click();
    }

}
