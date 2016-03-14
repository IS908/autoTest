package cn.net.cobot.autotest.JUI;

import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.openqa.selenium.WebElement;

import cn.net.cobot.autotest.common.getDriver;
import cn.net.cobot.autotest.common.getProjects;
import cn.net.cobot.autotest.module.autoTest;
import cn.net.cobot.autotest.service.autoTestPrj;

/**
 * Created by kevin on 10/13/15.
 */
public class SwingConsole extends JFrame {

	private static final long serialVersionUID = 1L;
	private int prjCount;
	private javax.swing.JButton btnDel;
    private javax.swing.JButton btnTestAll;
    private javax.swing.JButton btnTestSelected;
    private javax.swing.JButton btnUpload;
    private javax.swing.JPanel panel;
    private javax.swing.JScrollPane scrollPane;
    private javax.swing.JTable table;
    private autoTest testPrj;
    
    /**
     * Creates new form SwingConsole
     */
    public SwingConsole() {
        initComponents();
    }

    private void initComponents() {
    	testPrj = new autoTestPrj() ;
        panel = new javax.swing.JPanel();
        scrollPane = new javax.swing.JScrollPane();
        table = new javax.swing.JTable();
        btnTestAll = new javax.swing.JButton();
        btnTestSelected = new javax.swing.JButton();
        btnUpload = new javax.swing.JButton();
        btnDel = new javax.swing.JButton();
        
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        //表格数据填充
	    fillData();
	    
	    tableSet();
	    //button按钮监听设置
	    btnAction();
	    
	    myLayout();
        pack();
        
        closeing();
    }     

    /**
     * 填充表格数据
     * @param data
     */
    private void fillData() {
    	table.setModel(new javax.swing.table.DefaultTableModel(
    			getData(), 
                new String [] {
                    "编号", "工程名称", "选择"
                }
            ) {
				private static final long serialVersionUID = 1L;
				@SuppressWarnings("rawtypes")
				Class[] types = new Class [] {
                    String.class, String.class, Boolean.class
                };
                boolean[] canEdit = new boolean [] {
                    false, false, true
                };

                public Class<?> getColumnClass(int columnIndex) {
                    return types [columnIndex];
                }

                public boolean isCellEditable(int rowIndex, int columnIndex) {
                    return canEdit [columnIndex];
                }
            });
	}
    
    private Object[][] getData(){
    	
    	List<WebElement> elementsList = null;
    	try {
    		elementsList = getProjects.getAllProjects();
    		prjCount = elementsList.size();
		} catch (InterruptedException e) {
			e.printStackTrace();
			btnDelActionPerformed(null);
		}
    	Object[][] data = new Object[prjCount][3];
    	int i = 0;
    	for (WebElement element: elementsList){
    		Object[] tmp = new Object[]{i, element.getText(), false};
    		data[i++] = tmp;
    	}
    	return data;
    }
    
    /**
     * 绑定按钮相应操作
     */
    private void btnAction() {
    	 btnTestAll.setText("测试全部");
         btnTestAll.addActionListener(new java.awt.event.ActionListener() {
             public void actionPerformed(java.awt.event.ActionEvent evt) {
                 btnTestAllActionPerformed(evt);
             }
         });

         btnTestSelected.setText("测试所选");
         btnTestSelected.addActionListener(new java.awt.event.ActionListener() {
             public void actionPerformed(java.awt.event.ActionEvent evt) {
                 btnTestSelectedActionPerformed(evt);
             }
         });

         btnUpload.setText("批量上传");
         btnUpload.addActionListener(new java.awt.event.ActionListener() {
             public void actionPerformed(java.awt.event.ActionEvent evt) {
                 btnUploadActionPerformed(evt);
             }
         });

         btnDel.setText("退出");
         btnDel.addActionListener(new java.awt.event.ActionListener() {
             public void actionPerformed(java.awt.event.ActionEvent evt) {
                 btnDelActionPerformed(evt);
             }
         });
	}
    
