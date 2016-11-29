package Main;

import java.util.ArrayList;
import java.util.Collections;

public class Main 
{
	// Constant throughout the program
	static final String resultFileName = "Result";
	static final String resultExtension = ".txt";
	static final String testFileName = "bigblue1";
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
		netOperation(netList, file, nodeList);
		
		//Calcute HPWL
		int hpwl = totalHPWL(netList);

		System.out.println("Analyzing completed");
		file.deInitFileIO();
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
	
	public static void netOperation(NetList netList, FileIO file, NodeList nodeList)
	{
		System.out.println("Start processing .nets file");
		netList.netListReadAndAnalyseFile(testFileName, testFileDirectory, file);
		netList.updateNodelist(nodeList);
		System.out.println("Process .nets file done");
		System.out.println("Dumping nets data to "+resultFileName + resultExtension);
		netList.printSummary(file);
		System.out.println("Done processing .nets file");
	}
	
	public static int totalHPWL(NetList nList)
	{
		int hpwl = 0, inputNodeSize = 0, outputNodeSize = 0;
		ArrayList<NodeCoordinate> nCoor = new ArrayList<NodeCoordinate>();
		
		for(int i = 0; i < nList.netlist.size(); i++)
		{
			inputNodeSize = nList.netlist.get(i).inputNodes.size();
			outputNodeSize = nList.netlist.get(i).outputNodes.size();
			
			for(int j = 0; j < inputNodeSize; j++)
				nCoor.add(nList.netlist.get(i).inputNodes.get(j).getNodeCoordinate());
			
			for(int j = 0; j < outputNodeSize; j++)
				nCoor.add(nList.netlist.get(i).outputNodes.get(j).getNodeCoordinate());
			
			hpwl += calHPWL(nCoor);
		}
		
		return hpwl;
	}
	
	public static int calHPWL(ArrayList<NodeCoordinate> nCoor)
	{
		int xCoor = 0, yCoor = 0;
		ArrayList<Integer> x = new ArrayList<Integer>();
		ArrayList<Integer> y = new ArrayList<Integer>();
		
		for(int i = 0; i < nCoor.size(); i++)
		{
			x.add(nCoor.get(i).getNodeXCoordinate());
			y.add(nCoor.get(i).getNodeYCoordinate());
		}
		
		xCoor = Collections.max(x) - Collections.min(x);
		yCoor = Collections.max(y) - Collections.min(y);
		
		return xCoor + yCoor;
	}
}