import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class LoginPage {

    WebDriver driver = new ChromeDriver();

    @Before
    public void init() {
        System.setProperty("webdriver.chrome.driver", "chromedriver");
        driver.navigate().to("http://localhost:8080/");
    }

    @After
    public void cleanUp(){
        driver.close();
        driver.quit();
    }

    @Test
    public void titleCheck() {
        // check if title is correct
        Assert.assertEquals("Title is not correct!", "SWARM Composer", driver.getTitle());
    }

    @Test
    public void missingEmail(){
        // go to login
        driver.findElement(By.linkText("Login")).click();

        //go to register tab
        driver.findElement(
                By.xpath("//div[@class='mainlayout']//div[@class='tabs']//ul[@role='tablist']/li[2]/a[@role='tab']"))
                .click();

        // enter valid data except for an missing email
        driver.findElement(By.cssSelector("#register\\.email")).sendKeys("");
        driver.findElement(By.cssSelector("#register\\.password")).sendKeys("1234");
        driver.findElement(By.cssSelector("#register\\.title")).sendKeys("Herr");
        driver.findElement(By.cssSelector("#register\\.firstname")).sendKeys("Max");
        driver.findElement(By.cssSelector("#register\\.lastname")).sendKeys("Mustermann");
        driver.findElement(By.cssSelector(".tab-content [role='tabpanel']:nth-of-type(2) button")).click();

        // alert
        Assert.assertNotNull(driver.findElement(By.cssSelector("#register\\.email")).getAttribute("required"));
    }

    @Test
    public void missingPassword(){
        // go to login
        driver.findElement(By.linkText("Login")).click();

        //go to register tab
        driver.findElement(
                By.xpath("//div[@class='mainlayout']//div[@class='tabs']//ul[@role='tablist']/li[2]/a[@role='tab']"))
                .click();

        // enter valid data except for an missing password
        driver.findElement(By.cssSelector("#register\\.email")).sendKeys("test@user.de");
        driver.findElement(By.cssSelector("#register\\.password")).sendKeys("");
        driver.findElement(By.cssSelector("#register\\.title")).sendKeys("Herr");
        driver.findElement(By.cssSelector("#register\\.firstname")).sendKeys("Max");
        driver.findElement(By.cssSelector("#register\\.lastname")).sendKeys("Mustermann");
        driver.findElement(By.cssSelector(".tab-content [role='tabpanel']:nth-of-type(2) button")).click();

        // alert
        Assert.assertNotNull(driver.findElement(By.cssSelector("#register\\.password")).getAttribute("required"));
    }

    @Test
    public void missingFirstName(){
        // go to login
        driver.findElement(By.linkText("Login")).click();

        //go to register tab
        driver.findElement(
                By.xpath("//div[@class='mainlayout']//div[@class='tabs']//ul[@role='tablist']/li[2]/a[@role='tab']"))
                .click();

        // enter valid data except for an missing first name
        driver.findElement(By.cssSelector("#register\\.email")).sendKeys("test@user.de");
        driver.findElement(By.cssSelector("#register\\.password")).sendKeys("1234");
        driver.findElement(By.cssSelector("#register\\.title")).sendKeys("Herr");
        driver.findElement(By.cssSelector("#register\\.firstname")).sendKeys("");
        driver.findElement(By.cssSelector("#register\\.lastname")).sendKeys("Mustermann");
        driver.findElement(By.cssSelector(".tab-content [role='tabpanel']:nth-of-type(2) button")).click();

        // alert
        Assert.assertNotNull(driver.findElement(By.cssSelector("#register\\.firstname")).getAttribute("required"));
    }

    @Test
    public void missingLastName(){
        // go to login
        driver.findElement(By.linkText("Login")).click();

        //go to register tab
        driver.findElement(
                By.xpath("//div[@class='mainlayout']//div[@class='tabs']//ul[@role='tablist']/li[2]/a[@role='tab']"))
                .click();

        // enter valid data except for an missing last name
        driver.findElement(By.cssSelector("#register\\.email")).sendKeys("test@user.de");
        driver.findElement(By.cssSelector("#register\\.password")).sendKeys("1234");
        driver.findElement(By.cssSelector("#register\\.title")).sendKeys("Herr");
        driver.findElement(By.cssSelector("#register\\.firstname")).sendKeys("Max");
        driver.findElement(By.cssSelector("#register\\.lastname")).sendKeys("");
        driver.findElement(By.cssSelector(".tab-content [role='tabpanel']:nth-of-type(2) button")).click();

        // alert
        Assert.assertNotNull(driver.findElement(By.cssSelector("#register\\.lastname")).getAttribute("required"));
    }

    @Test
    public void missingTitle() {
        // go to login
        driver.findElement(By.linkText("Login")).click();

        //go to register tab
        driver.findElement(
                By.xpath("//div[@class='mainlayout']//div[@class='tabs']//ul[@role='tablist']/li[2]/a[@role='tab']"))
                .click();

        // enter valid data, title is missing, which is okay
        driver.findElement(By.cssSelector("#register\\.email")).sendKeys("test@user10.de");
        driver.findElement(By.cssSelector("#register\\.password")).sendKeys("1234");
        driver.findElement(By.cssSelector("#register\\.firstname")).sendKeys("Max");
        driver.findElement(By.cssSelector("#register\\.lastname")).sendKeys("Mustermann");
        driver.findElement(By.cssSelector(".tab-content [role='tabpanel']:nth-of-type(2) button")).click();

        // alert
        try {
            Thread.sleep(1000);
        } catch (Exception e) {
            System.out.print(e);
        }
        Assert.assertEquals("User registration should have been successful!", "Erfolgreich registriert!\n" +
                        "Sie können sich jetzt einloggen.",
                driver.findElement(By.cssSelector("#regMod___BV_modal_body_ h4")).getText());

        // check that not logged in
        Assert.assertEquals("User shouldn't have been logged in!", "Login",
                driver.findElement(By.linkText("Login")).getText());
    }

    @Test
    public void successRegister(){
        // go to login
        driver.findElement(By.linkText("Login")).click();

        //go to register tab
        driver.findElement(
                By.xpath("//div[@class='mainlayout']//div[@class='tabs']//ul[@role='tablist']/li[2]/a[@role='tab']"))
                .click();

        // enter valid data except user with that email already exists
        driver.findElement(By.cssSelector("#register\\.email")).sendKeys("test@user2.de");
        driver.findElement(By.cssSelector("#register\\.password")).sendKeys("1234");
        driver.findElement(By.cssSelector("#register\\.title")).sendKeys("Herr");
        driver.findElement(By.cssSelector("#register\\.firstname")).sendKeys("Max");
        driver.findElement(By.cssSelector("#register\\.lastname")).sendKeys("Mustermann");
        driver.findElement(By.cssSelector(".tab-content [role='tabpanel']:nth-of-type(2) button")).click();

        // alert
        try {Thread.sleep(500); } catch(Exception e) {System.out.print(e);}
        Assert.assertEquals("User registration should have been successful!", "Erfolgreich registriert!\n" +
                        "Sie können sich jetzt einloggen.",
                driver.findElement(By.cssSelector("#regMod___BV_modal_body_ h4")).getText());

        // check that not logged in
        Assert.assertEquals("User shouldn't have been logged in!", "Login",
                driver.findElement(By.linkText("Login")).getText());
    }

    @Test
    public void emailAlreadyExistsRegister(){
        // go to login
        driver.findElement(By.linkText("Login")).click();

        //go to register tab
        driver.findElement(
                By.xpath("//div[@class='mainlayout']//div[@class='tabs']//ul[@role='tablist']/li[2]/a[@role='tab']"))
                .click();

        // enter valid data
        driver.findElement(By.cssSelector("#register\\.email")).sendKeys("test@user3.de");
        driver.findElement(By.cssSelector("#register\\.password")).sendKeys("1234");
        driver.findElement(By.cssSelector("#register\\.title")).sendKeys("Herr");
        driver.findElement(By.cssSelector("#register\\.firstname")).sendKeys("Max");
        driver.findElement(By.cssSelector("#register\\.lastname")).sendKeys("Mustermann");
        driver.findElement(By.cssSelector(".tab-content [role='tabpanel']:nth-of-type(2) button")).click();

        // alert
        try {Thread.sleep(500); } catch(Exception e) {System.out.print(e);}
        Assert.assertEquals("User registration should have been successful!", "Erfolgreich registriert!\n" +
                        "Sie können sich jetzt einloggen.",
                driver.findElement(By.cssSelector("#regMod___BV_modal_body_ h4")).getText());

        driver.findElement(By.cssSelector("#regMod___BV_modal_body_ [type]")).click();

        // check that not logged in
        Assert.assertEquals("User shouldn't have been logged in!", "Login",
                driver.findElement(By.linkText("Login")).getText());


        // go to login
        driver.findElement(By.linkText("Login")).sendKeys(Keys.SPACE);

        //go to register tab
        driver.findElement(
                By.xpath("//div[@class='mainlayout']//div[@class='tabs']//ul[@role='tablist']/li[2]/a[@role='tab']"))
                .click();

        driver.findElement(By.cssSelector(".tab-content [role='tabpanel']:nth-of-type(2) button")).click();
        // alert
        try {Thread.sleep(1000); } catch(Exception e) {System.out.print(e);}
        Assert.assertEquals("User registration shouldn't have been successful!", "Registrierung fehlgeschlagen!\n" +
                        "Vielleicht ist die Email bereits vergeben oder versuchen Sie es später noch einmal.",
                driver.findElement(By.cssSelector("#regFailMod___BV_modal_body_ h4")).getText());
    }

    @Test
    public void wrongPassword(){
        // go to login
        driver.findElement(By.linkText("Login")).click();

        //go to register tab
        driver.findElement(
                By.xpath("//div[@class='mainlayout']//div[@class='tabs']//ul[@role='tablist']/li[2]/a[@role='tab']"))
                .click();

        // enter valid data except user with that email already exists
        driver.findElement(By.cssSelector("#register\\.email")).sendKeys("test@user4.de");
        driver.findElement(By.cssSelector("#register\\.password")).sendKeys("1234");
        driver.findElement(By.cssSelector("#register\\.title")).sendKeys("Herr");
        driver.findElement(By.cssSelector("#register\\.firstname")).sendKeys("Max");
        driver.findElement(By.cssSelector("#register\\.lastname")).sendKeys("Mustermann");
        driver.findElement(By.cssSelector(".tab-content [role='tabpanel']:nth-of-type(2) button")).click();

        // alert
        try {Thread.sleep(500); } catch(Exception e) {System.out.print(e);}
        Assert.assertEquals("User registration should have been successful!", "Erfolgreich registriert!\n" +
                        "Sie können sich jetzt einloggen.",
                driver.findElement(By.cssSelector("#regMod___BV_modal_body_ h4")).getText());

        driver.findElement(By.cssSelector("#regMod___BV_modal_body_ [type]")).click();
        // check that not logged in
        Assert.assertEquals("User shouldn't have been logged in!", "Login",
                driver.findElement(By.linkText("Login")).getText());

        try {Thread.sleep(500); } catch(Exception e) {System.out.print(e);}
        // go to login
        driver.findElement(By.linkText("Login")).sendKeys(Keys.SPACE);

        // enter wrong password
        driver.findElement(By.cssSelector("#login\\.email")).sendKeys("test@user4.de");
        driver.findElement(By.cssSelector("#login\\.password")).sendKeys("123");
        driver.findElement(By.cssSelector("[type='submit']")).click();

        // alert
        try {Thread.sleep(1000); } catch(Exception e) {System.out.print(e);}
        driver.switchTo().alert().dismiss();
        Assert.assertEquals("User shouldn't have been logged in!", "Login fehlgeschlagen!\n" +
                        "Bitte versuchen Sie es noch einmal.",
                driver.findElement(By.cssSelector("#logFailMod___BV_modal_body_ h4")).getText());

        driver.findElement(By.cssSelector("#logFailMod___BV_modal_body_ [type]")).click();

        // check that not logged in
        Assert.assertEquals("User shouldn't have been logged in!", "Login",
                driver.findElement(By.linkText("Login")).getText());
    }

    @Test
    public void wrongUser(){
        // go to login
        driver.findElement(By.linkText("Login")).click();

        //go to register tab
        driver.findElement(
                By.xpath("//div[@class='mainlayout']//div[@class='tabs']//ul[@role='tablist']/li[2]/a[@role='tab']"))
                .click();

        // enter valid data except user with that email already exists
        driver.findElement(By.cssSelector("#register\\.email")).sendKeys("test@user5.de");
        driver.findElement(By.cssSelector("#register\\.password")).sendKeys("1234");
        driver.findElement(By.cssSelector("#register\\.title")).sendKeys("Herr");
        driver.findElement(By.cssSelector("#register\\.firstname")).sendKeys("Max");
        driver.findElement(By.cssSelector("#register\\.lastname")).sendKeys("Mustermann");
        driver.findElement(By.cssSelector(".tab-content [role='tabpanel']:nth-of-type(2) button")).click();

        // alert
        try {Thread.sleep(500); } catch(Exception e) {System.out.print(e);}
        Assert.assertEquals("User registration should have been successful!", "Erfolgreich registriert!\n" +
                        "Sie können sich jetzt einloggen.",
                driver.findElement(By.cssSelector("#regMod___BV_modal_body_ h4")).getText());

        driver.findElement(By.cssSelector("#regMod___BV_modal_body_ [type]")).click();

        // check that not logged in
        Assert.assertEquals("User shouldn't have been logged in!", "Login",
                driver.findElement(By.linkText("Login")).getText());

        // go to login
        driver.findElement(By.linkText("Login")).sendKeys(Keys.SPACE);

        // enter wrong username
        driver.findElement(By.cssSelector("#login\\.email")).sendKeys("test@user5.com");
        driver.findElement(By.cssSelector("#login\\.password")).sendKeys("1234");
        driver.findElement(By.cssSelector("[type='submit']")).click();

        // alert
        try {Thread.sleep(1000); } catch(Exception e) {System.out.print(e);}
        driver.switchTo().alert().dismiss();
        Assert.assertEquals("User shouldn't have been logged in!", "Login fehlgeschlagen!\n" +
                        "Bitte versuchen Sie es noch einmal.",
                driver.findElement(By.cssSelector("#logFailMod___BV_modal_body_ h4")).getText());
        driver.findElement(By.cssSelector("#logFailMod___BV_modal_body_ [type]"));

        // check that not logged in
        Assert.assertEquals("User shouldn't have been logged in!", "Login",
                driver.findElement(By.linkText("Login")).getText());
    }

    @Test
    public void successLogin(){
        // go to login
        driver.findElement(By.linkText("Login")).click();

        //go to register tab
        driver.findElement(
                By.xpath("//div[@class='mainlayout']//div[@class='tabs']//ul[@role='tablist']/li[2]/a[@role='tab']"))
                .click();

        // enter valid data except user with that email already exists
        driver.findElement(By.cssSelector("#register\\.email")).sendKeys("test@user6.de");
        driver.findElement(By.cssSelector("#register\\.password")).sendKeys("1234");
        driver.findElement(By.cssSelector("#register\\.title")).sendKeys("Herr");
        driver.findElement(By.cssSelector("#register\\.firstname")).sendKeys("Max");
        driver.findElement(By.cssSelector("#register\\.lastname")).sendKeys("Mustermann");
        driver.findElement(By.cssSelector(".tab-content [role='tabpanel']:nth-of-type(2) button")).click();

        // alert
        try {Thread.sleep(1000); } catch(Exception e) {System.out.print(e);}
        Assert.assertEquals("User registration should have been successful!", "Erfolgreich registriert!\n" +
                        "Sie können sich jetzt einloggen.",
                driver.findElement(By.cssSelector("#regMod___BV_modal_body_ h4")).getText());

        driver.findElement(By.cssSelector("#regMod___BV_modal_body_ [type]")).click();

        // check that not logged in
        Assert.assertEquals("User shouldn't have been logged in!", "Login",
                driver.findElement(By.linkText("Login")).getText());

        // go to login
        driver.findElement(By.linkText("Login")).sendKeys(Keys.SPACE);

        // enter correct details and submit
        driver.findElement(By.cssSelector("#login\\.email")).sendKeys("test@user6.de");
        driver.findElement(By.cssSelector("#login\\.password")).sendKeys("1234");
        driver.findElement(By.cssSelector("[type='submit']")).click();

        try {Thread.sleep(1000); } catch(Exception e) {System.out.print(e);}
        //check if logged in
        Assert.assertEquals("Logged in as wrong user!", "Sie sind eingeloggt als: Herr Max Mustermann",
                driver.findElement(By.cssSelector(".b-nav-dropdown > [href]")).getText());
    }

}
