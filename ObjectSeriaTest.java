import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/*
 * 使用ObjectOutputStream和ObjectInputStream实现对象的序列化和反序列化
 */
public class ObjectSeriaTest {

	public static void main(String[] args) throws IOException, ClassNotFoundException {
		File file=new File("ost.txt");
		if(!file.exists())
			file.createNewFile();
		
		ObjectOutputStream oos=new ObjectOutputStream(new FileOutputStream(file));
//		Student stu=new Student("0063","小茗",18);
//		oos.writeObject(stu);
		StuChildren sc=new StuChildren();
		oos.writeObject(sc);
		oos.flush();
		oos.close();
		
		ObjectInputStream ois=new ObjectInputStream(new FileInputStream(file));
//		Student newStu=(Student) ois.readObject();
//		System.out.println(newStu.toString());
		StuChildren newSc=(StuChildren) ois.readObject();
		/*
		 * 所有的没有实现序列化接口的父类的构造函数都会被显式调用
		 * 实现了序列化接口的父类已被写入文件，在反序列化时隐式调用
		 */
		ois.close();
	}

}
