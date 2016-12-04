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
		this.calculatedRowSize = ((int)Math.ceil(Math.sqrt(nodeList.gettotalNonTerminalWidth()/rowSeperation))); 
		this.widthGuard = this.calculatedRowSize * rowSeperation;
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
	}
	
	public void changeCoordinate (Nodes node, int x, int y)
	{
		NodeCoordinate newCoordinate = node.getNodeCoordinate();
		newCoordinate.setNodeXCoordinate(x);
		newCoordinate.setNodeYCoordinate(y);
	}
	
	public boolean moveNodeByWidth (Nodes node, int width, int row)
	{
		//System.out.println(node.toString());
		NodeCoordinate coordinateOfTheNode = node.getNodeCoordinate();
		//int rowNumber = coordinateOfTheNode.getNodeYCoordinate()/rowSeperation;
		int locationInPlacementList = this.placementList.get(row).indexOf(node);
//		if(locationInPlacementList == -1) {
//			System.out.println(node);
//			System.out.println(row);
//			for(int i=0;i<294;i++) {
//			System.out.println(i + " " + this.placementList.get(i).indexOf(node));
//			}
//		}
		Nodes tempNode = this.placementList.get(row).get(locationInPlacementList);
		if (coordinateOfTheNode.getNodeXCoordinate() + width > this.widthGuard)
		{
			return false; //This node at the end, cannot switch
		}
		else
		{
			tempNode.setNodeCoordinate(coordinateOfTheNode.getNodeXCoordinate() + width, coordinateOfTheNode.getNodeYCoordinate());
			return true; //This node can be switch.
		}
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
		newNode.setNodeCoordinate(x, y);
		NodeCoordinate oldCoordinate = newNode.getNodeCoordinate();
		this.placementList.get(oldCoordinate.getNodeYCoordinate()/rowSeperation).remove(newNode);
		newNode.setNodeCoordinate(x, y);
		this.placementList.get(y/rowSeperation).add(newNode);
	}
	/*
	public Graph(NodeList nodeList, int index)
	{
		this.currentWidthSize = 0;
		this.currentRowSize =0;
		this.calculatedRowSize = ((int)Math.ceil(Math.sqrt(nodeList.gettotalNonTerminalWidth()/rowSeperation))); 
		this.widthGuard = this.calculatedRowSize * rowSeperation;
		this.placementList = new ArrayList<ArrayList<Nodes>>();
		ArrayList<Nodes> tempNodeList = nodeList.getNonTerminalNodeList();
		this.addArrayNodeList(new ArrayList<Nodes>());
		this.placeNodes(tempNodeList, 0, 0, 0);
	}
	
	// Nested node placer
	public void placeNodes(ArrayList<Nodes> nodes, int row, int startingPoint, int index)
	{
		boolean exceedRowWidth = false;
		
		if(row % 2 == 0)
		{
			//left to right place
			exceedRowWidth = this.placeDirection(0, nodes.get(index), startingPoint, row);
			if(exceedRowWidth)
			{
				row++;
				if(row <= this.calculatedRowSize)
					this.addArrayNodeList(new ArrayList<Nodes>());
				placeNodes(nodes, row, 0, 0);
			}
			else
			{
				startingPoint += nodes.get(index).getNodeWidth();
				this.addNodeToRow(nodes.get(index), row);
				if(index < nodes.size() - 1)
				{
					if(startingPoint > this.widthGuard)
					{
						this.addArrayNodeList(new ArrayList<Nodes>());
						placeNodes(nodes, row+1, this.widthGuard, index+1);
					}
					else
						placeNodes(nodes, row, startingPoint, index+1);
				}
			}
		}
		else
		{
			//right to left place
			exceedRowWidth = this.placeDirection(1, nodes.get(index), startingPoint, row);
			if(exceedRowWidth)
			{
				row++;
				if(row <= this.calculatedRowSize)
					this.addArrayNodeList(new ArrayList<Nodes>());
				this.reverseSort(this.getRowNodeList(row));
				placeNodes(nodes, row, 0, 0);
			}
			else
			{
				startingPoint -= nodes.get(index).getNodeWidth();
				this.addNodeToRow(nodes.get(index), row);
				if(index < nodes.size() - 1)
				{
					if(startingPoint < 0)
					{
						this.addArrayNodeList(new ArrayList<Nodes>());
						placeNodes(nodes, row+1, 0, index+1);
					}
					else
						placeNodes(nodes, row, startingPoint, index+1);
				}
			}
		}
	}
	
	public boolean placeDirection(int leftOrRight, Nodes node, int startingPoint, int row)
	{
		if(leftOrRight == 0)
		{
			if(startingPoint + node.getNodeWidth() != this.widthGuard)
			{
				node.setNodeCoordinate(startingPoint, row);
				return false;
			}
		}
		else
		{
			if(startingPoint - node.getNodeWidth() >= 0)
			{
				node.setNodeCoordinate(startingPoint - node.getNodeWidth(), row);
				return false;
			}
		}
		
		return true;
	}
	
<<<<<<< HEAD
	public void reverseSort(ArrayList<Nodes> nodes)
	{
		for(int i = 0; i < nodes.size() / 2; i++)
		{
			Nodes tempNode = nodes.get(i);
			nodes.set(i, nodes.get(nodes.size() - i));
			nodes.set(nodes.size() - 1, tempNode);
		}
	}
	*/

	public void updateNodeCoordinateOnly(Nodes newNode, int x, int y) 
	{
		newNode.setNodeCoordinate(x, y);
	}

	public void legalizeNodes ()
	{
		for (int row = 0; row < this.placementList.size(); row++)
		{
			ArrayList<Nodes> tempList = this.placementList.get(row);
			this.sortListBy(tempList,tempList.size());
			Nodes leftNode = null;
			Nodes rightNode = null;
			ArrayList<Nodes> nodeToRemove = new ArrayList<Nodes>();
			NodeCoordinate leftNodeCoordinate,rightNodeCoordinate;
			int lengthToShift, accumulatedWidthSize = 0;
			for (Iterator<Nodes> i = tempList.iterator(); i.hasNext();)
			{
				rightNode = i.next();
				accumulatedWidthSize += rightNode.getNodeWidth();
				if (leftNode != null) //First iteration, first node.
				{
					leftNodeCoordinate = leftNode.getNodeCoordinate();
					rightNodeCoordinate = rightNode.getNodeCoordinate();
					lengthToShift = leftNodeCoordinate.getNodeXCoordinate() + leftNode.getNodeWidth() - rightNodeCoordinate.getNodeXCoordinate();
					
					if (leftNodeCoordinate.getNodeXCoordinate() + leftNode.getNodeWidth() > rightNodeCoordinate.getNodeXCoordinate())
						//Overlapped, shift the right cell, else do nothing
					{
						if (!this.moveNodeByWidth(rightNode, lengthToShift, row))
						{
							//Reached the end of the boundary.
							if (accumulatedWidthSize <= this.widthGuard)
							{
								//This row can fit all the nodes
								//Add node to the end of the last line
								this.reduceTheWidthSpacing (tempList,lengthToShift);
							}
							else
							{
								//This row can not fit all the nodes.
								//Change the coordinate of the node and add to the next row.
								rightNode.setNodeCoordinate(rightNodeCoordinate.getNodeXCoordinate(), rightNodeCoordinate.getNodeYCoordinate()+rowSeperation);
								nodeToRemove.add(rightNode);
								this.placementList.get(row+1).add(rightNode);
							}
						}
					}
					else if (rightNodeCoordinate.getNodeXCoordinate() +rightNode.getNodeWidth() > this.widthGuard)
					{
						lengthToShift = rightNodeCoordinate.getNodeXCoordinate()+rightNode.getNodeWidth()-this.widthGuard;
						if (accumulatedWidthSize <= this.widthGuard)
						{
							//This row can fit all the nodes
							//Add node to the end of the last line
							this.reduceTheWidthSpacing (tempList,lengthToShift);
						}
						else
						{
							//This row can not fit all the nodes.
							//Change the coordinate of the node and add to the next row.
							rightNode.setNodeCoordinate(rightNodeCoordinate.getNodeXCoordinate(), rightNodeCoordinate.getNodeYCoordinate()+rowSeperation);
							nodeToRemove.add(rightNode);
							this.placementList.get(row+1).add(rightNode);
						}
					}
				}
				leftNode = rightNode;
			}
			tempList.removeAll(nodeToRemove);
			this.sortListBy(tempList,tempList.size());
		}
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
	
	public ArrayList<ArrayList<Nodes>> getPlacementList() { return this.placementList; }
	public ArrayList<Nodes> getRowNodeList(int row) { return this.placementList.get(row); }
	public Nodes getNodeInARow(int row, int nodeIndex) { return this.placementList.get(row).get(nodeIndex); }
	public void addArrayNodeList(ArrayList<Nodes> nodes) { this.placementList.add(nodes); }
	public void addNodeToRow(Nodes node, int row) { this.placementList.get(row).add(node); }
}
