/*
 * ads-assignments: 2-SAT solver
 *
 * Copyright (c) 2019, Milten Plescott. All rights reserved.
 *
 * SPDX-License-Identifier: MIT
 */
package adsassignments.assignment3;

/**
 *
 * @author Milten Plescott
 */
public class Literal {

	// boolean variable index
	// indexed from 0; input file is indexing literals from 1
	private final int index;

	// true iff variable is NOT a negation
	private boolean positive;

	Literal(int index, boolean positive) {
		this.index = index;
		this.positive = positive;
	}

	// copy constructor
	Literal(Literal other) {
		this.index = other.index;
		this.positive = other.positive;
	}

	void negate() {
		this.positive = !this.positive;
	}

	public int getIndex() {
		return index;
	}

	public boolean isPositive() {
		return positive;
	}

}
