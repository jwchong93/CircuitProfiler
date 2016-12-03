package Main;

import java.util.ArrayList;

public class Nodes implements Comparable<Nodes>
{
	private String nodeName;
	private String nodeType;
	private int nodeWidth, nodeHeight;
	private int nodeArea;
	private NodeCoordinate nodeLocation;
	private ArrayList<Nodes> connectedNodes = new ArrayList<Nodes>();
	private int nodeDegree;
	private ArrayList<Nets> connectionNets = new ArrayList<Nets>();
	
	// constructor
	public Nodes() {
		this.nodeName = null;
		this.nodeType = null;
		this.nodeWidth = 0;
		this.nodeHeight = 0;
		this.nodeArea = this.nodeHeight*this.nodeWidth;
		this.nodeLocation = new NodeCoordinate();
		this.nodeDegree = 0;
	}

	// constructor overload for 4 arguments
	public Nodes(String nodeName, int nodeWidth, int nodeHeight, String nodeType) {
		this.nodeName = nodeName;
		this.nodeWidth = nodeWidth;
		this.nodeHeight = nodeHeight;
		this.nodeType = nodeType;
		this.nodeArea = this.nodeHeight*this.nodeWidth;
		this.nodeLocation = new NodeCoordinate();
		this.nodeDegree = 0;
	}

	// methods
	public int getArea() { return this.nodeArea; }
	public int getNodeWidth() { return this.nodeWidth; }
	public String getNodeName() { return this.nodeName; }
	public void setNodeName(String nodeName) { this.nodeName = nodeName; }
	public boolean isTerminal() { return this.nodeType == "terminal"; }
	public int getNodeDegree() { return this.nodeDegree; }
	public void setNodeDegree(int nodeDegree) { this.nodeDegree = nodeDegree; }
	public ArrayList<Nets> getConnectionNets() { return this.connectionNets; }
	public void addConnectionNets(Nets net) { this.getConnectionNets().add(net); }
	
	// deep copy
	public Nodes copyNode () { 
		Nodes copyNode = new Nodes(this.nodeName, this.nodeWidth, this.nodeHeight, this.nodeType);
		return copyNode;
	}
	
	//update node coordinate and get node coordinate 
	public void setNodeCoordinate(int nodeXCoordinate, int nodeYCoordinate) {
		this.nodeLocation.setNodeXCoordinate(nodeXCoordinate); 
		this.nodeLocation.setNodeYCoordinate(nodeYCoordinate);
	}
	
	public NodeCoordinate getNodeCoordinate() { return this.nodeLocation; }
	public ArrayList<Nodes> getConnectedNodes() { return this.connectedNodes; }
	public void addConnectedNode(Nodes node) { this.connectedNodes.add(node); }
	
	// Contain a list of node that connected to this node
	public void updateConnectedNodes(ArrayList<Nodes> io_nodes, Nets net)
	{
		// loop nodes in a net
		for(int i = 0; i < io_nodes.size(); i++)
		{
			if(!this.connectedNodes.contains(io_nodes.get(i)))
			{
				this.addConnectedNode(io_nodes.get(i));
				this.nodeDegree++;
			}
		}
	}
	
	// calculate the HPWL of net that connected to this node
	public int getNodeAllNetHPWL()
	{
		int hpwl = 0;
		
		for(int i = 0; i < this.connectionNets.size(); i++)
			hpwl += this.connectionNets.get(i).getNetHPWL();
		
		return hpwl; 
	}
	
	//@Overrides
	public int compareTo(Nodes compareNod) {
		// Sort according to ascending order of node degree.
		return compareNod.getNodeDegree() - this.nodeDegree;
	}
	
	//@Overrides
	public String toString() {
	        return "[nodeName = " + this.nodeName + " " + this.getNodeCoordinate() + " "+ this.nodeDegree + "]";
	}
}