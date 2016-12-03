package Main;

import java.util.*;

public class FDP {
	
	private NodeList nodeList;
	private Graph floorplan;
	
	public final static int iteration_limit = 5000;
	public final static int abort_limit = 3;
	
	public FDP(Graph floorplan, NodeList nodeList) {
		this.nodeList = nodeList;
		this.floorplan = floorplan;
	}
	public NodeList getNodeList() { return this.nodeList; }
	public Graph getFloorPlan() { return this.floorplan; }
	
	public void startAlgorithm() {

		int iteration_count = 0;
		int abort_count = 0;
		
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
				if(node_cond == null) // position is free --okay
				{
					//System.out.println("node_cond is empty");
					this.floorplan.updateNodeCoordinate(thisNodes, curr_ZFT.getNodeXCoordinate(), curr_ZFT.getNodeYCoordinate());
					thisNodes.lockNode();
					end_ripple_move = true;
					abort_count = 0;
				}else if(node_cond.isLock() == true) // ZFT position is occupied and fixed.
				{
					//System.out.println("node_cond is LOCKED");
					this.floorplan.updateNodeCoordinateNextFreePos(thisNodes, curr_ZFT.getNodeXCoordinate(), curr_ZFT.getNodeYCoordinate());
					thisNodes.lockNode();
					end_ripple_move = true;
					abort_count++;
					if(abort_count > abort_limit)
					{
						System.out.println("abort_count > abort_limit");
						for(Iterator<Nodes> j = this.nodeList.getNonTerminalNodeList().iterator(); j.hasNext();) 
						{
							Nodes tempNodes = j.next();
							tempNodes.unlockNode();
						}
					}
					iteration_count++;
				}else if(node_cond.isSameLocation(curr_ZFT)) // --okay
				{
					//System.out.println("node_cond is SAME");
					thisNodes.lockNode();
					end_ripple_move = true;
					abort_count= 0;
				}
				else if(node_cond.isLock() == false) // occupied but not locked--okay
				{
					//System.out.println("node_cond is FREE");
					this.floorplan.updateNodeCoordinate(node_cond, curr_ZFT.getNodeXCoordinate(), curr_ZFT.getNodeYCoordinate());
					this.floorplan.swapNodes(thisNodes, node_cond);
					thisNodes.lockNode();
					thisNodes = node_cond;
					end_ripple_move = false;
					abort_count = 0;
				}
			}
			System.out.println("Reach iteration at ===== " + iteration_count);
			if(iteration_count >= iteration_limit) {
				System.out.println("Reach iteration Limit break");
				break;
			}
		}
		// Unlock all Nodes
		for(Iterator<Nodes> i = this.nodeList.getNonTerminalNodeList().iterator(); i.hasNext();) 
		{
			Nodes thisNodes = i.next();
			thisNodes.unlockNode();
		}
	}
}
