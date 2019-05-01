/*
 * ads-assignments: 2-SAT solver
 *
 * Copyright (c) 2019, Milten Plescott. All rights reserved.
 *
 * SPDX-License-Identifier: MIT
 */
package adsassignments.assignment3;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Milten Plescott
 */
public class Clause {

	private List<Literal> literals = new ArrayList<>();

	Clause() {
	}

	void addLiteral(Literal literal) {
		if (this.literals.size() == 2) {
			throw new Error("No more than 2 literals in 2-SAT.");
		}
		this.literals.add(literal);
	}

	List<Literal> getLiterals() {
		return literals;
	}

}
