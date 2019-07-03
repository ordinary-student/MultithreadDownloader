package com.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import javax.swing.JOptionPane;

/**
 * 读写属性工具类
 * 
 * @author ordinary-student
 *
 */
public class PropertiesUtil
{
	// 下载文件存放目录
	public static String downloadDir = "C:/download/";
	// 文件分块大小
	public static int blockSize = 1;
	// 最小线程数
	public static int minThreadNum = 1;
	// 最大线程数
	public static int maxThreadNum = 20;

	static
	{
		// 如果属性文件不存在则创建
		if (createPropertiesFile())
		{
			// 加载属性
			loadProperties();
		}
	}

	/**
	 * 创建属性文件
	 */
	private static boolean createPropertiesFile()
	{
		// 属性文件
		File file = new File("properties.properties");
		// 判断
		if (!file.exists())
		{
			try
			{
				// 不存在就创建
				file.createNewFile();
			} catch (IOException e)
			{
				e.printStackTrace();
				JOptionPane.showMessageDialog(null, "创建属性文件失败！", "错误", JOptionPane.ERROR_MESSAGE);
				return false;
			}

			// 创建一个属性配置对象
			Properties properties = new Properties();
			// 设置属性
			properties.setProperty("downloadDir", "C:/download/");
			properties.setProperty("blockSize", "1");
			properties.setProperty("minThreadNum", "1");
			properties.setProperty("maxThreadNum", "20");

			try
			{
				// 输出流
				OutputStream os = new FileOutputStream("properties.properties");
				// 储存属性文件
				properties.store(os, "properties");

				// 关闭流
				os.close();

			} catch (FileNotFoundException e)
			{
				e.printStackTrace();
				JOptionPane.showMessageDialog(null, "找不到属性文件！", "警告", JOptionPane.WARNING_MESSAGE);
				return false;
			} catch (IOException e)
			{
				e.printStackTrace();
				JOptionPane.showMessageDialog(null, "储存属性文件失败！", "错误", JOptionPane.ERROR_MESSAGE);
				return false;
			}
		}

		// 返回
		return file.exists();
	}

	/**
	 * 加载属性
	 */
	private static void loadProperties()
	{

		// 创建一个属性配置对象
		Properties properties = new Properties();

		try
		{
			// 输入流
			InputStream is = new FileInputStream("properties.properties");
			// 导入输入流
			properties.load(is);

			// 读取属性
			downloadDir = properties.getProperty("downloadDir");
			blockSize = Integer.parseInt(properties.getProperty("blockSize"));
			minThreadNum = Integer.parseInt(properties.getProperty("minThreadNum"));
			maxThreadNum = Integer.parseInt(properties.getProperty("maxThreadNum"));

			// 关闭流
			is.close();

		} catch (FileNotFoundException ffe)
		{
			// 找不到属性文件
			ffe.printStackTrace();
			JOptionPane.showMessageDialog(null, "找不到属性文件！", "警告", JOptionPane.WARNING_MESSAGE);
			return;
		} catch (IOException e)
		{
			// 读取属性文件失败
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "读取属性文件失败！", "错误", JOptionPane.ERROR_MESSAGE);
			return;
		}
	}
}
