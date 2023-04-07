package org.example;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.example.enums.City;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;
import org.testng.annotations.*;
import org.testng.asserts.SoftAssert;

import java.time.Duration;
import java.util.List;

public class WaitsTest {
    private final String BASE_URL = "https://devexpress.github.io/devextreme-reactive/react/grid/docs/guides/filtering/";
    private WebDriver driver;
    private final Long IMPLICITLY_WAIT_SECONDS = 10L;
    private final Long ONE_SECOND_DELAY = 1000L;

    private String firstLetter = "L";

    private void presentationSleep() {
        presentationSleep(1);
    }

    private void presentationSleep(int seconds) {
        try {
            Thread.sleep(seconds * ONE_SECOND_DELAY); // For Presentation ONLY
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @BeforeSuite
    public void beforeSuite() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeClass
    public void beforeClass() {
        ChromeOptions options = new ChromeOptions();
        //options.addArguments("--remote-allow-origins=*");
        driver = new ChromeDriver(options);
        //
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(IMPLICITLY_WAIT_SECONDS));
        driver.manage().window().maximize();
    }

    @AfterClass(alwaysRun = true)
    public void afterClass() {
        if (driver != null) {
            driver.quit();
        }
    }

    @BeforeMethod
    public void beforeMethod() {
        driver.get(BASE_URL);
        presentationSleep();
    }

    @Test
    public void checkFilterTable(){
        SoftAssert asert = new SoftAssert();

        //move to the table
        WebElement position = driver.findElement(By.id("controlled-mode"));
        Actions action = new Actions(driver);
        action.moveToElement(position).perform();

        //switch to the frame
        driver.switchTo().frame(driver.findElement(By.xpath("//div[contains(@class,'embedded-demo')]//iframe")));

        //put first letter
        List<WebElement> inputs = driver.findElements(By.xpath("//input[contains(@class,'Editor-input')]"));
        WebElement input = inputs.get(2);
        input.sendKeys(firstLetter);

        //find cities after filtering
        List<WebElement> cities = driver.findElements(
                By.xpath("//tr[contains(@class,'MuiTableRow-root')]//td[3]")
        );

        //check result
        boolean containsLondon=false;
        boolean containsLasVegas=false;
        boolean containsCitiesStartsAnotherLetter=false;

        //System.out.println(cities.size());

        for(WebElement element:cities){
            if(element.getText().trim().replaceAll("  "," ").equalsIgnoreCase(City.LONDON.getName())){
                containsLondon=true;
            }else if(element.getText().trim().replaceAll("  "," ").equalsIgnoreCase(City.LAS_VEGAS.getName())){
                containsLasVegas=true;
            }else{
                if(!element.getText().startsWith(firstLetter)){
                    containsCitiesStartsAnotherLetter=true;
                }
            }
        }

        presentationSleep();

        asert.assertTrue(containsLondon);
        asert.assertTrue(containsLasVegas);
        asert.assertFalse(containsCitiesStartsAnotherLetter);
        asert.assertAll();
    }

}
