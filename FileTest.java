import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.util.RandomAccess;

public class FileTest {
	/*
	 * Java IO流
	 * 2016/10/6 
	 * 寝室
	 * 实现功能：利用File类、RandomAccessFile类读写XML文件
	 */

	// 遍历文件夹下所有文件
	public static void fileList(File dir) {
		if (!dir.exists())
			System.out.println("文件不存在");
		else {
			File[] files = dir.listFiles();
			if (files != null && files.length > 0) {
				for (File file : files) {
					if (file.isDirectory())
						// 递归
						fileList(file);
					else
						System.out.println(file);
				}
			}
		}
	}

	/*
	 * File类只能用于获取文件信息。不能对文件内容进行操作
	 */
	public static void rafileTest(File file) throws IOException {
		/*
		 * RandomAccessFile既可以读文件，也可以写文件。可以访问文件的任意位置
		 */
		if (file.exists())
			file.delete();
		if (!file.exists())
			file.createNewFile();
		RandomAccessFile books = new RandomAccessFile(file, "rw");
		System.out.println("当前指针所在位置：" + books.getFilePointer());// 输出指针位置

		// 写入文件，这样会重写原文件内容

		// books.writeChars("<?xml version=\"1.0\"
		// encoding=\"UTF-8\"?>");使用该方法写入会在每个字符间产生空格
		// 原因分析：将字符串的各字符按照unicode编码，以大端法写入文件。而Windows平台上默认的文件编码方式是小端法

		// books.writeUTF("<?xml version=\"1.0\"
		// encoding=\"UTF-8\"?>");该方法开始的地方出现小方块和&
		// 原因分析：会先写入00 03表示其后要写入的字节

		books.write("<?xml version=\"1.0\" encoding=\"GBK\"?>".getBytes());// 成功写入

		// 读取文件时，先把指针移到开头
		books.seek(0);
		byte[] buf = new byte[(int) books.length()];
		books.read(buf);
		System.out.println(new String(buf));
		System.out.println();

		// ！！对文件的操作，最后一定要关闭
		books.close();
	}

	// 用字节流FileInputStream实现对文件逐个字节读取并以16进制输出
	public static void fisTest1(File file) throws IOException {
		System.out.println("==============fisTest1开始执行====================");
		double start = System.currentTimeMillis();
		FileInputStream fis = new FileInputStream(file);
		int b;
		int i = 1;
		while ((b = fis.read()) != -1) {
			if (b <= 0xf)
				System.out.print("0");
			System.out.print(Integer.toHexString(b) + " ");
			if (i++ % 10 == 0)
				System.out.println();
		}
		fis.close();
		System.out.println();
		double end = System.currentTimeMillis();
		System.out.println("=========fisTest1执行完毕，用时：" + (end - start) + "毫秒===========");
		System.out.println();
	}

	// 用字节流FileInputStream实现对文件批量读取并以16进制输出
	public static void fisTest2(File file) throws IOException {
		System.out.println("==============fisTest2开始执行====================");
		double start = System.currentTimeMillis();
		FileInputStream fis = new FileInputStream(file);
		byte[] b = new byte[8 * 1024];// 可以一次性读完
		int len;// 该方法返回读到字节的个数
		int j = 1;
		while ((len = fis.read(b, 0, b.length)) != -1) {
			for (int i = 0; i < len; i++) {
				if (b[i] < 0xf)
					System.out.print("0");
				System.out.print(Integer.toHexString(b[i] & 0xff) + " ");
				if (j++ % 10 == 0)
					System.out.println();
			}
		}
		fis.close();
		System.out.println();
		double end = System.currentTimeMillis();
		System.out.println("=========fisTest2执行完毕，用时：" + (end - start) + "毫秒===========");
		System.out.println();
	}

	// 用字节流FileOutputStream向文件当中输入数据
	public static void fosTest(File file) throws IOException {
		FileOutputStream fos = new FileOutputStream(file, true);// 第二个参数true/false标识是否向文件中追加内容
		byte[] b = "\n  <bookstore>\n    <book	id=\"1\">".getBytes("GBK");
		fos.write(b);
		fos.flush();// 刷新缓冲区
		b = "\n		<auther>乔治马丁</auther>\n		<year>2014</year>\n		<price>89</price>\n    </book>".getBytes("GBK");
		fos.write(b);
		fos.flush();// 刷新缓冲区
		fos.close();
	}

