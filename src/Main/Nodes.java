package Main;

import java.util.ArrayList;

public class Nodes implements Comparable<Nodes>
{
	private String nodeName;
	private String nodeType;
	private int nodeWidth, nodeHeight;
	private int nodeArea;
	private NodeCoordinate nodeLocation;
	
	private long  nodeTotalnetHPWL;
	private boolean lock=false;
	private NodeCoordinate nodeZFT = new NodeCoordinate();
	private ArrayList<Nodes> connectedNodes = new ArrayList<Nodes>();
	private ArrayList<Nets> connectionNets = new ArrayList<Nets>();
	private int nodeDegree=0;
	
	// constructor
	public Nodes() {
		this.nodeName = null;
		this.nodeType = null;
		this.nodeWidth = 0;
		this.nodeHeight = 0;
		this.nodeArea = this.nodeHeight*this.nodeWidth;
		this.nodeLocation = new NodeCoordinate();
		this.lock=false;
	}

	// constructor overload for 4 arguments
	public Nodes(String nodeName, int nodeWidth, int nodeHeight, String nodeType) {
		this.nodeName = nodeName;
		this.nodeWidth = nodeWidth;
		this.nodeHeight = nodeHeight;
		this.nodeType = nodeType;
		this.nodeArea = this.nodeHeight*this.nodeWidth;
		this.nodeLocation = new NodeCoordinate();
		this.lock=false;
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
	public void lockNode() { this.lock = true; }
	public void unlockNode() { this.lock = false; }
	public boolean isLock() { return lock; }
	
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
	
	public long getnodeTotalnetHPWL() { return this.nodeTotalnetHPWL; }
	public NodeCoordinate getNodeCoordinate() { return this.nodeLocation; }
	public ArrayList<Nodes> getConnectedNodes() { return this.connectedNodes; }
	
	// Contain a list of node that connected to this node
	public void updateConnectedNodes(ArrayList<Nodes> io_nodes)
	{
		// loop nodes in a net
		for(int i = 0; i < io_nodes.size(); i++)
		{
			if(!this.connectedNodes.contains(io_nodes.get(i)))
			{
				this.connectedNodes.add(io_nodes.get(i));
				this.nodeDegree++;
			}
		}
	}
	
	// calculate the HPWL of net that connected to this node
	public long calcNodeAllNetHPWL()
	{
		long hpwl = 0;
		
		for(int i = 0; i < this.connectionNets.size(); i++)
			hpwl += this.connectionNets.get(i).getNetHPWL();
		
		this.nodeTotalnetHPWL = hpwl; 
		return this.nodeTotalnetHPWL;
	}
	
	// Compute ZFT location
	public NodeCoordinate computeAndReturnZFT() {
		double x_zft = 0, y_zft = 0;
		if(this.connectedNodes.isEmpty()) {
			this.nodeZFT = this.nodeLocation;
			return this.nodeZFT;
		}
		for(int i = 0; i < this.connectedNodes.size(); i++){
			x_zft += this.connectedNodes.get(i).getNodeCoordinate().getNodeXCoordinate();
			y_zft += this.connectedNodes.get(i).getNodeCoordinate().getNodeYCoordinate();
		}
		this.nodeZFT.setNodeXCoordinate((int)Math.round(x_zft/this.connectedNodes.size()));
		int y = (int)Math.round(y_zft/this.connectedNodes.size());
		double h = (double)y/36.0;
		this.nodeZFT.setNodeYCoordinate((int)Math.round(h)*36);
		return this.nodeZFT;
	}
	
	// Compare the NodeCoordinate is same location or not
	public boolean isSameLocation(NodeCoordinate nc) {
		if(this.nodeLocation.getNodeXCoordinate() == nc.getNodeXCoordinate() &&  this.nodeLocation.getNodeYCoordinate() == nc.getNodeYCoordinate())
			return true;
		else
			return false;
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