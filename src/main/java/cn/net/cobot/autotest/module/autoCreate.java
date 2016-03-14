package cn.net.cobot.autotest.module;

/**
 * Created by kevin on 10/12/15.
 */
public interface autoCreate {
    /**
     * 创建工程操作
     * @param prjName
     * @param typeofPrj
     * @return
     * @throws InterruptedException
     */
    String createPrj(String prjName, int typeofPrj) throws InterruptedException;

    /**
     * 上传工程操作
     * @param prjName
     * @param path
     */
    void uploadPrj(String prjName, String path) throws InterruptedException;
}
