package Main;

public class Nodes 
{
	private String nodeName;
	private String nodeType;
	private int nodeWidth, nodeHeight;
	private int nodeArea;
	
	// constructor
	public Nodes() {
		this.nodeName = null;
		this.nodeType = null;
		this.nodeWidth = 0;
		this.nodeHeight = 0;
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

	// methods
	public int getArea() { return this.nodeArea; }
	public int getNodeWidth() { return this.nodeWidth; }
	public String getNodeName() { return this.nodeName; }
	public void setNodeName(String nodeName) { this.nodeName = nodeName; }
	public boolean isTerminal() { return this.nodeType == "terminal"; }
	// deep copy
	public Nodes copyNode () { 
		Nodes copyNode = new Nodes(this.nodeName, this.nodeWidth, this.nodeHeight, this.nodeType);
		return copyNode;
	}
}