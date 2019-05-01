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
class DictionaryParser {

	private DictionaryParser() {
		throw new AssertionError("Suppress default constructor for noninstantiability.");
	}

	// input line format:
	// "freq word"
	// 6187267 the\n
	// output line format:
	// "word freq"
	// the 6187267\n
	static List<String> swapWordsFreqs(List<String> countFirst) {
		List<String> wordFirst = new ArrayList<>();
		for (String line : countFirst) {
			String[] strArr = line.split(" ");
			String newLine = strArr[1] + " " + strArr[0];
			wordFirst.add(newLine);
		}
		return wordFirst;
	}

	// line format:
	// "word freq"
	// the 6187267\n
	static Dictionary parseLines(List<String> lines) {
		Dictionary dict = new Dictionary();
		List<String> words = new ArrayList<>();
		List<Integer> absFreq = new ArrayList<>();
		for (String line : lines) {
			String[] splitLine = line.split(" ");
			String word = splitLine[0];
			try {
				int freq = Integer.parseInt(splitLine[1]);
				words.add(word);
				absFreq.add(freq);
			}
			catch (NumberFormatException ex) {
				System.out.println("Frequency '" + splitLine[1] + "' of word '" + word + "' could not be parsed as int.");
				System.exit(1);
			}
		}
		dict.setWords(words);
		dict.setAbsFreq(absFreq);
		return dict;
	}

	static Dictionary[] splitDict(Dictionary dict, int limit) {
		Dictionary[] dictArr = new Dictionary[2];
		Dictionary keysDict = new Dictionary();
		Dictionary dummyDict = new Dictionary();
		for (int i = 0; i < dict.getWords().size(); i++) {
			if (dict.getAbsFreq().get(i) > limit) {
				keysDict.getWords().add(dict.getWords().get(i));
				keysDict.getAbsFreq().add(dict.getAbsFreq().get(i));
			}
			else {
				dummyDict.getWords().add(dict.getWords().get(i));
				dummyDict.getAbsFreq().add(dict.getAbsFreq().get(i));
			}
		}
		dictArr[0] = keysDict;
		dictArr[1] = dummyDict;
		return dictArr;
	}

}
