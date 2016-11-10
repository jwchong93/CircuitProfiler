package Main;

import java.util.*;


public class Main 
{

	public static void main(String[] args) 
	{
		String testFileName = "adaptec1";
		String testFileDirectory = "C:/Users/dihjiann/Desktop/VLSIDA/testFiles/"+testFileName+"/";
		
		System.out.println("Start reading .nodes file");
		// Variable declaration
		int totalTerminalNodes = 0;
		int totalNonTerminalNodes = 0;
		int totalNode = 0;
		int totalNonTerminalWidth = 0;
		// Variable for storing comparison
		ArrayList<Nodes> largestAreaTerminalNodeList = new ArrayList<Nodes>();
		ArrayList<Nodes> smallestAreaTerminalNodeList = new ArrayList<Nodes>();
		ArrayList<Nodes> largestAreaNonTerminalNodeList = new ArrayList<Nodes>();
		ArrayList<Nodes> smallestAreaNonTerminalNodeList = new ArrayList<Nodes>();
		final Nodes largestAreaNode = new Nodes(null, 0, 0, null);
		final Nodes smallestAreaNode = new Nodes(null, (int)Math.sqrt(Integer.MAX_VALUE), (int)Math.sqrt(Integer.MAX_VALUE), null);
		// Initialize list for comparison
		largestAreaTerminalNodeList.add(largestAreaNode);
		smallestAreaTerminalNodeList.add(smallestAreaNode);
		largestAreaNonTerminalNodeList.add(largestAreaNode);
		smallestAreaNonTerminalNodeList.add(smallestAreaNode);
		// List of nodes
		ArrayList<Nodes> nodelist = new ArrayList<Nodes>();
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
		
		//============================================================================================================

		System.out.println("Start reading .nets file");
		int totalPins=0;
		ArrayList<Nets> netlist = new ArrayList<Nets>();
		FileIO file = new FileIO();
		file.initFileIO(testFileDirectory, testFileName, ".nets");
//		String line;
//		String[] tempArray;
		//Find the first net
		do
		{
			line = file.readTextFiles().trim();
			tempArray = line.split(" |\t");
		}
		while (!tempArray[0].equals("NetDegree")); 

		while (line !=null)
		{
			Nets newNet = new Nets();
			newNet.netDegree = Integer.parseInt(tempArray[2]);
			newNet.netName = tempArray[5].trim();
			line = file.readTextFiles().trim();
			tempArray = line.split(" |\t");
			
			while ((!tempArray[0].equals("NetDegree")) && line != null)
			{
				Nodes newNode = new Nodes();
				newNode.nodeName = tempArray[0].trim();
				Pins newPin = new Pins();
				newPin.coordinatesX = Float.parseFloat(tempArray[3]);
				newPin.coordinatesX = Float.parseFloat(tempArray[4]);
				
				if (tempArray[1].equals("I"))
				{
					newNet.inputPins.add(newPin);
					newNet.inputNodes.add(newNode);
					newNet.numberOfPins++;
				}
				else if (tempArray[1].equals("O"))
				{
					newNet.outputPins = newPin;
					newNet.outputNodes = newNode;
					newNet.numberOfPins++;
				}
				line = file.readTextFiles();
				
				if(line != null)
				{
					line = line.trim();
					tempArray = line.split(" |\t");
				}
					
			}
			newNet.numberOfPins = newNet.inputPins.size() + 1;
			totalPins += newNet.numberOfPins;
			netlist.add(newNet);
		}
		
		System.out.println(netlist.size());
		System.out.println(totalPins);
		//FileIO.readTextFiles(testFileDirectory, testFileName, ".nodes");
		
		//placementGreedy(testFileDirectory, testFileName);
		
	}
}