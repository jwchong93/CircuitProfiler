package Main;

import java.util.*;

public class FDP {
	
	private NodeList nodeList;
	private Graph floorplan;
	
	public final static int iteration_limit = 3;
	
	public FDP(Graph floorplan, NodeList nodeList) {
		this.nodeList = nodeList;
		this.floorplan = floorplan;
	}
	public NodeList getNodeList() { return this.nodeList; }
	public Graph getFloorPlan() { return this.floorplan; }
	
	public void startAlgorithm() {

		int iteration_count = 0;
		int iteration_limit = 10;
		
		// sort nodeList by descending order by degree
		nodeList.sortAllNodeList();
		// Start from highest node degree node
		for(Iterator<Nodes> i = this.nodeList.getNonTerminalNodeList().iterator(); i.hasNext();) 
		{
			boolean end_ripple_move = false;
			Nodes thisNodes = i.next();
			thisNodes.unlockNode();
			while(end_ripple_move == false) {
				NodeCoordinate curr_ZFT = thisNodes.computeAndReturnZFT();
				Nodes node_cond = this.floorplan.nodeInThisLocation(curr_ZFT.getNodeXCoordinate(), curr_ZFT.getNodeYCoordinate());
				if(node_cond == null)
				{
					end_ripple_move = true;
				}else if(node_cond.isLock() == true)
				{
					end_ripple_move = true;
				}else if(node_cond.isSameLocation(curr_ZFT))
				{
					end_ripple_move = true;
				}
				else if(node_cond.isLock() == false)
				{
					end_ripple_move = false;
				}
			}
			
		}
	}
}
