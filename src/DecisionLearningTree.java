import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class DecisionLearningTree {
	private List<String> categoryNames;
	private int numCategories;
	private List<String> attNames;
	private int numAtts;
	private List<Instance> allInstances;
	private List<Instance> testInstances;
	private String baseCat;
	private double baseProb;
	
	private Node decisionTree;
	
	private static String training;
	private static String test;
	
	
	public DecisionLearningTree(){
		this.categoryNames = new ArrayList<String>();
		this.attNames = new ArrayList<String>();
		this.allInstances = new ArrayList<Instance>();
		this.testInstances = new ArrayList<Instance>();
		
		readDataFile(this.training);
		findMostOccurring();
		List<String> att = new ArrayList<String>();
		
		int count = 0;
		while(count < this.attNames.size()){
			att.add(this.attNames.get(count));
			count = count + 1;
		}
		this.decisionTree = buildTree(this.allInstances, att);
		
		
		readTestFile(this.test);
		
		classifyTest();
		this.decisionTree.report("	");
		
		
		
		
		
		
		
		
	}
	
	private void classifyTest() {
		// TODO Auto-generated method stub
		double total = 0;
		double correct = 0;
		double count1 = 0;
		double[] counter = new double[this.numCategories];
		double[] tally = new double[this.numCategories];
		double[] tallyCorrect = new double[this.numCategories];
		
		int mostProb = 0;
		
		for(Instance in : this.testInstances){
			int decision = classifyInstance(in, this.decisionTree);
			tally[in.getCategory()] = tally[in.getCategory()] + 1;
			counter[decision] = counter[decision] + 1;
			if(decision == in.getCategory()){
				correct = correct + 1;
				tallyCorrect[in.getCategory()] = tallyCorrect[in.getCategory()] + 1;
			}
			
			
			
			total = total + 1;
			
		}
		
		if(counter[1] > counter[0]){
			mostProb = 1;
		}
		String mostOutcome = this.categoryNames.get(mostProb);
		System.out.println("Baseline: " + this.baseCat + "\n Probability: " + this.baseProb);
		System.out.println(this.categoryNames.get(0) + ": " + tallyCorrect[0] + " out of " + tally[0] + " correct");
		System.out.println(this.categoryNames.get(1) + ": " + tallyCorrect[1] + " out of " + tally[1] + " correct");
		
		System.out.println("Most probable outcome: " + mostOutcome + "\n With probability of: " + counter[mostProb]/total);
		
		System.out.println("Accuracy: " + correct/total);
		
		
		
		
	}
	
	private int classifyInstance(Instance instance, Node node){
		if(node instanceof Leaf){
			
			Leaf l = (Leaf)node;
			int category = 0;
			while(category<this.categoryNames.size()){
				if(this.categoryNames.get(category).equalsIgnoreCase(l.getClassName())){
					break;
				}
				category = category + 1;
			}
			
			return category;
		}
		
		NonLeaf n = (NonLeaf)node;
		String attr = n.getAttName();
			
		int i = 0;
		while(i<this.attNames.size()){
			if(attNames.get(i).equalsIgnoreCase(attr)){
				break;
			}
			i = i + 1;
		}
		
		int attrNum = i;
		
		if(instance.getAtt(attrNum)){
			//if true
			return classifyInstance(instance, n.getLeft());
		}
		else{
			//if false
			return classifyInstance(instance, n.getRight());
		}
		
		
	}

	private void findMostOccurring(){
		int[] tally = new int[this.allInstances.size()];
		int count = 0;
		String mostOccurring = null;
		
		for(Instance in : this.allInstances){
			tally[in.getCategory()] = tally[in.getCategory()] + 1;
			
			if(tally[in.getCategory()] > count){
				count = tally[in.getCategory()];
				mostOccurring = this.categoryNames.get(in.getCategory());
			}
		}
		
		double prob = (double)count/(double)this.allInstances.size();
		this.baseProb = prob;
		this.baseCat = mostOccurring;
		
		
	}
	
	private Node buildTree(List<Instance> instances, List<String> attNamesAll){
		List<String> attNamess = new ArrayList<String>();
		for(String s : attNamesAll){
			attNamess.add(s);
		}
		if(instances.isEmpty()){
			//should get the most probable class 
			//should calculate the probablity of the most probable class
			
			return new Leaf(this.baseCat, this.baseProb);
			
		}
		//pure means that all instances have same outcome 
		boolean pure = false;
		
		pure = isPure(instances);
		
		if(pure){
			//then it is pure
			return new Leaf(this.categoryNames.get(instances.get(0).getCategory()),1);
			
		}
		if(attNamess.isEmpty()){
			///might be wrong
			int count1 = 0;
			int count0 = 0;
			
			for(Instance in : instances){
				if(in.getCategory() == 0){
					count0 = count0 + 1;
				}
				else{
					count1 = count1 + 1;
				}
			}
			String att = null;
			double prob = 0;
			
			if(count1 > count0){
				att = categoryNames.get(1);
				prob = (double)count1/(double)instances.size();
				return new Leaf(att, prob);
			}
			else{
				att = categoryNames.get(0);
				prob = (double)count0/(double)instances.size();
				return new Leaf(att, prob);
			}
			
			
			
//			Node n = new Leaf(this.categoryNames.get(instances.get(0).getCategory()), 1);
		}
		else{
			//find best attribute
			
			//for each attribute:
				// // Create the lists to sort each instance into for this
				// attribute
			
				// separate instances into two sets
			
			// compute average purity of each set.
			// if weighted average purity of these sets is best so far
				// Update best data
			// build subtrees using the remaining attributes:
			// left = BuildTree(bestInstsTrue, attributes - bestAtt)
			
			
			String bestAtt = null;
			List<Instance> bestTrueInst = new ArrayList<Instance>();
			List<Instance> bestFalseInst = new ArrayList<Instance>();
			double bestPurity = 1;

			for (String attribute : attNamesAll) {

				// Find the index of the current attribute
				int indexOfAtt = attNames.indexOf(attribute);

				// Create the lists to sort each instance into for this
				// attribute
				List<Instance> trueInst = new ArrayList<Instance>();
				List<Instance> falseInst = new ArrayList<Instance>();

				// separate instances into two sets:
				for (Instance in : instances) {
					// instances for which the attribute is true, and
					if (in.getAtt(indexOfAtt)) {
						trueInst.add(in);
					}
					// instances for which the attribute is false
					else {
						falseInst.add(in);
					}
				}

				// compute average purity of each set.
				double aveAttPurity = findWeightedImpurity(trueInst, falseInst);
				// if weighted average purity of these sets is best so far
				if (aveAttPurity < bestPurity) {
					// Update best data
					bestPurity = aveAttPurity;
					bestAtt = attribute;
					bestTrueInst = trueInst;
					bestFalseInst = falseInst;
				}
			}
			attNamesAll.remove(bestAtt);
			// build subtrees using the remaining attributes:
			// left = BuildTree(bestInstsTrue, attributes - bestAtt)
			List<String> leftAtt = new ArrayList<String>();
			List<String> rightAtt = new ArrayList<String>();
			int count = 0;
			
			while(count<attNamesAll.size()){
				leftAtt.add(attNamesAll.get(count));
				rightAtt.add(attNamesAll.get(count));
				count = count + 1;
			}
			Node leftNode = buildTree(bestTrueInst, leftAtt);
			
			Node rightNode = buildTree(bestFalseInst, rightAtt);

		
			return new NonLeaf(bestAtt, leftNode, rightNode);
			
			
		

			
			
			
		}
	
	
	}
	
	//algorithm to find weighted impurity
	private double findWeightedImpurity(List<Instance> trueInst, List<Instance> falseInst){
		double totalInsts = trueInst.size() + falseInst.size();
		double impurityTrue = findImpurity(trueInst)*(trueInst.size() / totalInsts);
		double impurityFalse = findImpurity(falseInst)
				* (falseInst.size() / totalInsts);
		return impurityTrue + impurityFalse;
	}
	
	//method to find impurity of instances
	
	private double findImpurity(List<Instance> instances){
		
		//found this in the internet because my first implementation wasn't working properly 
		//for finding the correct impurity 
		
		int[] tally = new int[this.numCategories];
		
		for(Instance in : instances){
			tally[in.getCategory()] = tally[in.getCategory()] + 1;
		}
		
		
		double impurity = 1; 
		for (int index = 0; index < numCategories; index++) {
			impurity = impurity * (tally[index] / (double)instances.size());
		}

		return impurity;
		
	}
	
	private boolean isPure(List<Instance> instances) {
		// TODO Auto-generated method stub
		int[] count = new int[this.numCategories];
		for (Instance instance : instances) {
			count[instance.getCategory()] = count[instance.getCategory()] + 1;
		}

		int totalInstances = instances.size();
		double impurity = 1; 
		int i = 0;
		while(i<this.numCategories){
			impurity  = impurity * (count[i] / (double) totalInstances);
			i = i + 1;
		}

		if(impurity == 0){
			return true;
		}
		else{
			return false;
		}
		
	}

	//read test file
	private List<Instance> readTestFile(String fname){
		try {
			Scanner din = new Scanner(new File(fname));
			   
		      categoryNames = new ArrayList<String>();
		      for (Scanner s = new Scanner(din.nextLine()); s.hasNext();) categoryNames.add(s.next());
		      numCategories=categoryNames.size();
		      System.out.println(numCategories +" categories");

		      attNames = new ArrayList<String>();
		      for (Scanner s = new Scanner(din.nextLine()); s.hasNext();) attNames.add(s.next());
		      numAtts = attNames.size();
		      System.out.println(numAtts +" attributes");
		      
		      this.testInstances = readInstances(din);
		      
		      din.close();
		      
		      
			
			////---
			
			
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	private void readDataFile(String fname){
	    /* format of names file:
	     * names of categories, separated by spaces
	     * names of attributes
	     * category followed by true's and false's for each instance
	     * 
	     */
		
	    System.out.println("Reading data from file "+fname);
	    try {
	      Scanner din = new Scanner(new File(fname));
	   
	      categoryNames = new ArrayList<String>();
	      for (Scanner s = new Scanner(din.nextLine()); s.hasNext();) categoryNames.add(s.next());
	      numCategories=categoryNames.size();
	      System.out.println(numCategories +" categories");

	      attNames = new ArrayList<String>();
	      for (Scanner s = new Scanner(din.nextLine()); s.hasNext();) attNames.add(s.next());
	      numAtts = attNames.size();
	      System.out.println(numAtts +" attributes");
	      
	      allInstances = readInstances(din);
	      
	      din.close();
	    }
	   catch (IOException e) {
	     throw new RuntimeException("Data File caused IO exception");
	   }
	  }


	  private List<Instance> readInstances(Scanner din){
	    /* instance = classname and space separated attribute values */
	    List<Instance> instances = new ArrayList<Instance>();
	    String ln;
	    while (din.hasNext()){ 
	      Scanner line = new Scanner(din.nextLine());
	      instances.add(new Instance(categoryNames.indexOf(line.next()),line));
	    }
	    System.out.println("Read " + instances.size()+" instances");
	    return instances;
	  }


	 
	  private class Instance {

	    private int category;
	    private List<Boolean> vals;

	    public Instance(int cat, Scanner s){
	      category = cat;
	      vals = new ArrayList<Boolean>();
	      while (s.hasNextBoolean()) vals.add(s.nextBoolean());
	    }

	    public boolean getAtt(int index){
	      return vals.get(index);
	    }

	    public int getCategory(){
	      return category;
	    }
	    
	    public String toString(){
	      StringBuilder ans = new StringBuilder(categoryNames.get(category));
	      ans.append(" ");
	      for (Boolean val : vals)
		ans.append(val?"true  ":"false ");
	      return ans.toString();
	    }
	    
	  }
	
	public static void main(String[] args){
		
		if(args.length == 2){
			String training = "data/" + args[0];
			String test = "data/" + args[1];
			DecisionLearningTree.training = training;
			DecisionLearningTree.test = test;
			DecisionLearningTree dt = new DecisionLearningTree();
			
			
		}
		else{
			System.out.println("Ensure proper arguments");
		}
		
		
	}
}
