/*
Trenten Coffman
September 20, 2026
Assignment 2.2

Reads from a file named "trentendatafile.dat" and displays the data
*/
import java.io.*;

public class ReadData {
	
	public static void main(String[] args) {
		final int NUMBER_QUANTITY = 5;
		
		try (RandomAccessFile dataFile = new RandomAccessFile("trentendatafile.dat", "r")) {
			long currentPosition = dataFile.getFilePointer();
			long fileLength = dataFile.length();
			int batchNumber = 1;
			
			// loop continues until the file pointer is at the end of the file
			while (currentPosition < fileLength) {
				System.out.println("Batch " + batchNumber + ":");
				
				System.out.println("\tIntegers:");
				for (int i = 0; i < NUMBER_QUANTITY; i++) {
					System.out.println("\t\t" + dataFile.readInt());
				}
				
				System.out.println("\tDoubles:");
				for (int i = 0; i < NUMBER_QUANTITY; i++) {
					System.out.println("\t\t" + dataFile.readDouble());
				}
				
				batchNumber++;
				currentPosition = dataFile.getFilePointer();
			}
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
}