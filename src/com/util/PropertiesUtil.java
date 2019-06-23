package com.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
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
		try
		{
			// 创建一个属性配置对象
			Properties properties = new Properties();
			InputStream is = new FileInputStream("properties.properties");
			// 导入输入流
			properties.load(is);

			// 读取属性
			downloadDir = properties.getProperty("downloadDir");
			blockSize = Integer.parseInt(properties.getProperty("blockSize"));
			minThreadNum = Integer.parseInt(properties.getProperty("minThreadNum"));
			maxThreadNum = Integer.parseInt(properties.getProperty("maxThreadNum"));

		} catch (FileNotFoundException ffe)
		{
			// 找不到属性文件
			ffe.printStackTrace();
			JOptionPane.showMessageDialog(null, "找不到属性文件！", "警告", JOptionPane.WARNING_MESSAGE);

		} catch (IOException e)
		{
			// 读取属性文件失败
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "读取属性文件失败！", "错误", JOptionPane.ERROR_MESSAGE);

		}
	}

}
