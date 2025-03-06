package egovframework.let.uat.uia.web;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import lombok.extern.slf4j.Slf4j;

@Slf4j
class TestEgovLoginApiControllerSelenium {

	WebDriver driver;

	@BeforeEach
	public void setup() {
		driver = new ChromeDriver();
	}

	@Test
	void test() {
		if (log.isDebugEnabled()) {
			log.debug("[2024년 전자정부 표준프레임워크 컨트리뷰션][로그인] 셀레늄 단위 테스트");
		}

		// given
		if (log.isDebugEnabled()) {
			log.debug("로그인 화면 이동");
		}
		driver.get("http://localhost:3000/login");

		// 아이디 입력
		sleep();
		WebElement idWebElement = driver.findElement(By.cssSelector(
				"#contents > div > div.login_box > form > fieldset > span > input[type=text]:nth-child(1)"));
		idWebElement.sendKeys("admin");

		// 비밀번호 입력
		sleep();
		WebElement passwordWebElement = driver.findElement(By.cssSelector(
				"#contents > div > div.login_box > form > fieldset > span > input[type=password]:nth-child(2)"));
		passwordWebElement.sendKeys("1");

		// when
		// 로그인 버튼 클릭
		sleep();
		WebElement loginWebElement = driver
				.findElement(By.cssSelector("#contents > div > div.login_box > form > fieldset > button > span"));
		loginWebElement.click();

		// then
		sleep();
		WebElement spanWebElement = driver
				.findElement(By.cssSelector("#root > div > div.header > div.inner > div.user_info > span"));
		assertEquals("관리자", spanWebElement.getText(), "로그인 실패");
	}

	private void sleep() {
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			fail("InterruptedException: Thread.sleep");
		}
	}

}