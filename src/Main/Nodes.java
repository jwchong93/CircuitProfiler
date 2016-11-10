package Main;

public class Nodes 
{
	String nodeName;
	String nodeType;
	int nodeWidth, nodeHeight;
	private int nodeArea;
	
	// constructor
	public Nodes() {
		this.nodeName = null;
		this.nodeType = null;
		this.nodeWidth = 0;
		this.nodeHeight = 0;
		this.nodeArea = this.nodeHeight*this.nodeWidth;
	}
	// constructor overload
	public Nodes(String nodeName, int nodeWidth, int nodeHeight, String nodeType) {
		this.nodeName = nodeName;
		this.nodeWidth = nodeWidth;
		this.nodeHeight = nodeHeight;
		this.nodeType = nodeType;
		this.nodeArea = this.nodeHeight*this.nodeWidth;
	}

	// methods
	public int getArea () { return this.nodeArea; }
	public boolean isTerminal() { return this.nodeType == "terminal"; }
	public Nodes copyNode () {
		Nodes copyNode = new Nodes(this.nodeName, this.nodeWidth, this.nodeHeight, this.nodeType);
		return copyNode;
	}
}