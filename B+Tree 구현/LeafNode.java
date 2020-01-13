package bptree;

import java.util.*;


public class LeafNode extends Node {
	
	
	
	public LeafNode(int degree) {
		super(degree); // using Node class's constructor
		super.NodeType=2;
	}
	

	
	
	public void deletePairAndMakeLeftChildInLeaf() {
		int index=0;
		TreeMap<Integer,Integer> cmp = new TreeMap<Integer,Integer>(); //�ӽ� TreeMap
		cmp = (TreeMap<Integer, Integer>) Leafp.clone(); //�ӽ� TreeMap �� this.p ����
		this.Leafp.clear();
		this.setNumberOfKeys(0);
		for(Integer keys : cmp.keySet()){
			if(index == degree/2) // n/2�������� ����
			{
				break;
			}
			
			this.insertPair(keys.intValue(), cmp.get(keys.intValue()));
			
			index++;
		}
		//���� ��忡 n/2�� �������� ���ʺκи� �����Ѵ�.
		
		
		
	}
	
	public void deletePairAndMakeRightChildInLeaf() {
		int index=0;
		TreeMap<Integer,Integer> cmp = new TreeMap<Integer,Integer>(); //�ӽ� TreeMap
		cmp = (TreeMap<Integer, Integer>) Leafp.clone(); //�ӽ� TreeMap �� this.p ����
		
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
		//���� ��忡 n/2�� �������� ������ �κ�

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
	      int midkey = this.searchKeyInNodeByIndex(this.degree/2); //mid key�� ����
	      
	      TreeMap<Integer,Integer> cmp = new TreeMap<Integer,Integer>(); //�ӽ� TreeMap
	      cmp = (TreeMap<Integer, Integer>) this.Leafp.clone(); //�ӽ� TreeMap �� this.p ����
	      
	      LeafNode rightleaf = new LeafNode(this.degree);//�����ʳ��
	      rightleaf.Leafp = cmp;
	      this.deletePairAndMakeLeftChildInLeaf();// ���ʺκ�.
	      rightleaf.deletePairAndMakeRightChildInLeaf(); //�����ʺκ�
	      
	      
	      
	      if(this.getParent() == null ) { //��Ʈ�϶�
	    	 
	         InnerNode parent = new InnerNode(this.degree); //�θ��� �������
	         parent.insertPair(midkey, this); //ù p�� midkey�� ���� �������
	         parent.setRightMostChild(rightleaf);
	         this.setParent(parent); rightleaf.setParent(parent);
	         this.setRightMostChild(rightleaf);
	         
	      }
	      else if (this.getParent() != null) { //�������� Inner
	    	  
	         rightleaf.setParent(this.getParent());
	         
	         if(midkey > this.getParent().Innerp.lastKey().intValue()) { //���� �����ʿ� ������.
	        	 
	        	 this.getParent().insertPair(midkey,this);
	        	 this.getParent().setRightMostChild(rightleaf);
	        	 
	         
	         }
	         
	         else // �߰�, Ȥ�� ���� ���ʿ� ������.
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
		TreeMap<Integer,Node> cmp = new TreeMap<Integer,Node>(); //�θ��� key ��� ����
		cmp = (TreeMap<Integer, Node>) this.getParent().Innerp.clone();
		
		TreeMap<Integer,Integer> cmp2 = new TreeMap<Integer,Integer>(); //�θ��� key ��� ����
		
		
		for(Integer keys : cmp.keySet()) { //r ������ ����
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
		
		if(this.getParent().getRightMostChild() == this) { //this�� �θ��� �ڽ��� ���� �������϶�
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
		
		
		if(LeftborrowNode.getNumberOfKeys() >= RightborrowNode.getNumberOfKeys()){ //���� sibling�� ������ �� ������
			
			
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
				this.getParent().deletePairInInner(key); // �θ� ���� �ִ��ִ� ����
				this.insertPair(borrowKey.intValue(), borrowValue.intValue()); // �����ڸ��� ���� Ű�� ä��
				LeftborrowNode.deletePairInLeaf(borrowKey); //������ ��忡�� �����ִ� Ű�� ������
				
			}
			else {
				this.getParent().insertPair(borrowKey, this.getParent().Innerp.get(key)); //�θ� ����Ű�� ������带 �����ͷ� �־���
				this.getParent().deletePairInInner(key); // �θ� ���� �ִ��ִ� ����
				this.insertPair(borrowKey.intValue(), borrowValue.intValue()); // �����ڸ��� ���� Ű�� ä��
				LeftborrowNode.deletePairInLeaf(borrowKey); //������ ��忡�� �����ִ� Ű�� ������
			}
		
			
		}
		
		
		else if(LeftborrowNode.getNumberOfKeys() < RightborrowNode.getNumberOfKeys()) { //������ sibling�� ������ �� ������
			
			Integer borrowKey = RightborrowNode.Leafp.firstKey();
			
			if(root.Innerp.containsKey(key)) {
				root.insertPair(borrowKey, root.Innerp.get(key));
				root.deletePairInInner(key);
			}
			
			if(RightborrowNode.getNumberOfKeys()-1 < RightborrowNode.getDegree()/2 && this.getParent().getNumberOfKeys()-1 < this.getParent().getDegree()/2) {
				return true;
			}
			
			this.insertPair(borrowKey.intValue(), RightborrowNode.Leafp.get(borrowKey).intValue());
			//������ Ű�� this�� ����
			RightborrowNode.deletePairInLeaf(borrowKey);
			//���� ��忡�� ���� Ű ����
			this.getParent().insertPair(RightborrowNode.Leafp.firstKey().intValue(), this);
			//������忡�� ����Ű ������ �� ���� ó���� �θ� ����
			if(this.getParent().Innerp.containsKey(key)) {
				this.getParent().Innerp.replace(borrowKey, this.getParent().Innerp.get(key));
				this.getParent().deletePairInInner(key);
				//�θ𿡼� ���� Ű ����
			}
			else {
				this.getParent().deletePairInInner(borrowKey);
			}
			


			
		
		}

		
		return false;
	}
	


}