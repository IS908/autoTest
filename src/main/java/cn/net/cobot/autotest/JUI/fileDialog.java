package cn.net.cobot.autotest.JUI;

import java.awt.HeadlessException;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import cn.net.cobot.autotest.common.fileViewer;
import cn.net.cobot.autotest.common.getProjects;
import cn.net.cobot.autotest.module.autoCreate;
import cn.net.cobot.autotest.module.autoTest;
import cn.net.cobot.autotest.service.autoCreatePrj;
import cn.net.cobot.autotest.service.autoTestPrj;

public class fileDialog extends JDialog {
	private static final long serialVersionUID = 1L;
	private JButton btnPath;
    private JButton btnOK;
    private JButton btnExit;
    private JLabel lbPath;
    private JCheckBox chkBoxSwitch;
    private boolean flag;
    
	public fileDialog(java.awt.Frame parent, boolean modal) {
		super(parent, modal);
		this.setTitle("选择文件路径");
		this.setModal(true);  //这里很重要，modal - 指定 dialog 是否阻止在显示的时候将内容输入其他窗口；
        initComponents();
	}

	private void initComponents() {

        btnPath = new JButton();
        lbPath = new JLabel();
        btnOK = new JButton();
        btnExit = new JButton();
        chkBoxSwitch = new JCheckBox();
        chkBoxSwitch.setSelected(false);
        flag = false;

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        btnAction();
        layoutSet();
        pack();
    }                        

    private void btnPathActionPerformed(java.awt.event.ActionEvent evt) {                                        
        JFileChooser chooser =new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);//只能选择目录
        int flag = -1;
        String path = null;
        File f = null;
        try{     
            flag=chooser.showOpenDialog(null);     
        }    
        catch(HeadlessException head){     
             System.out.println("Open File Dialog ERROR!");    
        }        
        if(flag==JFileChooser.APPROVE_OPTION){
             //获得该文件    
            f=chooser.getSelectedFile();    
            path=f.getPath();
            lbPath.setText(path);
            this.flag = true;
         }    
    }                                       

    private void btnOKActionPerformed(java.awt.event.ActionEvent evt) throws InterruptedException {
    	if (flag) {
			//执行上传动作
    		fileViewer files = new fileViewer();
    		List<String> fileList = files.getListFiles(lbPath.getText());
    		autoCreate upload = new autoCreatePrj();
    		String fileName;
    		for (String wholePath: fileList){
    			fileName = findFileName(wholePath);
    			//System.out.println("fileName: " + fileName);
    			//System.out.println("wholePaht: " + wholePath);
    			try {
					upload.uploadPrj(fileName, wholePath);
				} catch (InterruptedException e) {
					e.printStackTrace();
					JOptionPane.showMessageDialog(fileDialog.this, fileName + "上传出错！");
				}
    		}
    		if (chkBoxSwitch.isSelected()) 
				// 执行相应测试操作
    			testOn(fileList.size());
    		else
    			JOptionPane.showMessageDialog(fileDialog.this, "批量上传完成！");
    		this.setVisible(false);
		}else{
			JOptionPane.showMessageDialog(fileDialog.this, "请选择上传工程文件夹！");
		}
    }                                     

    private void btnExitActionPerformed(java.awt.event.ActionEvent evt) {
    	this.setVisible(false);
    }                                       

    private String findFileName(String filePath) {
    	String os = System.getProperty("os.name");
    	String[] file = null;
    	if (os.contains("Windows")) {
			//windows
    		file = filePath.split("\\");
		}else {
			file = filePath.split("/");
		}
    	return file[file.length - 1];
	}
    
    private void testOn(int count) throws InterruptedException {
		int end = getProjects.getAllProjects().size();
		int first = end - count;
		ArrayList<Integer> list = new ArrayList<Integer>();
		for (int i = first; i < end; i++) {
			list.add(i - 3);
		}
		Thread.sleep(3000);
		autoTest test = new autoTestPrj();
		test.executeTesting(list);
		JOptionPane.showMessageDialog(fileDialog.this, "批量上传并测试完成！");
	}
    
    /**
     * 设置button监听事件绑定操作
     */
    private void btnAction() {
        btnPath.setText("浏   览");
        btnPath.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPathActionPerformed(evt);
            }
        });

        lbPath.setText("选择文件路径...");
        chkBoxSwitch.setText("上传并检测");
        
        btnOK.setText("上   传");
        btnOK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                try {
					btnOKActionPerformed(evt);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
            }
        });

        btnExit.setText("取   消");
        btnExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExitActionPerformed(evt);
            }
        });
	}
    
    /**
     * 设置layout
     */
    private void layoutSet() {
    	 javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
         getContentPane().setLayout(layout);
         layout.setHorizontalGroup(
             layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
             .addGroup(layout.createSequentialGroup()
                 .addGap(12, 12, 12)
                 .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                     .addGroup(layout.createSequentialGroup()
                         .addComponent(btnPath)
                         .addGap(12, 12, 12)
                         .addComponent(lbPath, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE))
                     .addGroup(layout.createSequentialGroup()
                         .addComponent(chkBoxSwitch)
                         .addGap(68, 68, 68)
                         .addComponent(btnOK)
                         .addGap(6, 6, 6)
                         .addComponent(btnExit))))
         );
         layout.setVerticalGroup(
             layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
             .addGroup(layout.createSequentialGroup()
                 .addGap(12, 12, 12)
                 .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                     .addComponent(btnPath)
                     .addGroup(layout.createSequentialGroup()
                         .addGap(6, 6, 6)
                         .addComponent(lbPath)))
                 .addGap(28, 28, 28)
                 .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                     .addGroup(layout.createSequentialGroup()
                         .addGap(2, 2, 2)
                         .addComponent(chkBoxSwitch))
                     .addComponent(btnOK)
                     .addComponent(btnExit)))
         );
	}
}
