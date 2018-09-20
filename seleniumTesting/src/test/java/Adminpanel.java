import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.Keys;

import java.nio.file.Paths;

public class Adminpanel {

    String adminUser = "admin@admin.com";
    String adminPw = "passwort";
    String adminName= "Dr. Armin Admin";
    WebDriver driver = new ChromeDriver();

    @Before
    public void init() {
        System.setProperty("webdriver.chrome.driver", "chromedriver");
        driver.navigate().to("http://localhost:8080/");
        adminSuccessLogin();
        driver.findElement(By.linkText("Adminpanel")).click();
    }

    @After
    public void cleanUp(){
        driver.close();
        driver.quit();
    }

    private void adminSuccessLogin(){
        // go to login
        driver.findElement(By.linkText("Login")).click();

        // enter correct details and submit
        driver.findElement(By.cssSelector("#login\\.email")).sendKeys(adminUser);
        driver.findElement(By.cssSelector("#login\\.password")).sendKeys(adminPw);
        driver.findElement(By.cssSelector("[type='submit']")).click();

        try {Thread.sleep(1000); } catch(Exception e) {System.out.print(e);}
        //check if logged in
        Assert.assertEquals("Logged in as wrong user!", "Sie sind eingeloggt als: " + adminName,
                driver.findElement(By.cssSelector(".b-nav-dropdown > [href]")).getText());
    }

    @Test
    public void emptyJSON() {
        // submit without data
        driver.findElement(By.cssSelector("[role='tabpanel']:nth-of-type(1) button")).click();
        Assert.assertEquals("Shouldn't have been able to upload something",
                "Die gelesene Datei ist nicht gültig.", driver.switchTo().alert().getText());
    }

    @Test
    public void correctJSON() {
        // select valid file
        driver.findElement(By.cssSelector("[type='file']")).sendKeys(Paths.get("material-von-adesso_products2.json").toAbsolutePath().toString());

        // check if all services were detected
        Assert.assertEquals("Should have found all 26 services", "26 Dienste wurden erkannt.", driver.findElement(By.xpath("//div[@class='mainlayout']/div/div/div[2]/div[1]//div[2]")).getText());

        // submit with data
        driver.findElement(By.cssSelector("[role='tabpanel']:nth-of-type(1) button")).click();

        // alert
        try {Thread.sleep(1000); } catch(Exception e) {System.out.print(e);}
        Assert.assertEquals("Should have been able to upload something",
                "Dienste erfolgreich eingelesen", driver.switchTo().alert().getText());
    }

    @Test
    public void wrongJSON() {
        // select invalid file
        driver.findElement(By.cssSelector("[type='file']")).sendKeys(Paths.get("material-von-adesso_products.json").toAbsolutePath().toString());

        // alert
        try {Thread.sleep(1000); } catch(Exception e) {System.out.print(e);}
        try {
            driver.switchTo().alert().accept();
        } catch (Exception e) {
            Assert.fail("There should have been an alert");
        }

        // check that no services were detected
        Assert.assertEquals("Should have found no services", "0 Dienste wurden erkannt.", driver.findElement(By.xpath("//div[@class='mainlayout']/div/div/div[2]/div[1]//div[2]")).getText());

        // submit
        driver.findElement(By.cssSelector("[role='tabpanel']:nth-of-type(1) button")).click();

        // alert
        try {Thread.sleep(500); } catch(Exception e) {System.out.print(e);}
        Assert.assertEquals("Shouldn't have been able to upload something",
                "Die gelesene Datei ist nicht gültig.", driver.switchTo().alert().getText());
    }

