package plagdetect;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class PlagiarismDetector implements IPlagiarismDetector {
	
	public int n;
	public Map<String, Map<String, Integer>> results = new HashMap<>();
	public  Map<String, Set<String>> ngram = new HashMap<>();
	public PlagiarismDetector(int n) {
		this.n = n;
		
	}
	
	@Override
	public int getN() {
		return this.n;
	}

	@Override
	public Collection<String> getFilenames() {
		// TODO Auto-generated method stub
		Collection<String> temp = new HashSet<>();
		for(Map.Entry<String, Set<String>> filename: ngram.entrySet()) {
			temp.add(filename.getKey());
		}
		return temp;
	}

	@Override
	public Collection<String> getNgramsInFile(String filename) {
		return ngram.get(filename);
	}

	@Override
	public int getNumNgramsInFile(String filename) {
		// TODO Auto-generated method stub
		Collection<String> temp = getNgramsInFile(filename);
		int temp2 = temp.size();
		return temp2;
	}

	@Override
	public Map<String, Map<String, Integer>> getResults() {
		// TODO Auto-generated method stub
		var entrySet = ngram.entrySet();
		for (var entry : entrySet) {
			//System.out.println(set.getKey());
			for (var entry2 : entrySet) { //For some reason these loops dont add all different types of combinations to the map.
				//System.out.println(set2.getKey());
				   if(entry.getKey() != entry2.getKey()) {
					int ctr = 0;
					   for(String s : entry.getValue()) {
						   if(entry2.getValue().contains(s)) {
							   ctr++;
						   }
					   }
				   	
				   final Integer i = Integer.valueOf(ctr);
				   Map<String, Integer> set2Map = new HashMap<>();
				   set2Map.put(entry2.getKey(), i);
				   results.put(entry.getKey(), set2Map);
			}
			}
	
	}
		return results;
	}
	@Override
	public void readFile(File file) throws IOException {
		// TODO Auto-generated method stub
		// most of your work can happen in this method
		Set<String> grams = new HashSet<>();
		Scanner sc = new Scanner(file);
		while(sc.hasNextLine()) {
			String temp = sc.nextLine();
			String[] arr = temp.split(" ");
			for(int i = 0; i <= arr.length - getN(); i++) {
				StringBuilder str = new StringBuilder();
				for(int j = i; j < i + getN(); j++) {
					if(j == i + getN() - 1) {
						str.append(arr[j]);
					}
					else {
						str.append(arr[j] + " ");
					}
				}
				//System.out.println(str);
				grams.add(str.toString());
			}
			//System.out.println(file.toString());
		}
		ngram.put(file.getName(), grams);
//System.out.println(ngram);
	}

	@Override
	public int getNumNGramsInCommon(String file1, String file2) {
		// TODO Auto-generated method stub
		//System.out.println(results);
		//System.out.println(temp);
		Integer i = results.get(file1).get(file2);
		if(i == null) return 0;
		return i.intValue();
	}

	@Override
	public Collection<String> getSuspiciousPairs(int minNgrams) {
		// TODO Auto-generated method stub
		Collection<String> set = new HashSet<>();
		for (Map.Entry<String, Map<String, Integer>> sus : results.entrySet()) {
			for(Map.Entry<String, Integer> sus2: sus.getValue().entrySet()) {
				if(sus2.getValue() >= minNgrams && sus.getKey() != sus2.getKey()) {
					set.add(sus.getKey() + " " + sus2.getKey() + " " +sus2.getValue().toString());
				}
			}
		}
		return set;
	}

	@Override
	public void readFilesInDirectory(File dir) throws IOException {
		// delegation!
		// just go through each file in the directory, and delegate
		// to the method for reading a file
		for (File f : dir.listFiles()) {
			readFile(f);
		}
		getResults();
	}
}
