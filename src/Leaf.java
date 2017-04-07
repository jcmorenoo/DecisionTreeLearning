import java.util.ArrayList;

public class Leaf implements Node  {
	private String className;
	private double probability;
	
	public Leaf(String className, double probability){
		this.className = className;
		this.probability = probability;
	}
	

	@Override
	public void report(String indent) {
		// TODO Auto-generated method stub
		
		System.out.format("%sClass %s, prob=%4.2f\n",
				indent, className, this.probability);
		
	}
	
	public String getClassName(){
		return this.className;
	}

	@Override
	public Node getLeft() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Node getRight() {
		// TODO Auto-generated method stub
		return null;
	}


}
