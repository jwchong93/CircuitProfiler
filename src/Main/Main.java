package Main;

public class Main 
{
	// Constant throughout the program
	static final String resultFileName = "Result";
	static final String resultExtension = ".txt";
	static final String testFileName = "adaptec1";
	static final String testFileDirectory = System.getProperty("user.dir")+"/testFiles/"+testFileName+"/";
	
	public static void main(String[] args) 
	{
		FileIO file = new FileIO();
		file.initFileOutput(System.getProperty("user.dir"), "/" +resultFileName, resultExtension);
		
		// object declaration to store node list and net list
		NodeList nodeList = new NodeList();
		NetList netList = new NetList();
		
		// Project 1 : Read .nodes and .nets file and write summary to Result file.
		nodeAndNetOps(nodeList, netList, file);
	
		//////////////////////////// Project 2 Start here ///////////////////////////

		System.out.println("-I- Linked netlist I/O nodelist with node ref and remove netlist that contained terminal node.");
		netList.updateNodelist(nodeList); // net list with node object and remove terminal
		System.out.println("Number of Net list with no terminal = " + netList.getNetlist().size()); // check net list size
		
		// Sort NetList and update node coordinate
		//System.out.println("Sorted Net List Descending order.");
		//netList.sortNetListDescending();
			
		// Display connected nodes
		System.out.println("-I- Get and linked node-to-node and net relationship in NODE");
		netList.updateAllConnectedNodes();

		//Random placement
		System.out.println("-I- Instantiate Floorplan");
		Graph floorplan = new Graph(nodeList);

		// Print out message
		//nodeList.printNonTerminalNodeCoordinate();
		//nodeList.printConnectedNodeDetail();
		//netList.printNetDegree();
		
		// Start Force-Directed Placement
		FDP fdp = new FDP(floorplan, nodeList);
		FDPrippleMove(fdp);
		//System.out.println(nodeList.getNonTerminalNodeList().get(2).computeAndReturnZFT());
		
		//Calculate total HPWL
		long hpwl = netList.getTotalHPWL();
		System.out.println("Total HPWL = " + hpwl);
	}
	
	public static void nodeAndNetOps(NodeList nodeList, NetList netList, FileIO file)
	{
		System.out.println("-I- Analysing " + testFileName + "...");

		file.writeToFiles("\""+testFileName+"\" .node and .net descriptions and parameters:");
		double startTime = System.currentTimeMillis();
		
		nodeOperation(nodeList, file);
		netOperation(netList, file);
		
		double endTime   = System.currentTimeMillis();
		double totalTime = (endTime - startTime)/1000;
		System.out.println("Runtime = " + totalTime + " seconds");
		
		file.deInitFileIO();
		
		System.out.println("-I- Analyzing completed");
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
	
	public static void FDPrippleMove(FDP fdp)
	{
		System.out.println("-I- Call FDP ripple Move Algorithm");
		fdp.startAlgorithm();
		//fdp.getNodeList().printNonTerminalNodeCoordinate();
	}
}