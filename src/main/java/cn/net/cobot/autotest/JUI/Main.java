package cn.net.cobot.autotest.JUI;

import java.awt.EventQueue;
import java.awt.Toolkit;

import cn.net.cobot.autotest.common.getDriver;

/**
 * Created by kevin on 10/13/15.
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        // 开启并登陆浏览器
        getDriver.getInstance();
        // 创建视图窗口
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                SwingConsole window = new SwingConsole();
                window.setSize(400, 600);
                window.setResizable(false);
                Toolkit toolkit = Toolkit.getDefaultToolkit();

                int x = (int)(toolkit.getScreenSize().getWidth() - window.getWidth())/2;

                int y = (int)(toolkit.getScreenSize().getHeight() - window.getHeight())/2;

                window.setLocation(x, y);

                window.setVisible(true);
            }
        });
    }

}
