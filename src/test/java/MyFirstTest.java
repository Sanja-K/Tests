import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

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
    public void formContributionIncome() {
        HashMap<String, Object> chromePrefs = new HashMap<String, Object>();
        chromePrefs.put("plugins.always_open_pdf_externally", true);
        int maxNumberPositions=6;
        Integer selectedSliderPosition=3;

        driver.navigate().to("https://rencredit.ru/");/** Открывает сайт rencredit.ru*/

        driver.findElement(By.xpath("//span[text()='Открыть вклад']"
                + "/ancestor::div[@class='service__title']")).click(); /** Переход на страницу "Вклад" */

        driver.findElement(By.xpath("//div[@class='jq-checkbox calculator__check']")).click();/**Выбирает чекбокс "В отделении банка" */
        driver.findElement(By.xpath("//input[@name='amount']")).sendKeys("130000"); /**Вводит в input сумму вклада 130000 */


        WebElement slider = driver.findElement(By.xpath("//div[@data-property='period']//span[@class='ui-slider-handle ui-state-default ui-corner-all']"));
         WebElement scale = driver.findElement(By.xpath("//div[@data-property='period']//span[@class='ui-slider-handle ui-state-default ui-corner-all']/preceding-sibling::div"));

        sliderMovementFormContributionIncome(slider,scale,maxNumberPositions,selectedSliderPosition);
        disablePluginsGoogleChrome(chromePrefs);

        driver.get("https://rencredit.ru/upload/iblock/0eb/obshchie-usloviya-po-vkladam-i-schetam_02.04.2018.pdf");/** Выгрузка Печатной Формы "Общие условия по вкладам */
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void FormRequestOfCard(){
        HashMap<String, String> formData = new HashMap<String, String>();
        formData.put("Имя","Алёша");
        formData.put("Фамилия","Бубликов");
        formData.put("Отчество","false");
        formData.put("Телефон","9374059999");
        formData.put("Email","Bubble@gmail.com");
        formData.put("Город","Пензенская область");

        driver.navigate().to("https://rencredit.ru/");
        driver.findElement(By.xpath("//span[text()='Оформить карту']/ancestor::div[@class='service__title']")).click();
        driver.findElement(By.xpath("//li[text()='До 7,25% на остаток по карте']/ancestor::div[@class='cards-list__item-content-block']//a[text()='Заполнить заявку на карту']")).click();

        dataEntryInTheCardRequestForm(formData);

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @After
    public void stop() {
        driver.quit();
        driver = null;

    }

    public void disablePluginsGoogleChrome(HashMap<String, Object> chromePrefs){ /** Отключение плагинов в Google Chrome*/
        ChromeOptions options = new ChromeOptions();
        options.setExperimentalOptions("prefs", chromePrefs);/**Отключение плагина PDF Viewer */

        DesiredCapabilities cap = DesiredCapabilities.chrome();
        cap.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
        cap.setCapability(ChromeOptions.CAPABILITY, options);
        driver = new ChromeDriver(cap);
    }

    public void sliderMovementFormContributionIncome(WebElement slider,WebElement scale,int maxNumberPositions,Integer selectedSliderPosition ){/** Перемещение ползунка в форме "Рассчёта доходности по вкладу"*/
        Integer width=scale.getSize().getWidth()/maxNumberPositions;/**вычисление ширины одного шага ползунка */

        double SliderCenterPx=Double.parseDouble(slider.getCssValue("left")
                .replaceAll("px",""))+ slider.getSize().width/2;/**вычисление дефолтного местоположения ползунка*/

        double xOffset = (selectedSliderPosition - (SliderCenterPx/width+1)) * width;/**Вычисление новой позици ползунка*/
        Actions actions = new Actions(driver);
        actions.dragAndDropBy(slider, (int) xOffset, 0).perform(); /**Перемещение позиции ползунка*/
    }

    public void dataEntryInTheCardRequestForm(HashMap formData ){ /**Заполнение формы Получения кредитной карты*/

        driver.findElement(By.xpath("//input[@name='ClientLastName']")).sendKeys(formData.get("Фамилия").toString());
        driver.findElement(By.xpath("//input[@name='ClientName']")).sendKeys(formData.get("Имя").toString());

        if(formData.get("Отчество").toString().equals("false")){
            System.out.println("Нажатие на чекбокс Нет отчества " +formData.get("Отчество").toString());
            driver.findElement(By.xpath("//input[@name='ClientSecondName']/parent::div//input[@type='checkbox']/parent::div")).click();
        }else{
            System.out.println("Ввод фамилии " +formData.get("Отчество").toString());
            driver.findElement(By.xpath("//input[@name='ClientSecondName']")).sendKeys(formData.get("Отчество").toString());
        }

        System.out.println("formData.get(\"Телефон\").toString() " + formData.get("Телефон").toString());
        driver.findElement(By.xpath("//input[@name='ClientMobilePhone']")).sendKeys(formData.get("Телефон").toString());
        driver.findElement(By.xpath("//input[@name='AdditionalEmail']")).sendKeys(formData.get("Email").toString());


        driver.findElement(By.xpath("//div[@data-messagetitle='Где Вы желаете получить карту?']")).click();
        driver.findElement(By.xpath("//li[text()='" + formData.get("Город").toString() + "']")).click();
    }
}

