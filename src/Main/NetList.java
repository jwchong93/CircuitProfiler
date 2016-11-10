package Main;

import java.util.ArrayList;

public class NetList 
{
	private int size;
	private int totalPins;
	private int totalOutputPins;
	private int totalInputPins;
	private int maxDegree;
	private int totalBidirectionalPins;
	private String maxDegreeName;
	public NetList()
	{
		this.size = 0;
		this.totalPins = 0;
		this.totalOutputPins = 0;
		this.totalInputPins = 0;
		this.maxDegree = 0;
		this.totalBidirectionalPins = 0;
		this.maxDegreeName = null;
	}
	public void netListOperation (String testFileName, String testFileDirectory)
	{
		ArrayList<Nets> netlist = new ArrayList<Nets>();
		FileIO file = new FileIO();
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
			newNet.setNetDegree(Integer.parseInt(tempArray[2]));
			newNet.setNetName(tempArray[5].trim());
			line = file.readTextFiles().trim();
			tempArray = line.split(" |\t");
			
			while ((!tempArray[0].equals("NetDegree")) && line != null)
			{
				Nodes newNode = new Nodes();
				newNode.setNodeName(tempArray[0].trim());
				Pins newPin = new Pins();
				newPin.coordinatesX = Float.parseFloat(tempArray[3]);
				newPin.coordinatesX = Float.parseFloat(tempArray[4]);
				
				if (tempArray[1].equals("I"))
				{
					newNet.addInputPin(newPin);
					newNet.addInputNode(newNode);
				}
				else if (tempArray[1].equals("O"))
				{
					newNet.addOutputPin(newPin);
					newNet.addOutputNode(newNode);
				}
				else if (tempArray[1].equals("B"))
				{
					newNet.addBidirectionalPin (newPin);
					newNet.addBidirectionalNode (newNode);
				}
				line = file.readTextFiles();
				
				if(line != null)
				{
					line = line.trim();
					tempArray = line.split(" |\t");
				}
					
			}
			this.size ++;
			this.totalPins += newNet.getTotalNumberOfPins();
			this.totalOutputPins += newNet.getNumberOfOutputPins();
			if (newNet.getDegree() > this.maxDegree)
			{
				this.maxDegree = newNet.getDegree(); 
				this.maxDegreeName = newNet.getNetName();
			}
			netlist.add(newNet);
		}
	}
	public void printSummary() {
		System.out.println("Total number of nets : " + this.size);
		System.out.println("Total number of pins : " + this.totalPins);
		System.out.println("Total number of input pins : " + this.totalInputPins);
		System.out.println("Total number of output pins : " + this.totalOutputPins);
		System.out.println("Total number of bidirectional pins : " + this.totalBidirectionalPins);
		System.out.println("Maximum net degree : " + this.maxDegree + "\nNet name of maximum net degree : " + this.maxDegreeName);
		
		
	}
}
