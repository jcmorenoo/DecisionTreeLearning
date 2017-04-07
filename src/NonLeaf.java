import java.util.ArrayList;

public class NonLeaf implements Node {
	private String attName;
	private String value;
	private Node left;
	private Node right;
	
	public NonLeaf(String attName, Node left, Node right){
		this.attName = attName;
		this.left = left;
		this.right = right;

	}
	
	public String getAttName(){
		return this.attName;
	}
	
	public String getValue(){
		return this.value;
	}
	

	@Override
	public void report(String indent) {
		// TODO Auto-generated method stub
		System.out.format("%s%s = True:\n",
				indent, attName);
				left.report(indent+" ");
				System.out.format("%s%s = False:\n",
				indent, attName);
				right.report(indent+" ");
	}

	@Override
	public Node getLeft() {
		// TODO Auto-generated method stub
		return this.left;
	}

	@Override
	public Node getRight() {
		// TODO Auto-generated method stub
		return this.right;
	}

}
