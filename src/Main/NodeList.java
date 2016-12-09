package Main;

import java.util.*;

public class NodeList
{
	// Different kind of NODE list
	private ArrayList<Nodes> nodelist = new ArrayList<Nodes>();;
	private ArrayList<Nodes> terminalnodelist = new ArrayList<Nodes>();;
	private ArrayList<Nodes> nonTerminalnodelist = new ArrayList<Nodes>();;
	
	// Counter for Node and Width
	private int totalTerminalNodes = 0;
	private int totalNonTerminalNodes = 0;
	private int totalNode = 0;
	private int totalNonTerminalWidth = 0;
	
	// Variable for storing comparison
	private ArrayList<Nodes> largestAreaTerminalNodeList = new ArrayList<Nodes>();;
	private ArrayList<Nodes> smallestAreaTerminalNodeList = new ArrayList<Nodes>();;
	private ArrayList<Nodes> largestAreaNonTerminalNodeList = new ArrayList<Nodes>();;
	private ArrayList<Nodes> smallestAreaNonTerminalNodeList = new ArrayList<Nodes>();;
	private final Nodes largestAreaNode = new Nodes(null, 0, 0, null);
	private final Nodes smallestAreaNode = new Nodes(null, (int)Math.sqrt(Integer.MAX_VALUE), (int)Math.sqrt(Integer.MAX_VALUE), null);
	
	// Constructor
	public NodeList () {
		this.largestAreaTerminalNodeList.add(this.largestAreaNode);
		this.smallestAreaTerminalNodeList.add(this.smallestAreaNode);
		this.largestAreaNonTerminalNodeList.add(this.largestAreaNode);
		this.smallestAreaNonTerminalNodeList.add(this.smallestAreaNode);
	}
	
	// Read and process file from .nodes file
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
				nonTerminalnodelist.add(newNode);
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
				terminalnodelist.add(newNode);
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
	
	// get methods
	public ArrayList<Nodes> getNodeList() { return this.nodelist; }
	public ArrayList<Nodes> getTerminalNodeList() { return terminalnodelist; }
	public ArrayList<Nodes> getNonTerminalNodeList() { return nonTerminalnodelist; }
	public int getTotalNonTerminalWidth() { return this.totalNonTerminalWidth; }
	
	// set methods
	public void setNodeListAllNodeDegree() {
		for(int i=0; i<this.nonTerminalnodelist.size(); i++) {
			int totalNodeDegree=0;
			for(int j = 0; j<this.nonTerminalnodelist.get(i).getConnectionNets().size(); j++)
			{
				totalNodeDegree += this.nonTerminalnodelist.get(i).getConnectionNets().get(j).getIO_nodes().size()-1;
			}
			this.nonTerminalnodelist.get(i).setNodeDegree(totalNodeDegree);
		}
	}
	
	// print methods
	public void printNonTerminalNodeCoordinate() {
		for(Nodes str:this.nonTerminalnodelist) {
			System.out.println(str);
		}
	}
	
	public void printConnectedNodeDetail()
	{
		for(int i = 0; i < this.nonTerminalnodelist.size(); i++)
		{
			System.out.print(nonTerminalnodelist.get(i).getNodeName() + ": ");
			for(int j = 0; j < nonTerminalnodelist.get(i).getConnectedNodes().size(); j++)
				System.out.print(nonTerminalnodelist.get(i).getConnectedNodes().get(j).getNodeName() + " ");
			System.out.print("\nNets: ");
			for(int j = 0; j < nonTerminalnodelist.get(i).getConnectionNets().size(); j++)
				System.out.print(nonTerminalnodelist.get(i).getConnectionNets().get(j).getNetName() + " ");
			System.out.println("\nNodeDeg: " + nonTerminalnodelist.get(i).getNodeDegree() + " NodeTotalHPWL: " /*+ nonTerminalnodelist.get(i).calcNodeAllNetHPWL()*/);
			System.out.println();
		}
	}
	
	// Other method
	public void sortAllNodeList() {
		Collections.sort(this.nodelist);
		Collections.sort(this.nonTerminalnodelist);
		Collections.sort(this.terminalnodelist);
	}
	
	public void sortNodesAccordingToNets(ArrayList<Nets> nets)
	{
		for(int i = 0; i < nets.size(); i++)
		{
			ArrayList<Nodes> nodes = new ArrayList<Nodes>();
			nodes = nets.get(i).getIO_nodes();
			
			for(int j = 0; j < nodes.size(); j++)
			{
				if(!this.nodelist.contains(nodes.get(j)))
				{
					this.nodelist.add(nodes.get(j));
					//System.out.println(nodes.get(j).getNodeName());
				}
			}
		}
	}
	
	public void sortNodesAccordingToNodesConnection(ArrayList<Nodes> nList)
	{
		for(int i = 0; i < nList.size(); i++)
		{
			ArrayList<Nodes> nodes = new ArrayList<Nodes>();
			
			if(!this.nodelist.contains(nList.get(i)))
			{
				nodes = nList.get(i).getConnectedNodes();
				
				for(int j = 0; j < nodes.size(); j++)
				{
					if(!this.nodelist.contains(nodes.get(j)))
					{
						this.nodelist.add(nodes.get(j));
						//System.out.println(nodes.get(j).getNodeName());
					}
				}
			}
		}
	}
}
