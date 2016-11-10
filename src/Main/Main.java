package Main;


public class Main 
{

	public static void main(String[] args) 
	{
		String testFileName = "adaptec1";
		String testFileDirectory = "C:/Users/Jing Wen/Desktop/testFiles/"+testFileName+"/";
		NodeList nodeList = new NodeList();
		nodeList.nodeListOperation(testFileName, testFileDirectory);

		//============================================================================================================

		System.out.println("Start reading .nets file");
		
		NetList netList = new NetList();
		netList.netListOperation(testFileName, testFileDirectory);
		
		System.out.println(netList.size);
		System.out.println(netList.totalPins);
		//FileIO.readTextFiles(testFileDirectory, testFileName, ".nodes");
		
		//placementGreedy(testFileDirectory, testFileName);
		
	}
}