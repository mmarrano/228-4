package edu.iastate.cs228.hw4;

/**
 * @author Mark Marrano
 */

import java.util.Arrays;

import java.util.Comparator;

/**
 * This class sorts an array of Point objects using a provided Comparator.  You may 
 * modify your implementation of quicksort from Project 2.  
 */

public class QuickSortPoints
{
	private Point[] points;  	// Array of points to be sorted.
	

	/**
	 * Constructor takes an array of Point objects. 
	 * 
	 * @param pts
	 */
	QuickSortPoints(Point[] pts)
	{
		points = pts;
	}
	
	
	/**
	 * Copy the sorted array to pts[]. 
	 * 
	 * @param pts  array to copy onto
	 */
	void getSortedPoints(Point[] pts)
	{ 
		points = pts;
	}

	
	/**
	 * Perform quicksort on the array points[] with a supplied comparator. 
	 * 
	 * @param arr
	 * @param comp
	 */
	public void quickSort(Comparator<Point> comp)
	{
		// If we didn't get a comp
		if (comp.toString() == "" || comp == null) {
			throw new IllegalArgumentException();
		}
		quickSortRec(0, points.length - 1, comp);
	}
	
	
	/**
	 * Operates on the subarray of points[] with indices between first and last. 
	 * 
	 * @param first  starting index of the subarray
	 * @param last   ending index of the subarray
	 */
	private void quickSortRec(int first, int last, Comparator<Point> comp)
	{
		if (first >= last) {
			return;
		}
		// Initialize partition into int p
		int p = partition(first, last, comp);
		quickSortRec(first, p - 1, comp);
		quickSortRec(p + 1, last, comp);
	}
	

	/**
	 * Operates on the subarray of points[] with indices between first and last.
	 * 
	 * @param first
	 * @param last
	 * @return
	 */
	private int partition(int first, int last, Comparator<Point> comp)
	{
		int i = first - 1;
		// General quick sorting
		for (int j = first; j < last; j++) {
			if (comp.compare(points[j], points[last]) == -1) {
				i++;
				swap(i, j);
			}
		}
		swap(i + 1, last);
		return i + 1;
	}
	
	/**
	 * Swaps the two points recognized by variables i and j
	 * 
	 * @param i
	 * @param j
	 */
	private void swap(int i, int j) {
		Point swap = points[i];
		points[i] = points[j];
		points[j] = swap;
	}
}


