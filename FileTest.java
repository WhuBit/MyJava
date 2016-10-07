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
	 * Java IO��
	 * 2016/10/6 
	 * ����
	 * ʵ�ֹ��ܣ�����File�ࡢRandomAccessFile���дXML�ļ�
	 */

	// �����ļ����������ļ�
	public static void fileList(File dir) {
		if (!dir.exists())
			System.out.println("�ļ�������");
		else {
			File[] files = dir.listFiles();
			if (files != null && files.length > 0) {
				for (File file : files) {
					if (file.isDirectory())
						// �ݹ�
						fileList(file);
					else
						System.out.println(file);
				}
			}
		}
	}

	/*
	 * File��ֻ�����ڻ�ȡ�ļ���Ϣ�����ܶ��ļ����ݽ��в���
	 */
	public static void rafileTest(File file) throws IOException {
		/*
		 * RandomAccessFile�ȿ��Զ��ļ���Ҳ����д�ļ������Է����ļ�������λ��
		 */
		if (file.exists())
			file.delete();
		if (!file.exists())
			file.createNewFile();
		RandomAccessFile books = new RandomAccessFile(file, "rw");
		System.out.println("��ǰָ������λ�ã�" + books.getFilePointer());// ���ָ��λ��

		// д���ļ�����������дԭ�ļ�����

		// books.writeChars("<?xml version=\"1.0\"
		// encoding=\"UTF-8\"?>");ʹ�ø÷���д�����ÿ���ַ�������ո�
		// ԭ����������ַ����ĸ��ַ�����unicode���룬�Դ�˷�д���ļ�����Windowsƽ̨��Ĭ�ϵ��ļ����뷽ʽ��С�˷�

		// books.writeUTF("<?xml version=\"1.0\"
		// encoding=\"UTF-8\"?>");�÷�����ʼ�ĵط�����С�����&
		// ԭ�����������д��00 03��ʾ���Ҫд����ֽ�

		books.write("<?xml version=\"1.0\" encoding=\"GBK\"?>".getBytes());// �ɹ�д��

		// ��ȡ�ļ�ʱ���Ȱ�ָ���Ƶ���ͷ
		books.seek(0);
		byte[] buf = new byte[(int) books.length()];
		books.read(buf);
		System.out.println(new String(buf));
		System.out.println();

		// �������ļ��Ĳ��������һ��Ҫ�ر�
		books.close();
	}

	// ���ֽ���FileInputStreamʵ�ֶ��ļ�����ֽڶ�ȡ����16�������
	public static void fisTest1(File file) throws IOException {
		System.out.println("==============fisTest1��ʼִ��====================");
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
		System.out.println("=========fisTest1ִ����ϣ���ʱ��" + (end - start) + "����===========");
		System.out.println();
	}

	// ���ֽ���FileInputStreamʵ�ֶ��ļ�������ȡ����16�������
	public static void fisTest2(File file) throws IOException {
		System.out.println("==============fisTest2��ʼִ��====================");
		double start = System.currentTimeMillis();
		FileInputStream fis = new FileInputStream(file);
		byte[] b = new byte[8 * 1024];// ����һ���Զ���
		int len;// �÷������ض����ֽڵĸ���
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
		System.out.println("=========fisTest2ִ����ϣ���ʱ��" + (end - start) + "����===========");
		System.out.println();
	}

	// ���ֽ���FileOutputStream���ļ�������������
	public static void fosTest(File file) throws IOException {
		FileOutputStream fos = new FileOutputStream(file, true);// �ڶ�������true/false��ʶ�Ƿ����ļ���׷������
		byte[] b = "\n  <bookstore>\n    <book	id=\"1\">".getBytes("GBK");
		fos.write(b);
		fos.flush();// ˢ�»�����
		b = "\n		<auther>������</auther>\n		<year>2014</year>\n		<price>89</price>\n    </book>".getBytes("GBK");
		fos.write(b);
		fos.flush();// ˢ�»�����
		fos.close();
	}

	// ���ַ��� DataOutPutStream���ļ���д������
	/*
	 * ���е�writeUTF������UTF-8�ķ�ʽд�� writeChars������UTF-16be����д��
	 */
	public static void dosTest(File file) throws IOException {
		DataOutputStream dos = new DataOutputStream(new FileOutputStream(file, true));
		String str = "\n    <book	id=\"2\">\n		<name>��ͽ��ͯ��</name>\n		<year>2004</year>\n		<price>77</price>\n		<language>English</language>\n    </book>";
		byte[] b = str.getBytes("GBK");
		dos.write(b);
		dos.close();
	}

	// ���ַ���DataInputStream��ȡ�ļ�����
	@SuppressWarnings("deprecation")
	public static void disTest(File file) throws IOException {
		DataInputStream dis = new DataInputStream(new FileInputStream(file));
		String str;
		while ((str = dis.readLine()) != null)
			System.out.println(new String(str.getBytes("GBK")));// ������Ĳ���ȷ�������
		dis.close();
	}

	// ���ֽڻ�����BufferedInputStream/BufferOutputStreamʵ���ļ�����
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
		System.out.println("======�ļ����ƽ�������ʱ" + (end - start) + "����=====");
	}

	// ���ַ���InputStreamReader��OutputStreamWriter�����ļ�
	public static void iosTest(File file) throws IOException {
		InputStreamReader isr = new InputStreamReader(new FileInputStream(file), "GBK");
		OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(file, true), "GBK");
		String str = "\n  </bookstore>";
		osw.write(str.toCharArray());
		osw.flush();
		char[] c = new char[8 * 1024];
		while (isr.read(c) != -1)
			// System.out.println(new String(c));//�����������ӡС���飬�����
			System.out.println(new String(c).trim());// ���������ʹ��trim����ȥ���ַ�����β�ո�2016/10/7��
		isr.close();
		osw.close();
	}

	// ���ֽ���FileReader��FileWriter���ļ����ж�д
	public static void frwTest(File file) throws IOException {
		FileReader fr = new FileReader(file);
		FileWriter fw = new FileWriter(file, true);
		String str = "\n  </bookstore>";
		fw.write(str);// �޷�д��
		fw.flush();// �������������flush������֮ǰд��Ļ�����δ����û��д��
		System.out.println();
		char[] cbuf = new char[8 * 1024];
		int b;
		while ((b = fr.read(cbuf, 0, cbuf.length)) != -1)
			System.out.println(new String(cbuf).trim());
		fw.close();
		fr.close();

	}

	// ���ֽڻ�����BufferedReader/BufferedWriter/PrintWriterer������ʵ���ļ�����
	public static void brwTest(File file, File dest) throws IOException {
		long start = System.currentTimeMillis();
		if (!dest.exists())
			dest.createNewFile();
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(dest)));
		PrintWriter pw=new PrintWriter(dest);
		String str;
		while ((str=br.readLine()) !=null) {//��������ȡʱû�ж�ȡ����
//			bw.write(str);
//			bw.newLine();//����������ֶ�����
//			bw.flush();
			
			pw.println(str);//���Զ����У����Գ���BufferedReader��PrintWriter����ʹ��
			pw.flush();
		}
		br.close();
		br.close();
		long end = System.currentTimeMillis();
		System.out.println("======�ļ����ƽ�������ʱ" + (end - start) + "����=====");
	}

	public static void main(String[] args) throws IOException {
		// ����fileList
		File file = new File("E:\\Eclipse Workspace\\Imooc");
		fileList(file);

		File books = new File("books.xml");

		// ����rafileTest()
		rafileTest(books);

		// ����fosTest1
		fosTest(books);

		// ����fisTest1
		fisTest1(books);

		// ����fisTest2
		fisTest2(books);

		// ����dosTest
		dosTest(books);

		// ����disTest
		disTest(books);

		File dst = new File("book_copy.xml");
		// ����biosTest
		biosTest(books, dst);

		// ����iosTest
		iosTest(books);

		// ����frwTest
		// frwTest(new File("book_copy.xml"));//�����������޷����
		frwTest(dst);
		
		//����brwTest
		brwTest(books,new File("book_copy1.xml"));
	}

}
