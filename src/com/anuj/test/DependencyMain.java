package com.anuj.test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class DependencyMain {
	private static Map<String, HashSet<Type>> dependencyMap = new HashMap<String, HashSet<Type>>();
	private static HashSet<String> installedSet = new HashSet<String>();
	
	public ArrayList<String> createDependency(String[] str){
		
		ArrayList<String> subStrings = new ArrayList<String>();
		
		for(int i=0 ; i<str.length; i++){
			String sub = new String();

			for(int j =i+1; j<str.length; j++){
				sub+=str[j];
			}
			if(sub.length()>1){
				subStrings.add(sub);
			}
		}
		
		return subStrings;
	}
	
	
	public static void cleanMap(String removingType){
		boolean typeFound = false;

		for (HashSet<Type> types : dependencyMap.values()) {

			for (Type t : types) {
				if (t.equals(removingType)) {
					typeFound = true;
					types.remove(removingType);
				}
			}
		}

		Iterator<Entry<String, HashSet<Type>>> it = dependencyMap.entrySet().iterator();
		while (it.hasNext()) {

			Map.Entry pairs = (Map.Entry) it.next();
			HashSet<Type> existingTypes = ((HashSet<Type>)pairs.getValue());
			if(existingTypes.size()==0){
				it.remove();
			}
		}
		
		if(typeFound){
			System.out.println("\t" + "Removing " + removingType);
		}
	}
	
	
	public static void main(String[] args) {

		try {
			BufferedReader br = new BufferedReader(new FileReader(
					"res/input.txt"));
			String line;
			while ((line = br.readLine()) != null) {
				String[] str = line.split(" ");

				if (str[0].equals("DEPEND")) {

					// use the createDependency to generated the list of arrays
					
					String key = str[1];
					HashSet<Type> types = null;
					if (dependencyMap.containsKey(key)) {
						types = dependencyMap.get(key);
					} else {
						types = new HashSet<Type>();
					}
					for (int i = 2; i < str.length; i++) {
						Type type = new Type(str[i]);
						types.add(type);
					}

					dependencyMap.put(key, types);
					
				} else if (str[0].equals("INSTALL")) {
					// print
					String key = str[1];
					HashSet<Type> types = null;
					if (dependencyMap.containsKey(key)) {
						types = dependencyMap.get(key);
					}
					System.out.println("INSTALL " + key);
					System.out.println("\t" + "Installing " + key);
					if(types!=null){
						for(Type t :  types){
							installedSet.add(t.getType());
							t.setInstalled(true);
							System.out.println("\t" + "Installing " + t.getType());
						}

					}
					
				} else if (str[0].equals("REMOVE")) {
					// validate and remove
					String removingType = str[1];
					System.out.println("REMOVING " + removingType);
					if (dependencyMap.containsKey(removingType)) {
						if (installedSet.contains(removingType)){
							System.out.println("\t" + removingType + " is still needed");
						}
						else{
							// remove it from all the values
							cleanMap(removingType);
						}
					}
					
					else {
						// If there is no key remove the Type from all the map
						// values
						cleanMap(removingType);
					}
				} else if (str[0].equals("LIST")) {
					Iterator it = dependencyMap.entrySet().iterator();
					
					Set<Type> printingTypes = new HashSet<Type>();
					while (it.hasNext()) {

						Map.Entry pairs = (Map.Entry) it.next();
						HashSet<Type> existingTypes = ((HashSet<Type>)pairs.getValue());
						printingTypes.addAll(existingTypes);
					}
					System.out.println("LIST");
					for(Type t : printingTypes){
						System.out.println("\t"+t.getType());
					}
					
				} else if (str[0].equals("END")) {
					System.out.println("END");
				}
			}
			br.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