    /**
     * 测试所有工程
     * @param evt
     */
    private void btnTestAllActionPerformed(java.awt.event.ActionEvent evt) {                                           
    	try {
			this.testPrj.executeTesting();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    	JOptionPane.showMessageDialog(SwingConsole.this, "全选测试完成！");
    }                                          

    /**
     * 测试选择的工程
     * @param evt
     */
    private void btnTestSelectedActionPerformed(java.awt.event.ActionEvent evt) {   
    	ArrayList<Integer> list = new ArrayList<>();
    	for (int i=0;i<prjCount;i++){
    		if ((boolean)table.getModel().getValueAt(i, 2))		list.add(i);
    	}
		try {
			this.testPrj.executeTesting(list);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		// 提示测试完成
		JOptionPane.showMessageDialog(SwingConsole.this, "部分测试完成！");
    }                                               

    /**
     * 执行创建并上传工程操作
     * @param evt
     */
    private void btnUploadActionPerformed(java.awt.event.ActionEvent evt) {                                          
    	JDialog dialog = new fileDialog(this, true);
          //调整Dialog在显示器的显示位置
          java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
          dialog.setBounds((screenSize.width-330)/2, (screenSize.height-110)/2, 330, 110);
          dialog.setResizable(false);
          dialog.setVisible(true);
    }                                        

    /**
     * 退出操作
     * @param evt
     */
    private void btnDelActionPerformed(java.awt.event.ActionEvent evt) {                                        
    	try {
    		getDriver.getInstance().close();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(SwingConsole.this, "相应Chrome已提前关闭！");
		}
    	System.exit(NORMAL);
    }   
    
    /**
     * 注册确认关闭事件
     */
    private void closeing() {
    	setSize(100, 200);
    	setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

    	addWindowListener(new WindowAdapter(){
	    	@Override
	    	public void windowClosing(WindowEvent e) {
		    	int option= JOptionPane.showConfirmDialog( SwingConsole.this, "确定退出工具? ", "提示 ",JOptionPane.YES_NO_CANCEL_OPTION); 
		    	if(option==JOptionPane.YES_OPTION) 
		    	if(e.getWindow() == SwingConsole.this) {
		    		try {
		        		getDriver.getInstance().close();
		    		} catch (Exception ee) {
		    			ee.printStackTrace();
		    		}
		    		System.exit(0); 
		    	} else 
		    		return; 
	    	}
    	});
	}
    
    /**
     * table设置
     */
    private void tableSet() {
    	table.setPreferredScrollableViewportSize(new Dimension(350, 420));
    	table.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        //table.setColumnSelectionAllowed(true);
        table.setName("工程列表"); 
        scrollPane.setViewportView(table);
        table.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        if (table.getColumnModel().getColumnCount() > 0) {
        	table.getColumnModel().getColumn(0).setPreferredWidth(50);
        	table.getColumnModel().getColumn(1).setPreferredWidth(190);
        	table.getColumnModel().getColumn(02).setPreferredWidth(50);
            table.getColumnModel().getColumn(0).setResizable(false);
            table.getColumnModel().getColumn(1).setResizable(false);
            table.getColumnModel().getColumn(2).setResizable(false);
        }

        javax.swing.GroupLayout panelLayout = new javax.swing.GroupLayout(panel);
        panel.setLayout(panelLayout);
        panelLayout.setHorizontalGroup(
            panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelLayout.createSequentialGroup()
                .addContainerGap(13, Short.MAX_VALUE)
                .addComponent(scrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 315, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        panelLayout.setVerticalGroup(
            panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(scrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 500, Short.MAX_VALUE)
                .addContainerGap())
        );
	}
    
    /**
     * layout设置
     */
    private void myLayout() {
    	javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
            		.addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnTestAll)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 30, Short.MAX_VALUE)
                        .addComponent(btnTestSelected)
                        .addGap(18, 18, 18)
                        .addComponent(btnUpload)
                        .addGap(18, 18, 18)
                        .addComponent(btnDel))
                    .addComponent(panel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnTestAll)
                    .addComponent(btnTestSelected)
                    .addComponent(btnUpload)
                    .addComponent(btnDel))
                .addGap(18, 18, 18))
        );
	}

}
