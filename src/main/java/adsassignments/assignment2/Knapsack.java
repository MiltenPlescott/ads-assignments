/*
 * ads-assignments: 0-1 fragile knapsack
 *
 * Copyright (c) 2019, Milten Plescott. All rights reserved.
 *
 * SPDX-License-Identifier: MIT
 */
package adsassignments.assignment2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author Milten Plescott
 */
public class Knapsack {

	private final int itemsCount;
	private final int weightLimit;
	private final int fragileLimit;

	// items data
	private final int[] weight;
	private final int[] profit;
	private final int[] fragile;

	// optimally filled fragile 0-1 knapsack
	private int maxProfit;  // profit of all items chosen to be in the knpsack
	private List<Integer> chosenItems;  // indices of items chosen to be in the knapsack

	public Knapsack(int itemsCount, int weightLimit, int fragileLimit, List<Integer> weight, List<Integer> profit, List<Integer> fragile) {
		this.itemsCount = itemsCount;
		this.weightLimit = weightLimit;
		this.fragileLimit = fragileLimit;

		this.weight = new int[itemsCount];
		this.profit = new int[itemsCount];
		this.fragile = new int[itemsCount];
		for (int i = 0; i < itemsCount; i++) {
			this.weight[i] = weight.get(i);
			this.profit[i] = profit.get(i);
			this.fragile[i] = fragile.get(i);
		}
	}

	void fillKnapsack() {
		int[][][] array = new int[itemsCount][weightLimit][fragileLimit + 1];  // [1 .. itemsCount][1 .. weightLimit][0 .. fragileLimit]
		Set<Integer>[][][] itemIndices = new HashSet[itemsCount][weightLimit][fragileLimit + 1];
		initArray(itemIndices);

		for (int i = 0; i < itemsCount; i++) {  // item
			System.out.println("i: " + i);
			for (int w = 1; w <= weightLimit; w++) {  // weight limit of knapsack
				for (int f = 0; f <= fragileLimit; f++) {  // number of fragile items allowed
					if (weight[i] > w) {  // i-th item does not fit in the knapsack with weight limit w
						array[i][w - 1][f] = getElement(array, i - 1, w - 1, f);
						itemIndices[i][w - 1][f] = getSetCopy(getElement(itemIndices, i - 1, w - 1, f));
					}
					else { // i-th item does fit in the knapsack with weight limit w
						if (f == 0 && fragile[i] == 1) {  // preventing [f-1] from throwing ArrayIndexOutOfBoundsException
							array[i][w - 1][f] = getElement(array, i - 1, w - 1, f);
							itemIndices[i][w - 1][f] = getSetCopy(getElement(itemIndices, i - 1, w - 1, f));
						}
						else {
							int a = getElement(array, i - 1, w - 1, f);
							int b = getElement(array, i - 1, w - 1 - weight[i], f - fragile[i]) + profit[i];
							if (a >= b) {
								array[i][w - 1][f] = a;
								itemIndices[i][w - 1][f] = getSetCopy(getElement(itemIndices, i - 1, w - 1, f));
							}
							else {
								array[i][w - 1][f] = b;
								itemIndices[i][w - 1][f] = getSetCopy(getElement(itemIndices, i - 1, w - 1 - weight[i], f - fragile[i]));
								itemIndices[i][w - 1][f].add(i);
							}
						}
					}
				}
			}
		}

		this.maxProfit = array[itemsCount - 1][weightLimit - 1][fragileLimit];
		System.out.println("Max profit: " + maxProfit);
		this.chosenItems = new ArrayList<>(itemIndices[itemsCount - 1][weightLimit - 1][fragileLimit]);
		verifyChosenItems();
	}

	private void initArray(Set<Integer>[][][] array) {
		for (int i = 0; i < array.length; i++) {
			for (int j = 0; j < array[0].length; j++) {
				for (int k = 0; k < array[0][0].length; k++) {
					array[i][j][k] = new HashSet<>();
				}
			}
		}
	}

	int getElement(int[][][] array, int i, int j, int k) {
		if (i < 0 || j < 0 || k < 0) {
			return 0;
		}
		return array[i][j][k];
	}

	Set<Integer> getElement(Set<Integer>[][][] array, int i, int j, int k) {
		if (i < 0 || j < 0 || k < 0) {
			return new HashSet<>();
		}
		return array[i][j][k];
	}

	private Set getSetCopy(Set<Integer> set) {
		Set<Integer> newSet = new HashSet<>();
		newSet.addAll(set);
		return newSet;
	}

	private void verifyChosenItems() {
		Set<Integer> set = new HashSet<>(chosenItems);
		if (set.size() != chosenItems.size()) {
			throw new Error("Chosen items list contains duplicates.");
		}
		int currentFragile = 0;
		int currentWeight = 0;
		int currentProfit = 0;
		for (int i : chosenItems) {
			currentProfit += profit[i];
			currentWeight += weight[i];
			currentFragile += fragile[i];
		}
		if (currentProfit != maxProfit) {
			System.out.println("Sum of profits of chosen items: " + currentProfit);
			System.out.println("Max profit from matrix: " + maxProfit);
			throw new Error("Chosen items add up to a different profit.");
		}
		if (currentWeight > weightLimit) {
			System.out.println("Knapsack weight: " + currentWeight);
			System.out.println("Weight limit: " + weightLimit);
			throw new Error("Chosen items do not fit in the weight limit.");
		}
		if (currentFragile > fragileLimit) {
			System.out.println("Fragile items: " + currentFragile);
			System.out.println("Fragile limit: " + fragileLimit);
			throw new Error("Chosen items contain too many fragile items.");
		}
	}

	String knapsackToString() {
		StringBuilder builder = new StringBuilder();
		String ls = System.lineSeparator();
		builder.append(this.maxProfit).append(ls);
		builder.append(chosenItems.size()).append(ls);
		Collections.sort(chosenItems);
		for (int i : chosenItems) {
			builder.append(i + 1).append(ls);  // chosenItems are indexed from 0; items in .txt file are indexed from 1
		}
		return builder.toString();
	}

}
