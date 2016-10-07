import java.io.Serializable;

/*
 * ��������л��ͷ����л�
 * 2016/10/7
 * ����
 * ��������л���Ҫʵ��Serializable
 */
public class Student {
	private String name;
	private int age;
	private transient String stuNum;
	/*
	 * ʹ��transient�ؼ����ø�Ԫ�ر��ⱻJVMĬ�����л���
	 * ���������Լ�дreadObject��writeObject����ʵ�����л�
	 */
	
	public Student(){
		System.out.println("����Student");
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
		System.out.println("����stuChildren");
	}
}

class StuGrandchildren extends StuChildren{
	public StuGrandchildren(){
		System.out.println("����StuGrandchildren");
	}
}
