package Main;

import java.util.*;

public class NodeList {
	ArrayList<Nodes> nodelist;
	
	int totalTerminalNodes = 0;
	int totalNonTerminalNodes = 0;
	int totalNode = 0;
	int totalNonTerminalWidth = 0;
	
	// Variable for storing comparison
	ArrayList<Nodes> largestAreaTerminalNodeList;
	ArrayList<Nodes> smallestAreaTerminalNodeList;
	ArrayList<Nodes> largestAreaNonTerminalNodeList;
	ArrayList<Nodes> smallestAreaNonTerminalNodeList;
	final Nodes largestAreaNode = new Nodes(null, 0, 0, null);
	final Nodes smallestAreaNode = new Nodes(null, (int)Math.sqrt(Integer.MAX_VALUE), (int)Math.sqrt(Integer.MAX_VALUE), null);
	
	public NodeList () {
		this.nodelist = new ArrayList<Nodes>();
		this.largestAreaTerminalNodeList = new ArrayList<Nodes>();
		this.smallestAreaTerminalNodeList = new ArrayList<Nodes>();
		this.largestAreaNonTerminalNodeList = new ArrayList<Nodes>();
		this.smallestAreaNonTerminalNodeList = new ArrayList<Nodes>();
		this.largestAreaTerminalNodeList.add(this.largestAreaNode);
		this.smallestAreaTerminalNodeList.add(this.smallestAreaNode);
		this.largestAreaNonTerminalNodeList.add(this.largestAreaNode);
		this.smallestAreaNonTerminalNodeList.add(this.smallestAreaNode);
	}
	
	public void nodeListOperation (String testFileName, String testFileDirectory) {
		System.out.println("Start reading .nodes file");
		// Set .nodes file pointer
		FileIO nodeFile = new FileIO();
		nodeFile.initFileIO(testFileDirectory, testFileName, ".nodes");
		// Now we have pointer to the file, parse the file, process line by line
		String line;
		String[] tempArray;
 		// Go to first node
		do
		{
			line = nodeFile.readTextFiles().trim();
			tempArray = line.split(" |\t");
		}
		while (!tempArray[0].equals("o0"));
		
		// Start process node by node
		while (line !=null)
		{
			// Get node and store in nodeList
			Nodes newNode;
			// Non-terminal node
			if(tempArray.length == 3) {
				newNode = new Nodes(tempArray[0], Integer.parseInt(tempArray[1]), Integer.parseInt(tempArray[2]), "non-terminal");
				totalNonTerminalNodes++;
				totalNonTerminalWidth += newNode.nodeWidth;
				
				// Find largest and smallest non-terminal nodes
				if(newNode.getArea() > largestAreaNonTerminalNodeList.get(0).getArea()) {
					largestAreaNonTerminalNodeList.clear();
					largestAreaNonTerminalNodeList.add(newNode);
				}
				else if(newNode.getArea() < smallestAreaNonTerminalNodeList.get(0).getArea()) {
					smallestAreaNonTerminalNodeList.clear();
					smallestAreaNonTerminalNodeList.add(newNode);
				}
				else if(newNode.getArea() == largestAreaNonTerminalNodeList.get(0).getArea()) {
					largestAreaNonTerminalNodeList.add(newNode);
				}
				else if(newNode.getArea() == smallestAreaNonTerminalNodeList.get(0).getArea()) {
					smallestAreaNonTerminalNodeList.add(newNode);
				}
			}
			// Terminal node
			else {
				newNode = new Nodes(tempArray[0], Integer.parseInt(tempArray[1]), Integer.parseInt(tempArray[2]), tempArray[3]);
				totalTerminalNodes++;
				
				// Find largest and smallest terminal nodes
				if(newNode.getArea() > largestAreaTerminalNodeList.get(0).getArea()) {
					largestAreaTerminalNodeList.clear();
					largestAreaTerminalNodeList.add(newNode);
				}
				else if(newNode.getArea() < smallestAreaTerminalNodeList.get(0).getArea()) {
					smallestAreaTerminalNodeList.clear();
					smallestAreaTerminalNodeList.add(newNode);
				}
				else if(newNode.getArea() == largestAreaTerminalNodeList.get(0).getArea()) {
					largestAreaTerminalNodeList.add(newNode);
				}
				else if(newNode.getArea() == smallestAreaTerminalNodeList.get(0).getArea()) {
					smallestAreaTerminalNodeList.add(newNode);
				}
			}
			nodelist.add(newNode);
			
			// Read next file line
			line = nodeFile.readTextFiles();
			if(line != null)
			{
				line = line.trim();
				tempArray = line.split(" |\t");
			}
		}
		
		// Print out description and parameter
		totalNode = totalNonTerminalNodes + totalTerminalNodes;
		System.out.println("NumTerminals: "+totalTerminalNodes);
		System.out.println("NumNonTerminals: "+totalNonTerminalNodes);
		System.out.println("NumNodes: "+totalNode);
		System.out.println("Largest Non-Terminal Node("+largestAreaNonTerminalNodeList.size()+"): Area "+largestAreaNonTerminalNodeList.get(0).getArea());
		for(int i = 0; i < largestAreaNonTerminalNodeList.size(); i++) {
			System.out.println(largestAreaNonTerminalNodeList.get(i).nodeName);
		}
		System.out.println("Smallest Non-Terminal Node("+smallestAreaNonTerminalNodeList.size()+"): Area "+smallestAreaNonTerminalNodeList.get(0).getArea());
		for(int i = 0; i < smallestAreaNonTerminalNodeList.size(); i++) {
			System.out.println(smallestAreaNonTerminalNodeList.get(i).nodeName);
		}
		System.out.println("Largest Terminal Node("+largestAreaTerminalNodeList.size()+"): Area "+largestAreaTerminalNodeList.get(0).getArea());
		for(int i = 0; i < largestAreaTerminalNodeList.size(); i++) {
			System.out.println(largestAreaTerminalNodeList.get(i).nodeName);
		}
		System.out.println("Smallest Terminal Node("+smallestAreaTerminalNodeList.size()+"): Area "+smallestAreaTerminalNodeList.get(0).getArea());
		for(int i = 0; i < smallestAreaTerminalNodeList.size(); i++) {
			System.out.println(smallestAreaTerminalNodeList.get(i).nodeName);
		}
		System.out.println("Total non-terminal node width length: "+totalNonTerminalWidth);
		
		// Reach end of .nodes file
		System.out.println("End read .nodes file");
	}
}
