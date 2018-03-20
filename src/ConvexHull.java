package edu.iastate.cs228.hw4;

/**
 *  
 * @author Mark Marrano
 *
 */

import java.util.ArrayList;

import java.util.Arrays;
import java.util.Comparator;
import java.io.File;
import java.io.FileNotFoundException; 
import java.util.InputMismatchException; 
import java.io.PrintWriter;
import java.util.Random; 
import java.util.Scanner;



/**
 * 
 * This class implements construction of the convex hull of a finite set of points. 
 *
 */

public abstract class ConvexHull 
{
	// ---------------
	// Data Structures 
	// ---------------
	protected String algorithm;  // has value either "Graham's scan" or "Jarvis' march". Initialized by a subclass.
	
	protected long time;         // execution time in nanoseconds
	
	/**
	 * The array points[] holds an input set of Points, which may be randomly generated or 
	 * input from a file.  Duplicates are possible. 
	 */
	private Point[] points;    
	

	/**
	 * Lowest point from points[]; and in case of a tie, the leftmost one of all such points. 
	 * To be set by a constructor. 
	 */
	protected Point lowestPoint; 

	
	/**
	 * This array stores the same set of points from points[] with all duplicates removed. 
	 * These are the points on which Graham's scan and Jarvis' march will be performed. 
	 */
	protected Point[] pointsNoDuplicate; 
	
	
	/**
	 * Vertices of the convex hull in counterclockwise order are stored in the array 
	 * hullVertices[], with hullVertices[0] storing lowestPoint. 
	 */
	protected Point[] hullVertices;
	
	
	protected QuickSortPoints quicksorter;  // used (and reset) by this class and its subclass GrahamScan

	
	
	// ------------
	// Constructors
	// ------------
	
	
	/**
	 * Constructor over an array of points.  
	 * 
	 *    1) Store the points in the private array points[].
	 *    
	 *    2) Initialize quicksorter. 
	 *    
	 *    3) Call removeDuplicates() to store distinct points from the input in pointsNoDuplicate[].
	 *    
	 *    4) Set lowestPoint to pointsNoDuplicate[0]. 
	 * 
	 * @param pts
	 * @throws IllegalArgumentException  if pts.length == 0
	 */
	public ConvexHull(Point[] pts) throws IllegalArgumentException 
	{
		// 1)
		points = pts;
		
		if(points.length == 0){
			throw new IllegalArgumentException();
		}
		
		// 2)
		quicksorter = new QuickSortPoints(points);
		
		// 3)
		removeDuplicates();
		
		// 4)
		lowestPoint = pointsNoDuplicate[0];
	}
	
	/**
	 * Read integers from an input file.  Every pair of integers represent the x- and y-coordinates 
	 * of a point.  Generate the points and store them in the private array points[]. The total 
	 * number of integers in the file must be even.
	 * 
	 * You may declare a Scanner object and call its methods such as hasNext(), hasNextInt() 
	 * and nextInt(). An ArrayList may be used to store the input integers as they are read in 
	 * from the file.  
	 * 
	 * Perform the operations 1)-4) for the first constructor. 
	 * 
	 * @param  inputFileName
	 * @throws FileNotFoundException
	 * @throws InputMismatchException   when the input file contains an odd number of integers
	 */
	public ConvexHull(String inputFileName) throws FileNotFoundException, InputMismatchException
	{
		int count = 0;
		int j = 0;
		ArrayList<Integer> arrayList = new ArrayList<Integer>();
		
		try {
			// 1)
			File file = new File(inputFileName);
			Scanner scanner = new Scanner(file);
			// Find how many integers there are in the file
			while (scanner.hasNextInt()) {
				count++;
				arrayList.add(scanner.nextInt());
			}
			scanner.close();
			// If odd number of integers, throw InputMismatchException
			if (count % 2 != 0) {
				throw new InputMismatchException();
			}
			
			points = new Point[count / 2];
			
			for(int i = 0; i < points.length; i++){
				points[i] = new Point(arrayList.get(j), arrayList.get(j + 1));
				j += 2;
			}
			
			// 2)
			quicksorter = new QuickSortPoints(points);
			
			// 3)
			removeDuplicates();
			
			// 4)
			lowestPoint = pointsNoDuplicate[0];
			
			
		} catch (FileNotFoundException error) {
			throw new FileNotFoundException();
		}
	}

	
	/**
	 * Construct the convex hull of the points in the array pointsNoDuplicate[]. 
	 */
	public abstract void constructHull(); 

	
		
