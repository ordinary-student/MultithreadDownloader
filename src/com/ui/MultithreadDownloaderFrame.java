package com.ui;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

import com.thread.MultiDownloadThread;
import com.util.PropertiesUtil;

/**
 * 多线程下载器窗口类
 * 
 * @author ordinary-student
 *
 */
public class MultithreadDownloaderFrame extends JFrame implements ActionListener, MouseListener
{
	private static final long serialVersionUID = 868625235671330285L;
	private JPanel panel;
	private JTextField textField;
	private JButton button;
	private JTextArea outputTextArea;
	// 右键弹出菜单
	private JPopupMenu popupMenu;
	private JMenuItem popupMenu_clear;
	private JMenuItem popupMenu_open;
	private MultiDownloadThread multiDownloadThread;

	/*
	 * 构造方法
	 */
	public MultithreadDownloaderFrame()
	{
		// 初始化界面
		initUI();
		// 多个下载线程
		multiDownloadThread = new MultiDownloadThread(outputTextArea);
	}

	/**
	 * 初始化界面
	 */
	private void initUI()
	{
		// 设置标题
		setTitle("多线程下载器");
		// 设置位置和大小
		setBounds(200, 100, 600, 400);
		// 设置最小的大小
		setMinimumSize(new Dimension(600, 200));
		// 设置布局
		getContentPane().setLayout(new BorderLayout(5, 5));
		// 设置关闭方式
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		// 创建面板
		panel = new JPanel();
		panel.setBorder(BorderFactory.createTitledBorder("填写下载地址"));
		panel.setLayout(new BorderLayout(5, 5));

		// 输入框
		textField = new JTextField();
		textField.setPreferredSize(new Dimension(350, 30));
		textField.setFont(new Font("Microsoft YaHei", Font.PLAIN, 15));
		panel.add(textField, BorderLayout.CENTER);

		// 按钮
		button = new JButton("开始下载");
		button.setPreferredSize(new Dimension(100, 30));
		button.setFont(new Font("Microsoft YaHei", Font.PLAIN, 15));
		button.setFocusPainted(false);
		button.addActionListener(this);
		panel.add(button, BorderLayout.EAST);

		getContentPane().add(panel, BorderLayout.NORTH);

		// 信息输出区域
		outputTextArea = new JTextArea(20, 50);
		outputTextArea.setFont(new Font("Microsoft YaHei", Font.PLAIN, 16));
		outputTextArea.setEditable(false);
		// 添加鼠标监听
		outputTextArea.addMouseListener(this);

		// 创建带滚动条的面板
		JScrollPane scroller = new JScrollPane(outputTextArea);
		scroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

		// 向窗口添加信息输出区
		getContentPane().add(scroller, BorderLayout.CENTER);

		// 右键菜单
		popupMenu = new JPopupMenu();
		// 清空记录-右键菜单项
		popupMenu_clear = new JMenuItem("清空记录");
		popupMenu_clear.addActionListener(this);
		popupMenu.add(popupMenu_clear);

		// 分隔线
		popupMenu.addSeparator();

		// 打开下载目录-右键菜单项
		popupMenu_open = new JMenuItem("打开下载目录");
		popupMenu_open.addActionListener(this);
		popupMenu.add(popupMenu_open);

		validate();
		// 设置可视
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		// 判断来源
		if (e.getSource() == button)
		{
			// 检查下载地址
			if (checkUrl())
			{
				// 下载
				download(textField.getText().replaceAll(" ", ""));
			}
		} else if (e.getSource() == popupMenu_clear)
		{
			// 清空记录
			outputTextArea.setText("");

		} else if (e.getSource() == popupMenu_open)
		{
			try
			{
				// 打开下载目录
				Desktop.getDesktop().open(new File(PropertiesUtil.downloadDir));
			} catch (IOException e1)
			{
				e1.printStackTrace();
				JOptionPane.showMessageDialog(this, "打开失败！");
			}
		}

	}

	/**
	 * 检查下载地址
	 * 
	 * @return 检查结果
	 */
	private boolean checkUrl()
	{
		// 判断
		if ((textField.getText() == null) || (textField.getText().replaceAll(" ", "").equals("")))
		{
			JOptionPane.showMessageDialog(this, "请填写下载地址！");
			return false;
		} else
		{
			return true;
		}

		// 其它检查地址正确性的业务逻辑
		// TODO...

	}

	/**
	 * 下载方法
	 * 
	 * @param url下载地址
	 */
	private void download(String url)
	{
		try
		{
			multiDownloadThread.download(url);
		} catch (Exception e)
		{
			e.printStackTrace();
			JOptionPane.showMessageDialog(outputTextArea, "下载出错！", "错误", JOptionPane.ERROR_MESSAGE);
			return;
		}

	}

	@Override
	public void mouseClicked(MouseEvent e)
	{

	}

	@Override
	public void mousePressed(MouseEvent e)
	{
		// 判断是否触发弹出菜单事件
		if (e.isPopupTrigger())
		{
			// 显示弹出菜单
			popupMenu.show(e.getComponent(), e.getX(), e.getY());
		}
	}

	@Override
	public void mouseReleased(MouseEvent e)
	{
		// 判断是否触发弹出菜单事件
		if (e.isPopupTrigger())
		{
			// 显示弹出菜单
			popupMenu.show(e.getComponent(), e.getX(), e.getY());
		}
	}

	@Override
	public void mouseEntered(MouseEvent e)
	{

	}

	@Override
	public void mouseExited(MouseEvent e)
	{

	}

}
