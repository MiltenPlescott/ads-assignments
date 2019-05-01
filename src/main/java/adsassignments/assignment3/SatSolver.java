/*
 * ads-assignments: 2-SAT solver
 *
 * Copyright (c) 2019, Milten Plescott. All rights reserved.
 *
 * SPDX-License-Identifier: MIT
 */
package adsassignments.assignment3;

import adsassignments.Launchable;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Milten Plescott
 */
public class SatSolver implements Launchable {

	private final Formula formula;  // 2sat formula
	private Graph graph;  // implication graph
	private Graph transposed;  // transposed implication graph
	private final Deque<Integer> stack = new ArrayDeque<>();
	private final Map<Integer, List<Integer>> components = new HashMap<>();
	private final List<Integer> roots = new ArrayList<>();  // insertion order of components keys
	private final Map<Integer, Boolean> boolVars = new HashMap<>();  // index -> value of boolean variable

	public SatSolver() {
		List<String> lines = readLines("/sat.txt");
		this.formula = SatParser.parseSat(lines);
	}

	@Override
	public void launch() {
		this.graph = formulaToGraph();
		this.graph.removeDuplicateEdges();
		this.transposed = graph.getTransposedGraph();
		kosaraju();

		if (isSatisfiable()) {
			System.out.println("SATISFIABLE");
			graph.setAllUnvisited();
			booleanVariablesAssignment();
			for (int i = 0; i < formula.getNumBoolVars(); i++) {
				if (boolVars.get(i)) {
					System.out.println("TRUE");
				}
				else {
					System.out.println("FALSE");
				}
			}
			formula.verifySolution(boolVars);
		}
		else {
			System.out.println("UNSATISFIABLE");
		}

//		System.out.println("\n" + graph.toString());
//		System.out.println("" + transposed.toString());
	}

	private Graph formulaToGraph() {
		graph = new Graph(2 * formula.getNumBoolVars());

		// for each clause add 2 edges
		for (Clause clause : formula.getClauses()) {
			Literal l1 = clause.getLiterals().get(0);
			Literal l2 = clause.getLiterals().get(1);
			Literal[] implication = disjunctionToImplication(l1, l2);  // edge 1
			Literal[] contrapositive = contrapositiveImplication(implication);  // edge 2
			graph.addEdge(implication[0], implication[1]);
			graph.addEdge(contrapositive[0], contrapositive[1]);
		}

		return graph;
	}

	private Literal[] disjunctionToImplication(Literal l1, Literal l2) {
		Literal[] implication = new Literal[2];
		implication[0] = new Literal(l1);
		implication[0].negate();
		implication[1] = new Literal(l2);
		return implication;
	}

	private Literal[] contrapositiveImplication(Literal[] implication) {
		Literal[] contrapositive = new Literal[2];
		contrapositive[0] = new Literal(implication[1]);
		contrapositive[1] = new Literal(implication[0]);
		contrapositive[0].negate();
		contrapositive[1].negate();
		return contrapositive;
	}

	private void kosaraju() {
		for (int vertex = 0; vertex < graph.getVertexCount(); vertex++) {
			visit(vertex);
		}
		graph.setAllUnvisited();
		for (Integer vertex : stack) {
			if (graph.isUnvisited(vertex)) {
				components.put(vertex, new ArrayList<>());
				roots.add(vertex);
				assign(vertex, vertex);
			}
		}
	}

	private void visit(int vertex) {
		if (graph.isUnvisited(vertex)) {
			graph.setVisited(vertex);
			for (Integer outNeighbour : graph.getOutNeighbours(vertex)) {
				visit(outNeighbour);
			}
			stack.push(vertex);
		}
	}

	private void assign(int vertex, int root) {
		if (graph.isUnvisited(vertex)) {
			graph.setVisited(vertex);
			components.get(root).add(vertex);
			for (Integer inNeighbour : transposed.getOutNeighbours(vertex)) {
				assign(inNeighbour, root);
			}
		}
	}

	private boolean isSatisfiable() {
		for (List<Integer> scc : components.values()) {
			if (isSccUnsatisfiable(scc)) {
				return false;
			}
		}
		return true;
	}

	private boolean isSccUnsatisfiable(List<Integer> scc) {
		for (Integer vertex : scc) {
			if (scc.contains(graph.getVertexNegation(vertex))) {
				return true;
			}
		}
		return false;
	}

	private void booleanVariablesAssignment() {
		graph.setAllUnvisited();
		Collections.reverse(roots);
		for (Integer i : roots) {
			List<Integer> scc = components.get(i);
			for (Integer vertex : scc) {
				if (graph.isUnvisited(vertex)) {
					graph.setVisited(vertex);
					graph.setVisited(graph.getVertexNegation(vertex));
					if (graph.isPositive(vertex)) {
						boolVars.put(vertex, true);
					}
					else {
						boolVars.put(graph.getVertexNegation(vertex), false);
					}
				}
			}
		}
	}

	private List<String> readLines(String fileName) {
		List<String> lines = new ArrayList<>();
		InputStream is = this.getClass().getResourceAsStream(fileName);
		try (BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
			String line;
			while ((line = br.readLine()) != null) {
				lines.add(line);
			}
		}
		catch (IOException ex) {
			System.out.println("File " + fileName + " could not be opened.");
			System.exit(1);
		}
		return lines;
	}

}
