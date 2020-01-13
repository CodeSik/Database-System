package bptree;

import java.awt.font.TextHitInfo;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.*;

public class Bplustree{
	
	private Node root; //The root node
	
	
	public void setDegree(int degree){
		root.setDegree(degree);
	}
	public Bplustree(int degree) {
		this.root = new LeafNode(degree); //The first node is leafnode
	}
	
	public void makeRoot() {
		while(this.root.getRightMostChild()!=null) {
			this.root = this.root.getRightMostChild(); 
		}
		while(this.root.getParent() != null) {
			this.root = this.root.getParent();
		}
	}
	
	public LeafNode Search(Integer key) {
		Node innernode = this.root; //root부터 시작하여 본다.
		
		while(innernode.getNodeType() != 2) { // LeafNode까지 탐색
			innernode.setNodeType(1);//innernode type을 명시해준다.
			
			Integer higherkey=innernode.Innerp.higherKey(key); //큰값중에 가장 근접한 값
			int highestkey =innernode.Innerp.lastKey(); //노드안에서 가장 큰값
			if(highestkey<=key) {//가장 오른쪽에넣어야할때
				innernode = innernode.rightMostChild;
			}
			else {
			innernode = innernode.Innerp.get(higherkey.intValue());//가장근접한값의 value(node)에 저장된 정보를 다시 innernode에 넣어준다.
			}	
		}
		
		
		return (LeafNode)innernode;
	}
	
	public LeafNode SearchinMain(Integer key) {
		Node innernode = this.root; //root부터 시작하여 본다.
		
		while(innernode.getNodeType() != 2) { // LeafNode까지 탐색
			innernode.setNodeType(1);//innernode type을 명시해준다.
			innernode.printInnerNode();
			Integer higherkey=innernode.Innerp.higherKey(key); //큰값중에 가장 근접한 값
			int highestkey =innernode.Innerp.lastKey(); //노드안에서 가장 큰값
			if(highestkey<=key) {//가장 오른쪽에넣어야할때
				innernode = innernode.rightMostChild;
			}
			else {
			innernode = innernode.Innerp.get(higherkey.intValue());//가장근접한값의 value(node)에 저장된 정보를 다시 innernode에 넣어준다.
			}	
		}
		
		
		return (LeafNode)innernode;
	}
	
	public int SearchValueByKey(Integer key) {
		LeafNode leafnode = Search(key);
		if(leafnode.Leafp.containsKey(key)) {//노드안에 키가 있을때
			return leafnode.Leafp.get(key);
		}
		else //키가없을때
			return 0;
	}
	
	public void Insert(Integer key, Integer value) {
		
		LeafNode leafnode = Search(key);
		
		InnerNode parent  =  new InnerNode(this.root.degree);

		leafnode.insertPair(key, value);
		
		if(leafnode.isOverFlow()) { //오버플로우가 일어났을때
			
			leafnode.splitInLeaf(); 

			if(leafnode.getParent().isOverFlow()) {// 부모도 오버플로우일때
				parent = (InnerNode)leafnode.getParent();
				while(parent.isOverFlow()) {//오버플로우가 일어나는 동안 계속 돌림.
					parent.splitInInner();
					parent = (InnerNode) parent.getParent();
				}
			}
			
			this.makeRoot();
		}
		
	}
	
	
	
	
	public void Delete(int key) {
		LeafNode leafnode = Search(key);
		InnerNode parent  =  new InnerNode(this.root.degree);
		boolean rootChange =false;
		Node rootChild = new Node(this.root.degree);
		if(this.getRoot().Innerp.containsKey(key)) {
			rootChange = true;
			rootChild = this.getRoot().Innerp.get(key);
		}
		if(!leafnode.Leafp.containsKey(key)) {
			System.out.println("key is not in Tree");
		}
		else {
			leafnode.deletePairInLeaf(key);
			
			if(leafnode.getParent().Innerp.containsKey(key)&&!leafnode.isUnderFlow()) { // 지운것이 부모에 있을때는, 리프에서 지워졌을때 리프의 첫번째값을 원래 부모의 key값과 바꿔준다.
				leafnode.getParent().insertPair(leafnode.Leafp.firstKey(), leafnode.getParent().Innerp.get(key));
				leafnode.getParent().deletePairInInner(key);
			
			}
			
			if(leafnode.isUnderFlow()) {
				
				if(leafnode == this.getRoot())
				{
					System.out.println("node is root.");
				}
				else if (leafnode.re_distribute(key,this.root)) { //false를 반환한다면, 빌려온 것도 underflow인것임
				
					leafnode.mergeInLeaf(key);
					if(leafnode.getParent().getNumberOfKeys()-1 < leafnode.getParent().getDegree()/2)
					{
						parent = (InnerNode)leafnode.getParent();
						while(parent.getNumberOfKeys()-1 < parent.getDegree()/2) {//오버플로우가 일어나는 동안 계속 돌림.
							if(parent == this.getRoot())
							{
								break;
							}
					
							parent.mergeInInner(key,rootChange);
							parent = (InnerNode) parent.getParent();
						}
					
						
					}
				
				}
				
				if(rootChange)
				{
					this.setRoot(rootChild);
				}
			}
			
		}
		
	}

	public Node getRoot() {
		return root;
	}

	public void setRoot(Node root) {
		this.root = root;
	}
	
	
			
			
		
	}
	