	// 用字符流 DataOutPutStream向文件中写入数据
	/*
	 * 其中的writeUTF方法以UTF-8的方式写入 writeChars方法以UTF-16be方法写入
	 */
	public static void dosTest(File file) throws IOException {
		DataOutputStream dos = new DataOutputStream(new FileOutputStream(file, true));
		String str = "\n    <book	id=\"2\">\n		<name>安徒生童话</name>\n		<year>2004</year>\n		<price>77</price>\n		<language>English</language>\n    </book>";
		byte[] b = str.getBytes("GBK");
		dos.write(b);
		dos.close();
	}

	// 用字符流DataInputStream读取文件数据
	@SuppressWarnings("deprecation")
	public static void disTest(File file) throws IOException {
		DataInputStream dis = new DataInputStream(new FileInputStream(file));
		String str;
		while ((str = dis.readLine()) != null)
			System.out.println(new String(str.getBytes("GBK")));// 输出中文不正确，待解决
		dis.close();
	}

	// 用字节缓冲流BufferedInputStream/BufferOutputStream实现文件复制
	public static void biosTest(File file, File dest) throws IOException {
		long start = System.currentTimeMillis();
		if (!dest.exists())
			dest.createNewFile();
		BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
		BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(dest));
		int i;
		while ((i = bis.read()) != -1) {
			bos.write(i);
			bos.flush();
		}
		bis.close();
		bos.close();
		long end = System.currentTimeMillis();
		System.out.println("======文件复制结束，用时" + (end - start) + "毫秒=====");
	}

	// 用字符流InputStreamReader和OutputStreamWriter操纵文件
	public static void iosTest(File file) throws IOException {
		InputStreamReader isr = new InputStreamReader(new FileInputStream(file), "GBK");
		OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(file, true), "GBK");
		String str = "\n  </bookstore>";
		osw.write(str.toCharArray());
		osw.flush();
		char[] c = new char[8 * 1024];
		while (isr.read(c) != -1)
			// System.out.println(new String(c));//！！！最后会打印小方块，待解决
			System.out.println(new String(c).trim());// 解决方法：使用trim方法去除字符串首尾空格（2016/10/7）
		isr.close();
		osw.close();
	}

	// 用字节流FileReader和FileWriter对文件进行读写
	public static void frwTest(File file) throws IOException {
		FileReader fr = new FileReader(file);
		FileWriter fw = new FileWriter(file, true);
		String str = "\n  </bookstore>";
		fw.write(str);// 无法写入
		fw.flush();// 解决方法：加上flush方法，之前写入的缓冲区未满，没有写入
		System.out.println();
		char[] cbuf = new char[8 * 1024];
		int b;
		while ((b = fr.read(cbuf, 0, cbuf.length)) != -1)
			System.out.println(new String(cbuf).trim());
		fw.close();
		fr.close();

	}

	// 用字节缓冲流BufferedReader/BufferedWriter/PrintWriterer过滤器实现文件复制
	public static void brwTest(File file, File dest) throws IOException {
		long start = System.currentTimeMillis();
		if (!dest.exists())
			dest.createNewFile();
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(dest)));
		PrintWriter pw=new PrintWriter(dest);
		String str;
		while ((str=br.readLine()) !=null) {//！！！读取时没有读取换行
//			bw.write(str);
//			bw.newLine();//解决方法：手动换行
//			bw.flush();
			
			pw.println(str);//会自动换行，所以常用BufferedReader和PrintWriter搭配使用
			pw.flush();
		}
		br.close();
		br.close();
		long end = System.currentTimeMillis();
		System.out.println("======文件复制结束，用时" + (end - start) + "毫秒=====");
	}

	public static void main(String[] args) throws IOException {
		// 测试fileList
		File file = new File("E:\\Eclipse Workspace\\Imooc");
		fileList(file);

		File books = new File("books.xml");

		// 测试rafileTest()
		rafileTest(books);

		// 测试fosTest1
		fosTest(books);

		// 测试fisTest1
		fisTest1(books);

		// 测试fisTest2
		fisTest2(books);

		// 测试dosTest
		dosTest(books);

		// 测试disTest
		disTest(books);

		File dst = new File("book_copy.xml");
		// 测试biosTest
		biosTest(books, dst);

		// 测试iosTest
		iosTest(books);

		// 测试frwTest
		// frwTest(new File("book_copy.xml"));//！！！错误：无法输出
		frwTest(dst);
		
		//测试brwTest
		brwTest(books,new File("book_copy1.xml"));
	}

}
