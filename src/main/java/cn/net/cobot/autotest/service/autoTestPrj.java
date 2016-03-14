package cn.net.cobot.autotest.service;

import java.util.ArrayList;
import java.util.List;

import cn.net.cobot.autotest.module.autoTest;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import cn.net.cobot.autotest.common.getDriver;
import cn.net.cobot.autotest.common.getProjects;
import cn.net.cobot.autotest.common.out2log;

public class autoTestPrj implements autoTest {

	private WebDriver driver;
	
	public autoTestPrj() {
		this.driver = getDriver.getInstance();
	}
	
	@Override
	public void showAllProjects() throws InterruptedException {
		List<WebElement> elementsList = getProjects.getAllProjects();
		
        int i = 0;
        for (WebElement element: elementsList){
        	System.out.println((i++) + " ---> " + element.getText());
        }
	}

	/**
	 * 进行所有项目的缺陷测试
	 * @throws InterruptedException
	 */
	@Override
	public void executeTesting() throws InterruptedException{
		List<WebElement> elementsList = getProjects.getAllProjects();

		String currentPrjName = null;
		Actions action = new Actions(driver) ;
		for(WebElement element : elementsList){
			currentPrjName = element.getText();
			System.out.println("当前测试工程：" + currentPrjName);

			action.contextClick(element).perform();

			Thread.sleep(500);
			driver.findElement(By.id("redcheck-textEl")).click();
			isTestFinish();
			getVDGCost(currentPrjName);
			downloadAction(currentPrjName);
			
       	}
	}
	
	/**
	 * 选择性测试相应工程
	 * @param numbers
	 * @return
	 * @throws InterruptedException
	 */
	@Override
	public boolean executeTesting(ArrayList<Integer> numbers) throws InterruptedException{
		List<WebElement> elementsList = getProjects.getAllProjects();
		Actions action = new Actions(driver) ;
		WebElement element;
		String currentPrjName = null;
		for(int i=0;i<elementsList.size();i++){
			if(!numbers.contains(i))	continue;

			element = elementsList.get(i);
			currentPrjName = element.getText();
			System.out.println("当前测试工程：" + currentPrjName);

			action.contextClick(element).perform();

			Thread.sleep(500);
			driver.findElement(By.id("redcheck-textEl")).click();
			isTestFinish();
			getVDGCost(currentPrjName);
			downloadAction(currentPrjName);
		}
		return true;
	}

	@Override
	public void getVDGCost(String prjName){
		WebElement consolElement = driver.findElement(By.xpath("//button[contains(span, '输出控制台')]"));
		consolElement.click();
		consolElement = driver.findElement(By.xpath("//textarea[@id='consoleOutput-inputEl' and @name='consoleOutput']"));
		String[] consoleOutput = consolElement.getAttribute("value").split("\n");
		String VDGCost = null;
		for (String line: consoleOutput){
			if (line.contains("耗时"))	VDGCost = line;
		}
		out2log.log(prjName + "\t构建VDG" + VDGCost);
		System.out.println(prjName + "\t构建VDG" + VDGCost);
		List<WebElement>  elements = driver.findElements(By.xpath(
				"//span[contains(@id,'tabpanel-') and" +
						" contains(@id,'_header_hd-textEl') and " +
						"@class='x-panel-header-text x-panel-header-text-default']"));
		WebElement prjCost = null;
		for(WebElement element: elements){
			if(element.getText().contains("输出区"))
				prjCost = element;
		}
		String[] tmp = prjCost.getText().split("；");
		String allCost = null;
		for (String str:tmp) {
			if(str.contains("检测耗时")) allCost = str;
		}
		out2log.log(prjName + "\t整个工程" + allCost);
		System.out.println(prjName + "\t整个工程" + allCost);
		WebElement clearButton = driver.findElement(By.xpath("//button[@data-qtip='清空控制台']"));
		clearButton.click();
	}


	private void downloadAction(String prjName) {
		driver.findElement(By.xpath("//div[contains(@id, 'tabbar') and contains(@id, 'innerCt')]"
					+ "/div[contains(@id, 'targetEl')]/div[contains(@id, 'tab')]"
					+ "/em[contains(@id, 'btnWrap')]/button[contains(span, '缺陷检测')]")).click();
		try {
			Thread.sleep(1000);
			driver.findElement(By.xpath("//div[contains(@id, 'button')]"
					+ "/em[contains(@id, 'btnWrap')]/button[contains(span, '导出检测结果')]")).click();
			Thread.sleep(1000);
			driver.findElement(By.xpath("//div[contains(@id, 'menuitem')]/a[contains(@id, 'itemEl') and contains(span, '导出Excel')]")).click();
			Thread.sleep(2000);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("工程" + prjName + "导出检测结果失败");
		}
	}
	
	/**
	 * 每10秒查看页面一次是否检测完成
	 * @throws InterruptedException
	 */
	private void isTestFinish() throws InterruptedException {
		List<WebElement>  elements = driver.findElements(By.xpath(
				"//span[contains(@id,'tabpanel-') and" +
				" contains(@id,'_header_hd-textEl') and " +
				"@class='x-panel-header-text x-panel-header-text-default']"));
		//System.out.println(elements.size());
		WebElement current = null;
		for(WebElement element: elements){
			if(element.getText().contains("输出区"))
				current = element;
		}
		//System.out.println(current.getText());
		int num = 0;
		while (!current.getText().contains("检测耗时") && num < 3600){
			Thread.sleep(1000);
			num++;
		}
		if (num == 3600)	System.out.println("该工程一小时没有跑出结果！");
		elements = driver.findElements(By.xpath("//div[contains(div, '操作中...')]"));
		while (elements.size() > 0)	{
			Thread.sleep(1000);
			elements = driver.findElements(By.xpath("//div[contains(div, '操作中...')]"));
		}
	}
}
