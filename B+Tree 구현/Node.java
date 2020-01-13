package bptree;

import java.util.*;

class Node {
	protected int m; // number of keys
	protected int degree; //The degree of the BplusTree
	protected Node rightMostChild; // rightmost child
	protected Node parent; //The information of parent Node
	protected int NodeType; //Inner:1 Leaf:2 Default : 0
	protected TreeMap<Integer, Node> Innerp; //pair of key,left child node in InnerNode
	protected TreeMap<Integer, Integer> Leafp; //pair of key,value in LeafNode
	
	
	public Node(int degree) {
		this.m =0;
		this.degree = degree;
		this.rightMostChild = null;
		this.parent = null;
		this.NodeType =0;
		Leafp= new TreeMap<Integer,Integer>();
		Innerp= new TreeMap<Integer , Node>();	

	}
	
	
	public boolean isOverFlow(){
		return this.getNumberOfKeys()== this.getDegree();
	} //Check overflow
	

	public boolean isUnderFlow() {
		
		return this.getNumberOfKeys() < this.getDegree()/2;
	} //Check underflow
	
	
	//Overloaded insert function
	
	public void insertPair(int key, int value) {// using Leafp
		Leafp.put(key, value);
		this.m++;
	}
	
	public void insertPair(Integer key,Node node) { //using Innerp when insert LeafNode into InnerNode
	
	
		
		if(key ==null && this.rightMostChild==null) { //Ű���� null �϶� ���� �����ʿ� �ϴ� �־�д�.
			this.rightMostChild = node;
		}
		
		else if (node == null) { //Ű���� ������ node�� null�϶� rightMostChild�� �־�� �� �ٽ� �ִ´�.
			Innerp.put(key, rightMostChild);
		}
		else if(!this.isOverFlow()){
			
			Innerp.put(key, node);
		}
		
		this.m++;
		
		
	}
	
	public void deletePairInInner(Integer key)
	{
		this.Innerp.remove(key);
		this.m--;
	}
	public void deletePairInLeaf(Integer key) {
		this.Leafp.remove(key);
		this.m--;
	}

	
	//getter, setter
	public int getNumberOfKeys() { //identify the number of keys in the node
		return m;
	}

	public void setNumberOfKeys(int m) { //set the the number of keys in the node
		this.m = m;
	}
	
	public int getDegree() {
		return degree;
	}
	


	public void setDegree(int degree) {
		this.degree = degree;
	}


	public Node getRightMostChild() {
		return rightMostChild;
	}


	public void setRightMostChild(Node rightMostChild) {
		this.rightMostChild = rightMostChild;
	}


	public Node getParent() {
		return parent;
	}


	public void setParent(Node parent) {
		this.parent = parent;
	}

	public int getNodeType() {
		return NodeType;
	}

	public void setNodeType(int nodeType) {
		NodeType = nodeType;
	}
	
	
	public void printLeafNode() //�� ����� ��� ������ ����Ʈ
	{
		for(Integer keys : Leafp.keySet())
		{
			System.out.println(keys.intValue() + ","+ Leafp.get(keys.intValue()));
		}
	
	}
	public void printInnerNode() {
		TreeMap<Integer,Node> cmp = new TreeMap<Integer,Node>(); //�ӽ� TreeMap
		cmp = (TreeMap<Integer, Node>) Innerp.clone(); //�ӽ� TreeMap �� this.p ����
		
		System.out.print(cmp.firstKey());
		cmp.remove(cmp.firstKey());
		
		for(Integer keys : cmp.keySet())
		{
			System.out.print(",");
			System.out.print(keys.intValue());
		}
		System.out.println();
	}
}
