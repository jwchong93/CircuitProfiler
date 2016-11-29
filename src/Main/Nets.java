package Main;

import java.util.ArrayList;


public class Nets 
{
	private String netName;
	private int netDegree;
	public  ArrayList<Nodes> inputNodes = new ArrayList<Nodes>();
	public  ArrayList<Nodes> outputNodes = new ArrayList<Nodes>();
	private ArrayList<Nodes> bidirectionalNodes = new ArrayList<Nodes>();
	private ArrayList<Pins> inputPins = new ArrayList<Pins>();
	private ArrayList<Pins> outputPins = new ArrayList<Pins>();
	private ArrayList<Pins> bidirectionalPins = new ArrayList<Pins>();
	
	
	public Nets ()
	{
	}
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
	public void addInputNode (Nodes newInputNode)
	{
		this.inputNodes.add(newInputNode);
	}
	public void addOutputPin (Pins outputPin)
	{
		this.outputPins.add(outputPin);
	}
	public void addOutputNode (Nodes outputNode)
	{
		this.outputNodes.add(outputNode);
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
}
