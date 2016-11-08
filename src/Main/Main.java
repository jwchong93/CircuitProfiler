package Main;

import java.util.ArrayList;


public class Main 
{

	public static void main(String[] args) 
	{
		int totalPins=0;
		ArrayList<Nets> netlist = new ArrayList<Nets>();
		FileIO file = new FileIO();
		System.out.println("Start");
		String testFileName = "adaptec1";
		//change to your directory!
		String testFileDirectory = "C:/Users/Jing Wen/Desktop/testFiles/"+testFileName+"/";
		
		file.initFileIO(testFileDirectory, testFileName, ".nets");
		String line;
		String[] tempArray;
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
