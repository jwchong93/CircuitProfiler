package Main;


import java.util.ArrayList;
import java.util.Iterator;

public class Graph {
	
	private int widthGuard;
	private int currentWidthSize;
	private int currentRowSize;
	private ArrayList<ArrayList<Nodes>> placementList;
	private int calculatedRowSize;

	public Graph(int widthSize, NodeList nodeList)
	{
		this.widthGuard = widthSize;
		this.currentWidthSize = 0;
		this.currentRowSize =0;
		this.calculatedRowSize = 294; 
		this.placementList = new ArrayList<ArrayList<Nodes>>();
		try
		{
			for (Iterator<Nodes> i = nodeList.getNonTerminalNodeList().iterator(); i.hasNext();)
			{
				Nodes tempNode = i.next();
				this.addNode(tempNode);
			}
		}catch (Exception e)
		{
			System.out.println(e.getMessage());
		}
		
	}
	
	public Nodes nodeInThisLocation (int x, int y)
	{
		Nodes tempNode;
		NodeCoordinate tempCoordinate;
		for (int k =0; k < this.placementList.size() ;k++)
		{
			for (Iterator<Nodes> i = this.placementList.get(k).iterator(); i.hasNext();)
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
		}
		return null;
	}
	public void swapNodes (Nodes node1, Nodes node2)
	{
		int yIndex1 = node1.getNodeCoordinate().getNodeYCoordinate()/36;
		int yIndex2 = node2.getNodeCoordinate().getNodeYCoordinate()/36;
		int xIndex1 = this.placementList.get(yIndex1).indexOf(node1);
		int xIndex2 = this.placementList.get(yIndex2).indexOf(node2);
		Nodes tempNode1 = this.placementList.get(yIndex1).get(xIndex1);
		Nodes tempNode2 = this.placementList.get(yIndex2).get(xIndex2);
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
		NodeCoordinate coordinateOfTheNode = node.getNodeCoordinate();
		int rowNumber = coordinateOfTheNode.getNodeYCoordinate()/36;
		int locationInPlacementList = this.placementList.get(rowNumber).indexOf(node);
		Nodes tempNode = this.placementList.get(rowNumber).get(locationInPlacementList);
		if (coordinateOfTheNode.getNodeYCoordinate() + width > this.widthGuard)
		{
			return false; //This node at the end, cant switch
		}
		else
		{
			tempNode.setNodeCoordinate(coordinateOfTheNode.getNodeXCoordinate(), coordinateOfTheNode.getNodeYCoordinate() + width);
			return true; //This node can be switch.
		}
		
		
	}
	
	public boolean addNode(Nodes newNode)
	{
		if (this.calculateTheWidthGuard(newNode))
		{
			this.updateNodeCoordinate (newNode,this.currentWidthSize,this.currentRowSize);
			this.currentWidthSize += newNode.getNodeWidth();
			this.placementList.get(this.currentRowSize/36).add(newNode);
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
