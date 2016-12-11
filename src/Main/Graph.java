package Main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.Iterator;

public class Graph {
	
	private int widthGuard;
	private int currentWidthSize;
	private int currentRowSize;
	private ArrayList<ArrayList<Nodes>> placementList;
	private int calculatedRowSize;
	public static final int rowSeperation = 36;
	
	public Graph(NodeList nodeList)
	{
		this.currentWidthSize = 0;
		this.currentRowSize =0;
		this.calculatedRowSize = ((int)Math.ceil(Math.sqrt(nodeList.getTotalNonTerminalWidth()/rowSeperation))); 
		this.widthGuard = ((int)Math.ceil(Math.sqrt(nodeList.getTotalNonTerminalWidth()*rowSeperation))); 
		//this.widthGuard = this.calculatedRowSize * rowSeperation;
		this.placementList = new ArrayList<ArrayList<Nodes>>();
		ArrayList<Nodes> tempNodeList = nodeList.getNonTerminalNodeList();
		try
		{
			for (Iterator<Nodes> i = tempNodeList.iterator(); i.hasNext();)
			{
				Nodes tempNode = i.next();
				this.addNode(tempNode);
			}
		}catch (ConcurrentModificationException e)
		{
			System.out.println(e.getMessage());
			System.out.println("failed");
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
				if (tempCoordinate.getNodeXCoordinate()+tempNode.getNodeWidth()-1>= x &&
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
		int yIndex1 = node1.getNodeCoordinate().getNodeYCoordinate()/rowSeperation;
		int yIndex2 = node2.getNodeCoordinate().getNodeYCoordinate()/rowSeperation;
		int xIndex1 = this.placementList.get(yIndex1).indexOf(node1);
		int xIndex2 = this.placementList.get(yIndex2).indexOf(node2);
		Nodes tempNode1 = this.placementList.get(yIndex1).get(xIndex1);
		Nodes tempNode2 = this.placementList.get(yIndex2).get(xIndex2);		
		int tempCoordinate1x = tempNode1.getNodeCoordinate().getNodeXCoordinate();
		int tempCoordinate2x = tempNode2.getNodeCoordinate().getNodeXCoordinate();
		int tempCoordinate1y = tempNode1.getNodeCoordinate().getNodeYCoordinate();
		int tempCoordinate2y = tempNode2.getNodeCoordinate().getNodeYCoordinate();
		tempNode1.setNodeCoordinate(tempCoordinate2x, tempCoordinate2y);
		tempNode2.setNodeCoordinate(tempCoordinate1x, tempCoordinate1y);
		this.placementList.get(yIndex1).remove(node1);
		this.placementList.get(yIndex2).add(node1);
		this.placementList.get(yIndex2).remove(node2);
		this.placementList.get(yIndex1).add(node2);
		this.sortListBy(this.placementList.get(yIndex1),this.placementList.get(yIndex1).size());
		this.sortListBy(this.placementList.get(yIndex2),this.placementList.get(yIndex2).size());
	}
	
	public void changeCoordinate (Nodes node, int x, int y)
	{
		NodeCoordinate newCoordinate = node.getNodeCoordinate();
		newCoordinate.setNodeXCoordinate(x);
		newCoordinate.setNodeYCoordinate(y);
	}
	
	public boolean addNode(Nodes newNode)
	{
		if (this.calculateWithWidthGuard(newNode))
		{
			this.updateNodeCoordinateOnly(newNode,this.currentWidthSize,this.currentRowSize);
			this.currentWidthSize += newNode.getNodeWidth();
			if (this.placementList.size() < (this.currentRowSize/rowSeperation) + 1 )
			{
				this.placementList.add(new ArrayList<Nodes>());
			}
			this.placementList.get(this.currentRowSize/rowSeperation).add(newNode);
			return true;
		}
		else
		{
			return false; //Fulled and cannot place already
		}
	}
	
	private boolean calculateWithWidthGuard(Nodes newNode) 
	{
		if (this.currentWidthSize + newNode.getNodeWidth() <= this.widthGuard)
		{
			return true;
		}
		else
		{
			this.currentRowSize += rowSeperation;
			this.currentWidthSize = 0;
			if (this.currentRowSize/rowSeperation > this.calculatedRowSize)
			{
				return false; //Fulled.
			}
			else
			{
				return true;
			}
		}
	}

	public void updateNodeCoordinate(Nodes newNode, int x, int y) 
	{
		NodeCoordinate oldCoordinate = newNode.getNodeCoordinate();
		this.placementList.get(oldCoordinate.getNodeYCoordinate()/rowSeperation).remove(newNode);
		newNode.setNodeCoordinate(x, y);
		this.placementList.get(y/rowSeperation).add(newNode);
		this.sortListBy(this.placementList.get(y/rowSeperation),this.placementList.get(y/rowSeperation).size());
	}

	public void updateNodeCoordinateOnly(Nodes newNode, int x, int y) 
	{
		newNode.setNodeCoordinate(x, y);
	}

	public void legalizeNodes (ArrayList<Integer> rowList)
	{
		for (int row = 0; row < rowList.size(); row++)
		{
			ArrayList<Nodes> tempList = this.placementList.get(rowList.get(row));
			this.sortListBy(tempList,tempList.size());
			Nodes leftNode = null;
			Nodes rightNode = null;
			//ArrayList<Nodes> nodeToRemove = new ArrayList<Nodes>();
			NodeCoordinate leftNodeCoordinate,rightNodeCoordinate;
			int lengthToShift;
			//int accumulatedWidthSize = 0;
			//for (Iterator<Nodes> i = tempList.iterator(); i.hasNext();)
			for(int i = 0; i<tempList.size();i++)
			{
				rightNode = tempList.get(i);
				//accumulatedWidthSize += rightNode.getNodeWidth();
				if (leftNode != null) //First iteration, first node.
				{
					leftNodeCoordinate = leftNode.getNodeCoordinate();
					rightNodeCoordinate = rightNode.getNodeCoordinate();
					lengthToShift = leftNodeCoordinate.getNodeXCoordinate() + leftNode.getNodeWidth() - rightNodeCoordinate.getNodeXCoordinate();
					
					if (leftNodeCoordinate.getNodeXCoordinate() + leftNode.getNodeWidth() > rightNodeCoordinate.getNodeXCoordinate())
						//Overlapped, shift the right cell, else do nothing
					{
						this.moveNodeByWidth(rightNode, lengthToShift, rowList.get(row));
					}
					else if(leftNodeCoordinate.getNodeXCoordinate() + leftNode.getNodeWidth() < rightNodeCoordinate.getNodeXCoordinate())
					{
						this.updateNodeCoordinate(rightNode, leftNodeCoordinate.getNodeXCoordinate() + leftNode.getNodeWidth() , rightNodeCoordinate.getNodeYCoordinate());
					}
					else if(leftNodeCoordinate.getNodeXCoordinate() + leftNode.getNodeWidth() == rightNodeCoordinate.getNodeXCoordinate())
					{
						
					}
					else {
						System.out.println("Other Condition....!!!");
					}
				}
				leftNode = rightNode;
			}
			//tempList.removeAll(nodeToRemove);
			this.sortListBy(tempList,tempList.size());
		}
	}
	
	public boolean moveNodeByWidth (Nodes node, int width, int row)
	{
		//System.out.println(node.toString());
		NodeCoordinate coordinateOfTheNode = node.getNodeCoordinate();
		int locationInPlacementList = this.placementList.get(row).indexOf(node);
		Nodes tempNode = this.placementList.get(row).get(locationInPlacementList);
			tempNode.setNodeCoordinate(coordinateOfTheNode.getNodeXCoordinate() + width, coordinateOfTheNode.getNodeYCoordinate());
			return true; //This node can be switch.
	}

	private void sortListBy(ArrayList<Nodes> tempList, int max_size) 
	{
		//Bubble sort algorithm
		int firstXValue,secondXValue;
		for (int k =1; k < max_size; k++)
		{
			firstXValue = tempList.get(k-1).getNodeCoordinate().getNodeXCoordinate();
			secondXValue = tempList.get(k).getNodeCoordinate().getNodeXCoordinate();
			if (firstXValue > secondXValue)
			{
				Collections.swap(tempList, k-1, k);
			}
		}
		if (max_size >= 1)
		{
			this.sortListBy(tempList, max_size - 1);
		}
	}

	private void reduceTheWidthSpacing(ArrayList<Nodes> tempList, int lengthToShift) 
	{
		int spaceToGet = lengthToShift;
		Nodes rightNode,leftNode =null ;
		NodeCoordinate rightCoordinate,leftCoordinate;
		for (Iterator<Nodes> i = tempList.iterator(); i.hasNext();)
		{
			rightNode = i.next();
			rightCoordinate = rightNode.getNodeCoordinate();
			if (spaceToGet > 0)
			{
				if (leftNode == null)
				{
					//First Iteration
					if (rightCoordinate.getNodeXCoordinate() !=0)
					{
						rightNode.setNodeCoordinate(rightCoordinate.getNodeXCoordinate()-1,rightCoordinate.getNodeYCoordinate());
						spaceToGet --;
					}
				}
				else
				{
					leftCoordinate = leftNode.getNodeCoordinate();
					if (leftCoordinate.getNodeXCoordinate()+leftNode.getNodeWidth()< rightCoordinate.getNodeXCoordinate())
					{
						rightNode.setNodeCoordinate(rightCoordinate.getNodeXCoordinate()-1, rightCoordinate.getNodeYCoordinate());
						spaceToGet--;
					}
				}
				leftNode = rightNode;
			}
			else
			{
				rightNode.setNodeCoordinate(rightCoordinate.getNodeXCoordinate()-lengthToShift, rightCoordinate.getNodeYCoordinate());
			}
		}
		if (spaceToGet > 0)
		{
			this.reduceTheWidthSpacing(tempList, spaceToGet);
		}
	}

	public void updateNodeCoordinateNextFreePos(Nodes thisNodes, int nodeXCoordinate, int nodeYCoordinate) 
	{
		int tempX = nodeXCoordinate;
		int tempY = nodeYCoordinate;
		Nodes node = this.nodeInThisLocation(tempX,tempY);
		if (node ==null)
		{
			return;
		}
		else
		{
			NodeCoordinate coordinate = node.getNodeCoordinate();
			int rowIndex = (int)(coordinate.getNodeYCoordinate()/rowSeperation);
			ArrayList<Nodes> rowList = this.placementList.get(rowIndex);
			int widthIndex = rowList.indexOf(node);
			while (node.isLock())
			{
				if (widthIndex >= rowList.size() -1)
				{
					widthIndex =0;
					if (rowIndex >= this.placementList.size()-1)
					{
						rowIndex =0;
					}
					else
					{
						rowIndex +=1;
					}
				}
				else
				{
					widthIndex += 1;
				}
				rowList = this.placementList.get(rowIndex);
				node = rowList.get(widthIndex);
				
			}
			coordinate = node.getNodeCoordinate();
			thisNodes.setNodeCoordinate(coordinate.getNodeXCoordinate(), coordinate.getNodeYCoordinate());
			this.placementList.get((int)(thisNodes.getNodeCoordinate().getNodeYCoordinate()/rowSeperation)).remove(thisNodes);
			this.placementList.get((int)(coordinate.getNodeYCoordinate()/rowSeperation)).add(thisNodes);
		}
	}
	
	public void printRowNodeListCoor(int row, FileIO file)
	{
		ArrayList<Nodes> nodes = this.getRowNodeList(row);
		
		for(int i = 0; i < nodes.size(); i++)
		{
			file.writeToFiles(nodes.get(i).toString() + nodes.get(i).getNodeWidth());
		}
	}
	
	public void printFloorPlan()
	{
		for(int i = 0; i < this.placementList.size(); i++)
		{
			for(int j = 0; j < this.placementList.get(i).size(); j++)
			{
				System.out.println(this.placementList.get(i).get(j));
			}
		}
	}
	
	public void showAllRowLength()
	{
		System.out.println("The width of each row: "+ this.widthGuard);
		for (int row =0; row<this.placementList.size();row++)
		{
			System.out.println("Row: "+row+"; Width size: "+ (this.placementList.get(row).get(this.placementList.get(row).size()-1).getNodeCoordinate().getNodeXCoordinate()
					+this.placementList.get(row).get(this.placementList.get(row).size()-1).getNodeWidth()));
		}
	}
	
	public ArrayList<ArrayList<Nodes>> getPlacementList() { return this.placementList; }
	public ArrayList<Nodes> getRowNodeList(int row) { return this.placementList.get(row); }
	public Nodes getNodeInARow(int row, int nodeIndex) { return this.placementList.get(row).get(nodeIndex); }
	public void addArrayNodeList(ArrayList<Nodes> nodes) { this.placementList.add(nodes); }
	public void addNodeToRow(Nodes node, int row) { this.placementList.get(row).add(node); }
}
