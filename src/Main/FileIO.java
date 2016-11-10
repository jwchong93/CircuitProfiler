package Main;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;


public class FileIO 
{

private static BufferedReader in;
private FileInputStream fStream;
public void initFileInput (String testFileDirectory, String testFileName, String ext)
{
	try
	{
		fStream = new FileInputStream(testFileDirectory+testFileName+ext);
	    in = new BufferedReader(new InputStreamReader(fStream));
	}
	catch (IOException e) 
    {
    	System.out.println("File input "+ext+" error");
    }
}
public String readTextFiles()
{   
            try 
            {
				if (in.ready())
				{
					//reads the line and stores into string "line"
					String line = in.readLine();
					return line;
				}
				else
				{
				    //close the input stream
				    in.close();
				}
			} 
            catch (IOException e) 
            {
				System.out.println("Reading error" + e.getMessage());
			}
			return null;
			
		
	}
}
