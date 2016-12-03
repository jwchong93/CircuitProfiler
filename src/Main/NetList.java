package Main;

import java.util.*;

public class NetList
{
	private int size;
	private int totalPins;
	private int totalOutputPins;
	private int totalInputPins;
	private int maxDegree;
	private int totalBidirectionalPins;
	private long totalHPWL;
	private ArrayList<String> maxDegreeName = new ArrayList<String>();
	private TreeMap<Integer, Integer> histogramOfConnectivity = new TreeMap<Integer, Integer>();
	private ArrayList<Nets> netlist = new ArrayList<Nets>();
	
	public NetList()
	{
		this.size = 0;
		this.totalPins = 0;
		this.totalOutputPins = 0;
		this.totalInputPins = 0;
		this.maxDegree = 0;
		this.totalBidirectionalPins = 0;
	}
	
	public ArrayList<Nets> getNetlist() { return netlist; }
	
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
					newNet.addNode(newNode);
				}
				else if (tempArray[1].equals("O"))
				{
					newNet.addOutputPin(newPin);
					newNet.addOutputNode(newNode);
					newNet.addNode(newNode);
				}
				else if (tempArray[1].equals("B"))
				{
					newNet.addBidirectionalPin (newPin);
					newNet.addBidirectionalNode (newNode);
					newNet.addNode(newNode);
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
	
	public long getTotalHPWL()
	{
		this.totalHPWL = 0;
		
		for(int i = 0; i < this.netlist.size(); i++)
			this.totalHPWL += this.netlist.get(i).getNetHPWL();

		return this.totalHPWL;
	}
	
	public void sortNetListDescending() { // Sort Net list descending according to degree
		Collections.sort(this.netlist);
	}
	
	public void printNetDegree(){
		for(Nets str:netlist) {
			System.out.println(str);
		}
	}
	
	public void updateNodelist(NodeList nodeList) 
	{
		ArrayList<Nets> netsToRemove = new ArrayList<Nets> ();
		for (Iterator<Nets> i = this.netlist.iterator(); i.hasNext();)
		{
			Nets thisNet = i.next();
			ArrayList<Nodes> tempNodes = thisNet.getIO_nodes();
			ArrayList<Nodes> nonTerminalNodes = nodeList.getNonTerminalNodeList();
			
			for (int j = 0 ; j< tempNodes.size(); j++)
			{
				int nodeNameNumber = Integer.parseUnsignedInt(tempNodes.get(j).getNodeName().substring(1));
				if (nodeNameNumber < nonTerminalNodes.size()) // If bigger then out of bound
				{
					tempNodes.set(j, nonTerminalNodes.get(nodeNameNumber));
				}
				else //This is terminal node that non existed.
				{
					netsToRemove.add(thisNet);
					break; //Stop working on this net.
				}
			}
		}
		this.netlist.removeAll(netsToRemove);
	}
	
	// Copy node and net reference so to maintain data consistency
	public void updateAllConnectedNodes()
	{
		// Update all node's connected node in each net 
		for(int i = 0; i < this.netlist.size(); i++)
		{
			// Loop all I/O nodes in the net
			for(int j = 0; j < this.netlist.get(i).getIO_nodes().size(); j++)
			{
				Nodes node = this.netlist.get(i).getIO_nodes().get(j);
				node.updateConnectedNodes(this.netlist.get(i).getIO_nodes());
				node.addConnectionNets(this.netlist.get(i));
			}
		}
	}
}

