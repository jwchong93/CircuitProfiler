package Main;


public class Main 
{

	public static void main(String[] args) 
	{
		String testFileName = "bigblue1";
		System.out.println("Analysing " + testFileName);
		String testFileDirectory = System.getProperty("user.dir")+"/testFiles/"+testFileName+"/";
		
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