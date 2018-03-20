package edu.iastate.cs228.hw4;

/**
 *  
 * @author Mark Marrano
 *
 */

import java.io.FileNotFoundException;


import java.util.Scanner;
import java.util.InputMismatchException;
import java.util.Random; 


public class CompareHullAlgorithms 
{
	/**
	 * Repeatedly take points either randomly generated or read from files. Perform Graham's scan and 
	 * Jarvis' march over the input set of points, comparing their performances.  
	 * 
	 * @param args
	 **/
	public static void main(String[] args) 
	{
		// 
		// Conducts multiple rounds of convex hull construction. Within each round, performs the following: 
		// 
		//    1) If the input are random points, calls generateRandomPoints() to initialize an array 
		//       pts[] of random points. Use pts[] to create two objects of GrahamScan and JarvisMarch, 
		//       respectively.
		//
		//    2) If the input is from a file, construct two objects of the classes GrahamScan and  
		//       JarvisMarch, respectively, using the file.     
		//
		//    3) Have each object call constructHull() to build the convex hull of the input points.
		//
		//    4) Meanwhile, prints out the table of runtime statistics.
		// 
		// A sample scenario is given in Section 4 of the project description. 
		// 	
		
		int trial = 1;
		int length = 0;
		String fileName;
		Point[] randomPoints;
		
		ConvexHull[] algorithms = new ConvexHull[2];
		
		System.out.println("Comparison between Convex Hull Algorithms");
		Scanner scanner = new Scanner(System.in);
		
		while(true){
			System.out.println("\n" + "Trial " + trial + ": ");
			trial++;
			int input = scanner.nextInt();
			// Random points
			if(input == 1){
				System.out.println("Enter number of random points:");
				length = scanner.nextInt();
				randomPoints = generateRandomPoints(length, new Random());
				
				// Initialize Graham's and Jarvis'
				algorithms[0] = new GrahamScan(randomPoints);
				algorithms[1] = new JarvisMarch(randomPoints);
			}
			// Input file
			else if(input == 2){
				System.out.println("Points from a file" + "\n" + "File name:");
				fileName = scanner.next();
				// Try to catch any errors (FileNotFound and InputMismatch) if the input file does not exist or is improperly formatted
				try{
					// Initialize Graham's and Jarvis'
					algorithms[0] = new GrahamScan(fileName);
					algorithms[1] = new JarvisMarch(fileName);
				} catch (FileNotFoundException | InputMismatchException error) {
					error.printStackTrace();
				}
			}
			// End program
			else if(input == 3){
				scanner.close();
				return;
			}
			
			System.out.println("\n" + "algorithm" + "\t" + "size" + "\t" + "time (ns)" + "\n" + "------------------------------------" + "\n");

			// print stats
			System.out.println(algorithms[0].stats());
			System.out.println(algorithms[1].stats());
			
			System.out.println("------------------------------------" + "\n");
			
		// Within a hull construction round, have each algorithm call the constructHull() and draw()
		// methods in the ConvexHull class.  You can visually check the result. (Windows 
		// have to be closed manually before rerun.)  Also, print out the statistics table 
		// (see Section 4). 
		}
	}
	
	
	/**
	 * This method generates a given number of random points.  The coordinates of these points are 
	 * pseudo-random numbers within the range [-50,50] × [-50,50]. 
	 * 
	 * Reuse your implementation of this method in the CompareSorter class from Project 2.
	 * 
	 * @param numPts  	number of points
	 * @param rand      Random object to allow seeding of the random number generator
	 * @throws IllegalArgumentException if numPts < 1
	 */
	private static Point[] generateRandomPoints(int numPts, Random rand) throws IllegalArgumentException
	{ 
		if (numPts < 1) {
			throw new IllegalArgumentException();
		}
		Point[] point = new Point[numPts];
		for (int i = 0; i < numPts; i++) {
			// Bound = 101 then subtracted by 50 so range is [-50,50]
			point[i] = new Point(rand.nextInt(101) - 50, rand.nextInt(101) - 50);
		}
		return point;
	}
}
