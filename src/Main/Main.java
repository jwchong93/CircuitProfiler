package Main;

import java.time.LocalDateTime;

public class Main 
{
	// Constant throughout the program
	static final String resultFileName = "Result";
	static final String resultExtension = ".txt";
	static final String testFileName = "test1";
	static final String testFileDirectory = System.getProperty("user.dir")+"/testFiles/"+testFileName+"/";
	
	public static void main(String[] args) 
	{
		FileIO file = new FileIO();
		file.initFileOutput(System.getProperty("user.dir"), "/" +resultFileName, resultExtension);
		
		// object declaration to store node list and net list
		NodeList nodeList = new NodeList();
		NetList netList = new NetList();
		
		System.out.println("-I- Analysing " + testFileName);

		// object always pass by reference
		file.writeToFiles("\""+testFileName+"\" .node and .net descriptions and parameters:");
		long startTime = System.currentTimeMillis();
		nodeOperation(nodeList, file);
		netOperation(netList, file);
		long endTime   = System.currentTimeMillis();
		long totalTime = (endTime - startTime)/1000;
		System.out.println("Runtime = " + totalTime + " seconds");

		file.deInitFileIO();
		
		System.out.println("-I- Analyzing completed");
	
		//////////////// Project 2 Start here //////////////

		System.out.println("-I- Updated node started here....");
		
		netList.updateNodelist(nodeList); // net list with node object and remove terminal
		System.out.println("Number of Net list with no terminal = " + netList.getNetlist().size()); // check net list size
		
		
		
		System.out.println("-I- Updated node completed");
		
		// Sort NetList and update node coordinate
		//System.out.println("Sorted Net List Descending order.");
		//netList.sortNetListDescending();
		
		// random assign Coordinate - testing
		for(int i=0; i< nodeList.getNonTerminalNodeList().size(); i++) {
			nodeList.getNonTerminalNodeList().get(i).setNodeCoordinate(5*i, i);
		}
		
		// Display connected nodes
		System.out.println("-I- Update node-to-node connection");
		netList.updateAllConnectedNodes();
		
		//Random placement
		//NodeList nList = new NodeList();
		//nList.sortNodesAccordingToNets(netList.getNetlist());
		//nList.sortNodesAccordingToNodesConnection(nodeList.getNodeList());
		//Graph floorplan = new Graph(nList);
		Graph floorplan = new Graph(nodeList);
		System.out.println("Algorithm finished");
		
		// Print out message
		//netList.printNetDegree();
		//nodeList.printNonTerminalNodeCoordinate();
		nodeList.printConnectedNodeDetail();
		
		FDPrippleMove(nodeList);
		
		//Calculate HPWL...testing
		int hpwl = netList.getTotalHPWL();
		System.out.println("-I- Total hpwl = " + hpwl);
	}
	
	public static void nodeOperation(NodeList nodeList, FileIO file)
	{
		System.out.println("-I- Start process .nodes file");
		nodeList.readAndAnalyseFile(testFileName, testFileDirectory, file);
		System.out.println("-I- Process .nodes file done");
		System.out.println("-I- Dumping nodes data to "+resultFileName + resultExtension);
		nodeList.printSummary(file);
		System.out.println("-I- Done processing .nodes file");
	}
	
	public static void netOperation(NetList netList, FileIO file)
	{
		System.out.println("-I- Start processing .nets file");
		netList.netListReadAndAnalyseFile(testFileName, testFileDirectory, file);
		System.out.println("-I- Process .nets file done");
		System.out.println("-I- Dumping nets data to "+resultFileName + resultExtension);
		netList.printSummary(file);
		System.out.println("-I- Done processing .nets file");
	}
	
	public static void FDPrippleMove(NodeList nodeList)
	{
		System.out.println("-I- Call FDP ripple Move");
		// assume initial placement done
		// 
		nodeList.sortAllNodeList();
		
		
	}
}