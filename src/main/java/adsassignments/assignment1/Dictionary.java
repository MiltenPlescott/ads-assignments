/*
 * ads-assignments: optimal binary search tree
 *
 * Copyright (c) 2019, Milten Plescott. All rights reserved.
 *
 * SPDX-License-Identifier: MIT
 */
package adsassignments.assignment1;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Milten Plescott
 */
class Dictionary {

	private List<String> words;
	private List<Integer> absFreq;
	private List<Double> probability;

	Dictionary() {
		this.words = new ArrayList<>();
		this.absFreq = new ArrayList<>();
		this.probability = new ArrayList<>();
	}

	int sumAbsFreq() {
		int sum = 0;
		for (int i : this.absFreq) {
			sum += i;
		}
		return sum;
	}

	double sumProbabilities() {
		double sum = 0;
		for (double i : this.probability) {
			sum += i;
		}
		return sum;
	}

	void computeProbabilities(int absFreqSum) {
		for (int i = 0; i < words.size(); i++) {
			probability.add((double) absFreq.get(i) / absFreqSum);
		}
	}

	List<String> getWords() {
		return words;
	}

	void setWords(List<String> words) {
		this.words = words;
	}

	List<Integer> getAbsFreq() {
		return absFreq;
	}

	void setAbsFreq(List<Integer> absFreq) {
		this.absFreq = absFreq;
	}

	List<Double> getProbability() {
		return probability;
	}

	void setProbability(List<Double> probability) {
		this.probability = probability;
	}

}
