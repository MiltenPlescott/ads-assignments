/*
 * ads-assignments: 2-SAT solver
 *
 * Copyright (c) 2019, Milten Plescott. All rights reserved.
 *
 * SPDX-License-Identifier: MIT
 */
package adsassignments.assignment3;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Milten Plescott
 */
public class Formula {

	private int numBoolVars;
	private int numBoolClauses;
	private final Set<Clause> clauses = new HashSet<>();

	Formula(int numBoolVars, int numBoolClauses) {
		this.numBoolVars = numBoolVars;
		this.numBoolClauses = numBoolClauses;
	}

	void addClause(Clause clause) {
		if (this.clauses.size() == this.numBoolClauses) {
			throw new Error("No more than " + this.numBoolClauses + " clauses expected.");
		}
		if (clause.getLiterals().size() == 1) {  // if there is a clause with single literal then duplicate the literal
			clause.addLiteral(clause.getLiterals().get(0));
		}
		this.clauses.add(clause);
	}

	void verifySolution(Map<Integer, Boolean> boolVars) {
		for (Clause cl : clauses) {
			verifyClause(cl, boolVars);
		}
	}

	private void verifyClause(Clause cl, Map<Integer, Boolean> boolVars) {
		boolean[] b = new boolean[2];
		for (int i = 0; i < cl.getLiterals().size(); i++) {
			Literal lit = cl.getLiterals().get(i);
			if (lit.isPositive()) {
				b[i] = boolVars.get(lit.getIndex());
			}
			else {
				b[i] = !boolVars.get(lit.getIndex());
			}
		}
		if (b[0] == false && b[1] == false) {
			throw new Error("Solution seems to be incorrect.");
		}
	}

	Set<Clause> getClauses() {
		return clauses;
	}

	int getNumBoolVars() {
		return this.numBoolVars;
	}

	int getNumBoolClauses() {
		return this.numBoolClauses;
	}

	void setNumBoolVars(int numBoolVars) {
		if (numBoolVars < 1) {
			throw new Error("At least 1 boolean variable is required.");
		}
		this.numBoolVars = numBoolVars;
	}

	void setNumBoolClauses(int numBoolClauses) {
		if (numBoolClauses < 1) {
			throw new Error("At least 1 clause is required.");
		}
		this.numBoolClauses = numBoolClauses;
	}

}
