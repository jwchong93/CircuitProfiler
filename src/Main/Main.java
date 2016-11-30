package Main;

import java.util.Collections;
import java.util.List;

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
		netOperation(netList, file);
		System.out.println("Analyzing completed");
		file.deInitFileIO();
	
		//////////////// Project 2 Start here //////////////
		//netList.updateNodelist(nodeList);
		System.out.println("Updated node completed");
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