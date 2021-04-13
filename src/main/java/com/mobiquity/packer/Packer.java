package com.mobiquity.packer;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.mobiquity.exception.APIException;

public class Packer {

	private Packer() {
	}
	
	public static ArrayList<ArrayList<Item>> createCombinations(List<Item> items){
		ArrayList<ArrayList<Item>> combinations = new ArrayList<ArrayList<Item>>();
				
		for(int x = 0; x < items.size() ; x++) {
			Item currentItem = items.get(x);			 
			int combinationSize = combinations.size();
			 
			for(int y = 0; y < combinationSize; y++){
				ArrayList<Item> combination = combinations.get(y);
				ArrayList<Item> newCombination = new ArrayList<Item>(combination);
				newCombination.add(currentItem);
				combinations.add(newCombination);
			}
			
			ArrayList<Item> current = new ArrayList<Item>();
			current.add(currentItem);
			combinations.add(current);
			
		}
		return combinations;
	
	}
	
	public static ArrayList<Item> getBestPackage(ArrayList<ArrayList<Item>> combinations ,double weightLimit){
		ArrayList<Item> targetComb= new ArrayList<Item>();
		
		double targetWeight = 100; //max weight is 100		
		double targetCost = 0;
		
		
		for(ArrayList<Item> combinationItems : combinations){
			 			
			
			double combinationWeight = combinationItems.
										stream().
										map(i ->i.getWeight()).
										reduce(0d ,(i1 ,i2) -> i1 + i2);
			
			double combinationPrice= combinationItems.
										stream().
										map(i ->i.getPrice()).
										reduce(0d ,(i1 ,i2) -> i1 + i2);
			
			if(combinationWeight > weightLimit)
				continue;			
			else if(combinationItems.size() > 15)
				continue;
			else{
				if(combinationPrice > targetCost){
					targetWeight = combinationWeight;
					targetCost = combinationPrice;
					targetComb = combinationItems;					
					
				}else if(combinationPrice == targetCost){	 
					if(combinationWeight < targetWeight){
						targetWeight = combinationWeight;
						targetCost = combinationPrice;
						targetComb = combinationItems;						
					}
				}
			}
		}
		return targetComb;
	}
	
	public static String findPackage(List<Item> items ,double weightLimit){
		
		//remove items with weight over the limit		
		List<Item> itemsNew = items.
								stream().
								filter(i -> i.getWeight() <= weightLimit).collect(Collectors.toList());
		
		ArrayList<ArrayList<Item>> combinations = createCombinations(itemsNew);
		
		
		ArrayList<Item> bestCombination = getBestPackage(combinations ,weightLimit);
			
		bestCombination.sort((o1 ,o2) ->o1.getIndex() -o2.getIndex());
			
		return getItemsIndexesSeparatedWithCommas(bestCombination);
					
	}

	public static String getItemsIndexesSeparatedWithCommas(ArrayList<Item> items){
		
		if( items == null || items.size() ==0)
			return "-";
		
		StringBuilder resultStr = new StringBuilder();
		
		items.stream()
			.forEach(i -> resultStr.append("," + i.getIndex()));
		
		return resultStr.substring(1);//just remove the comma(,) at the beginning
	}
	
	 public static String pack(String absoluteFilePath) throws APIException {
		  
		 	Path path = Paths.get(absoluteFilePath);
		 	
		 	StringBuilder output = new StringBuilder();	 
		 			
		 	try(Stream<String> stream = Files.lines(path, StandardCharsets.UTF_8))
	        {
	            stream.forEach(s -> {
	            	
	            	String[] lineArray = s.split(":");
					
					double weightLimit = Integer.parseInt(lineArray[0].trim());
					String[] itemsArr = lineArray[1].trim().split(" ");
					
					ArrayList<Item> items = new ArrayList<Item>();
					
					for(String itemStr: itemsArr) {
						String itemDetails[] = itemStr.split(",");
						int index = Integer.parseInt(itemDetails[0].substring(1));
						
						double weight = Double.parseDouble(itemDetails[1]);
						double price = Double.parseDouble(itemDetails[2].substring(1, itemDetails[2].length()-1));
						
						Item item = new Item(index, weight, price);
						items.add(item);
					}
					 
					output.append(findPackage(items ,weightLimit));
					output.append(System.lineSeparator());	            	
	            });	             
	        } 
	        catch (IOException e) {
				// TODO Auto-generated catch block
				throw new APIException("an exception occurred: "+ e.getMessage());
			}
		 
		   return output.toString();
	  }

}
