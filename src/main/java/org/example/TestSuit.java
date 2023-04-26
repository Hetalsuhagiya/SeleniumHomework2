package org.example;

import com.google.common.annotations.VisibleForTesting;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

//import static org.example.ReferProduct.timestamp;

public class TestSuit {
    protected static WebDriver driver;

    public static void clickOnElement(By by) {
        driver.findElement(by).click();
    }

    public static void typeText(By by, String text) {
        driver.findElement(by).sendKeys(text);
    }

    public static String getTextFromElement(By by) {
        return driver.findElement(by).getText();
    }

    public static long timestamp() {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        return timestamp.getTime();
    }

    public static long datestamp() {
        SimpleDateFormat simpledateformat = new SimpleDateFormat("dd-MM-yyyy-hh:mm");
        return simpledateformat.hashCode();
    }

    static String expectedRegistrationCompleteMessage = "Thank You for Registration";

    @BeforeMethod
    public static void openBrowser() {
        driver = new ChromeDriver();
        driver.get("https://demo.nopcommerce.com/");
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
    }

    @AfterMethod
    public static void closeBrowser() {
        driver.close();
    }

    @Test
    public static void verifyUserShouldBeAbleToRegisterSuccessfully() {
        String expectedRegistrationCompleteMessage = "Thank You for Registration";

        //click on Register button
        clickOnElement(By.className("ico-register"));
        //type first name
        typeText(By.id("FirstName"), "TestFirstName");
        //type last name
        typeText(By.id("LastName"), "TestLastName");
        //type e-mail address
        typeText(By.name("Email"), "test1234" + timestamp() + "@gmail.com");
        //type password
        typeText(By.id("Password"), "test1234");
        //type confirm password
        typeText(By.id("ConfirmPassword"), "test1234");
        //click on register submit button
        clickOnElement(By.name("register-button"));
        //get text from element
        String actualMessage = getTextFromElement(By.xpath("//div[@class='result']"));
        System.out.println("My message: " + actualMessage);
        Assert.assertEquals(actualMessage, expectedRegistrationCompleteMessage, "Registration is not working");
    }


    @Test
    public static void verifyOnlyRegisteredUserCanEmailAProductToAFriend() {
        String expectedMessage = "Only Registered customers can use Email a Friend feature to refer a product";
        //click on Add to cart button on Appple Macbook Pro 13-inch
        clickOnElement(By.xpath("(//button[@class='button-2 product-box-add-to-cart-button'])[2]"));
        //click on email a friend button
        clickOnElement(By.xpath("//button[@class='button-2 email-a-friend-button']"));
        //type Friend's email
        typeText(By.xpath("//input[@id='FriendEmail']"), "friend123@gmail.com");
        //type Your email address
        typeText(By.xpath("//input[@id='YourEmailAddress']"), "test123@gmail.com");
        //click send email
        clickOnElement(By.name("send-email"));
        //get text from element
        String message = getTextFromElement(By.xpath("//div[@class='message-error validation-summary-errors']/ul/li"));
        System.out.println("Message: " + message);
        Assert.assertEquals(message, expectedMessage, "Email to a friend is not showing correct message");
    }

