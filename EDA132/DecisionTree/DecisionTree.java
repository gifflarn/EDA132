package DecisionTree;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DecisionTree {
	public static String relation;
	// possible "splitters", known factors
	public static List<String> attributes;
	// possible final answers, i.e. True and False for a binary question
	public static List<String> possibilities;
	// map to get attribute position in each data entry
	public static Map<String, Integer> attributeMap;
	// the data entries
	public static List<List<String>> entries;

	static int counter = 0;

	public static void main(String[] args) {
		relation = new String();
		attributes = new ArrayList<String>();
		possibilities = new ArrayList<String>();
		attributeMap = new HashMap<String, Integer>();
		entries = new ArrayList<List<String>>();
		parse(args[0]);
		// Entry e = null;
		System.out.println(relation);
		System.out.println(attributes);
		System.out.println(possibilities);
		System.out.println(attributeMap);
		System.out.println(entries);

		buildTree(entries, entries.size());

	}

	private static void buildTree(List<List<String>> entries, int size) {
		System.out.println(size);
		if (size == 0 || entries.isEmpty()) {
			return;
		}
		counter = 0;
		double entropy = calcEntropy(entries, size);
		double maxgain = 0;
		int toSplit = 0;
		for (int i = 0; i < attributes.size() - 1; i++) {
			double currGain = calcGain(entropy, i);
			if (currGain > maxgain) {
				maxgain = currGain;
				toSplit = i;
			}
		}
		attributes.remove(toSplit);
		HashMap<String, ArrayList<List<String>>> map = new HashMap<String, ArrayList<List<String>>>();
		for (List<String> temp : entries) {
			try {
				ArrayList<List<String>> copy = map.get(temp.get(toSplit));
			//	temp.remove(toSplit);
				copy.add(temp);
				map.put(temp.remove(toSplit), copy);
			} catch (NullPointerException e) {
				ArrayList<List<String>> copy = new ArrayList<List<String>>();
				copy.add(temp);
				map.put(temp.remove(toSplit), copy);
			}
		}
		System.out.println(map);
		for(Map.Entry<String, ArrayList<List<String>>> currMap : map.entrySet()){
			buildTree(currMap.getValue(), size--);
		}
	}

	private static double calcGain(double entropy, int current) {
		double gain = entropy;
		Map<String, Integer> map = new HashMap<String, Integer>();
		for (List<String> temp : entries) {
			Integer count = map.get(temp.get(current));
			map.put(temp.get(current), (count == null) ? 1 : count + 1);
		}
		// Windy, {weak = 8, strong = 6}

		HashMap<String, List<List<String>>> list = new HashMap<String, List<List<String>>>();
		for (Map.Entry<String, Integer> curr : map.entrySet()) {

			ArrayList<List<String>> tempList = new ArrayList<List<String>>();
			for (int i = 0; i < entries.size(); i++) {
				ArrayList<String> tempTempList = new ArrayList<String>();
				if (entries.get(i).get(counter).equals(curr.getKey())) {
					tempTempList.add(entries.get(i).get(attributes.size() - 1));
					tempList.add(tempTempList);
					list.put(curr.getKey(), tempList);
				}
			}
		}
		counter++;
		for (Map.Entry<String, Integer> curr : map.entrySet()) {
			double temp = curr.getValue();
			double temp2 = entries.size();
			double temp3 = calcEntropy(list.get(curr.getKey()), curr.getValue());
			double temp4 = (temp / temp2) * temp3;
			gain -= temp4;
		}
		return gain;
	}

	private static double calcEntropy(List<List<String>> entries, int size) {
		double entropy = 0;

		Map<String, Integer> map = new HashMap<String, Integer>();
		for (List<String> temp : entries) {
			Integer count = map.get(temp.get(entries.get(0).size() - 1));
			map.put(temp.get(entries.get(0).size() - 1), (count == null) ? 1
					: count + 1);
		}
		for (Map.Entry<String, Integer> curr : map.entrySet()) {
			double value = curr.getValue();
			entropy -= (value / size) * log2(value / size);
		}
		return entropy;

	}

	private static double log2(double value) {
		return (Math.log(value) / Math.log(2.));
	}

	private static void parse(String input) {
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(input));
			int counter = 0;
			String line;
			while ((line = br.readLine()) != null) {
				if (line.isEmpty() || line.charAt(0) == '%') {
					continue;
				} else if (line.charAt(0) == '@') {
					if (line.toLowerCase().contains("relation")) {
						relation = line.split(" ")[1];
					} else if (line.toLowerCase().contains("attribute")) {
						line = line.replace("{", "").replace("}", "")
								.replace(",", "");
						String[] array = line.split(" ");
						attributeMap.put(array[1], counter);
						attributes.add(array[1]);
						counter++;
					} else if (line.toLowerCase().contains("data")) {
						Map<String, List<String>> preliminaryComparators = new HashMap<String, List<String>>();
						for (String s : attributes) {
							preliminaryComparators.put(s,
									new ArrayList<String>());
						}
						while ((line = br.readLine()) != null) {
							String[] dataArray = line.split(",");
							ArrayList<String> entry = new ArrayList<String>();
							for (int i = 0; i < dataArray.length - 1; i++) {
								// the recieved data, could be numbers or
								// strings
								entry.add(dataArray[i]);
								List<String> curr = preliminaryComparators
										.get(attributes.get(i));
								curr.add(dataArray[i]);
							}
							if (!possibilities
									.contains(dataArray[dataArray.length - 1])) {
								possibilities
										.add(dataArray[dataArray.length - 1]);
							}
							entry.add(dataArray[dataArray.length - 1]);
							entries.add(entry);
						}
					}
				}

			}

			br.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
