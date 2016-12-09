package Main;

import java.util.ArrayList;

public class Nodes implements Comparable<Nodes>
{
	private String nodeName = null;;
	private String nodeType = null;;
	private int nodeWidth = 0, nodeHeight = 0;
	private int nodeArea;
	private NodeCoordinate nodeLocation = new NodeCoordinate();;
	
	private long  nodeTotalnetHPWL;
	private boolean lock=false;
	private NodeCoordinate nodeZFT = new NodeCoordinate();
	private ArrayList<Nodes> connectedNodes = new ArrayList<Nodes>();
	private ArrayList<Nets> connectionNets = new ArrayList<Nets>();
	private int nodeDegree=0;
	
	// constructor
	public Nodes() {
		this.nodeArea = this.nodeHeight*this.nodeWidth;
	}

	// constructor overload for 4 arguments
	public Nodes(String nodeName, int nodeWidth, int nodeHeight, String nodeType) {
		this.nodeName = nodeName;
		this.nodeWidth = nodeWidth;
		this.nodeHeight = nodeHeight;
		this.nodeType = nodeType;
		this.nodeArea = this.nodeHeight*this.nodeWidth;
	}

	// get methods
	public int getArea() { return this.nodeArea; }
	public int getNodeWidth() { return this.nodeWidth; }
	public int getNodeDegree() { return this.nodeDegree; }
	public long getnodeTotalnetHPWL() { return this.nodeTotalnetHPWL; }
	public String getNodeName() { return this.nodeName; }
	public NodeCoordinate getNodeCoordinate() { return this.nodeLocation; }
	public ArrayList<Nodes> getConnectedNodes() { return this.connectedNodes; }
	public ArrayList<Nets> getConnectionNets() { return this.connectionNets; }
	
	// set methods
	public void setNodeDegree(int nodeDegree) { this.nodeDegree = nodeDegree; }
	public void setNodeName(String nodeName) { this.nodeName = nodeName; }
	public void addConnectionNets(Nets net) { this.getConnectionNets().add(net); }
	public void setNodeCoordinate(int nodeXCoordinate, int nodeYCoordinate) {
		this.nodeLocation.setNodeXCoordinate(nodeXCoordinate); 
		this.nodeLocation.setNodeYCoordinate(nodeYCoordinate);
	}
	
	// is methods
	public boolean isTerminal() { return this.nodeType == "terminal"; }
	public boolean isLock() { return lock; }
	
	// control methods
	public void lockNode() { this.lock = true; }
	public void unlockNode() { this.lock = false; }
	
	// other methods
	// Contain a list of node that connected to this node
	public void updateConnectedNodes(ArrayList<Nodes> io_nodes)
	{
		// loop nodes in a net
		for(int i = 0; i < io_nodes.size(); i++)
		{
			if(!this.connectedNodes.contains(io_nodes.get(i)) && io_nodes.get(i).getNodeName().compareTo(this.getNodeName()) != 0)
			{
				this.connectedNodes.add(io_nodes.get(i));
			}
		}
	}
	
	// calculate the HPWL of net that connected to this node
	public long calcNodeAllNetHPWL(NetList netlist)
	{
		long hpwl = 0;
		
		for(int i = 0; i < netlist.getNetlist().size(); i++)
			hpwl += netlist.getNetlist().get(i).getNetHPWL();
		
		this.nodeTotalnetHPWL = hpwl; 
		return this.nodeTotalnetHPWL;
	}
	
	// calculate the new HPWL of net that connected to this node with new location
	public long calcNewNodeAllNetHPWL1(Graph floorplan, NetList netlist, int x, int y)
	{
		ArrayList<Integer> rowList = new ArrayList<Integer>();
		rowList.add(this.nodeLocation.getNodeYCoordinate()/36);
		floorplan.updateNodeCoordinate(this, x, y);
		rowList.add(this.nodeLocation.getNodeYCoordinate()/36);
		floorplan.legalizeNodes(rowList);
		
		long hpwl = 0;
		
		for(int i = 0; i < netlist.getNetlist().size(); i++)
			hpwl += netlist.getNetlist().get(i).getNetHPWL();
		
		return hpwl;
	}
	
	public long calcNewNodeAllNetHPWL2(Graph floorplan, Nodes node_cond, NetList netlist)
	{
		floorplan.swapNodes(this, node_cond);
		ArrayList<Integer> rowList = new ArrayList<Integer>();
		rowList.add(this.nodeLocation.getNodeYCoordinate()/36);
		rowList.add(node_cond.getNodeCoordinate().getNodeYCoordinate()/36);
		floorplan.legalizeNodes(rowList);
		
		long hpwl = 0;
		
		for(int i = 0; i < netlist.getNetlist().size(); i++)
			hpwl += netlist.getNetlist().get(i).getNetHPWL();
		
		return hpwl;
	}
	
	// Compute ZFT location
	public NodeCoordinate computeAndReturnZFT() {
		double x_zft = 0, y_zft = 0;
		if(this.connectedNodes.isEmpty()) {
			this.nodeZFT = this.nodeLocation;
			return this.nodeZFT;
		}
		if(this.connectedNodes.size()==1) {
			this.nodeZFT.setNodeXCoordinate(this.connectedNodes.get(0).getNodeCoordinate().getNodeXCoordinate()+this.connectedNodes.get(0).getNodeWidth());
			this.nodeZFT.setNodeYCoordinate(this.connectedNodes.get(0).getNodeCoordinate().getNodeYCoordinate());
			return this.nodeZFT;
		}
		int totalweight=0;
		totalweight = this.connectedNodes.size();
		for(int i = 0; i < this.connectedNodes.size(); i++){
			x_zft += this.connectedNodes.get(i).getNodeCoordinate().getNodeXCoordinate();
			y_zft += this.connectedNodes.get(i).getNodeCoordinate().getNodeYCoordinate();
		}
		this.nodeZFT.setNodeXCoordinate((int)Math.round(x_zft/totalweight));
		int y = (int)Math.round(y_zft/totalweight);
		double h = (double)y/36.0;
		this.nodeZFT.setNodeYCoordinate((int)Math.round(h)*36);
		//System.out.println("ZFT = " + this.nodeZFT);
		return this.nodeZFT;
	}
	
	// Compare the NodeCoordinate is same location or not
	public boolean isSameLocation(NodeCoordinate nc) {
		if(this.nodeLocation.getNodeXCoordinate() <= nc.getNodeXCoordinate() && nc.getNodeXCoordinate() <= (this.nodeLocation.getNodeXCoordinate()+this.getNodeWidth())
				&&  this.nodeLocation.getNodeYCoordinate() == nc.getNodeYCoordinate())
			return true;
		else
			return false;
	}
	
	//@Overrides
	public int compareTo(Nodes compareNod) {
		// Sort according to descending order of node degree.
		return compareNod.getNodeDegree() - this.nodeDegree;
		// Sort according to ascending order of node degree.
		//return this.nodeDegree - compareNod.getNodeDegree();
	}
	
	//@Overrides
	public String toString() {
	        return "[nodeName = " + this.nodeName + " " + this.getNodeCoordinate() + " NodeDegree: "+ this.nodeDegree + " NodeWidth: " + this.nodeWidth + "]";
	}
}