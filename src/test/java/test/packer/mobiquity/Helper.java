package test.packer.mobiquity;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import com.mobiquity.exception.APIException;
import com.mobiquity.packer.Item;
import com.mobiquity.packer.Packer;

class Helper {

	@Test
	void test() {
		ArrayList<Item> items = new ArrayList<Item>();
		
		
		assertTrue(Packer.getItemsIndexesSeparatedWithCommas(items).equals("-") );
		
		items.add(new Item(1, 15.3, 34));
		assertTrue(Packer.getItemsIndexesSeparatedWithCommas(items).equals("1") );
		
		items.add(new Item(2, 88.62, 98));		
		assertTrue(Packer.getItemsIndexesSeparatedWithCommas(items).equals("1,2") );	
	}
	
	@Test
	public void testCreateCombinations() {
		//Test with only 1 element
		ArrayList<Item> items1 = new ArrayList<Item>();
		Item item1 = new Item(1,53.38,45);
		items1.add(item1);
		
		ArrayList<ArrayList<Item>> expecteditems1 = new ArrayList<ArrayList<Item>>();					
		expecteditems1.add(items1);
		
		assertEquals(expecteditems1 , Packer.createCombinations(items1));
		
		//Test with 2 elements
		Item item3 = new Item(3, 78.48, 3);		
		ArrayList<Item> items1and3 =  new ArrayList<Item>();
		items1and3.add(item1);
		items1and3.add(item3);
		
		ArrayList<Item> items3 = new ArrayList<Item>();
		items3.add(item3);
		
		ArrayList<ArrayList<Item>> expecteditems2 = new ArrayList<ArrayList<Item>>();	
		expecteditems2.add(items1);
		expecteditems2.add(items1and3);
		expecteditems2.add(items3);
				
		assertEquals(expecteditems2 , Packer.createCombinations(items1and3));
		
		//Test with 3 elements
		Item item4 = new Item(4 ,72.30 ,76);
		
		ArrayList<Item> items4 = new ArrayList<Item>();
		items4.add(item4);
		
		ArrayList<Item> items1and4 =  new ArrayList<Item>();
		items1and4.add(item1);
		items1and4.add(item4);
		
		ArrayList<Item> items1and3and4 =  new ArrayList<Item>();
		items1and3and4.add(item1);
		items1and3and4.add(item3);
		items1and3and4.add(item4);
		
		ArrayList<Item> items3and4 =  new ArrayList<Item>();
		items3and4.add(item3);
		items3and4.add(item4);
		
		ArrayList<ArrayList<Item>> expecteditems3 = new ArrayList<ArrayList<Item>>();	
		expecteditems3.add(items1);
		expecteditems3.add(items1and3);
		expecteditems3.add(items3);
		expecteditems3.add(items1and4);
		expecteditems3.add(items1and3and4);
		expecteditems3.add(items3and4);
		expecteditems3.add(items4);
		
		assertEquals(expecteditems3 , Packer.createCombinations(items1and3and4));
		
	}
	
	@Test
	public void testGetBestPackage() {
		
		ArrayList<Item> items134 = new ArrayList<Item>();	
		double weightLimit = 81;
		
		Item item1 = new Item(1 ,53.38 ,45);
		Item item3 = new Item(3, 78.48 , 3);	
		Item item4 = new Item(4 ,72.30 ,76);
		
		
		items134.add(item1);		 
		items134.add(item3);		
		items134.add(item4);
		
		ArrayList<ArrayList<Item>> combinations = Packer.createCombinations(items134);
		
		ArrayList<Item> expectedCombination = new ArrayList<Item>();
		expectedCombination.add(item4);
		
		assertEquals(expectedCombination , Packer.getBestPackage(combinations ,weightLimit));		
	}
	
	@Test
	public void testFindPackage() {
		double weightLimit = 81;
		
		List<Item> items = new ArrayList<Item>();
		Item item1 = new Item(1 ,53.38 ,45);
		Item item2 = new Item(2, 88.62 ,98);
		Item item3 = new Item(3, 78.48 , 3);	
		Item item4 = new Item(4 ,72.30 , 76);
		Item item5 = new Item(5, 30.18 , 9);
		Item item6 = new Item(6 ,46.34 , 48);

		items.add(item1);items.add(item2);items.add(item3);items.add(item4);items.add(item5);items.add(item6);
		
		assertEquals("4" , Packer.findPackage(items ,weightLimit));
	}
	
	@Test
	public void testPack() {
		String absoluteFilePath = "C:\\www\\PackageTest.txt";
		
		File f = new File(absoluteFilePath);
		if(!f.exists()) {
			assertNull(f, "File path needs to be provided");
		}
		
		StringBuilder result = new StringBuilder();
		
		result.append("4"); result.append(System.lineSeparator());
		result.append("-"); result.append(System.lineSeparator());
		result.append("2,7"); result.append(System.lineSeparator());
		result.append("8,9"); result.append(System.lineSeparator());
		
		try {
			assertEquals( result.toString() , Packer.pack(absoluteFilePath) );
		} catch (APIException e) {			
			e.printStackTrace();
		}
	
	}

}
