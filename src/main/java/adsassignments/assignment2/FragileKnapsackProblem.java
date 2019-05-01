/*
 * ads-assignments: 0-1 fragile knapsack
 *
 * Copyright (c) 2019, Milten Plescott. All rights reserved.
 *
 * SPDX-License-Identifier: MIT
 */
package adsassignments.assignment2;

import adsassignments.Launchable;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Milten Plescott
 */
public class FragileKnapsackProblem implements Launchable {

	private final Knapsack knapsack;

	public FragileKnapsackProblem() {
		List<String> lines = readLines("/predmety.txt");
		this.knapsack = parseLines(lines);
	}

	@Override
	public void launch() {
		knapsack.fillKnapsack();
		writeToFile(knapsack.knapsackToString());
	}

	private Knapsack parseLines(List<String> lines) {
		int itemsCount;
		int weightLimit;
		int fragileLimit;
		List<Integer> weight = new ArrayList<>();
		List<Integer> profit = new ArrayList<>();
		List<Integer> fragile = new ArrayList<>();

		try {
			itemsCount = Integer.parseInt(lines.get(0));
			weightLimit = Integer.parseInt(lines.get(1));
			fragileLimit = Integer.parseInt(lines.get(2));
			for (int i = 3; i < itemsCount + 3; i++) {
				String[] str = lines.get(i).split(" ");
				profit.add(Integer.parseInt(str[1]));
				weight.add(Integer.parseInt(str[2]));
				fragile.add(Integer.parseInt(str[3]));
			}
			return new Knapsack(itemsCount, weightLimit, fragileLimit, weight, profit, fragile);
		}
		catch (NumberFormatException e) {
			System.out.println("Integer parsing error.");
			System.exit(1);
		}
		return null;
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

	void writeToFile(String text) {
		try (FileOutputStream fos = new FileOutputStream("out.txt");
			OutputStreamWriter osw = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
			BufferedWriter bw = new BufferedWriter(osw);//
			) {
			bw.write(text);
		}
		catch (IOException e) {
			System.out.println("File out.txt could not be saved.");
			System.exit(1);
		}

	}

}
