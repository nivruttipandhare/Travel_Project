package Collections;

public class DifferentData {

	public static void main(String[] args) {
	Object ob[]=new Object[5];
	
		ob[0]=5;
		ob[1]=5.4f;
		ob[2]=false;
		//ob[3]=null;
		ob[3]="ABC";
		ob[4]=new java.util.Date();
		System.out.println("Display the array values:");
		for(int i=0; i<ob.length; i++) {
			System.out.println(ob[i]);
	
		}
	

	}

}