    @Test
    public void correctManualService() {
        // goto adding a service manually
        driver.findElement(By.linkText("Dienst manuell hinzufügen")).click();

        // enter valid data
        driver.findElement(By.cssSelector("#genName")).sendKeys("Test Dienst 1");
        driver.findElement(By.cssSelector("#genOrg")).sendKeys("Test Org 1");
        driver.findElement(By.cssSelector("#genVersion")).sendKeys("1.0");
        Select dropdown = new Select(driver.findElement(By.id("cert")));
        dropdown.selectByVisibleText("Ja");
        driver.findElement(By.cssSelector("#genLogo")).sendKeys("testLogo.png");
        driver.findElement(By.cssSelector("#genTag0")).sendKeys("Test Tag 1");

        // add second tag
        driver.findElement(By.cssSelector("#addTag")).click();
        driver.findElement(By.cssSelector("#genTag1")).sendKeys("Test Tag 2");

        // add two in-formats
        driver.findElement(By.cssSelector("#addIn")).click();
        driver.findElement(By.cssSelector("#addIn")).click();

        // add two out-formats
        driver.findElement(By.cssSelector("#addOut")).click();
        driver.findElement(By.cssSelector("#addOut")).click();

        // enter formats
        driver.findElement(By.cssSelector("#InputType0")).sendKeys("TestFormat");
        driver.findElement(By.cssSelector("#InputVersion0")).sendKeys("1.0");
        Select dropdown1 = new Select(driver.findElement(By.id("InputComp0")));
        dropdown1.selectByVisibleText("strikt");

        driver.findElement(By.cssSelector("#OutputType0")).sendKeys("TestFormat");
        driver.findElement(By.cssSelector("#OutputVersion0")).sendKeys("1.0");
        Select dropdown2 = new Select(driver.findElement(By.id("OutputComp0")));
        dropdown2.selectByVisibleText("strikt");

        driver.findElement(By.cssSelector("#InputType1")).sendKeys("TestFormat2");
        driver.findElement(By.cssSelector("#InputVersion1")).sendKeys("2.0");
        Select dropdown3 = new Select(driver.findElement(By.id("InputComp1")));
        dropdown3.selectByVisibleText("flexibel");

        driver.findElement(By.cssSelector("#OutputType1")).sendKeys("TestFormat2");
        driver.findElement(By.cssSelector("#OutputVersion1")).sendKeys("2.0");
        Select dropdown4 = new Select(driver.findElement(By.id("OutputComp1")));
        dropdown4.selectByVisibleText("flexibel");

        // submit
        driver.findElement(By.cssSelector("[type='submit']")).sendKeys(Keys.SPACE);

        // alert
        try {Thread.sleep(1000); } catch(Exception e) {System.out.print(e);}
        Assert.assertEquals("Should have been successful", "Erfolgreich gespeichert", driver.switchTo().alert().getText());
    }

    @Test
    public void missingName() {
        // goto adding a service manually
        driver.findElement(By.linkText("Dienst manuell hinzufügen")).click();

        // enter valid data
        driver.findElement(By.cssSelector("#genName")).sendKeys("");
        driver.findElement(By.cssSelector("#genOrg")).sendKeys("Test Org 1");
        driver.findElement(By.cssSelector("#genVersion")).sendKeys("1.0");
        Select dropdown = new Select(driver.findElement(By.id("cert")));
        dropdown.selectByVisibleText("Ja");
        driver.findElement(By.cssSelector("#genLogo")).sendKeys("testLogo.png");
        driver.findElement(By.cssSelector("#genTag0")).sendKeys("Test Tag 1");

        // add second tag
        driver.findElement(By.cssSelector("#addTag")).click();
        driver.findElement(By.cssSelector("#genTag1")).sendKeys("Test Tag 2");

        // add two in-formats
        driver.findElement(By.cssSelector("#addIn")).click();
        driver.findElement(By.cssSelector("#addIn")).click();

        // add two out-formats
        driver.findElement(By.cssSelector("#addOut")).click();
        driver.findElement(By.cssSelector("#addOut")).click();

        // enter formats
        driver.findElement(By.cssSelector("#InputType0")).sendKeys("TestFormat");
        driver.findElement(By.cssSelector("#InputVersion0")).sendKeys("1.0");
        Select dropdown1 = new Select(driver.findElement(By.id("InputComp0")));
        dropdown1.selectByVisibleText("strikt");

        driver.findElement(By.cssSelector("#OutputType0")).sendKeys("TestFormat");
        driver.findElement(By.cssSelector("#OutputVersion0")).sendKeys("1.0");
        Select dropdown2 = new Select(driver.findElement(By.id("OutputComp0")));
        dropdown2.selectByVisibleText("strikt");

        driver.findElement(By.cssSelector("#InputType1")).sendKeys("TestFormat2");
        driver.findElement(By.cssSelector("#InputVersion1")).sendKeys("2.0");
        Select dropdown3 = new Select(driver.findElement(By.id("InputComp1")));
        dropdown3.selectByVisibleText("flexibel");

        driver.findElement(By.cssSelector("#OutputType1")).sendKeys("TestFormat2");
        driver.findElement(By.cssSelector("#OutputVersion1")).sendKeys("2.0");
        Select dropdown4 = new Select(driver.findElement(By.id("OutputComp1")));
        dropdown4.selectByVisibleText("flexibel");

        // submit
        driver.findElement(By.cssSelector("[type='submit']")).sendKeys(Keys.SPACE);

        // alert
        Assert.assertNotNull(driver.findElement(By.cssSelector("#genName")).getAttribute("required"));
    }

