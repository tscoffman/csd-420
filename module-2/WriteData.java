/*
Trenten Coffman
September 20, 2026
Assignment 2.2

Creates an array of 5 random integers, an array of 5 random doubles, and stores them in datafile.dat
*/
import java.util.Random;
import java.io.*;

public class WriteData {
	
	public static void main(String[] args) {
		final int NUMBER_QUANTITY = 5;
		int[] randInts = new int[NUMBER_QUANTITY];
		double[] randDoubles = new double[NUMBER_QUANTITY];
		Random rand = new Random();
		
		try (RandomAccessFile dataFile = new RandomAccessFile("trentendatafile.dat", "rw")) {
			// start at end of file
			dataFile.seek(dataFile.length());
			
			// generate 5 random numbers for each array
			for (int i = 0; i < NUMBER_QUANTITY; i++) {
				randInts[i] = rand.nextInt();
				randDoubles[i] = rand.nextDouble();
			}
			
			// write ints to file
			for (int i = 0; i < NUMBER_QUANTITY; i++) {
				dataFile.writeInt(randInts[i]);
			}
			
			// write doubles to file
			for (int i = 0; i < NUMBER_QUANTITY; i++) {
				dataFile.writeDouble(randDoubles[i]);
			}
			
			// print confirmation message
			System.out.println("Data saved successfully");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}