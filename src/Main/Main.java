package Main;


public class Main 
{

	public static void main(String[] args) 
	{
		FileIO file = new FileIO();
		String resultFileName = "Result";
		String resultExtension = ".txt";
		file.initFileOutput(System.getProperty("user.dir"), "/" +resultFileName, resultExtension);
		
		String testFileName = "bigblue1";
		System.out.println("Analysing " + testFileName);
		String testFileDirectory = System.getProperty("user.dir")+"/testFiles/"+testFileName+"/";
		
		NodeList nodeList = new NodeList();
		System.out.println("Start process .nodes file");
		nodeList.readAndAnalyseFile(testFileName, testFileDirectory, file);
		System.out.println("Process .nodes File done");
		System.out.println("Dumping nodes data to "+resultFileName + resultExtension);
		nodeList.printSummary(file);
		System.out.println("Done processing .nodes file");
		//============================================================================================================

		System.out.println("Start reading .nets file");
		NetList netList = new NetList();
		netList.netListReadAndAnalyseFile(testFileName, testFileDirectory, file);
		System.out.println("Process .nets File done");
		System.out.println("Dumping nets data to "+resultFileName + resultExtension);
		netList.printSummary(file);
		System.out.println("Done processing .nets file");
		
		file.deInitFileIO();
		System.out.println("Analyzing completed");
		
		
		
	}
}