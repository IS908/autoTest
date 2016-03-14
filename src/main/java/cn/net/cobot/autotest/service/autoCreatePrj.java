package cn.net.cobot.autotest.service;


import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import cn.net.cobot.autotest.common.getDriver;
import cn.net.cobot.autotest.module.autoCreate;

public class autoCreatePrj implements autoCreate {
	private WebDriver driver;
	
	public autoCreatePrj() {
		this.driver = getDriver.getInstance();
	}

	@Override
	public void uploadPrj(String fileName, String wholePath) throws InterruptedException{
		String prjName = null;
		String tag = Long.toString(System.currentTimeMillis());
		tag = tag.substring(tag.length()-5, tag.length());
		if (fileName.length() >= 15) {
			prjName = fileName.substring(0, 15) + tag;
		}else {
			prjName = fileName + tag;
		}
		//out2log.log("开始创建工程 - " + prjName);
		System.out.println("开始创建工程 - " + prjName);
		createPrj(prjName, getDriver.C);
		
		isCreateOK(prjName);
		//out2log.log("创建工程 - " + prjName + " - 完成！\n开始导入工程压缩包 - " + fileName);
		System.out.println("创建工程 - " + prjName + " - 完成！\n"
				+ "开始导入工程压缩包 - " + fileName);
		
		WebElement element = driver.findElement(By.xpath("//td[contains(div, \"" + prjName + "\")]"));
		Actions action = new Actions(driver) ;
		action.contextClick(element).perform();
		element = driver.findElement(By.xpath("//a/span[@id='importcop-textEl']/.."));
		element.click();
		List<WebElement> elements = driver.findElements(
				By.xpath("//div[contains(span, '导入项目压缩包文件（不支持.rar 压缩包，会删除原项目下所有文件）')]"));
		while (elements.size() < 1){
			Thread.sleep(1000);
			elements = driver.findElements(
					By.xpath("//div[contains(span, '导入项目压缩包文件（不支持.rar 压缩包，会删除原项目下所有文件）')]"));
		}
		driver.findElement(By.xpath("//input[@id='compressedfile-inputEl']")).sendKeys(wholePath);
		driver.findElement(
				By.xpath("//div[@id='compressedWin']"
						+ "/div[contains(@id, 'toolbar')]"
						+ "/div[contains(@id, 'toolbar')]"
						+ "/div[contains(@id, 'targetEl')]"
						+ "/div[contains(@id, 'button')]"
						+ "/em/button")).click();
		do {
			Thread.sleep(2000);
			elements = driver.findElements(By.xpath("//div[contains(div, '操作中...')]"));
		} while (elements.size() > 0);
		//out2log.log("导入工程压缩包 - " + fileName +" - 完成！");
		System.out.println("导入工程压缩包 - " + fileName +" - 完成！");
		Thread.sleep(2000);
	}

	@Override
	public String createPrj(String prjName, int typeofPrj) throws InterruptedException {
		stepBeforeUpload();
		WebElement element = driver.findElement(By.xpath("//input[@id='prj_name-inputEl']"));
		element.sendKeys(prjName);
		switch (typeofPrj) {
			case getDriver.C:
				
				break;
			case getDriver.CPP:
				
				break;
			default:
				break;
		}
		element = driver.findElement(By.xpath("//button[@data-qtip='确定(Enter+Ctrl)']"));
		element.click();
		Thread.sleep(1000);
		return prjName;
	}
	
	/**
	 * 询问创建工程是否完成
	 * @param prjName
	 */
	private void isCreateOK(String prjName){
		String tmp = "\"" + prjName + "\"";
		List<WebElement> elements = driver.findElements(By.xpath("//td[contains(div, " + tmp + ")]"));
		while (elements.size() == 0){
			try {
				Thread.sleep(1500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			elements = driver.findElements(By.xpath("//td[contains(div, " + tmp + ")]"));
		}
	}
	
	/**
	 * 创建工程前一些准备操作
	 */
	private void stepBeforeUpload() {
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		WebElement step1 = driver.findElement(By.xpath("//button[@data-qtip='新建']"));
		step1.click();
		WebElement step2 = driver.findElement(
				By.xpath("//div[contains(@id, 'menu')]/div[contains(@id, 'body')]"
						+ "/div[contains(@id, 'innerCt')]/div[contains(@id, 'targetEl')]"
						+ "/div[contains(@id, 'menuitem')]"
						+ "/a[contains(@id, 'itemEl') and contains(span, '新建项目')]"));
		step2.click();
	}
}
