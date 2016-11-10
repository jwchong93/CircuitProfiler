package Main;

import java.util.ArrayList;


public class Nets 
{
	public String netName;
	public int netDegree;
	public ArrayList<Nodes> inputNodes = new ArrayList<Nodes>();
	public Nodes outputNodes;
	public ArrayList<Pins> inputPins = new ArrayList<Pins>();
	public Pins outputPins;
	public Nets ()
	{
		this.outputNodes =null;
		this.outputPins = null;
		
	}


	
	
}
