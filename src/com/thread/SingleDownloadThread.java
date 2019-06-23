package com.thread;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import javax.swing.JOptionPane;
import javax.swing.JTextArea;

/**
 * 单个下载线程类
 * 
 * @author ordinary-student
 *
 */
public class SingleDownloadThread extends Thread
{
	// 链接
	private URL url;
	// 索引-第几块
	private int index;
	// 块的大小
	private int blockSize;
	// 块数量
	private int blockNum;
	// 文件大小
	private int fileSize;
	// 文件名
	private String fileName;
	// 信息输出区
	private JTextArea outputTextArea;
	// 文件临时存放目录
	private File tempDir;

	/*
	 * 构造方法
	 */
	public SingleDownloadThread(JTextArea textPane, URL url, int index, int blockSize, int blockNum, int fileSize,
			String fileName, File tempDir)
	{
		this.outputTextArea = textPane;
		this.url = url;
		this.index = index;
		this.blockSize = blockSize;
		this.blockNum = blockNum;
		this.fileSize = fileSize;
		this.fileName = fileName;
		this.tempDir = tempDir;
	}

	@Override
	public void run()
	{
		// 当文件夹不存在时，则新建
		if (!tempDir.exists())
		{
			tempDir.mkdirs();
		}

		// 输出信息
		outputTextArea.append(">正在下载第" + (index + 1) + "块文件\r\n");

		try
		{
			// 重新获取连接
			URLConnection conn = url.openConnection();
			// 重新获取输入流
			InputStream in = conn.getInputStream();
			// 文件输出流
			FileOutputStream fos = new FileOutputStream(new File(tempDir, fileName + "_" + (index + 1)));

			// 定义起始和结束点
			int beginPoint = 0, endPoint = 0;

			// 起点
			beginPoint = index * blockSize;

			// 判断结束点
			if (index < blockNum - 1)
			{
				endPoint = beginPoint + blockSize;
			} else
			{
				// 如果是最后一块，结束点为文件末尾
				endPoint = fileSize;
			}

			// 跳过 beginPoint个字节进行读取
			in.skip(beginPoint);

			// 缓冲大小
			byte[] buffer = new byte[1024];

			// 读写数量
			int count;
			// 定义当前下载进度
			int process = beginPoint;
			// 当进度未到达结束字节数时，继续
			while (process < endPoint)
			{
				// 读取数据
				count = in.read(buffer);
				// 判断是否读到最后一块
				if (process + count >= endPoint)
				{
					count = endPoint - process;
					process = endPoint;
				} else
				{
					// 计算当前进度
					process = process + count;
				}

				// 保存文件流
				fos.write(buffer, 0, count);
			}

			// 关闭流
			fos.close();
			in.close();

		} catch (Exception e)
		{
			e.printStackTrace();
			JOptionPane.showMessageDialog(outputTextArea, "下载线程出错！", "错误", JOptionPane.ERROR_MESSAGE);
			return;
		}
	}

}
