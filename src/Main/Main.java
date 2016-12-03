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
		
		System.out.println("Analysing " + testFileName);
		
		// object always pass by reference
		file.writeToFiles("\""+testFileName+"\" .node and .net descriptions and parameters:");
		nodeOperation(nodeList, file);
		netOperation(netList, file);
		file.deInitFileIO();
		
		System.out.println("Analyzing completed");
	
		//////////////// Project 2 Start here //////////////

		System.out.println("Updated node started here....");
		
		System.out.println(LocalDateTime.now().toString());
		netList.updateNodelist(nodeList); // net list with node object and remove terminal
		System.out.println(LocalDateTime.now().toString());
		System.out.println("Net list remainder =" + netList.getNetlist().size()); // check net list size
		
		System.out.println("Updated node completed");
		
		// Sort NetList and update node coordinate
		System.out.println("Sorted Net List Descending order.");
		netList.sortNetListDescending();
		//netList.printNetDegree();

		
		
		// random assign Coordinate - testing
		for(int i=0; i< nodeList.getNonTerminalNodeList().size(); i++) {
			nodeList.getNonTerminalNodeList().get(i).setNodeCoordinate(5+i, i);
		}
				
		// Display connected nodes
		System.out.println("Display node connection");
		netList.updateAllConnectedNodes(nodeList.getNodeList());
		//nodeList.printNonTerminalNodeCoordinate();
		nodeList.printConnectedNodeDetail();
		
		//Random placement
		Graph floorplan = new Graph(10577,nodeList);
		System.out.println("Algorithm finished");
		//nodeList.sortAllNodeList();
		
		//Calculate HPWL...testing
		int hpwl = netList.getTotalHPWL();
		System.out.println(hpwl);
	}
	
	public static void nodeOperation(NodeList nodeList, FileIO file)
	{
		System.out.println("Start process .nodes file");
		nodeList.readAndAnalyseFile(testFileName, testFileDirectory, file);
		System.out.println("Process .nodes file done");
		System.out.println("Dumping nodes data to "+resultFileName + resultExtension);
		nodeList.printSummary(file);
		System.out.println("Done processing .nodes file");
	}
	
	public static void netOperation(NetList netList, FileIO file)
	{
		System.out.println("Start processing .nets file");
		netList.netListReadAndAnalyseFile(testFileName, testFileDirectory, file);
		System.out.println("Process .nets file done");
		System.out.println("Dumping nets data to "+resultFileName + resultExtension);
		netList.printSummary(file);
		System.out.println("Done processing .nets file");
	}
}
