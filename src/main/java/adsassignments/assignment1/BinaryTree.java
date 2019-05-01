/*
 * ads-assignments: optimal binary search tree
 *
 * Copyright (c) 2019, Milten Plescott. All rights reserved.
 *
 * SPDX-License-Identifier: MIT
 */
package adsassignments.assignment1;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 *
 * @author Milten Plescott
 */
class BinaryTree {

	private Node root;

	BinaryTree(List<String> words, List<List<Integer>> roots) {
		root = constructTree(words, roots, 1, words.size());
	}

	// inspired by: CLRS Solutions by Michelle Bodnar and Andrew Lohr
	// http://sites.math.rutgers.edu/~ajl213/CLRS/CLRS.html
	private Node constructTree(List<String> words, List<List<Integer>> roots, int left, int right) {
		if (left > right) {
			return null;
		}
		else if (left == right) {
			Node leaf = new Node(words.get(roots.get(left).get(left) - 1));
			leaf.leftChild = null;
			leaf.rightChild = null;
			return leaf;
		}
		int ind = roots.get(left).get(right);
		Node node = new Node(words.get(ind - 1));
		node.leftChild = constructTree(words, roots, left, ind - 1);
		node.rightChild = constructTree(words, roots, ind + 1, right);
		return node;
	}

	int pocet_porovnani(String hladaneSlovo) {
		Node currentNode = this.root;
		int comparisons = 0;
		while (currentNode != null) {
			comparisons++;
			int cmp = hladaneSlovo.compareTo(currentNode.word);
			if (cmp < 0) {
				currentNode = currentNode.leftChild;
			}
			else if (cmp > 0) {
				currentNode = currentNode.rightChild;
			}
			else {
				return comparisons;
			}
		}
		return comparisons;
	}

	void printTree(int depth) {
		System.out.println("=== OBST ===");
		Queue<Node> queue = new LinkedList<>();
		queue.add(this.root);
		for (int i = 0; i <= depth; i++) {
			StringBuilder stringBuilder = new StringBuilder();
			stringBuilder.append(String.format("Level %2d: ", i));
			int queueSize = queue.size();
			for (int j = 0; j < queueSize; j++) {
				Node n = queue.remove();
				if (n == null) {
					stringBuilder.append("-");
				}
				else {
					stringBuilder.append(n.word);
					queue.add(n.leftChild);
					queue.add(n.rightChild);
				}
				stringBuilder.append(", ");
			}
			System.out.println("" + stringBuilder.toString());
		}
	}

	static class Node {

		private String word;
		private Node leftChild;
		private Node rightChild;

		private Node(String word) {
			this.word = word;
		}

	}

}