    @Test
    public static void verifyNonRegisteredUserCanNotVote() {
        String expectedMessage = "Only Registered users should be able to vote ";
        //click on Good on community poll
        clickOnElement(By.id("pollanswers-2"));
        //click on vote button
        clickOnElement(By.xpath("//button[@id='vote-poll-1']"));
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("block-poll-vote-error-1")));
        //get text from element
        String message = getTextFromElement(By.id("block-poll-vote-error-1"));
        System.out.println("My message: " + message);
        Assert.assertEquals(message, expectedMessage, "voting eligibility message is not correct");
    }

    @Test
    public static void verifyUserCanCompareTwoProductsInCompareList() {
        String expectedMessage = "There are no items to compare";
        //click on Add to compare list on HTC One M8 Android L5.0 Lollipop
        clickOnElement(By.xpath("(//button[@class='button-2 add-to-compare-list-button'])[3]"));
        //add wait time
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//div[@class='bar-notification success']")));
        //click on Add to compare list on $25 Virtual Gift Card
        clickOnElement(By.xpath("(//button[@class='button-2 add-to-compare-list-button'])[4]"));
        //click on product comparison list
        clickOnElement(By.partialLinkText("product comparison"));
        //print name of first product
        String name1 = getTextFromElement(By.partialLinkText("$25 Virtual Gift Card"));
        System.out.println("First Product is: " + name1);
        //print name of second product
        String name2 = getTextFromElement(By.partialLinkText("HTC One M8 Android L 5.0 Lollipop"));
        System.out.println("Second Product is: " + name2);
        //click on clear list
        driver.findElement(By.className("clear-list")).click();
        //get text from element
        String message = driver.findElement(By.className("no-data")).getText();
        System.out.println(message);
        Assert.assertEquals(message, expectedMessage, "Comparision message is not correct");
    }

    @Test
    public static void verifySameProductIsAddedToTheShoppingCart() {
        String expectedMessage = "Leica-T Mirrorless Digital Camera";
        //click on Electronics
        clickOnElement(By.xpath("(//a[@title='Show products in category Electronics'])[1]"));
        //click on camera and phone
        clickOnElement(By.xpath("(//a[@title='Show products in category Camera & photo'])[1]"));
        //click on add to cart button
        clickOnElement(By.xpath("(//button[@class='button-2 product-box-add-to-cart-button'])[2]"));
        //print name before adding to cart
        String name1 = getTextFromElement(By.partialLinkText("Leica T Mirrorless Digital Camera"));
        System.out.println("Product name before adding to cart: " + name1);
        // add wait until element is invisible & then click on shopping cart
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.className("content")));
        clickOnElement(By.xpath("//span[@class='cart-label']"));
        //verify same product in shopping cart
        String name2 = getTextFromElement(By.partialLinkText("Leica T Mirrorless Digital Camera"));
        System.out.println("Product name after adding to cart: " + name2);
        Assert.assertEquals(name2, expectedMessage, "Product name does not appear same on shopping cart");

    }


    @Test
    public static void verifyRegisteredUserShouldBeAbleToReferAProductToAFriendSuccessfully() {
        String expectedMessage = "Your E-mail has been sent";
        //click on Register button
        clickOnElement(By.className("ico-register"));
        //type first name
        typeText(By.id("FirstName"), "TestFirstName");
        //type last name
        typeText(By.id("LastName"), "TestLastName");
        //type e-mail address
        typeText(By.name("Email"), "test1234@gmail.com");
        //type password
        typeText(By.id("Password"), "test1234");
        //type confirm password
        typeText(By.id("ConfirmPassword"), "test1234");
        //click on register submit button
        clickOnElement(By.name("register-button"));
        //click on Log-in button
        clickOnElement(By.partialLinkText("Log in"));
        //type e-mail address
        typeText(By.id("Email"), "test1234@gmail.com");
        //type password
        typeText(By.id("Password"), "test1234");
        //click on Log-in
        clickOnElement(By.xpath("//button[@class='button-1 login-button']"));
        //go to Homepage
        clickOnElement(By.xpath("//img[@alt='nopCommerce demo store']"));
        //click on a product to refer to a friend
        clickOnElement(By.partialLinkText("HTC One M8 Android L 5.0 Lollipop"));
        //click on email friend
        clickOnElement(By.xpath("//button[@onclick='setLocation(\"/productemailafriend/18\")']"));
        //type friend's e-mail
        typeText(By.id("FriendEmail"), "abc1234@gmail.com");
        //click send e-mail
        clickOnElement(By.xpath("//button[@class='button-1 send-email-a-friend-button']"));
        //print message(Your email has been sent)
        String message = getTextFromElement(By.xpath("//div[@class='result']"));
        System.out.println("Sent Message: " + message);
        Assert.assertEquals(message, expectedMessage, "Message sent to friend is not correct");
    }

    @Test
    public static void verifyRegisteredUserShouldBeAbletoVoteSuccessfully() {
        String expectedMessage = "Good-(Total number of votes)";
      //click on Register button
        clickOnElement(By.className("ico-register"));
        //type first name
        typeText(By.id("FirstName"), "TestFirstName");
        //type last name
        typeText(By.id("LastName"), "TestLastName");
        //type e-mail address
        typeText(By.name("Email"), "test1234" + datestamp() + "@gmail.com");
        //type password
        typeText(By.id("Password"), "test1234");
        //type confirm password
        typeText(By.id("ConfirmPassword"), "test1234");
        //click on register submit button
        clickOnElement(By.name("register-button"));
        //click on Log-in button
        clickOnElement(By.partialLinkText("Log in"));
        //type your e-mail
        typeText(By.xpath("//input[@data-val-required='Please enter your email']"), "test1234" + datestamp() + "gmail.com");
        //type password
        typeText(By.xpath("//input[@type='password']"), "test1234");
        //click on Log-in
        clickOnElement(By.xpath("(//button[@type='submit'])[2]"));
        //click on homepage logo
        clickOnElement(By.xpath("//img[@alt='nopCommerce demo store']"));
        //click on Good on community poll
        clickOnElement(By.xpath("//label[@for='pollanswers-2']"));
        //click on vote button
        clickOnElement(By.xpath("//button[@id='vote-poll-1']"));
        //get text from element
//String votingMessage= driver.findElement(By.xpath("(//li[@class='answer'])[2]")).getText();
        String votingMessage = getTextFromElement(By.xpath("(//li[@class='answer'])[2]"));
        System.out.println("Community Poll Vote: " + votingMessage);
        Assert.assertEquals(votingMessage, expectedMessage, "voting poll is not showing complete data");
    }

}
