package Main;


public class Main 
{

	public static void main(String[] args) 
	{
		String testFileName = "adaptec1";
		String testFileDirectory = "C:/Users/Jing Wen/Desktop/testFiles/"+testFileName+"/";
		NodeList nodeList = new NodeList();
		nodeList.readAndAnalyseFile(testFileName, testFileDirectory);
		nodeList.printSummary();

		//============================================================================================================

		System.out.println("Information: Start reading .nets file");
		NetList netList = new NetList();
		netList.netListReadAndAnalyseFile(testFileName, testFileDirectory);
		netList.printSummary();
		System.out.println("Information: End reading .nets file");

		
	}
}