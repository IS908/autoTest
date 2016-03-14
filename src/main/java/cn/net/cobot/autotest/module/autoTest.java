package cn.net.cobot.autotest.module;

import java.util.ArrayList;

/**
 * Created by kevin on 10/12/15.
 */
public interface autoTest {
    /**
     * 显示所有测试项目
     * @throws InterruptedException
     */
    void showAllProjects() throws InterruptedException;

    /**
     * 进行所有项目的缺陷测试
     * @throws InterruptedException
     */
    void executeTesting() throws InterruptedException;

    /**
     * 选择性测试相应工程
     * @param numbers
     * @return
     * @throws InterruptedException
     */
    boolean executeTesting(ArrayList<Integer> numbers) throws InterruptedException;

    /**
     * 从前台获取构建VDG所耗时间
     */
    void getVDGCost(String prjName);
}
