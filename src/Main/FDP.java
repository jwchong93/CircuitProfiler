package Main;

public class FDP {
	
	NodeList nodeList;
	Graph floorplan;
	
	public FDP(Graph floorplan, NodeList nodeList) {
		this.nodeList = nodeList;
		this.floorplan = floorplan;
	}
	
	public void callOps() {
		// assume initial placement done
		// 
		nodeList.sortAllNodeList();
		// Start from highest node degree node
		for(int i=0; i<nodeList.getNonTerminalNodeList().size();i++) {
			
		}
	}
}
