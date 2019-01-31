package at.fhv.badegr.tests;

import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.openqa.selenium.*;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.URL;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

/**
 * Step implementation for the giphy search UAT tests
 */
public class GiphyBrowseSteps {

	private WebDriver driver;

	/**
	 * Setup the firefox test driver. This needs the environment variable
	 * 'webdriver.gecko.driver' with the path to the geckodriver binary
	 */
	@Before
	public void before(Scenario scenario) throws Exception {
		DesiredCapabilities capabilities = new DesiredCapabilities();
		capabilities.setCapability("platform", "WIN10");
		capabilities.setCapability("version", "64");
		capabilities.setCapability("browserName", "firefox");
		capabilities.setCapability("name", scenario.getName());

		if (!scenario.getName().endsWith("(video)")) {
			capabilities.setCapability("headless", true);
		}

		driver = new RemoteWebDriver(
				new URL("http://" + System.getenv("TESTINGBOT_CREDENTIALS") + "@hub.testingbot.com/wd/hub"),
				capabilities);

		// prevent errors if we start from a sleeping heroku instance
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
	}

	/**
	 * Shutdown the driver
	 */
	@After
	public void after() {
		driver.quit();
	}

	@Given("^Open (.*?)$")
	public void openUrl(String url) {
		driver.navigate().to(url);
	}

	@Given("^Login with user '(.*?)' and password '(.*?)'$")
	public void login(String email, String password) {
		WebElement emailField = driver.findElement(By.id("email"));
		emailField.sendKeys(email);
		WebElement passwordField = driver.findElement(By.id("inputPassword"));
		passwordField.sendKeys(password);
		WebElement loginButton = driver.findElement(By.id("login"));
		loginButton.click();
	}

	@When("^I press sign out$")
	public void logout() {
		WebDriverWait wait = new WebDriverWait(driver, 10);
		By logoutLinkId = By.id("Logout");
		wait.until(ExpectedConditions.elementToBeClickable(logoutLinkId));

		WebElement logoutLink = driver.findElement(logoutLinkId);
		logoutLink.click();
	}

	@Then("^I see the login page$")
	public void checkLoginPage() {
		WebElement emailField = driver.findElement(By.id("email"));
		WebElement passwordField = driver.findElement(By.id("inputPassword"));
		WebElement loginButton = driver.findElement(By.id("login"));
		
		assertTrue(emailField.isDisplayed());
		assertTrue(passwordField.isDisplayed());
		assertTrue(loginButton.isDisplayed());
	}

	@When("^I search for the term '(.*?)' and hit enter$")
	public void searchGif(String text) {
		WebElement searchBar = driver.findElement(By.id("search"));
		searchBar.clear();
		searchBar.sendKeys(text);
		searchBar.sendKeys(Keys.RETURN);
	}

	@When("^I press the random search button$")
	public void searchGifRandomly() {
		WebElement randomSearchButton = driver.findElement(By.id("RandomSearch"));
		randomSearchButton.click();
	}

	@Then("^I see a gif from giphy$")
	public void checkForGif() {
		// wait until the result has been received
		WebDriverWait wait = new WebDriverWait(driver, 10);
		By gif = By.className("imgFull");
		wait.until(ExpectedConditions.elementToBeClickable(gif));
		
		WebElement gifElement = driver.findElement(gif);
		String url = gifElement.getAttribute("src");
		assertTrue(url.endsWith(".gif"));
	}
}
