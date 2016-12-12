package Main;

import java.util.*;

public class FDP {
	
	private NodeList nodeList;
	private Graph floorplan;
	private NetList netlist;
	
	public final static int iteration_limit = 500;
	public final static int abort_limit = 0;
	
	public FDP(Graph floorplan, NodeList nodeList, NetList netlist) {
		this.nodeList = nodeList;
		this.floorplan = floorplan;
		this.netlist = netlist;
	}
	public NodeList getNodeList() { return this.nodeList; }
	public Graph getFloorPlan() { return this.floorplan; }
	
	public void startAlgorithm() {

		int iteration_count = 0;
		int abort_count = 0;
		int abc=0;
		
		// sort nodeList by descending order by degree
		nodeList.sortAllNodeList();
		// Start from highest node degree node
		for(Iterator<Nodes> i = this.nodeList.getNonTerminalNodeList().iterator(); i.hasNext();) 
		{
			boolean end_ripple_move = false;
			Nodes thisNodes = i.next();
			thisNodes.unlockNode();
			System.out.println(thisNodes + " " + abc++);
			while(end_ripple_move == false) {
				//System.out.println(thisNodes);
				NodeCoordinate curr_ZFT = thisNodes.computeAndReturnZFT();
				Nodes node_cond = this.floorplan.nodeInThisLocation(curr_ZFT.getNodeXCoordinate(), curr_ZFT.getNodeYCoordinate());
				if(node_cond == null) // position is free --okay
				{
					//System.out.println("node_cond is empty");
					int oldX = thisNodes.getNodeCoordinate().getNodeXCoordinate();
					int oldY = thisNodes.getNodeCoordinate().getNodeYCoordinate();
					if(thisNodes.calcNodeAllNetHPWL(netlist) > thisNodes.calcNewNodeAllNetHPWL1(floorplan, netlist, curr_ZFT.getNodeXCoordinate(), curr_ZFT.getNodeYCoordinate()))
					{
						thisNodes.lockNode();
					}
					else
					{
						this.floorplan.updateNodeCoordinate(thisNodes, oldX, oldY);
						ArrayList<Integer> rowList = new ArrayList<Integer>();
						rowList.add(thisNodes.getNodeCoordinate().getNodeYCoordinate()/36);
						rowList.add(oldY/36);
						this.floorplan.legalizeNodes(rowList);
					}
					end_ripple_move = true;
					abort_count = 0;
				}else if(node_cond.isLock() == true) // ZFT position is occupied and fixed.
				{
					//System.out.println("node_cond is LOCKED");
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
						iteration_count++;
					}
				}else if(thisNodes.isSameLocation(curr_ZFT)) // --okay
				{
					//System.out.println("node_cond is SAME");
					thisNodes.lockNode();
					end_ripple_move = true;
					abort_count= 0;
				}
				else if(node_cond.isLock() == false) // occupied but not locked--okay
				{
					//System.out.println("node_cond is FREE");
					long a = thisNodes.calcNodeAllNetHPWL(netlist);
					long b = thisNodes.calcNewNodeAllNetHPWL2(floorplan, node_cond, netlist);
					//System.out.println( a + ">" + b);
					if(a > b)
					{
						//System.out.println("node_cond is FREE");
						end_ripple_move = false;
						thisNodes.lockNode();
						thisNodes = node_cond;
					}
					else {
						this.floorplan.swapNodes(thisNodes, node_cond);
						ArrayList<Integer> rowList = new ArrayList<Integer>();
						rowList.add(thisNodes.getNodeCoordinate().getNodeYCoordinate()/36);
						rowList.add(node_cond.getNodeCoordinate().getNodeYCoordinate()/36);
						this.floorplan.legalizeNodes(rowList);
						thisNodes.lockNode();
						end_ripple_move = true;
					}
					abort_count = 0;
				}
				//System.out.println(thisNodes.calcNodeAllNetHPWL(netlist));
			}
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