    @Test
    public void missingOrg() {
        // goto adding a service manually
        driver.findElement(By.linkText("Dienst manuell hinzufügen")).click();

        // enter valid data
        driver.findElement(By.cssSelector("#genName")).sendKeys("Test Dienst 1");
        driver.findElement(By.cssSelector("#genOrg")).sendKeys("");
        driver.findElement(By.cssSelector("#genVersion")).sendKeys("1.0");
        Select dropdown = new Select(driver.findElement(By.id("cert")));
        dropdown.selectByVisibleText("Ja");
        driver.findElement(By.cssSelector("#genLogo")).sendKeys("testLogo.png");
        driver.findElement(By.cssSelector("#genTag0")).sendKeys("Test Tag 1");

        // add second tag
        driver.findElement(By.cssSelector("#addTag")).click();
        driver.findElement(By.cssSelector("#genTag1")).sendKeys("Test Tag 2");

        // add two in-formats
        driver.findElement(By.cssSelector("#addIn")).click();
        driver.findElement(By.cssSelector("#addIn")).click();

        // add two out-formats
        driver.findElement(By.cssSelector("#addOut")).click();
        driver.findElement(By.cssSelector("#addOut")).click();

        // enter formats
        driver.findElement(By.cssSelector("#InputType0")).sendKeys("TestFormat");
        driver.findElement(By.cssSelector("#InputVersion0")).sendKeys("1.0");
        Select dropdown1 = new Select(driver.findElement(By.id("InputComp0")));
        dropdown1.selectByVisibleText("strikt");

        driver.findElement(By.cssSelector("#OutputType0")).sendKeys("TestFormat");
        driver.findElement(By.cssSelector("#OutputVersion0")).sendKeys("1.0");
        Select dropdown2 = new Select(driver.findElement(By.id("OutputComp0")));
        dropdown2.selectByVisibleText("strikt");

        driver.findElement(By.cssSelector("#InputType1")).sendKeys("TestFormat2");
        driver.findElement(By.cssSelector("#InputVersion1")).sendKeys("2.0");
        Select dropdown3 = new Select(driver.findElement(By.id("InputComp1")));
        dropdown3.selectByVisibleText("flexibel");

        driver.findElement(By.cssSelector("#OutputType1")).sendKeys("TestFormat2");
        driver.findElement(By.cssSelector("#OutputVersion1")).sendKeys("2.0");
        Select dropdown4 = new Select(driver.findElement(By.id("OutputComp1")));
        dropdown4.selectByVisibleText("flexibel");

        // submit
        driver.findElement(By.cssSelector("[type='submit']")).sendKeys(Keys.SPACE);

        // alert
        Assert.assertNotNull(driver.findElement(By.cssSelector("#genOrg")).getAttribute("required"));
    }

