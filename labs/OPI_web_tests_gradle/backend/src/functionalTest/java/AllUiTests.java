import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.*;
import java.util.List;
import java.time.Duration;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AllUiTests {

    WebDriver driver;
    WebDriverWait wait;

    @BeforeEach
    void beforeTests() {
        driver = new ChromeDriver();
        driver.get("http://localhost:8081/");
        wait = new WebDriverWait(driver, Duration.ofSeconds(3));
    }

    @AfterEach
    void afterTests() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    @Order(1)
    void testInvalidLoginShowsErrorMessage() {
        WebElement loginField = driver.findElement(By.cssSelector("input[type='text']"));
        WebElement passwordField = driver.findElement(By.cssSelector("input[type='password']"));

        loginField.clear();
        passwordField.clear();

        loginField.sendKeys("My_test_1");
        passwordField.sendKeys("My_test_1");

        driver.findElement(By.cssSelector("button[type=submit]")).click();

        WebElement error = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[contains(text(),'Неправильный логин или пароль')]")));

        Assertions.assertTrue(error.isDisplayed(), "Должно появиться сообщение об ошибке при неверном логине/пароле");
    }

    @Test
    @Order(2)
    void checkRegistration(){
        WebElement loginField = driver.findElement(By.cssSelector("input[type='text']"));
        WebElement passwordField = driver.findElement(By.cssSelector("input[type='password']"));
        loginField.clear();
        passwordField.clear();
        loginField.sendKeys("My_test_1");
        passwordField.sendKeys("My_test_1");
        driver.findElement(By.cssSelector("button[type='button']")).click();
        WebElement error = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[contains(text(),'Пользователь зарегистрирован!')]")));

        Assertions.assertTrue(error.isDisplayed(), "Должно появиться сообщение о том что пользователь зареган");
    }

    @Test
    @Order(3)
    void checkIfWasRegistrtion(){
        WebElement loginField = driver.findElement(By.cssSelector("input[type='text']"));
        WebElement passwordField = driver.findElement(By.cssSelector("input[type='password']"));
        loginField.clear();
        passwordField.clear();
        loginField.sendKeys("My_test_1");
        passwordField.sendKeys("My_test_1");
        driver.findElement(By.cssSelector("button[type='button']")).click();
        WebElement error = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[contains(text(),'User already exists!')]")));

        Assertions.assertTrue(error.isDisplayed(), "Должно появиться сообщение о том что пользователь уже зареган");
    }
    @Test
    @Order(4)
    void checkRelocateToMainPage(){
        WebElement loginField = driver.findElement(By.cssSelector("input[type='text']"));
        WebElement passwordField = driver.findElement(By.cssSelector("input[type='password']"));
        loginField.clear();
        passwordField.clear();
        loginField.sendKeys("My_test_1");
        passwordField.sendKeys("My_test_1");
        driver.findElement(By.cssSelector("button[type='submit']")).click();
        wait.until(ExpectedConditions.urlContains("/main"));
        Assertions.assertTrue(driver.getCurrentUrl().endsWith("/main"), "После успешного логина должен быть переход на /main");
    }

    @Test
    @Order(5)
    void checkRelocateToMainAndBack(){
        WebElement loginField = driver.findElement(By.cssSelector("input[type='text']"));
        WebElement passwordField = driver.findElement(By.cssSelector("input[type='password']"));
        loginField.clear();
        passwordField.clear();
        loginField.sendKeys("My_test_1");
        passwordField.sendKeys("My_test_1");
        driver.findElement(By.cssSelector("button[type='submit']")).click();
        wait.until(ExpectedConditions.urlContains("/main"));
        driver.findElement(By.cssSelector("button[class='logout-btn']")).click();
        wait.until(ExpectedConditions.urlContains("http://localhost:8081/"));
        Assertions.assertEquals("http://localhost:8081/", driver.getCurrentUrl(), "После выхода должен быть редирект на страницу авторизации");
    }


    @Test
    @Order(6)
    void testAddPointAndCheckTable() {
        WebElement loginField = driver.findElement(By.cssSelector("input[type='text']"));
        WebElement passwordField = driver.findElement(By.cssSelector("input[type='password']"));

        loginField.clear();
        passwordField.clear();
        loginField.sendKeys("My_test_1");
        passwordField.sendKeys("My_test_1");

        driver.findElement(By.cssSelector("button[type='submit']")).click();

        wait.until(driver -> driver.findElements(By.cssSelector("input[type='number']")).size() >= 3);
        List<WebElement> numberInputs = driver.findElements(By.cssSelector("input[type='number']"));
        WebElement xField = numberInputs.get(0);
        WebElement yField = numberInputs.get(1);
        WebElement rField = numberInputs.get(2);

        xField.clear();
        xField.sendKeys("1");
        yField.clear();
        yField.sendKeys("2");
        rField.clear();
        rField.sendKeys("3");

        List<WebElement> rowsBefore = driver.findElements(By.cssSelector("table tbody tr"));
        int beforeCount = rowsBefore.size();

        driver.findElement(By.xpath("//button[contains(text(),'Проверить')]")).click();

        wait.until(driver -> driver.findElements(By.cssSelector("table tbody tr")).size() == beforeCount + 1);

        List<WebElement> rowsAfter = driver.findElements(By.cssSelector("table tbody tr"));
        WebElement lastRow = rowsAfter.get(rowsAfter.size() - 1);
        List<WebElement> cells = lastRow.findElements(By.tagName("td"));

        Assertions.assertEquals("1", cells.get(0).getText());
        Assertions.assertEquals("2", cells.get(1).getText());
        Assertions.assertEquals("3", cells.get(2).getText());
    }

    @Test
    @Order(7)
    void testInvalidXInput() {
        WebElement loginField = driver.findElement(By.cssSelector("input[type='text']"));
        WebElement passwordField = driver.findElement(By.cssSelector("input[type='password']"));

        loginField.clear();
        passwordField.clear();

        loginField.sendKeys("My_test_1");
        passwordField.sendKeys("My_test_1");

        driver.findElement(By.cssSelector("button[type='submit']")).click();

        wait.until(ExpectedConditions.urlContains("/main"));

        WebElement xField = driver.findElements(By.cssSelector("input[type='number']")).get(0);
        xField.clear();
        xField.sendKeys("1000");

        Assertions.assertFalse(xField.getAttribute("validationMessage").isEmpty(), "Должна быть ошибка валидации для недопустимого X");
    }

    @Test
    @Order(8)
    void testSubmitWithEmptyFields() {
        WebElement loginField = driver.findElement(By.cssSelector("input[type='text']"));
        WebElement passwordField = driver.findElement(By.cssSelector("input[type='password']"));

        loginField.clear();
        passwordField.clear();

        loginField.sendKeys("My_test_1");
        passwordField.sendKeys("My_test_1");

        driver.findElement(By.cssSelector("button[type='submit']")).click();

        wait.until(ExpectedConditions.urlContains("/main"));

        WebElement submitButton = driver.findElement(By.xpath("//button[contains(text(),'Проверить')]"));
        submitButton.click();

        Assertions.assertEquals(driver.getCurrentUrl(), "http://localhost:8081/main", "Перехода не должно быть при пустых полях — остаемся на той же странице");
    }

    @Test
    @Order(9)
    void checkTouchToCanvas() {
        WebElement loginField = driver.findElement(By.cssSelector("input[type='text']"));
        WebElement passwordField = driver.findElement(By.cssSelector("input[type='password']"));

        loginField.clear();
        passwordField.clear();

        loginField.sendKeys("My_test_1");
        passwordField.sendKeys("My_test_1");

        driver.findElement(By.cssSelector("button[type='submit']")).click();

        wait.until(ExpectedConditions.urlContains("/main"));

        List<WebElement> numberInputs = driver.findElements(By.cssSelector("input[type='number']"));
        numberInputs.get(0).sendKeys("1");
        numberInputs.get(1).sendKeys("1");
        numberInputs.get(2).sendKeys("1");

        driver.findElement(By.xpath("//button[contains(text(),'Проверить')]")).click();

        int countBefore = driver.findElements(By.cssSelector("table tbody tr")).size();
        driver.findElement(By.tagName("canvas")).click();
        int countAfter = driver.findElements(By.cssSelector("table tbody tr")).size();
        Assertions.assertEquals(countBefore, countAfter, "После нажатия на график прибавилась строка");
    }



    @Test
    @Order(10)
    void checkVeryBigR() {
        WebElement loginField = driver.findElement(By.cssSelector("input[type='text']"));
        WebElement passwordField = driver.findElement(By.cssSelector("input[type='password']"));

        loginField.clear();
        passwordField.clear();

        loginField.sendKeys("My_test_1");
        passwordField.sendKeys("My_test_1");

        driver.findElement(By.cssSelector("button[type='submit']")).click();

        wait.until(ExpectedConditions.urlContains("/main"));

        List<WebElement> numberInputs = driver.findElements(By.cssSelector("input[type='number']"));
        numberInputs.get(0).sendKeys("0");
        numberInputs.get(1).sendKeys("0");
        numberInputs.get(2).sendKeys("99");

        int countBefore = driver.findElements(By.cssSelector("table tbody tr")).size();
        driver.findElement(By.xpath("//button[contains(text(),'Проверить')]")).click();

        int countAfter = driver.findElements(By.cssSelector("table tbody tr")).size();
        Assertions.assertEquals(countBefore, countAfter, "Строк в таблице не должно добавиться при некорректном R");
    }

    @Test
    @Order(11)
    void testLoginFieldIsEmpty() {
        WebElement loginField    = driver.findElement(By.cssSelector("input[type='text']"));
        WebElement passwordField = driver.findElement(By.cssSelector("input[type='password']"));

        loginField.clear();
        passwordField.clear();
        passwordField.sendKeys("My_test_1");

        driver.findElement(By.cssSelector("button[type='submit']")).click();

        Assertions.assertTrue(driver.getCurrentUrl().endsWith("/"), "Если логин пустой — перехода быть не должно");
    }

    @Test
    @Order(12)
    void testPasswordFieldIsRequired() {
        WebElement loginField    = driver.findElement(By.cssSelector("input[type='text']"));
        WebElement passwordField = driver.findElement(By.cssSelector("input[type='password']"));

        loginField.clear();
        passwordField.clear();
        loginField.sendKeys("My_test_1");

        driver.findElement(By.cssSelector("button[type='submit']")).click();

        Assertions.assertTrue(driver.getCurrentUrl().endsWith("/"), "Если пароль пустой — перехода быть не должно");
    }


    @Test
    @Order(13)
    void testInvalidYInput() {
        WebElement loginField    = driver.findElement(By.cssSelector("input[type='text']"));
        WebElement passwordField = driver.findElement(By.cssSelector("input[type='password']"));
        loginField.sendKeys("My_test_1");
        passwordField.sendKeys("My_test_1");
        driver.findElement(By.cssSelector("button[type='submit']")).click();
        wait.until(ExpectedConditions.urlContains("/main"));

        List<WebElement> nums = driver.findElements(By.cssSelector("input[type='number']"));
        nums.get(0).sendKeys("0");    
        nums.get(1).sendKeys("1000");     
        nums.get(2).sendKeys("3");        

        int before = driver.findElements(By.cssSelector("table tbody tr")).size();
        driver.findElement(By.xpath("//button[contains(text(),'Проверить')]")).click();
        int after  = driver.findElements(By.cssSelector("table tbody tr")).size();

        Assertions.assertEquals(before, after, "Строка не должна добавляться при недопустимом значении Y");
    }

    @Test
    @Order(14)
    void testNegativeRInput() {
        WebElement loginField    = driver.findElement(By.cssSelector("input[type='text']"));
        WebElement passwordField = driver.findElement(By.cssSelector("input[type='password']"));
        loginField.sendKeys("My_test_1");
        passwordField.sendKeys("My_test_1");
        driver.findElement(By.cssSelector("button[type='submit']")).click();
        wait.until(ExpectedConditions.urlContains("/main"));

        List<WebElement> nums = driver.findElements(By.cssSelector("input[type='number']"));
        nums.get(0).sendKeys("1");
        nums.get(1).sendKeys("1");
        nums.get(2).sendKeys("-1");

        Assertions.assertFalse(nums.get(2).getAttribute("validationMessage").isEmpty(), "Для отрицательного R должно появиться сообщение валидации");
    }


    @Test
    @Order(15)
    void testLogoutButtonIsVisible() {
        WebElement loginField    = driver.findElement(By.cssSelector("input[type='text']"));
        WebElement passwordField = driver.findElement(By.cssSelector("input[type='password']"));
        loginField.sendKeys("My_test_1");
        passwordField.sendKeys("My_test_1");
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        WebElement logout = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("button.logout-btn")));

        Assertions.assertTrue(logout.isDisplayed(), "На /main должна отображаться кнопка выхода");
    }


}
