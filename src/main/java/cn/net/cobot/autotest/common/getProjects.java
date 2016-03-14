package cn.net.cobot.autotest.common;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class getProjects {
	public static List<WebElement> getAllProjects() throws InterruptedException{
		List<WebElement> elementsList;
		elementsList = getDriver.getInstance().findElements(By.className("x-grid-cell-inner"));
		while(elementsList.size() < 1){
			Thread.sleep(500);
			elementsList = getDriver.getInstance().findElements(By.className("x-grid-cell-inner"));
		}
		return elementsList;
	}
}