    @Test
    public void missingVersion() {
        // goto adding a service manually
        driver.findElement(By.linkText("Dienst manuell hinzufügen")).click();

        // enter valid data
        driver.findElement(By.cssSelector("#genName")).sendKeys("Test Dienst 1");
        driver.findElement(By.cssSelector("#genOrg")).sendKeys("Test Org 1");
        driver.findElement(By.cssSelector("#genVersion")).sendKeys("");
        Select dropdown = new Select(driver.findElement(By.id("cert")));
        dropdown.selectByVisibleText("Ja");
        driver.findElement(By.cssSelector("#genLogo")).sendKeys("testLogo.png");
        driver.findElement(By.cssSelector("#genTag0")).sendKeys("Test Tag 1");

        // add second tag
        driver.findElement(By.cssSelector("#addTag")).click();
        driver.findElement(By.cssSelector("#genTag1")).sendKeys("Test Tag 2");

        // add two in-formats
        driver.findElement(By.cssSelector("#addIn")).click();
        driver.findElement(By.cssSelector("#addIn")).click();

        // add two out-formats
        driver.findElement(By.cssSelector("#addOut")).click();
        driver.findElement(By.cssSelector("#addOut")).click();

        // enter formats
        driver.findElement(By.cssSelector("#InputType0")).sendKeys("TestFormat");
        driver.findElement(By.cssSelector("#InputVersion0")).sendKeys("1.0");
        Select dropdown1 = new Select(driver.findElement(By.id("InputComp0")));
        dropdown1.selectByVisibleText("strikt");

        driver.findElement(By.cssSelector("#OutputType0")).sendKeys("TestFormat");
        driver.findElement(By.cssSelector("#OutputVersion0")).sendKeys("1.0");
        Select dropdown2 = new Select(driver.findElement(By.id("OutputComp0")));
        dropdown2.selectByVisibleText("strikt");

        driver.findElement(By.cssSelector("#InputType1")).sendKeys("TestFormat2");
        driver.findElement(By.cssSelector("#InputVersion1")).sendKeys("2.0");
        Select dropdown3 = new Select(driver.findElement(By.id("InputComp1")));
        dropdown3.selectByVisibleText("flexibel");

        driver.findElement(By.cssSelector("#OutputType1")).sendKeys("TestFormat2");
        driver.findElement(By.cssSelector("#OutputVersion1")).sendKeys("2.0");
        Select dropdown4 = new Select(driver.findElement(By.id("OutputComp1")));
        dropdown4.selectByVisibleText("flexibel");

        // submit
        driver.findElement(By.cssSelector("[type='submit']")).sendKeys(Keys.SPACE);

        // alert
        Assert.assertNotNull(driver.findElement(By.cssSelector("#genVersion")).getAttribute("required"));
    }

    @Test
    public void missingLogo() {
        // goto adding a service manually
        driver.findElement(By.linkText("Dienst manuell hinzufügen")).click();

        // enter valid data
        driver.findElement(By.cssSelector("#genName")).sendKeys("Test Dienst 1");
        driver.findElement(By.cssSelector("#genOrg")).sendKeys("Test Org 1");
        driver.findElement(By.cssSelector("#genVersion")).sendKeys("1.0");
        Select dropdown = new Select(driver.findElement(By.id("cert")));
        dropdown.selectByVisibleText("Ja");
        driver.findElement(By.cssSelector("#genLogo")).sendKeys("");
        driver.findElement(By.cssSelector("#genTag0")).sendKeys("Test Tag 1");

        // add second tag
        driver.findElement(By.cssSelector("#addTag")).click();
        driver.findElement(By.cssSelector("#genTag1")).sendKeys("Test Tag 2");

        // add two in-formats
        driver.findElement(By.cssSelector("#addIn")).click();
        driver.findElement(By.cssSelector("#addIn")).click();

        // add two out-formats
        driver.findElement(By.cssSelector("#addOut")).click();
        driver.findElement(By.cssSelector("#addOut")).click();

        // enter formats
        driver.findElement(By.cssSelector("#InputType0")).sendKeys("TestFormat");
        driver.findElement(By.cssSelector("#InputVersion0")).sendKeys("1.0");
        Select dropdown1 = new Select(driver.findElement(By.id("InputComp0")));
        dropdown1.selectByVisibleText("strikt");

        driver.findElement(By.cssSelector("#OutputType0")).sendKeys("TestFormat");
        driver.findElement(By.cssSelector("#OutputVersion0")).sendKeys("1.0");
        Select dropdown2 = new Select(driver.findElement(By.id("OutputComp0")));
        dropdown2.selectByVisibleText("strikt");

        driver.findElement(By.cssSelector("#InputType1")).sendKeys("TestFormat2");
        driver.findElement(By.cssSelector("#InputVersion1")).sendKeys("2.0");
        Select dropdown3 = new Select(driver.findElement(By.id("InputComp1")));
        dropdown3.selectByVisibleText("flexibel");

        driver.findElement(By.cssSelector("#OutputType1")).sendKeys("TestFormat2");
        driver.findElement(By.cssSelector("#OutputVersion1")).sendKeys("2.0");
        Select dropdown4 = new Select(driver.findElement(By.id("OutputComp1")));
        dropdown4.selectByVisibleText("flexibel");

        // submit
        driver.findElement(By.cssSelector("[type='submit']")).sendKeys(Keys.SPACE);

        // alert
        Assert.assertNotNull(driver.findElement(By.cssSelector("#genLogo")).getAttribute("required"));
    }

