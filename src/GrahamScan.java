package edu.iastate.cs228.hw4;

/**
 * @author Mark Marrano
 */

import java.io.FileNotFoundException;
import java.util.InputMismatchException;
import java.util.Stack;
import java.util.ArrayList; 

public class GrahamScan extends ConvexHull
{
	/**
	 * Stack used by Grahma's scan to store the vertices of the convex hull of the points 
	 * scanned so far.  At the end of the scan, it stores the hull vertices in the 
	 * counterclockwise order. 
	 */
	private PureStack<Point> vertexStack;  


	/**
	 * Call corresponding constructor of the super class.  Initialize the variables algorithm 
	 * (from the class ConvexHull) and vertexStack. 
	 * 
	 * @param n  number of points 
	 * @throws IllegalArgumentException  if pts.length == 0
	 */
	public GrahamScan(Point[] pts) throws IllegalArgumentException 
	{
		super(pts);
		
		algorithm = "Graham's scan";
		
		// construct stack
		vertexStack = new ArrayBasedStack<Point>();
		
		// Time
		long startingTime = System.nanoTime();
		
		constructHull();
		
		time = System.nanoTime() - startingTime;
		
		draw();
	}
	

	/**
	 * Call corresponding constructor of the super class.  Initialize algorithm and vertexStack.  
	 * 
	 * @param  inputFileName
	 * @throws FileNotFoundException
	 * @throws InputMismatchException   when the input file contains an odd number of integers
	 */
	public GrahamScan(String inputFileName) throws FileNotFoundException, InputMismatchException
	{
		super(inputFileName); 
		
		algorithm = "Graham's scan";
		
		// construct stack
		vertexStack = new ArrayBasedStack<Point>();
		
		// time
		long startingTime = System.nanoTime();
		
		constructHull();
		
		time = System.nanoTime() - startingTime;
		
		draw();
	}

	
	// -------------
	// Graham's scan
	// -------------
	
	/**
	 * This method carries out Graham's scan in several steps below: 
	 * 
	 *     1) Call the private method setUpScan() to sort all the points in the array 
	 *        pointsNoDuplicate[] by polar angle with respect to lowestPoint.    
	 *        
	 *     2) Perform Graham's scan. To initialize the scan, push pointsNoDuplicate[0] and 
	 *        pointsNoDuplicate[1] onto vertexStack.  
	 * 
     *     3) As the scan terminates, vertexStack holds the vertices of the convex hull.  Pop the 
     *        vertices out of the stack and add them to the array hullVertices[], starting at index
     *        vertexStack.size() - 1, and decreasing the index toward 0.    
     *        
     * Two degenerate cases below must be handled: 
     * 
     *     1) The array pointsNoDuplicates[] contains just one point, in which case the convex
     *        hull is the point itself. 
     *     
     *     2) The array contains only two points, in which case the hull is the line segment 
     *        connecting them.   
	 */
	public void constructHull()
	{	
		if(pointsNoDuplicate.length == 1 || pointsNoDuplicate.length == 2){
			hullVertices = pointsNoDuplicate;
			return;
		}
		
		setUpScan();
		
		// push first two
		vertexStack.push(pointsNoDuplicate[0]);
		vertexStack.push(pointsNoDuplicate[1]);
		//vertexStack.push(pointsNoDuplicate[2]);
		
		// initialize new point
		Point top = new Point();
		
		for(int i = 2; i < pointsNoDuplicate.length; i++){
			top = vertexStack.pop();
			while(whichWay(vertexStack.peek(), top, pointsNoDuplicate[i]) != 2){
				top = vertexStack.pop();
			}
			// Push the top back on
			vertexStack.push(top);
			vertexStack.push(pointsNoDuplicate[i]);
		}
		
		// construct hullVertices
		hullVertices = new Point[vertexStack.size()];	
		
		// copy stack to hullVertices
		for(int i = vertexStack.size() - 1; i >= 0; i--){
			hullVertices[i] = vertexStack.pop();
		}
		
	}
	
	
	/**
	 * Set the variable quicksorter from the class ConvexHull to sort by polar angle with respect 
	 * to lowestPoint, and call quickSort() from the QuickSortPoints class on pointsNoDupliate[]. 
	 * The argument supplied to quickSort() is an object created by the constructor call 
	 * PolarAngleComparator(lowestPoint, true).       
	 * 
	 * Ought to be private, but is made public for testing convenience. 
	 *
	 */
	public void setUpScan()
	{
		quicksorter = new QuickSortPoints(pointsNoDuplicate);
		quicksorter.quickSort(new PolarAngleComparator(lowestPoint, true));	
	}	
	
	/**
	 * Find out what the orientation of the next point will be.
	 * 
	 * @param p Second to top point in vertexStack
	 * @param q Top Point in the certexStack
	 * @param r current pointsNoDuplicate item we are checking
	 * @return integer designating the orientation
	 */
	private int whichWay(Point p, Point q, Point r)
	{
	    int count = (q.getY() - p.getY()) * (r.getX() - q.getX()) - (q.getX() - p.getX()) * (r.getY() - q.getY());

	    if (count == 0){
	    	// colinear
	    	return 0;
	    }
	    else{
	    	// clock or counterclock wise
	    	 return (count > 0)? 1 : 2; 
	    }
	}
}
