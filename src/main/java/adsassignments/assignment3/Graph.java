/*
 * ads-assignments: 2-SAT solver
 *
 * Copyright (c) 2019, Milten Plescott. All rights reserved.
 *
 * SPDX-License-Identifier: MIT
 */
package adsassignments.assignment3;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author Milten Plescott
 */
public class Graph {

	private final int vertexCount;
	private final List<List<Integer>> adjList;  // order of literals in outer list: x0, x1, ..., xn, ¬x0, ¬x1, ..., ¬xn
	private final int offset;
	private final List<Boolean> visited;

	Graph(int vertexCount) {
		if (vertexCount % 2 == 1) {
			throw new Error("Adjacency matrix needs an even number of vertices.");
		}
		this.vertexCount = vertexCount;
		this.offset = vertexCount / 2;
		this.adjList = new ArrayList<>();
		this.visited = new ArrayList<>();
		for (int i = 0; i < vertexCount; i++) {
			this.adjList.add(new ArrayList<>());
			this.visited.add(false);
		}
	}

	Graph getTransposedGraph() {
		Graph transposed = new Graph(this.vertexCount);
		for (int i = 0; i < vertexCount; i++) {
			for (int j = 0; j < this.adjList.get(i).size(); j++) {
				// transposed.adjList.get(this.adjList.get(i).get(j)).add(i);
				int oldFrom = i;
				int oldTo = this.adjList.get(i).get(j);
				transposed.adjList.get(oldTo).add(oldFrom);
			}
		}
		return transposed;
	}

	void removeDuplicateEdges() {
		for (int i = 0; i < adjList.size(); i++) {
			Set<Integer> set = new HashSet<>();
			for (int j = 0; j < adjList.get(i).size(); j++) {
				set.add(adjList.get(i).get(j));
			}
			adjList.set(i, new ArrayList<>());
			for (Integer k : set) {
				adjList.get(i).add(k);
			}
		}
	}

	// from, to: indexed from 0
	void addEdge(Literal from, Literal to) {
		addEdge(getAdjListIndex(from), getAdjListIndex(to));
	}

	// from, to: indexed from 0
	void addEdge(int from, int to) {
		this.adjList.get(from).add(to);
	}

	int getAdjListIndex(Literal lit) {
		return lit.isPositive() ? lit.getIndex() : lit.getIndex() + offset;
	}

	int getVertexNegation(int vertex) {
		if (vertex < offset) {
			return vertex + offset;
		}
		return vertex - offset;
	}

	boolean isPositive(int vertex) {
		return (vertex < offset);
	}

	boolean isNegative(int vertex) {
		return !isPositive(vertex);
	}

	boolean isVisited(int vertex) {
		return this.visited.get(vertex);
	}

	boolean isUnvisited(int vertex) {
		return !this.visited.get(vertex);
	}

	void setVisited(int vertex) {
		this.visited.set(vertex, true);
	}

	void setAllUnvisited() {
		for (int i = 0; i < this.visited.size(); i++) {
			this.visited.set(i, false);
		}
	}

	// G: A->B, A->C
	// B, C are out-neighbours of A
	List<Integer> getOutNeighbours(int vertex) {
		return this.adjList.get(vertex);
	}

	int getVertexCount() {
		return vertexCount;
	}

	int getOffset() {
		return offset;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		String ls = System.lineSeparator();
		String not = "¬";
		for (int i = 0; i < vertexCount; i++) {
			if (i < offset) {
				builder.append(" ");
			}
			else {
				builder.append(not);
			}
			builder.append("x").append(i % offset).append(" -> = ").append(adjList.get(i).toString()).append(ls);
		}
		return builder.toString();
	}

}