    @Test
    public void missingTag() {
        // goto adding a service manually
        driver.findElement(By.linkText("Dienst manuell hinzufügen")).click();

        // enter valid data
        driver.findElement(By.cssSelector("#genName")).sendKeys("Test Dienst 1");
        driver.findElement(By.cssSelector("#genOrg")).sendKeys("Test Org 1");
        driver.findElement(By.cssSelector("#genVersion")).sendKeys("1.0");
        Select dropdown = new Select(driver.findElement(By.id("cert")));
        dropdown.selectByVisibleText("Ja");
        driver.findElement(By.cssSelector("#genLogo")).sendKeys("testLogo.png");
        driver.findElement(By.cssSelector("#genTag0")).sendKeys("");

        // add two in-formats
        driver.findElement(By.cssSelector("#addIn")).click();
        driver.findElement(By.cssSelector("#addIn")).click();

        // add two out-formats
        driver.findElement(By.cssSelector("#addOut")).click();
        driver.findElement(By.cssSelector("#addOut")).click();

        // enter formats
        driver.findElement(By.cssSelector("#InputType0")).sendKeys("TestFormat");
        driver.findElement(By.cssSelector("#InputVersion0")).sendKeys("1.0");
        Select dropdown1 = new Select(driver.findElement(By.id("InputComp0")));
        dropdown1.selectByVisibleText("strikt");

        driver.findElement(By.cssSelector("#OutputType0")).sendKeys("TestFormat");
        driver.findElement(By.cssSelector("#OutputVersion0")).sendKeys("1.0");
        Select dropdown2 = new Select(driver.findElement(By.id("OutputComp0")));
        dropdown2.selectByVisibleText("strikt");

        driver.findElement(By.cssSelector("#InputType1")).sendKeys("TestFormat2");
        driver.findElement(By.cssSelector("#InputVersion1")).sendKeys("2.0");
        Select dropdown3 = new Select(driver.findElement(By.id("InputComp1")));
        dropdown3.selectByVisibleText("flexibel");

        driver.findElement(By.cssSelector("#OutputType1")).sendKeys("TestFormat2");
        driver.findElement(By.cssSelector("#OutputVersion1")).sendKeys("2.0");
        Select dropdown4 = new Select(driver.findElement(By.id("OutputComp1")));
        dropdown4.selectByVisibleText("flexibel");

        // submit
        driver.findElement(By.cssSelector("[type='submit']")).sendKeys(Keys.SPACE);

        // alert
        Assert.assertNotNull(driver.findElement(By.cssSelector("#genTag0")).getAttribute("required"));
    }

    @Test
    public void missingFormats() {
        // goto adding a service manually
        driver.findElement(By.linkText("Dienst manuell hinzufügen")).click();

        // enter valid data
        driver.findElement(By.cssSelector("#genName")).sendKeys("Test Dienst 1");
        driver.findElement(By.cssSelector("#genOrg")).sendKeys("Test Org 1");
        driver.findElement(By.cssSelector("#genVersion")).sendKeys("1.0");
        Select dropdown = new Select(driver.findElement(By.id("cert")));
        dropdown.selectByVisibleText("Ja");
        driver.findElement(By.cssSelector("#genLogo")).sendKeys("testLogo.png");
        driver.findElement(By.cssSelector("#genTag0")).sendKeys("Test Tag 1");

        // submit
        driver.findElement(By.cssSelector("[type='submit']")).sendKeys(Keys.SPACE);

        // alert
        try {Thread.sleep(500); } catch(Exception e) {System.out.print(e);}
        Assert.assertEquals("Shouldn't have been able to add service", "Bitte geben Sie mindestens ein Ein- oder Ausgabeformat an.", driver.switchTo().alert().getText());
    }

}
