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
		Node innernode = this.root; //root���� �����Ͽ� ����.
		
		while(innernode.getNodeType() != 2) { // LeafNode���� Ž��
			innernode.setNodeType(1);//innernode type�� ������ش�.
			
			Integer higherkey=innernode.Innerp.higherKey(key); //ū���߿� ���� ������ ��
			int highestkey =innernode.Innerp.lastKey(); //���ȿ��� ���� ū��
			if(highestkey<=key) {//���� �����ʿ��־���Ҷ�
				innernode = innernode.rightMostChild;
			}
			else {
			innernode = innernode.Innerp.get(higherkey.intValue());//��������Ѱ��� value(node)�� ����� ������ �ٽ� innernode�� �־��ش�.
			}	
		}
		
		
		return (LeafNode)innernode;
	}
	
	public LeafNode SearchinMain(Integer key) {
		Node innernode = this.root; //root���� �����Ͽ� ����.
		
		while(innernode.getNodeType() != 2) { // LeafNode���� Ž��
			innernode.setNodeType(1);//innernode type�� ������ش�.
			innernode.printInnerNode();
			Integer higherkey=innernode.Innerp.higherKey(key); //ū���߿� ���� ������ ��
			int highestkey =innernode.Innerp.lastKey(); //���ȿ��� ���� ū��
			if(highestkey<=key) {//���� �����ʿ��־���Ҷ�
				innernode = innernode.rightMostChild;
			}
			else {
			innernode = innernode.Innerp.get(higherkey.intValue());//��������Ѱ��� value(node)�� ����� ������ �ٽ� innernode�� �־��ش�.
			}	
		}
		
		
		return (LeafNode)innernode;
	}
	
	public int SearchValueByKey(Integer key) {
		LeafNode leafnode = Search(key);
		if(leafnode.Leafp.containsKey(key)) {//���ȿ� Ű�� ������
			return leafnode.Leafp.get(key);
		}
		else //Ű��������
			return 0;
	}
	
	public void Insert(Integer key, Integer value) {
		
		LeafNode leafnode = Search(key);
		
		InnerNode parent  =  new InnerNode(this.root.degree);

		leafnode.insertPair(key, value);
		
		if(leafnode.isOverFlow()) { //�����÷ο찡 �Ͼ����
			
			leafnode.splitInLeaf(); 

			if(leafnode.getParent().isOverFlow()) {// �θ� �����÷ο��϶�
				parent = (InnerNode)leafnode.getParent();
				while(parent.isOverFlow()) {//�����÷ο찡 �Ͼ�� ���� ��� ����.
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
			
			if(leafnode.getParent().Innerp.containsKey(key)&&!leafnode.isUnderFlow()) { // ������� �θ� ��������, �������� ���������� ������ ù��°���� ���� �θ��� key���� �ٲ��ش�.
				leafnode.getParent().insertPair(leafnode.Leafp.firstKey(), leafnode.getParent().Innerp.get(key));
				leafnode.getParent().deletePairInInner(key);
			
			}
			
			if(leafnode.isUnderFlow()) {
				
				if(leafnode == this.getRoot())
				{
					System.out.println("node is root.");
				}
				else if (leafnode.re_distribute(key,this.root)) { //false�� ��ȯ�Ѵٸ�, ������ �͵� underflow�ΰ���
				
					leafnode.mergeInLeaf(key);
					if(leafnode.getParent().getNumberOfKeys()-1 < leafnode.getParent().getDegree()/2)
					{
						parent = (InnerNode)leafnode.getParent();
						while(parent.getNumberOfKeys()-1 < parent.getDegree()/2) {//�����÷ο찡 �Ͼ�� ���� ��� ����.
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
	