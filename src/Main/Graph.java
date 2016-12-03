package Main;


import java.util.ArrayList;
import java.util.Iterator;

public class Graph {
	
	private int widthGuard;
	private int currentWidthSize;
	private int currentRowSize;
	private ArrayList<Nodes> placementList;
	private int calculatedRowSize;

	public Graph(int widthSize, NodeList nodeList)
	{
		this.widthGuard = widthSize;
		this.currentWidthSize = 0;
		this.currentRowSize =0;
		this.calculatedRowSize = 294; 
		this.placementList = new ArrayList<Nodes>(nodeList.getNonTerminalNodeList());
		
	}
	
	public Nodes nodeInThisLocation (int x, int y)
	{
		Nodes tempNode;
		NodeCoordinate tempCoordinate;
		for (Iterator<Nodes> i = this.placementList.iterator(); i.hasNext();)
		{
			tempNode = i.next();
			tempCoordinate = tempNode.getNodeCoordinate();
			if (tempCoordinate.getNodeXCoordinate()+tempNode.getNodeWidth()>= x &&
				tempCoordinate.getNodeXCoordinate() <= x &&
				tempCoordinate.getNodeYCoordinate() == y	)
			{
				return tempNode;
			}
		}
		return null;
	}
	public void swapNodes (Nodes node1, Nodes node2)
	{
		int index1 = this.placementList.indexOf(node1);
		int index2 = this.placementList.indexOf(node2);
		Nodes tempNode1 = this.placementList.get(index1);
		Nodes tempNode2 = this.placementList.get(index2);
		NodeCoordinate tempCoordinate1 = tempNode1.getNodeCoordinate();
		NodeCoordinate tempCoordinate2 = tempNode2.getNodeCoordinate();
		tempNode1.setNodeCoordinate(tempCoordinate2.getNodeXCoordinate(), tempCoordinate2.getNodeYCoordinate());
		tempNode2.setNodeCoordinate(tempCoordinate1.getNodeXCoordinate(), tempCoordinate1.getNodeYCoordinate());
		
		
	}
	
	public void changeCoordinate (Nodes node, int x, int y)
	{
		NodeCoordinate newCoordinate = node.getNodeCoordinate();
		newCoordinate.setNodeXCoordinate(x);
		newCoordinate.setNodeYCoordinate(y);
	}
	
	
	public boolean moveNodeByWidth (Nodes node, int width)
	{
		int locationInPlacementList = this.placementList.indexOf(node);
		Nodes tempNode = this.placementList.get(locationInPlacementList);
		NodeCoordinate coordinate = node.getNodeCoordinate();
		if (coordinate.getNodeYCoordinate() + width > this.widthGuard)
		{
			return false; //This node at the end, cant switch
		}
		else
		{
			tempNode.setNodeCoordinate(coordinate.getNodeXCoordinate(), coordinate.getNodeYCoordinate() + width);
			return true; //This node can be switch.
		}
		
		
	}
	public boolean addNode(Nodes newNode)
	{
		if (this.calculateTheWidthGuard(newNode))
		{
			this.updateNodeCoordinate (newNode,this.currentWidthSize,this.currentRowSize);
			this.currentWidthSize += newNode.getNodeWidth();
			this.placementList.add(newNode);
			return true;
		}
		else
		{
			return false; //Fulled and cant place d 
		}
	}
	
	private boolean calculateTheWidthGuard(Nodes newNode) 
	{
		if (this.currentWidthSize + newNode.getNodeWidth() <= this.widthGuard)
		{
			return true;
		}
		else
		{
			this.currentRowSize += 36;
			this.currentWidthSize = 0;
			if (this.currentRowSize/36 > this.calculatedRowSize)
			{
				return false; //Fulled.
			}
			else
			{
				return true;
			}
		}
	}

	private void updateNodeCoordinate(Nodes newNode, int x, int y) 
	{
		
		newNode.setNodeCoordinate(x, y);
		
	}


}
