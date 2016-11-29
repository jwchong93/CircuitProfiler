package Main;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class NetList 
{
	private int size;
	private int totalPins;
	private int totalOutputPins;
	private int totalInputPins;
	private int maxDegree;
	private int totalBidirectionalPins;
	private ArrayList<String> maxDegreeName = new ArrayList<String>();
	private TreeMap<Integer, Integer> histogramOfConnectivity = new TreeMap<Integer, Integer>();
	public ArrayList<Nets> netlist = new ArrayList<Nets>();
	
	public NetList()
	{
		this.size = 0;
		this.totalPins = 0;
		this.totalOutputPins = 0;
		this.totalInputPins = 0;
		this.maxDegree = 0;
		this.totalBidirectionalPins = 0;
	}
	public void netListReadAndAnalyseFile (String testFileName, String testFileDirectory, FileIO file)
	{
		file.initFileInput(testFileDirectory, testFileName, ".nets");
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
				Pins newPin = new Pins(
						tempArray[0].trim(),
						Float.parseFloat(tempArray[3]),
						Float.parseFloat(tempArray[4])
				);
				
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
			this.totalInputPins += newNet.getNumberOfInputPins();
			this.totalOutputPins += newNet.getNumberOfOutputPins();
			this.totalBidirectionalPins += newNet.getNumberOfBidirectionalPins();
			if (newNet.getDegree() >= this.maxDegree)
			{
				if (newNet.getDegree() == this.maxDegree)
				{
					this.maxDegreeName.add(newNet.getNetName());
				}
				else
				{
					this.maxDegreeName.clear();
					this.maxDegreeName.add(newNet.getNetName());
				}
				this.maxDegree = newNet.getDegree(); 
				
			} 
				
			if (this.histogramOfConnectivity.containsKey(newNet.getDegree()))
			{
				this.histogramOfConnectivity.put(newNet.getDegree(),this.histogramOfConnectivity.get(newNet.getDegree()) + 1);
			}
			else
			{
				this.histogramOfConnectivity.put(newNet.getDegree(),1);
			}
			
			this.netlist.add(newNet);
		}
	}
	
	public void printSummary(FileIO file) 
	{
		file.writeToFiles(".nets file summary:");
		file.writeToFiles("Total number of nets : " + this.size);
		file.writeToFiles("Total number of pins : " + this.totalPins);
		file.writeToFiles("Total number of input pins : " + this.totalInputPins);
		file.writeToFiles("Total number of output pins : " + this.totalOutputPins);
		file.writeToFiles("Total number of bidirectional pins : " + this.totalBidirectionalPins);
		file.writeToFiles("Maximum net degree : " + this.maxDegree);
		printArrayList("Net name of maximum net degree : ", this.maxDegreeName,file);	
		Set<Map.Entry<Integer, Integer>> keys = this.histogramOfConnectivity.entrySet();
		file.writeToFiles("Net Degree | Number of Nets");
		for (Iterator<Map.Entry<Integer, Integer>> i = keys.iterator();i.hasNext();)
		{
			Map.Entry<Integer, Integer> entry = (Map.Entry<Integer, Integer>) i.next();
			file.writeToFiles( String.format("%10s", entry.getKey()) + " | " + entry.getValue() );
		}
	}
	
	private void printArrayList(String headerString, ArrayList<String> list, FileIO file)
	{
		String tempString = "";
		for (Iterator<String> i = list.iterator();i.hasNext();)
		{
			tempString += " " + i.next().toString();
		}
		file.writeToFiles(headerString + " " + tempString);
	}
	public void updateNodelist(NodeList nodeList) 
	{
		for (Iterator<Nets> i = this.netlist.iterator(); i.hasNext();)
		{
			Nets thisNet = i.next();
			ArrayList<Nodes> tempInputNodes = thisNet.inputNodes;
			ArrayList<Nodes> tempOutputNodes = thisNet.outputNodes;
			int searchStatus =0;
			ArrayList<Nodes> terminalList = nodeList.getTerminalNodeList();
			for (int j =0 ; j< tempInputNodes.size();j++)
			{
				for (int k =0;k<terminalList.size();k++)
				{
					if (terminalList.get(k).getNodeName().equals(tempInputNodes.get(j).getNodeName()))
					{
						tempInputNodes.set(j, terminalList.get(k)) ;
						searchStatus = 1;
					}
				}
				if (searchStatus == 0)
				{
					tempInputNodes.remove(j);
				}
			}
			
			
			searchStatus = 0;
			for (int j =0 ; j< tempOutputNodes.size();j++)
			{
				for (int k =0;k<terminalList.size();k++)
				{
					if (terminalList.get(k).getNodeName().equals(tempOutputNodes.get(j).getNodeName()))
					{
						tempOutputNodes.set(j, terminalList.get(k));
						searchStatus = 1;
					}
				}
				if (searchStatus == 0)
				{
					tempOutputNodes.remove(j);
				}
			}
			searchStatus = 0;
			
		}
		
	}
}

