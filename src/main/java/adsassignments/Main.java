/*
 * ads-assignments
 *
 * Copyright (c) 2019, Milten Plescott. All rights reserved.
 *
 * SPDX-License-Identifier: MIT
 */
package adsassignments;

import adsassignments.assignment1.OptimalBinarySearchTree;
import adsassignments.assignment2.FragileKnapsackProblem;
import adsassignments.assignment3.SatSolver;

/**
 *
 * @author Milten Plescott
 */
public class Main {

	public static void main(String[] args) {
		// assignment 1: optimal binary search tree
		// OptimalBinarySearchTree obst = new OptimalBinarySearchTree();
		// obst.launch();

		// assignment 2: 0-1 fragile knapsack
		// FragileKnapsackProblem fk = new FragileKnapsackProblem();
		// fk.launch();

		// assignment 3: 2-SAT solver
		SatSolver sat = new SatSolver();
		sat.launch();
	}

}
