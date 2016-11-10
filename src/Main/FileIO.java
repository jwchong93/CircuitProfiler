package Main;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;


public class FileIO 
{

private BufferedReader in;
private FileInputStream fStream;
private PrintWriter writer;
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

public void initFileOutput (String testFileDirectory, String testFileName, String ext)
{
	try
	{
		this.writer = new PrintWriter(testFileDirectory+testFileName+ext, "UTF-8");	
	}
	catch (IOException e) 
    {
    	System.out.println("File output "+testFileName+ext+" error");
    }
}

public void deInitFileIO()
{
	try
	{
		this.fStream.close();
		this.in.close();
		this.writer.close();
	}
	catch (IOException e) 
    {
    	System.out.println("File closing error");
    } 
	
}
public void writeToFiles(String outputLine)
{
	this.writer.println(outputLine);
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
