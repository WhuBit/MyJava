import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/*
 * ʹ��ObjectOutputStream��ObjectInputStreamʵ�ֶ�������л��ͷ����л�
 */
public class ObjectSeriaTest {

	public static void main(String[] args) throws IOException, ClassNotFoundException {
		File file=new File("ost.txt");
		if(!file.exists())
			file.createNewFile();
		
		ObjectOutputStream oos=new ObjectOutputStream(new FileOutputStream(file));
//		Student stu=new Student("0063","С��",18);
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
		 * ���е�û��ʵ�����л��ӿڵĸ���Ĺ��캯�����ᱻ��ʽ����
		 * ʵ�������л��ӿڵĸ����ѱ�д���ļ����ڷ����л�ʱ��ʽ����
		 */
		ois.close();
	}

}
