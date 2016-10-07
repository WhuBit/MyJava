import java.io.Serializable;

/*
 * 对象的序列化和反序列化
 * 2016/10/7
 * 寝室
 * 对象的序列化需要实现Serializable
 */
public class Student {
	private String name;
	private int age;
	private transient String stuNum;
	/*
	 * 使用transient关键字让该元素避免被JVM默认序列化，
	 * 不过可以自己写readObject和writeObject方法实现序列化
	 */
	
	public Student(){
		System.out.println("我是Student");
	}
	
	public Student(String stuNum,String name,int age){
		this.stuNum=stuNum;
		this.name=name;
		this.age=age;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getStuNum() {
		return stuNum;
	}

	public void setStuNum(String stuNum) {
		this.stuNum = stuNum;
	}

	@Override
	public String toString() {
		return "Student [name=" + name + ", age=" + age + ", stuNum=" + stuNum + "]";
	}
	
}


class StuChildren extends Student implements Serializable{

	public StuChildren(){
		System.out.println("我是stuChildren");
	}
}

class StuGrandchildren extends StuChildren{
	public StuGrandchildren(){
		System.out.println("我是StuGrandchildren");
	}
}
