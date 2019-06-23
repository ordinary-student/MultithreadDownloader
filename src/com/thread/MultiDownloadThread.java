package com.thread;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JOptionPane;
import javax.swing.JTextArea;

import com.util.PropertiesUtil;

/**
 * 多线程下载类
 * 
 * @author ordinary-student
 *
 */
public class MultiDownloadThread
{
	private JTextArea outputTextArea;
	private File downloadDir;
	private File tempDir;

	/*
	 * 构造方法
	 */
	public MultiDownloadThread(JTextArea outputTextArea)
	{
		this.outputTextArea = outputTextArea;
		// 准备工作
		prepare();
	}

	/*
	 * 准备工作
	 */
	private void prepare()
	{
		try
		{
			// 判断
			if (PropertiesUtil.downloadDir != null)
			{
				// 下载文件存放目录
				downloadDir = new File(PropertiesUtil.downloadDir);
			} else
			{
				// 默认下载文件存放目录
				downloadDir = new File("C:/download/");
			}

			// 当下载目录不存在时，则新建
			if (!downloadDir.exists())
			{
				downloadDir.mkdirs();
			}

			// 临时文件夹
			tempDir = new File(downloadDir.getAbsolutePath() + "/temp/");
			// 当临时文件夹不存在时，则新建
			if (!tempDir.exists())
			{
				tempDir.mkdirs();
			}
		} catch (NullPointerException e)
		{
			e.printStackTrace();
			JOptionPane.showMessageDialog(outputTextArea, "找不到文件路径！", "错误", JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * 根据下载链接去下载
	 * 
	 * @param url_str
	 * @throws Exception
	 */
	public void download(String url_str) throws Exception
	{
		// 开始时间
		long beginTime = new Date().getTime();

		// 封装
		URL url = new URL(url_str);

		// 获取连接
		URLConnection connection = url.openConnection();

		// 获取文件
		String fileName = url.getFile();

		// 获取文件名
		fileName = fileName.substring(fileName.lastIndexOf("/"));

		// 开始下载
		outputTextArea.append("<<<开始下载 " + getCurrentTime() + ">>>\r\n");
		outputTextArea.append("链接：" + url_str + "\r\n");
		outputTextArea.append("主机：" + url.getHost() + "\r\n");
		outputTextArea.append("端口：" + url.getPort() + "\r\n");
		outputTextArea.append("协议：" + url.getProtocol() + "\r\n");

		// 获取文件大小
		int fileSize = connection.getContentLength();
		outputTextArea.append("文件大小：" + fileSize + "字节\r\n");

		// 分块大小
		int fileBlockSize = PropertiesUtil.blockSize;
		// 分块大小
		int blockSize = fileBlockSize * 1024 * 1024;
		// 分块数量
		int blockNum = fileSize / blockSize;
		// 不能整分，则分块数加一
		if ((fileSize % blockSize) != 0)
		{
			blockNum = blockNum + 1;
		}

		// 获取最大线程属性值
		int maxThreadNum = PropertiesUtil.maxThreadNum;
		// 判断
		if (blockNum > maxThreadNum)
		{
			// 重新设置
			// TODO...
			JOptionPane.showMessageDialog(outputTextArea, "超出最大线程数量！请把文件分块大小调大一点！");
			return;
		}

		outputTextArea.append("分块数/线程数：" + blockNum + "\r\n");

		// 创建线程组
		Thread[] threads = new Thread[blockNum];
		for (int i = 0; i < blockNum; i++)
		{
			// 创建单个线程
			threads[i] = new SingleDownloadThread(outputTextArea, url, i, blockSize, blockNum, fileSize, fileName,
					tempDir);
			threads[i].start();
		}

		// 当所有线程都结束时才开始文件的合并
		for (Thread t : threads)
		{
			t.join();
		}

		// 合并文件
		mergeFile(blockNum, fileName);
		// 合并完成
		outputTextArea.append("合并完成！\r\n");
		// 删除临时文件
		tempDir.delete();

		// 计算用时
		long endTime = new Date().getTime();
		long seconds = (endTime - beginTime) / 1000;
		long minutes = seconds / 60;
		long second = seconds % 60;

		// 下载完成
		outputTextArea.append("用时：" + minutes + "分" + second + "秒\r\n");
		outputTextArea.append("<<<下载完成！ " + getCurrentTime() + ">>>\r\n");
		outputTextArea.append("\r\n");
	}

	/**
	 * 合并文件
	 * 
	 * @param blockNum分块数量
	 * @param fileName文件名
	 */
	private void mergeFile(int blockNum, String fileName)
	{
		try
		{
			outputTextArea.append("正在合并文件...\r\n");
			// 定义文件输出流
			FileOutputStream fos = new FileOutputStream(downloadDir.getAbsolutePath() + "/" + fileName);
			// 遍历读分块文件
			for (int i = 0; i < blockNum; i++)
			{
				// 读分块文件
				FileInputStream fis = new FileInputStream(tempDir.getAbsolutePath() + "/" + fileName + "_" + (i + 1));
				byte[] buffer = new byte[1024];
				int count;
				while ((count = fis.read(buffer)) > 0)
				{
					// 写入一个文件
					fos.write(buffer, 0, count);
				}

				// 关闭流
				fis.close();
			}

			// 关闭流
			fos.close();

		} catch (Exception e)
		{
			e.printStackTrace();
			JOptionPane.showMessageDialog(outputTextArea, "合并文件出错！", "错误", JOptionPane.ERROR_MESSAGE);
			return;
		}
	}

	/**
	 * 获取当前时间
	 * 
	 * @return 当前时间
	 */
	private String getCurrentTime()
	{
		// 格式
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		// 返回
		return df.format(System.currentTimeMillis());
	}
}