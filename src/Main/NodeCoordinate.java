package Main;

public class NodeCoordinate
{
	private int nodeXCoordinate;
	private int nodeYCoordinate;
	
	public NodeCoordinate() {}
	public void setNodeXCoordinate(int nodeXCoordinate) { this.nodeXCoordinate = nodeXCoordinate; }
	public void setNodeYCoordinate(int nodeYCoordinate) { this.nodeYCoordinate = nodeYCoordinate; }
	public int getNodeXCoordinate() { return this.nodeXCoordinate; }
	public int getNodeYCoordinate() { return this.nodeYCoordinate; }
	
	//@Overrides
	public String toString() {
	        return "(" + this.nodeXCoordinate +", "+ this.nodeYCoordinate + ")";
	}
}
