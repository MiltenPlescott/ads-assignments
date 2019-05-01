/*
 * ads-assignments: optimal binary search tree
 *
 * Copyright (c) 2019, Milten Plescott. All rights reserved.
 *
 * SPDX-License-Identifier: MIT
 */
package adsassignments.assignment1;

import adsassignments.Launchable;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Milten Plescott
 */
public class OptimalBinarySearchTree implements Launchable {

	// keys: freq > 50000
	// dummy keys: freq <= 50000
	private static final int LIMIT = 50000;

	private static Dictionary fullDict;
	private static Dictionary keysDict;
	private static Dictionary dummyDict;

	public OptimalBinarySearchTree() {
		List<String> linesFreqFirst = readLines("/dictionary.txt");
		List<String> linesWordFirst = DictionaryParser.swapWordsFreqs(linesFreqFirst);
		Collections.sort(linesWordFirst);

		createDicts(linesWordFirst);
		computeProbabilities();
		verifyProbabilities();
	}

	/**
	 * @param lines [word space frequency], sorted lexicographically
	 */
	private void createDicts(List<String> lines) {
		fullDict = DictionaryParser.parseLines(lines);
		Dictionary[] splitDictionaries = DictionaryParser.splitDict(fullDict, LIMIT);
		keysDict = splitDictionaries[0];
		dummyDict = splitDictionaries[1];
	}

	private void computeProbabilities() {
		int absFreqSum = fullDict.sumAbsFreq();
		keysDict.computeProbabilities(absFreqSum);

		int dummyAbsSumTmp = 0;
		List<Double> dummyProbabilities = new ArrayList<>();
		for (int indKeys = 0, indFull = 0; indFull < fullDict.getWords().size(); indFull++) {
			if (indKeys < keysDict.getWords().size() && keysDict.getWords().get(indKeys).equals(fullDict.getWords().get(indFull))) {
				dummyProbabilities.add((double) dummyAbsSumTmp / absFreqSum);
				dummyAbsSumTmp = 0;
				indKeys++;
			}
			else {
				dummyAbsSumTmp += fullDict.getAbsFreq().get(indFull);
			}
		}
		dummyProbabilities.add((double) dummyAbsSumTmp / absFreqSum);
		dummyDict.setProbability(dummyProbabilities);
	}

	private void verifyProbabilities() {
		double probSum = keysDict.sumProbabilities() + dummyDict.sumProbabilities();
		System.out.println("Sum of probabilities p, q: " + dummyDict.sumProbabilities() + " + " + keysDict.sumProbabilities() + " = " + probSum);
		assert Math.abs(1.0 - probSum) < 0.00000000001;
		System.out.println("p.size: " + keysDict.getProbability().size() + ",  keys.words.size: " + keysDict.getWords().size());
		System.out.println("q.size: " + dummyDict.getProbability().size() + ",  dummy.words.size: " + dummyDict.getWords().size());
		assert dummyDict.getProbability().size() == keysDict.getProbability().size() + 1;
	}

	@Override
	public void launch() {
		keysDict.getProbability().add(0, null);  // p[0..n-1] -> p[null,1..n]
		List<List<Integer>> roots = optimalMatrices(keysDict.getProbability(), dummyDict.getProbability(), dummyDict.getProbability().size() - 1);
		BinaryTree tree = new BinaryTree(keysDict.getWords(), roots);

		tree.printTree(10);
		System.out.println("=== POCET POROVNANI ===");
		System.out.println("of: " + tree.pocet_porovnani("of"));
		System.out.println("for: " + tree.pocet_porovnani("for"));
		System.out.println("the: " + tree.pocet_porovnani("the"));
		System.out.println("again: " + tree.pocet_porovnani("again"));
		System.out.println("must: " + tree.pocet_porovnani("must"));
		System.out.println("peoz: " + tree.pocet_porovnani("peoz"));
		System.out.println("zzz: " + tree.pocet_porovnani("zzz"));
	}

	/**
	 * @param p {null, p_1, p_2, ... p_n}
	 * @param q {q_0 , q_1, q_2, ... q_n}
	 * @param n number of keys
	 * @return roots
	 */
	private List<List<Integer>> optimalMatrices(List<Double> p, List<Double> q, int n) {
		// e[0] = not_used,  e[1..n+1, 0..n] = null
		List<List<Double>> e = new ArrayList<>();
		e.add(null);
		for (int i = 1; i <= n + 1; i++) {
			e.add(new ArrayList<>());
			for (int j = 0; j <= n; j++) {
				e.get(i).add(null);
			}
		}

		// w[0] = not_used,  w[1..n+1, 0..n] = null
		List<List<Double>> w = new ArrayList<>();
		w.add(null);
		for (int i = 1; i <= n + 1; i++) {
			w.add(new ArrayList<>());
			for (int j = 0; j <= n; j++) {
				w.get(i).add(null);
			}
		}

		// roots[0] = not_used,  roots[1..n, 0] = not_used,  roots[1..n, 1..n] = null
		List<List<Integer>> roots = new ArrayList<>();
		roots.add(null);
		for (int i = 1; i <= n; i++) {
			roots.add(new ArrayList<>());
			roots.get(i).add(null);
			for (int j = 1; j <= n; j++) {
				roots.get(i).add(null);
			}
		}

		// init e, w : [1..n+1, 0..n] = q[0..n]
		for (int i = 1; i <= n + 1; i++) {
			e.get(i).set(i - 1, q.get(i - 1));
			w.get(i).set(i - 1, q.get(i - 1));
		}

		for (int l = 1; l <= n; l++) {
			for (int i = 1; i <= n - l + 1; i++) {
				int j = i + l - 1;
				e.get(i).set(j, Double.MAX_VALUE);
				w.get(i).set(j, w.get(i).get(j - 1) + p.get(j) + q.get(j));
				for (int r = i; r <= j; r++) {
					double t = e.get(i).get(r - 1) + e.get(r + 1).get(j) + w.get(i).get(j);
					if (t < e.get(i).get(j)) {
						e.get(i).set(j, t);
						roots.get(i).set(j, r);
					}
				}
			}
		}

		System.out.println("e[1,n]: " + e.get(1).get(n));
		System.out.println("w[1,n]: " + w.get(1).get(n));
		System.out.println("roots[1,n]: " + roots.get(1).get(n));
		return roots;
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
			ex.printStackTrace();
		}
		return lines;
	}

}
