package ui.login;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;

import java.time.Duration;

public class LoginTest {

    WebDriver driver;
    WebDriverWait wait;

    @BeforeTest
    public void setUp() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("https://practice.expandtesting.com/login");

        wait = new WebDriverWait(driver, Duration.ofSeconds(10L));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.tagName("h1")));
    }

    @AfterTest
    public void tearDown() {
        driver.quit();
    }

    @Test
    public void testSuccessfulLogin() {
        fillFieldsAndClickOnSubmit("practice", "SuperSecretPassword!");

        wait.until(ExpectedConditions.urlToBe("https://practice.expandtesting.com/secure"));

        WebElement message = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("flash")));
        Assert.assertEquals(message.getText(), "You logged into a secure area!");

        var button = driver.findElement(By.cssSelector("a.button.btn-danger"));
        Assert.assertEquals(button.getText(), "Logout");
    }

    @Test
    public void testInvalidUsername() {
        fillFieldsAndClickOnSubmit("wrongUser", "SuperSecretPassword!");

        WebElement message = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("flash")));
        Assert.assertEquals(message.getText(), "Your username is invalid!");
        Assert.assertEquals(driver.getCurrentUrl(), "https://practice.expandtesting.com/login");
    }

    @Test
    public void testInvalidPassword() {
        fillFieldsAndClickOnSubmit("practice", "WrongPassword");

        WebElement message = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("flash")));
        Assert.assertEquals(message.getText(), "Your password is invalid!");
        Assert.assertEquals(driver.getCurrentUrl(), "https://practice.expandtesting.com/login");
    }



    private void fillFieldsAndClickOnSubmit(String username, String password) {
        WebElement usernameField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("username")));
        WebElement passwordField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("password")));

        usernameField.click();
        usernameField.sendKeys(username);

        passwordField.click();
        passwordField.sendKeys(password);

        var button = driver.findElement(By.cssSelector("button.btn.btn-primary"));
        button.click();
    }
}
