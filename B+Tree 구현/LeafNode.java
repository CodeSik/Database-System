package bptree;

import java.util.*;


public class LeafNode extends Node {
	
	
	
	public LeafNode(int degree) {
		super(degree); // using Node class's constructor
		super.NodeType=2;
	}
	

	
	
	public void deletePairAndMakeLeftChildInLeaf() {
		int index=0;
		TreeMap<Integer,Integer> cmp = new TreeMap<Integer,Integer>(); //임시 TreeMap
		cmp = (TreeMap<Integer, Integer>) Leafp.clone(); //임시 TreeMap 에 this.p 저장
		this.Leafp.clear();
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
	
	public void deletePairAndMakeRightChildInLeaf() {
		int index=0;
		TreeMap<Integer,Integer> cmp = new TreeMap<Integer,Integer>(); //임시 TreeMap
		cmp = (TreeMap<Integer, Integer>) Leafp.clone(); //임시 TreeMap 에 this.p 저장
		
		this.Leafp.clear();
		this.setNumberOfKeys(0);
		for(Integer keys : cmp.keySet()){
			if(index >=degree/2) 
			{
				this.insertPair(keys.intValue(), cmp.get(keys.intValue()));
			}
			else 
				
				index++;
			
		}
		//현재 노드에 n/2로 나눴을때 오른쪽 부분

	}
	
	
	
	public int searchIndexInNodeByKey(int key) {
		int index=0;
		for(Integer keys : Leafp.keySet()) {
			if(keys.intValue() == key) {
				return index;
			}
			index++;
		}
		return index;
	
	}
	
	public int searchKeyInNodeByIndex(int index) {
		int i=0;
		for(Integer keys : Leafp.keySet()) {
			if(i == index) {
				return keys.intValue();
			}
			i++;
		}
		
		return 0;
	}
	
	
	
	public void splitInLeaf() {
	      int midkey = this.searchKeyInNodeByIndex(this.degree/2); //mid key값 저장
	      
	      TreeMap<Integer,Integer> cmp = new TreeMap<Integer,Integer>(); //임시 TreeMap
	      cmp = (TreeMap<Integer, Integer>) this.Leafp.clone(); //임시 TreeMap 에 this.p 저장
	      
	      LeafNode rightleaf = new LeafNode(this.degree);//오른쪽노드
	      rightleaf.Leafp = cmp;
	      this.deletePairAndMakeLeftChildInLeaf();// 왼쪽부분.
	      rightleaf.deletePairAndMakeRightChildInLeaf(); //오른쪽부분
	      
	      
	      
	      if(this.getParent() == null ) { //루트일때
	    	 
	         InnerNode parent = new InnerNode(this.degree); //부모노드 만들어줌
	         parent.insertPair(midkey, this); //첫 p에 midkey와 왼쪽 리프노드
	         parent.setRightMostChild(rightleaf);
	         this.setParent(parent); rightleaf.setParent(parent);
	         this.setRightMostChild(rightleaf);
	         
	      }
	      else if (this.getParent() != null) { //리프위에 Inner
	    	  
	         rightleaf.setParent(this.getParent());
	         
	         if(midkey > this.getParent().Innerp.lastKey().intValue()) { //제일 오른쪽에 들어갔을때.
	        	 
	        	 this.getParent().insertPair(midkey,this);
	        	 this.getParent().setRightMostChild(rightleaf);
	        	 
	         
	         }
	         
	         else // 중간, 혹은 제일 왼쪽에 들어갔을때.
	         {
	        	 
	        	 this.getParent().insertPair(midkey,this);
	        	 this.getParent().Innerp.replace(this.getParent().Innerp.higherKey(midkey), rightleaf);
	         }
	         
	         
	         rightleaf.setRightMostChild(this.rightMostChild);
	         this.setRightMostChild(rightleaf);
	        
	      }
	      else {
	      System.out.println("Error");
	      }
	       
	      
	   }
	
	public void mergeInLeaf(int key) {
		TreeMap<Integer,Node> cmp = new TreeMap<Integer,Node>(); //부모의 key 목록 저장
		cmp = (TreeMap<Integer, Node>) this.getParent().Innerp.clone();
		
		TreeMap<Integer,Integer> cmp2 = new TreeMap<Integer,Integer>(); //부모의 key 목록 저장
		
		
		for(Integer keys : cmp.keySet()) { //r 전까지 저장
			cmp2 = (TreeMap<Integer, Integer>) cmp.get(keys).Leafp.clone();
			for(Integer keys2 : cmp2.keySet()) {
				this.insertPair(keys2.intValue(),cmp2.get(keys2));
			}
		}
		
		cmp2 = (TreeMap<Integer, Integer>) this.getParent().getRightMostChild().Leafp.clone();
	
		for(Integer keys : cmp2.keySet()) {
			this.insertPair(keys.intValue(), cmp2.get(keys));
		}
	
		this.getParent().setRightMostChild(this);
	
	}


	public boolean re_distribute(int key,Node root) {
	
		LeafNode LeftborrowNode ;
		LeafNode RightborrowNode ;
	
		this.printLeafNode();
		
		if(this.getParent().getRightMostChild() == this) { //this가 부모의 자식중 가장 오른쪽일때
			RightborrowNode = this;
		}
		else{
			 RightborrowNode= (LeafNode)this.getRightMostChild();
		}
		
		
		if(this.getParent().Innerp.get(this.getParent().Innerp.ceilingKey(key)) == this) {	
			LeftborrowNode = this;
		}
		else {
			LeftborrowNode = (LeafNode)this.getParent().Innerp.get(key);
		
		}
		
		int firstkey = this.getParent().Innerp.firstKey();
		
		
		if(LeftborrowNode.getNumberOfKeys() >= RightborrowNode.getNumberOfKeys()){ //왼쪽 sibling이 갯수가 더 많을때
			
			
			Integer borrowKey = LeftborrowNode.Leafp.lastKey();
			Integer borrowValue = LeftborrowNode.Leafp.get(borrowKey);
					
			if(root.Innerp.containsKey(key)) {
				root.insertPair(borrowKey, root.Innerp.get(key));
				root.deletePairInInner(key);
			}
			
			if(LeftborrowNode.getNumberOfKeys()-1 < LeftborrowNode.getDegree()/2 && this.getParent().getNumberOfKeys()-1 < this.getParent().getDegree()/2) {

				return true;
			}
			
			
			
			if(LeftborrowNode.getNumberOfKeys()<=1) {
				this.getParent().deletePairInInner(key); // 부모에 원래 있던애는 지움
				this.insertPair(borrowKey.intValue(), borrowValue.intValue()); // 지운자리에 빌린 키를 채움
				LeftborrowNode.deletePairInLeaf(borrowKey); //빌려간 노드에는 원래있던 키를 삭제함
				
			}
			else {
				this.getParent().insertPair(borrowKey, this.getParent().Innerp.get(key)); //부모에 빌린키와 빌린노드를 포인터로 넣어줌
				this.getParent().deletePairInInner(key); // 부모에 원래 있던애는 지움
				this.insertPair(borrowKey.intValue(), borrowValue.intValue()); // 지운자리에 빌린 키를 채움
				LeftborrowNode.deletePairInLeaf(borrowKey); //빌려간 노드에는 원래있던 키를 삭제함
			}
		
			
		}
		
		
		else if(LeftborrowNode.getNumberOfKeys() < RightborrowNode.getNumberOfKeys()) { //오른쪽 sibling이 갯수가 더 많을때
			
			Integer borrowKey = RightborrowNode.Leafp.firstKey();
			
			if(root.Innerp.containsKey(key)) {
				root.insertPair(borrowKey, root.Innerp.get(key));
				root.deletePairInInner(key);
			}
			
			if(RightborrowNode.getNumberOfKeys()-1 < RightborrowNode.getDegree()/2 && this.getParent().getNumberOfKeys()-1 < this.getParent().getDegree()/2) {
				return true;
			}
			
			this.insertPair(borrowKey.intValue(), RightborrowNode.Leafp.get(borrowKey).intValue());
			//빌려온 키를 this에 삽입
			RightborrowNode.deletePairInLeaf(borrowKey);
			//빌린 노드에서 빌린 키 삭제
			this.getParent().insertPair(RightborrowNode.Leafp.firstKey().intValue(), this);
			//빌린노드에서 빌린키 삭제된 후 제일 처음값 부모에 삽입
			if(this.getParent().Innerp.containsKey(key)) {
				this.getParent().Innerp.replace(borrowKey, this.getParent().Innerp.get(key));
				this.getParent().deletePairInInner(key);
				//부모에서 빌린 키 삭제
			}
			else {
				this.getParent().deletePairInInner(borrowKey);
			}
			


			
		
		}

		
		return false;
	}
	


}