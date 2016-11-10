package Main;

import java.util.ArrayList;

public class NetList 
{
	int totalPins=0;
	int size =0;
	public NetList()
	{
		this.totalPins = 0;
		this.size =0;
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
					this.totalPins++;
				}
				else if (tempArray[1].equals("O"))
				{
					newNet.outputPins = newPin;
					newNet.outputNodes = newNode;
					this.totalPins++;
				}
				line = file.readTextFiles();
				
				if(line != null)
				{
					line = line.trim();
					tempArray = line.split(" |\t");
				}
					
			}
			this.size ++;
			netlist.add(newNet);
		}
	}
}
