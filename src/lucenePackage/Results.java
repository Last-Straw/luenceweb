package lucenePackage;
import java.util.ArrayList;


public class Results {
	public int totalHits=0;
	//array 是数组，arraylist是数组列表 数组列表可以根据自身变化扩大，而数组指定长度后不可以。
	//定义了一个名叫dir的arraylist，可以添加string类型的数据。
	public ArrayList<String> dir=new ArrayList<String>();
	public ArrayList<String> score=new ArrayList<String>();
}
