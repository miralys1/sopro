import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class NavBar {

    String adminUser = "admin@admin.com";
    String adminPw = "passwort";
    String adminName= "Dr. Armin Admin";
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

    private void adminLogout() {
        // click logout
        try {
            driver.findElement(By.cssSelector(".b-nav-dropdown > [href]")).click();
            driver.findElement(By.linkText("Logout")).click();
        } catch(Exception e) {
            Assert.fail("Should have been able to logout");
        }
        try {Thread.sleep(500); } catch(Exception e) {System.out.print(e);}
        // check if successful
        try {
            driver.findElement(By.linkText("Login"));
        } catch (Exception e) {
            Assert.fail("Logout didn't work");
        }
    }

    private void adminSuccessLogin(){
        // go to login
        driver.findElement(By.linkText("Login")).click();

        // enter correct details and submit
        driver.findElement(By.cssSelector("#login\\.email")).sendKeys(adminUser);
        driver.findElement(By.cssSelector("#login\\.password")).sendKeys(adminPw);
        driver.findElement(By.cssSelector("[type='submit']")).click();

        try {Thread.sleep(500); } catch(Exception e) {System.out.print(e);}

        //check if logged in
        Assert.assertEquals("Logged in as wrong user!", "Sie sind eingeloggt als: " + adminName,
                driver.findElement(By.cssSelector(".b-nav-dropdown > [href]")).getText());
    }

    @Test
    public void LogoLink() {
        // check if logo exists
        try {
            driver.findElement(By.xpath("//nav/div[@class='container']/a[@href='#/']/img[@alt='SWARMcomposer Logo']"));
        } catch (Exception e) {
            Assert.fail("Logo should exist");
        }

        // click on logo
        driver.findElement(By.xpath("//nav/div[@class='container']/a[@href='#/']/img[@alt='SWARMcomposer Logo']")).click();
    }

    @Test
    public void WorkspaceLink() {
        // check if link exists
        try {
            driver.findElement(By.linkText("Workspace"));
        } catch (Exception e) {
            Assert.fail("Link should exist");
        }

        // click on link
        driver.findElement(By.linkText("Workspace")).click();
    }

    @Test
    public void AdminpanelLink() {
        // check if link exists
        try {
            driver.findElement(By.linkText("Adminpanel"));
            Assert.fail("Link shouldn't exist");
        } catch (Exception e) {
            adminSuccessLogin();
            try {
                driver.findElement(By.linkText("Adminpanel"));
            } catch (Exception f) {
                Assert.fail("Link should exist");
            }
        }

        // click on link
        driver.findElement(By.linkText("Adminpanel")).click();

        // check whether on correct site
        try {
            driver.findElement(By.cssSelector("[role] [role='presentation']:nth-of-type(1) [role]"));
        } catch (Exception e) {
            Assert.fail("Wrong site");
        }
        adminLogout();
    }

    @Test
    public void loginLink() {
        // check if link exists
        try {
            driver.findElement(By.linkText("Login"));
        } catch (Exception e) {
            Assert.fail("Link should exist");
        }

        // click on link
        driver.findElement(By.linkText("Login")).click();

        // check whether on correct site
        try {
            driver.findElement(By.xpath("//div[@class='mainlayout']//div[@class='tabs']//ul[@role='tablist']/li[1]/a[@role='tab']"));
        } catch (Exception e) {
            Assert.fail("Wrong site");
        }
    }

    @Test
    public void logout() {
        adminSuccessLogin();
        adminLogout();
    }

}
