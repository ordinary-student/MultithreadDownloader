package com.downloader;

import javax.swing.JOptionPane;
import javax.swing.UIManager;

import com.ui.MultithreadDownloaderFrame;

/**
 * 多线程下载器
 * 
 * @author ordinary-student
 *
 */
public class MultithreadDownloader
{
	public static void main(String[] args)
	{
		try
		{
			// 设置本地系统窗口风格样式
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e)
		{
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "加载本地系统窗口样式失败！");
		} finally
		{
			new MultithreadDownloaderFrame().setVisible(true);
		}
	}
}
