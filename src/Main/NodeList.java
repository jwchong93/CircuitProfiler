package Main;

import java.util.*;

public class NodeList 
{
	public ArrayList<Nodes> nodelist;
	
	private int totalTerminalNodes = 0;
	private int totalNonTerminalNodes = 0;
	private int totalNode = 0;
	private int totalNonTerminalWidth = 0;
	
	// Variable for storing comparison
	private ArrayList<Nodes> largestAreaTerminalNodeList;
	private ArrayList<Nodes> smallestAreaTerminalNodeList;
	private ArrayList<Nodes> largestAreaNonTerminalNodeList;
	private ArrayList<Nodes> smallestAreaNonTerminalNodeList;
	private final Nodes largestAreaNode = new Nodes(null, 0, 0, null);
	private final Nodes smallestAreaNode = new Nodes(null, (int)Math.sqrt(Integer.MAX_VALUE), (int)Math.sqrt(Integer.MAX_VALUE), null);
	
	// Constructor
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
	
	public void readAndAnalyseFile (String testFileName, String testFileDirectory, FileIO file) {
		file.initFileInput(testFileDirectory, testFileName, ".nodes");
		// Now we have pointer to the file, parse the file, process line by line
		String line;
		String[] tempArray;
 		// Go to first node
		do{
			line = file.readTextFiles().trim();
			tempArray = line.split(" |\t");
		}
		while (!tempArray[0].equals("o0"));
		
		// Start process node by node
		while (line !=null){
			// Get node and store in nodeList
			Nodes newNode;
			// Non-terminal node
			if(tempArray.length == 3) {
				newNode = new Nodes(tempArray[0], Integer.parseInt(tempArray[1]), Integer.parseInt(tempArray[2]), "non-terminal");
				totalNonTerminalNodes++;
				totalNonTerminalWidth += newNode.getNodeWidth();
		
				// Find largest and smallest non-terminal nodes
				if(newNode.getArea() > largestAreaNonTerminalNodeList.get(0).getArea()) {
					largestAreaNonTerminalNodeList.removeAll(largestAreaNonTerminalNodeList);
					largestAreaNonTerminalNodeList.add(newNode);
				}
				else if(newNode.getArea() == largestAreaNonTerminalNodeList.get(0).getArea()) {
					largestAreaNonTerminalNodeList.add(newNode);
				}
				
				if(newNode.getArea() < smallestAreaNonTerminalNodeList.get(0).getArea()) {
					smallestAreaNonTerminalNodeList.removeAll(smallestAreaNonTerminalNodeList);
					smallestAreaNonTerminalNodeList.add(newNode);
				}
				else if(newNode.getArea() == smallestAreaNonTerminalNodeList.get(0).getArea()) {
					smallestAreaNonTerminalNodeList.add(newNode);
				}
				nodelist.add(newNode);
			}
			// Terminal node
			else if(tempArray.length == 4){
				newNode = new Nodes(tempArray[0], Integer.parseInt(tempArray[1]), Integer.parseInt(tempArray[2]), tempArray[3]);
				totalTerminalNodes++;
			
				// Find largest and smallest terminal nodes
				if(newNode.getArea() > largestAreaTerminalNodeList.get(0).getArea()) {
					largestAreaTerminalNodeList.removeAll(largestAreaTerminalNodeList);
					largestAreaTerminalNodeList.add(newNode);
				}
				else if(newNode.getArea() == largestAreaTerminalNodeList.get(0).getArea()) {
					largestAreaTerminalNodeList.add(newNode);
				}
				
				if(newNode.getArea() < smallestAreaTerminalNodeList.get(0).getArea()) {
					smallestAreaTerminalNodeList.removeAll(smallestAreaTerminalNodeList);
					smallestAreaTerminalNodeList.add(newNode);
				}
				else if(newNode.getArea() == smallestAreaTerminalNodeList.get(0).getArea()) {
					smallestAreaTerminalNodeList.add(newNode);
				}
				nodelist.add(newNode);
			}
			
			// Read next file line
			line = file.readTextFiles();
			if(line != null){
				line = line.trim();
				tempArray = line.split(" |\t");
			}
		}
	}
	
	// Print out description and parameter into Result.txt
	public void printSummary(FileIO file) {
		file.writeToFiles(".nodes file summary:");
		totalNode = totalNonTerminalNodes + totalTerminalNodes;
		file.writeToFiles("Total Number of Terminals Node: "+totalTerminalNodes);
		file.writeToFiles("Total Number of Non-Terminals Node: "+totalNonTerminalNodes);
		file.writeToFiles("Total Number of Nodes: "+totalNode);
		file.writeToFiles("Largest Non-Terminal Node("+largestAreaNonTerminalNodeList.size()+"): Area "+largestAreaNonTerminalNodeList.get(0).getArea());
		printInMatrixForm(largestAreaNonTerminalNodeList, file);
		file.writeToFiles("Smallest Non-Terminal Node("+smallestAreaNonTerminalNodeList.size()+"): Area "+smallestAreaNonTerminalNodeList.get(0).getArea());
		printInMatrixForm(smallestAreaNonTerminalNodeList, file);
		file.writeToFiles("Largest Terminal Node("+largestAreaTerminalNodeList.size()+"): Area "+largestAreaTerminalNodeList.get(0).getArea());
		printInMatrixForm(largestAreaTerminalNodeList, file);
		file.writeToFiles("Smallest Terminal Node("+smallestAreaTerminalNodeList.size()+"): Area "+smallestAreaTerminalNodeList.get(0).getArea());
		printInMatrixForm(smallestAreaTerminalNodeList, file);	
		file.writeToFiles("Total non-terminal node width length: "+totalNonTerminalWidth);
	}
	
	private void printInMatrixForm(ArrayList<Nodes> nodeList, FileIO file){
		String tempStr="";
		int remainNode = nodeList.size() % 5;
		int lastDigit;
		if(nodeList.size() < 5) {
			lastDigit = 0;
		}
		else {
			lastDigit = nodeList.size() - remainNode;
		}
		
		for(int i = 0; i < nodeList.size() - remainNode; i=i+5) {
			for(int j = 0; j < 5; j++) {
				tempStr=tempStr+String.format("%9s", nodeList.get(i+j).getNodeName());
			}
			file.writeToFiles(tempStr);
			tempStr="";
		}
		
		for(int j = 0; j < remainNode; j++) {
			tempStr=tempStr+String.format("%9s", nodeList.get(lastDigit+j).getNodeName());
		}
		if(remainNode != 0) file.writeToFiles(tempStr);
	}

	public ArrayList<Nodes> getTerminalNodeList() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public ArrayList<Nodes> getNodeList() {
		return this.nodelist;
	}
}
