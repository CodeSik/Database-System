package bptree;

import java.util.HashMap;
import java.util.TreeMap;
import java.util.Iterator;
import java.util.Set;

public class InnerNode extends Node {
	
	
	public InnerNode(int degree) {
		super(degree);
		super.NodeType = 1;
		Innerp= new TreeMap<Integer , Node>();	
	}
	
	
	public int getNodeType() {
		return NodeType;
	}


	public void setNodeType(int nodeType) {
		NodeType = nodeType;
	}


	public int searchIndexInNodeByKey(int key) {
		int index=0;
		
		for(Integer keys : Innerp.keySet()) {
			if(keys.intValue() == key) {
				return index;
			}
			index++;
		}
		return index;
	
	}
	
	public int searchKeyInNodeByIndex(int index) {
		int i=0;
		for(Integer keys : Innerp.keySet()) {
			if(i == index) {
				return keys.intValue();
			}
			i++;
		}
		
		return 0;
	}
	
	public void deletePairAndMakeLeftChildInInner() {
		int index=0;
		TreeMap<Integer,Node> cmp = new TreeMap<Integer,Node>(); //임시 TreeMap
		cmp = (TreeMap<Integer, Node>) Innerp.clone(); //임시 TreeMap 에 this.p 저장
		this.Innerp.clear();
		this.setNumberOfKeys(0);
		for(Integer keys : cmp.keySet()){
			if(index == degree/2) // n/2전까지만 삽입
			{
				break;
			}
			
			this.insertPair(keys.intValue(), cmp.get(keys.intValue()));
			
			index++;
		}
		//현재 노드에 n/2로 나눴을때 왼쪽부분만 저장한다.
		
		
		
	}
	
	public void deletePairAndMakeRightChildInInner() {
		int index=0;
		TreeMap<Integer,Node> cmp = new TreeMap<Integer,Node>(); //임시 TreeMap
		cmp = (TreeMap<Integer, Node>) Innerp.clone(); //임시 TreeMap 에 this.p 저장
		
		this.Innerp.clear();
		this.setNumberOfKeys(0);
		for(Integer keys : cmp.keySet()){
			if(index >degree/2) 
			{
				this.insertPair(keys.intValue(), cmp.get(keys.intValue()));
				
			}
			else 
		
				index++;
			
		}
		
		cmp = (TreeMap<Integer, Node>) this.Innerp.clone();
		for(Integer keys : cmp.keySet()) {
			this.Innerp.get(keys.intValue()).setParent(this);
		}
		this.rightMostChild.setParent(this);
	
	}
	
	public void splitInInner() {
		 int midkey = this.searchKeyInNodeByIndex(this.degree/2); //mid key값 저장
	     TreeMap<Integer,Node> cmp = new TreeMap<Integer,Node>(); //임시 TreeMap
	     cmp = (TreeMap<Integer, Node>) this.Innerp.clone(); //임시 TreeMap 에 this.p 저장
	      
	     InnerNode rightinner = new InnerNode(this.degree);//오른쪽노드
	     rightinner.Innerp = cmp;
	      
	     rightinner.setRightMostChild(this.rightMostChild); 
	     rightinner.deletePairAndMakeRightChildInInner(); 
	     this.setRightMostChild(this.Innerp.get(midkey));
	     this.deletePairAndMakeLeftChildInInner();// 왼쪽부분.
	      
	      if(this.getParent() == null ) { //루트일때
	         InnerNode parent = new InnerNode(this.degree); //부모노드 만들어줌
	         parent.insertPair(midkey, this); //첫 p에 midkey와 왼쪽 리프노드
	         parent.setRightMostChild(rightinner);
	         this.setParent(parent); rightinner.setParent(parent);
	         
	      }
	      else if (this.getParent() != null) { //루트아닐때      
	         rightinner.setParent(this.getParent());
	
	         if(getParent().Innerp.lastKey()<midkey) {
	        	 this.getParent().insertPair(midkey,this);
	        	 this.getParent().setRightMostChild(rightinner);
	         }
	         else {
	        	 
	        	 this.getParent().Innerp.replace(this.getParent().Innerp.higherKey(midkey), rightinner);
	        	 this.getParent().insertPair(midkey,this);
	         }
	         
	           
		      }
	      else {
	      System.out.println("Error");
	      }
	      
	
	}

	
	
	public void mergeInInner(int key, boolean rootChange) {
	
		InnerNode mergeNode;
		
		if(this.getParent().Innerp.firstKey().intValue() > this.Innerp.lastKey().intValue()) {
			//제일 왼쪽일때
			
			mergeNode = (InnerNode) this.getParent().Innerp.get(this.getParent().Innerp.higherKey((this.getParent().Innerp.firstKey().intValue())));
			this.Innerp.clear();
			
			mergeNode.insertPair(this.getParent().Innerp.firstKey(), this.getRightMostChild());
			this.getParent().deletePairInInner(this.getParent().Innerp.firstKey());
			
		
		}
		else if(this.getParent().Innerp.lastKey().intValue() < this.Innerp.firstKey().intValue()) {
			//제일오른쪽일때
			
			 mergeNode = (InnerNode) this.getParent().Innerp.get(this.getParent().Innerp.floorKey(this.Innerp.firstKey().intValue()));
			this.Innerp.clear();
			
			mergeNode.insertPair(this.getParent().Innerp.lastKey(), mergeNode.getRightMostChild());
			this.getParent().deletePairInInner(this.getParent().Innerp.lastKey());
			mergeNode.setRightMostChild(this.getRightMostChild());
			this.getParent().setRightMostChild(mergeNode);
			
		}
		else {
		
			mergeNode = (InnerNode) this.getParent().Innerp.get(this.getParent().Innerp.floorKey(this.Innerp.firstKey().intValue()));
			
			this.Innerp.clear();
			if(rootChange) {
				mergeNode.insertPair(this.getParent().Innerp.ceilingKey(mergeNode.Innerp.lastKey()), mergeNode.getRightMostChild());
				this.getParent().deletePairInInner(this.getParent().Innerp.ceilingKey(mergeNode.Innerp.lastKey()));
				mergeNode.setRightMostChild(this.getRightMostChild());
				this.getParent().Innerp.replace(key, mergeNode);
				mergeNode.printInnerNode();
			}
			else {
				mergeNode.insertPair(this.getParent().Innerp.floorKey(key), mergeNode.getRightMostChild());
				this.getParent().deletePairInInner(this.getParent().Innerp.floorKey(key));
				mergeNode.setRightMostChild(this.getRightMostChild());
				this.getParent().Innerp.replace(this.getParent().Innerp.ceilingKey(key), mergeNode);
			}
		}
		

	}


}
	