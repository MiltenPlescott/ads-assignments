/*
 * ads-assignments: 2-SAT solver
 *
 * Copyright (c) 2019, Milten Plescott. All rights reserved.
 *
 * SPDX-License-Identifier: MIT
 */
package adsassignments.assignment3;

import java.util.List;

/**
 *
 * @author Milten Plescott
 */
public class SatParser {

	private SatParser() {
		throw new AssertionError("Suppress default constructor for noninstantiability.");
	}

	static Formula parseSat(List<String> lines) {
		try {
			String[] str = lines.get(0).split(" ");
			Formula formula = new Formula(Integer.parseInt(str[0]), Integer.parseInt(str[1]));
			return parseClauses(formula, lines.subList(1, lines.size()));
		}
		catch (NumberFormatException e) {
			System.out.println("Integer parsing error.");
			System.exit(1);
		}
		throw new AssertionError("mIsSiNg rEtUrN StAtEmEnT");
	}

	private static Formula parseClauses(Formula formula, List<String> lines) {
		for (String line : lines) {
			String[] strArr = line.split(" ");
			Clause cl = new Clause();
			for (String str : strArr) {
				if ("0".equals(str)) {
					break;
				}
				else {
					try {
						int x = Integer.parseInt(str);
						if (Math.abs(x) < 1 || x > formula.getNumBoolVars()) {
							System.out.println("Variable index: " + x + " is out of range.");
							System.exit(1);
						}
						cl.addLiteral(new Literal(Math.abs(x) - 1, (x > 0)));
					}
					catch (NumberFormatException e) {
						System.out.println("Integer parsing error.");
						System.exit(1);
					}
				}
			}
			formula.addClause(cl);
		}
		return formula;
	}

}