	/**
	 * Outputs performance statistics in the format: 
	 * 
	 * <convex hull algorithm> <size>  <time>
	 * 
	 * For instance, 
	 * 
	 * Graham's scan   1000	  9200867
	 * 
	 * Use the spacing in the sample run in Section 4 of the project description. 
	 */
	public String stats()
	{
		String s = algorithm + "\t" + points.length + "\t" + time;
		return s; 
	}
	
	
	/**
	 * The string displays the convex hull with vertices in counterclockwise order starting at  
	 * lowestPoint.  When printed out, it will list five points per line with three blanks in 
	 * between. Every point appears in the format "(x, y)".  
	 * 
	 * For illustration, the convex hull example in the project description will have its 
	 * toString() generate the output below: 
	 * 
	 * (-7, -10)   (0, -10)   (10, 5)   (0, 8)   (-10, 0)   
	 * 
	 * lowestPoint is listed only ONCE.  
	 */
	public String toString()
	{
		int count = 0;
		String s = "";
		for(int i = 0; i < hullVertices.length; i++){
			s += hullVertices[i].toString() + "   ";
			count++;
			if(count % 5 == 0){
				s += "\n";
			}
		}
		return s; 
	}
	
	
	/** 
	 * 
	 * Writes to the file "hull.txt" the vertices of the constructed convex hull in counterclockwise 
	 * order.  These vertices are in the array hullVertices[], starting with lowestPoint.  Every line
	 * in the file displays the x and y coordinates of only one point.  
	 * 
	 * For instance, the file "hull.txt" generated for the convex hull example in the project 
	 * description will have the following content: 
	 * 
     *  -7 -10 
     *  0 -10
     *  10 5
     *  0  8
     *  -10 0
	 * 
	 * The generated file is useful for debugging as well as grading. 
	 * 
	 * Called only after constructHull().  
	 * 
	 * 
	 * @throws IllegalStateException  if hullVertices[] has not been populated (i.e., the convex 
	 *                                   hull has not been constructed)
	 * @throws FileNotFoundException 
	 */
	public void writeHullToFile() throws IllegalStateException, FileNotFoundException 
	{
			if(hullVertices == null){
				throw new IllegalStateException();
			}
			
			// Create new PrintWriter and write to file
			PrintWriter writer = new PrintWriter("hull.txt");
			for(int i = 0; i < hullVertices.length - 1; i++){
				writer.println(hullVertices[i].getX() + " " + hullVertices[i].getY());
			}
			// reprint lowestpoint cause we wrsap back around to it
			writer.println(hullVertices[0].getX() + " " + hullVertices[0].getY());
			writer.close();
	}
	

	/**
	 * Draw the points and their convex hull.  This method is called after construction of the 
	 * convex hull.  You just need to make use of hullVertices[] to generate a list of segments 
	 * as the edges. Then create a Plot object to call the method myFrame().  
	 */
	public void draw()
	{		
		int i;
		
			int numSegs = hullVertices.length;  // number of segments to draw 
			
			// Based on Section 4.1, generate the line segments to draw for display of the convex hull.
			// Assign their number to numSegs, and store them in segments[] in the order. 
			Segment[] segments = new Segment[numSegs]; 
			
			for (i = 0; i < hullVertices.length - 1; i++) {
				segments[i] = new Segment(hullVertices[i], hullVertices[i + 1]);
			}
			
			// Segment going from last item in the hullVertice array back to the lowestPoint (hullVertices[0])
			segments[i] = new Segment(hullVertices[i], hullVertices[0]);
			
			// The following statement creates a window to display the convex hull.
			Plot.myFrame(pointsNoDuplicate, segments, getClass().getName());		
	}

		
	/**
	 * Sort the array points[] by y-coordinate in the non-decreasing order.  Have quicksorter 
	 * invoke quicksort() with a comparator object which uses the compareTo() method of the Point 
	 * class. Copy the sorted sequence onto the array pointsNoDuplicate[] with duplicates removed.
	 *     
	 * Ought to be private, but is made public for testing convenience. 
	 */
	public void removeDuplicates()
	{
		//perform quicksort on all the points by y-coordinate
		quicksorter.quickSort((a,b) -> a.compareTo(b));
		
		//add all points to ArrayList list except for the repeating points
		ArrayList<Point> list = new ArrayList<Point>();
		for(int i = 0; i < points.length - 1; i++){
			if(points[i].compareTo(points[i + 1]) != 0){
				list.add(points[i]);
			}
		}
		
		// add last point
		list.add(points[points.length - 1]);
		
		//copies array without duplicates to pointsNoDuplicate[]
		pointsNoDuplicate = list.toArray(new Point[list.size()]);
	}
}
