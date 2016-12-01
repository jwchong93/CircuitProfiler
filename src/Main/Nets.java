package Main;

import java.util.ArrayList;
import java.util.Collections;


public class Nets 
{
	private String netName;
	private int netDegree, netHPWL;
	public  ArrayList<Nodes> nodes = new ArrayList<Nodes>();
	//public  ArrayList<Nodes> inputNodes = new ArrayList<Nodes>();
	//public  ArrayList<Nodes> outputNodes = new ArrayList<Nodes>();
	private ArrayList<Nodes> bidirectionalNodes = new ArrayList<Nodes>();
	private ArrayList<Pins> inputPins = new ArrayList<Pins>();
	private ArrayList<Pins> outputPins = new ArrayList<Pins>();
	private ArrayList<Pins> bidirectionalPins = new ArrayList<Pins>();
	
	public Nets ()	{}
	
	public int getNumberOfOutputPins() 
	{
		return this.outputPins.size();
	}
	
	public int getNumberOfInputPins() 
	{
		return this.inputPins.size();
	}
	
	public int getNumberOfBidirectionalPins() 
	{
		return this.bidirectionalPins.size();
	}
	
	public int getTotalNumberOfPins ()
	{
		return this.inputPins.size() + 
				this.outputPins.size() + 
				this.bidirectionalPins.size();
	}
	
	public void setNetName (String newNetName)
	{
		this.netName = newNetName;
	}
	
	public void setNetDegree (int newNetDegree)
	{
		this.netDegree = newNetDegree;
	}
	
	public void addInputPin (Pins newInputPin)
	{
		this.inputPins.add (newInputPin);
	}
	/*
	public void addInputNode (Nodes newInputNode)
	{
		this.inputNodes.add(newInputNode);
	}
	*/
	public void addOutputPin (Pins outputPin)
	{
		this.outputPins.add(outputPin);
	}
	/*
	public void addOutputNode (Nodes outputNode)
	{
		this.outputNodes.add(outputNode);
	}
	*/
	public void addNode(Nodes node)
	{
		this.nodes.add(node);
	}
	
	public void addBidirectionalPin(Pins newPin) 
	{
		this.bidirectionalPins.add(newPin);
	}
	
	public void addBidirectionalNode(Nodes newNode) 
	{
		this.bidirectionalNodes.add(newNode);
	}

	public int getDegree() 
	{
		return this.netDegree;
	}
	
	public String getNetName() 
	{
		return this.netName;
	}
	
	public int getHPWL()
	{
		return this.netHPWL = calHPWL();
	}
	
	public int calHPWL()
	{
		Object xCoor, yCoor;
		ArrayList<Integer> x = new ArrayList<Integer>();
		ArrayList<Integer> y = new ArrayList<Integer>();
		ArrayList<NodeCoordinate> nCoor = new ArrayList<NodeCoordinate>();
		
		// get all nodes coordinate
		for(int i = 0; i < this.nodes.size(); i++)
			nCoor.add(this.nodes.get(i).getNodeCoordinate());
		
		// get x and y coordinate from each node
		for(int i = 0; i < nCoor.size(); i++)
		{
			x.add(nCoor.get(i).getNodeXCoordinate());
			y.add(nCoor.get(i).getNodeYCoordinate());
		}
		
		// Find max and min ...//
		if(!x.isEmpty() && !y.isEmpty())
		{
			xCoor = Collections.max(x) - Collections.min(x);
			yCoor = Collections.max(y) - Collections.min(y);
			return (Integer)xCoor + (Integer)yCoor;
		}
		else
			return 0;
	}
}
