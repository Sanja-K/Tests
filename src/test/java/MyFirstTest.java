import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.openqa.selenium.support.ui.ExpectedConditions.titleIs;

public class MyFirstTest {

    private WebDriver driver;
    private WebDriverWait wait;

    @Before
    public void start() {
        System.setProperty("webdriver.chrome.driver", "D:/ChromeDriver/chromedriver.exe");
        driver = new ChromeDriver();

        driver .manage().timeouts().implicitlyWait(100, TimeUnit.SECONDS);
        wait = new WebDriverWait(driver,100);
    }

    @Test
    public void myFirstTest() {
        driver.navigate().to("https://rencredit.ru/");/** Открывает сайт rencredit.ru*/

        driver.findElement(By.xpath("//span[text()='Открыть вклад']"
                + "/ancestor::div[@class='service__title']")).click(); /** Переход на страницу "Вклад" */

        driver.findElement(By.xpath("//div[@class='jq-checkbox calculator__check']")).click();/**Выбирает чекбокс "В отделении банка" */
        driver.findElement(By.xpath("//input[@name='amount']")).sendKeys("130000"); /**Вводит в input сумму вклада 130000 */


        WebElement slider = driver.findElement(By.xpath("//div[@data-property='period']//span[@class='ui-slider-handle ui-state-default ui-corner-all']"));
         WebElement scale = driver.findElement(By.xpath("//div[@data-property='period']//span[@class='ui-slider-handle ui-state-default ui-corner-all']/preceding-sibling::div"));

       Integer width=scale.getSize().getWidth()/6;/**вычисление ширины одного шага ползунка */
       Integer position=3;
        double SliderCenterPx=Double.parseDouble(slider.getCssValue("left")
                .replaceAll("px",""))+ slider.getSize().width/2;/**вычисление дефолтного местоположения ползунка*/

        double xOffset = (position - (SliderCenterPx/width+1)) * width;/**Вычисление новой позици ползунка*/
        Actions actions = new Actions(driver);
        actions.dragAndDropBy(slider, (int) xOffset, 0).perform(); /**Перемещение позиции ползунка*/


        HashMap<String, Object> chromePrefs = new HashMap<String, Object>();
        chromePrefs.put("plugins.always_open_pdf_externally", true);
        ChromeOptions options = new ChromeOptions();
        options.setExperimentalOptions("prefs", chromePrefs);/**Отключение плагина PDF Viewer */

        DesiredCapabilities cap = DesiredCapabilities.chrome();
        cap.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
        cap.setCapability(ChromeOptions.CAPABILITY, options);
        WebDriver driver = new ChromeDriver(cap);

        driver.get("https://rencredit.ru/upload/iblock/0eb/obshchie-usloviya-po-vkladam-i-schetam_02.04.2018.pdf");/** Выгрузка Печатной Формы "Общие условия по вкладам */

    }


    @After
    public void stop() {
        driver.quit();
        driver = null;
    }

}

