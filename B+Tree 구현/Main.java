package bptree;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Main {

	public static void main(String[] args) throws IOException {
		//CLI
		Bplustree Tree = new Bplustree(0);
		switch (args[0]) {
		
			case "-c":
				try {
				BufferedWriter cwriter = new BufferedWriter(new FileWriter("C:\\Users\\서건식\\eclipse-workspace\\B+Tree\\src\\bptree\\"+args[1]));
				cwriter.write(args[2]); cwriter.write("\n");
				cwriter.close();
				}
				catch(IOException e) {
					e.printStackTrace();
				}
				break;
			case "-i":
				try {
				BufferedReader ireader = new BufferedReader(new FileReader("C:\\Users\\서건식\\eclipse-workspace\\B+Tree\\src\\bptree\\"+args[2]));
				BufferedWriter iwriter = new BufferedWriter(new FileWriter("C:\\Users\\서건식\\eclipse-workspace\\B+Tree\\src\\bptree\\"+args[1],true));
				
				String data = "";
				
				Integer key; Integer value;
				String skey ; String svalue;
		
				
				while((data=ireader.readLine()) != null) {
					skey = data.split(",")[0]; svalue = data.split(",")[1];
					
					key = Integer.parseInt(skey);
					value=Integer.parseInt(svalue);
					
					iwriter.write(key.toString()); iwriter.write(","); 
					iwriter.write(value.toString()); iwriter.write("\n");
				}
				
				iwriter.close();
				ireader = new BufferedReader(new FileReader("C:\\Users\\서건식\\eclipse-workspace\\B+Tree\\src\\bptree\\"+args[1]));
				String data2 = "";
				data2 = ireader.readLine();
				
				Tree.setDegree(Integer.parseInt(data2.toString()));
				while((data2=ireader.readLine()) != null) {
					skey = data2.split(",")[0]; svalue = data2.split(",")[1];
					
					key = Integer.parseInt(skey);
					value=Integer.parseInt(svalue);
					
					Tree.Insert(key.intValue(), value.intValue());
					
					}
				
				}
				catch(IOException e) {
					e.printStackTrace();
				}
				
				break;
			case "-d":
				try {
				BufferedReader ireader = new BufferedReader(new FileReader("C:\\Users\\서건식\\eclipse-workspace\\B+Tree\\src\\bptree\\"+args[2]));
				BufferedWriter iwriter = new BufferedWriter(new FileWriter("C:\\Users\\서건식\\eclipse-workspace\\B+Tree\\src\\bptree\\"+args[1],true));
				
				String data = "";
				
				Integer key; Integer value;
				String skey ; String svalue;
		
				
				while((data=ireader.readLine()) != null) {
					skey = data;
					key = Integer.parseInt(skey);
					
					iwriter.write(key.toString()); iwriter.write("\n"); 
				}
				
				iwriter.close();
				ireader = new BufferedReader(new FileReader("C:\\Users\\서건식\\eclipse-workspace\\B+Tree\\src\\bptree\\"+args[1]));
				String data2 = "";
				data2 = ireader.readLine();
				
				Tree.setDegree(Integer.parseInt(data2.toString()));
				while((data2=ireader.readLine()) != null) {
					if(data2.contains(",")) {
						skey = data2.split(",")[0]; svalue = data2.split(",")[1];
					
						key = Integer.parseInt(skey);
						value=Integer.parseInt(svalue);
					
						Tree.Insert(key.intValue(), value.intValue());
					}
					else {
						skey = data2;
						key = Integer.parseInt(skey);
						Tree.Delete(key.intValue());
					}
				}
				
				}
				catch(IOException e) {
					e.printStackTrace();
				}
				
				
				break;
			case "-s":
				try {
					BufferedReader ireader = new BufferedReader(new FileReader("C:\\Users\\서건식\\eclipse-workspace\\B+Tree\\src\\bptree\\"+args[1]));
					
					String data = "";
					
					Integer key; Integer value;
					String skey ; String svalue;
					
					data = ireader.readLine();
					
					Tree.setDegree(Integer.parseInt(data.toString()));
					while((data=ireader.readLine()) != null) {
						skey = data.split(",")[0]; svalue = data.split(",")[1];
						
						key = Integer.parseInt(skey);
						value=Integer.parseInt(svalue);
						
						Tree.Insert(key.intValue(), value.intValue());
						
						}
					
					}
					catch(IOException e) {
						e.printStackTrace();
					}
				
				
				Integer searchkey = Integer.parseInt(args[2]);
				
				LeafNode findedNode = Tree.SearchinMain(searchkey.intValue());
			
				if(findedNode.Leafp.containsKey(searchkey)) {
					System.out.println(findedNode.Leafp.get(searchkey));
				}
			
				else {
					System.out.println("‘NOT FOUND’");
				}
				
				
				break;
			case "-r":
				try {
					BufferedReader ireader = new BufferedReader(new FileReader("C:\\Users\\서건식\\eclipse-workspace\\B+Tree\\src\\bptree\\"+args[1]));
					
					String data = "";
					
					Integer key; Integer value;
					String skey ; String svalue;
					
					data = ireader.readLine();
					
					Tree.setDegree(Integer.parseInt(data.toString()));
					while((data=ireader.readLine()) != null) {
						skey = data.split(",")[0]; svalue = data.split(",")[1];
						
						key = Integer.parseInt(skey);
						value=Integer.parseInt(svalue);
						
						Tree.Insert(key.intValue(), value.intValue());
						
						}
					
					}
				catch(IOException e) {
						e.printStackTrace();
					}
				
				
				Integer start_key = Integer.parseInt(args[2]); Integer end_key = Integer.parseInt(args[3]);
				System.out.println(start_key + " " + end_key);
				
				LeafNode startNode = Tree.Search(start_key.intValue());
				LeafNode endNode = Tree.Search(end_key.intValue());
				
				
				while(startNode != endNode) {
					startNode = (LeafNode) startNode.rightMostChild;
					startNode.printLeafNode();
				}
				
				break;
				
				
				
			default:
				System.out.println("Complie");
				
		}
	

	}
	
}