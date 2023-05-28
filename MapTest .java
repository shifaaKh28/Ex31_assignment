package exe.ex3;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class MapTest {

	@Test
	void testInitIntIntInt() {

		 // Create a new map with width = 3, height = 3, and value = 5
		Map map =new Map(3,3,5);
		int [][]result=map.getMap();
		// Define the expected map
		int[][]exc= {
				{5,5,5},
				{5,5,5},
				{5,5,5}};
		assertArrayEquals(exc,result);

		Map map2 =new Map(2,3,6);
		int[][]exc2= {
				{6,6,6},
				{6,6,6}};
	}

	@Test
	void testInitIntArrayArray() {
		  // Define a 2D array representing the initial map values
		int[][] mat= {
				{5,5,5},
				{5,5,5}};
		  // Create a new map by initializing it with the provided 2D array
		Map copy=new Map (mat);
		int [][] result= copy.getMap();
		assertArrayEquals(mat,result);

	}

	@Test
	
	void testGetMap() {
		// Define a 2D array representing the expected map values
		int [][]mat={{5,5,5}
		            ,{6,6,6},
		             {7,7,7}};
		Map map = new Map(mat);
		
		 // Retrieve the map as a 2D array using the `getMap` method
		int[][]result= map.getMap();
		assertArrayEquals(mat, result);
	}

	@Test
	void testGetWidth() {
		int[][]	mat={
				{1,2,3},
				{4,5,6},
				{7,8,9}};
		Map map=new Map(mat);
		int expWidth=mat.length;    // Calculate the expected width of the map
		int result= map.getWidth(); // Retrieve the width of the map using the `getWidth` method
		assertEquals(expWidth,result);

		int[][]mat2={{1,2},{4,5}};
		Map map2= new Map(mat2);
		int expWidth2=mat.length;
		int result2=map2.getWidth();
		assertEquals(expWidth2,result);

	}

	@Test
	void testGetHeight() {
		int[][]	mat={
				{1,2,3},
				{4,5,6},
				{7,8,9}};
		Map map=new Map(mat);
		int expWidth=mat[0].length;// Calculate the expected height of the ma
		int result= map.getHeight();  // Retrieve the height of the map using the `getWidth` method
		assertEquals(expWidth,result);

		int[][]mat2={{1,2},{4,5}};
		Map map2= new Map(mat2);
		int expWidth2=mat[0].length;
		int result2=map2.getHeight();
		assertEquals(expWidth2,result);

	}

	@Test
	void testGetPixelIntInt() {
		int[][]mat={
				{1,2,3},
				{4,5,6},
				{7,8,9}};
		Map map= new Map(mat);
		int digit1= map.getPixel(0, 0);//1
		int digit2= map.getPixel(2,1);//8

		assertEquals(1,digit1);
		assertEquals(8,digit2);
	}

	@Test
	void testGetPixelPixel2D() {
		int[][]mat={
				{8,2,3},
				{4,5,6},
				{7,8,9}};
		Map map= new Map(mat);
		 // Define two pixel positions using Pixel2D objects
		Pixel2D p= new Index2D(0,0);
		Pixel2D p2= new Index2D(1,2);
		
	    // Retrieve the pixel values at the specified positions using the `getPixel` method
		int digit1=map.getPixel(p);
		int digit2=map.getPixel(p2);

		assertEquals(digit1,8);
		assertEquals(digit2,6);

	}

	@Test
	void testSetPixelIntIntInt() {
		int[][]mat={
				{8,2,3}
		       ,{4,5,6},
		        {7,8,9}};

		Map map= new Map(mat);
		// Define the new pixel value and the position to set it
		int v=2;
		Pixel2D p= new Index2D(0,1);
		// Set the pixel value at the specified position using the `setPixel` method
		map.setPixel(p, v);
		int[][]mat2=map.getMap();
		assertArrayEquals(mat, mat2);

	}
	@Test
	void testSetPixelPixel2DInt() {
		int[][]mat={
				{8,2,3},
				{4,5,6},
				{7,8,9}};
		Map map= new Map(mat);
		// Define the new pixel values and positions to set them
		int v1=2;
		int v2=3;
		map.setPixel(0, 1, v1);
		map.setPixel(0, 2, v2);
		  // Retrieve the modified map after setting the pixel values
		int[][]mat2=map.getMap();
		assertArrayEquals(mat, mat2);
	}

	@Test
	void testFill() {
		// Define the initial map with pixel values
		int [][] mat = {
				{1,1,1,1,1,1,1,1}
				, {1,1,0,0,0,0,0,1}
				, {1,0,1,0,0,0,0,1}
				, {1,0,0,1,0,0,0,1}
				, {1,0,0,0,1,0,0,1}
				, {1,0,0,0,0,1,0,1}
				, {1,0,0,0,0,0,1,1}
				, {1,1,1,1,1,1,1,1}};
		
		// Define the expected map after the fill operation
		int [][] mat2 = {
				{1,1,1,1,1,1,1,1}
				, {1,1,0,0,0,0,0,1}
				, {1,6,1,0,0,0,0,1}
				, {1,6,6,1,0,0,0,1}
				, {1,6,6,6,1,0,0,1}
				, {1,6,6,6,6,1,0,1}
				, {1,6,6,6,6,6,1,1}
				, {1,1,1,1,1,1,1,1}};

		Map map = new Map(mat);
		 // Define the Pixel2D coordinates to start the fill operation and the fill value
		Pixel2D p = new Index2D(3,1);
		
		// Perform the fill operation and retrieve the modified map
		int result = map.fill(p, 6);
		int [][] new_Map = map.getMap();

		assertEquals(result,15);
		assertArrayEquals(new_Map, mat2);

		int[][]mat3={
				{1,1,1,1,1,1}
				, {1,0,0,0,0,1}
				, {1,0,0,0,0,1}
				, {1,0,0,0,0,1}
				, {1,0,0,0,0,1}
				, {1,1,1,1,1,1}
		};

		int[][]mat4={
				{1,1,1,1,1,1}
				, {1,6,6,6,6,1}
				, {1,6,6,6,6,1}
				, {1,6,6,6,6,1}
				, {1,6,6,6,6,1}
				, {1,1,1,1,1,1}
		};
		Map map2= new Map(mat3);
		Pixel2D p2= new Index2D(1,1);
		int result2= map2.fill(p2,6);
		int [][]new_map2=map2.getMap();
		assertEquals(result2,16);
		assertArrayEquals(new_map2, mat4);
	}

	@Test
	void testShortestPath() {
		 // Define the initial map with pixel values
		int[][]mat= {
				{1,1,1,1},
				{0,0,0,0},
				{0,1,0,0},
				{1,1,1,1},
		};
		Map map = new Map(mat);
		
		 // Define the start and end points for the shortest path calculation
		Pixel2D p1 = new Index2D(1,0);
		Pixel2D p2 = new Index2D(2,2);
		
		// Calculate the shortest path between the specified points with the obscolor1
		Pixel2D [] shortestPath = map.shortestPath(p1, p2, 1);

		 // Define the expected shortest path
		Pixel2D p_1 = new Index2D(1,0); 
		Pixel2D p_2 = new Index2D(1,1); 
		Pixel2D p_3= new Index2D(1,2); 
		Pixel2D p_4= new Index2D(2,2); 
		Pixel2D[] expected = { p_1,p_2,p_3,p_4};
		assertArrayEquals(expected, shortestPath);

		int[][]mat2= {
				{-1,0,-1,-1,-1,-1},
				{-1,0,0,0,0,-1},
				{-1,-1,0,0,-1,-1},
				{-1,-1,0,0,-1,-1},
				{-1,0,0,0,0,-1}
		};
		Map map2= new Map(mat2);
		Pixel2D po1= new Index2D(0,1);
		Pixel2D po2=new Index2D(4,3);
		Pixel2D [] shortestPath2= map2.shortestPath(po1, po2, -1);

		Pixel2D po_1 = new Index2D(0,1); 
		Pixel2D po_2 = new Index2D(1,1); 
		Pixel2D po_3= new Index2D(1,2); 
		Pixel2D po_4= new Index2D(2,2);
		Pixel2D po_5 = new Index2D(3,2); 
		Pixel2D po_6 = new Index2D(4,2); 
		Pixel2D po_7 = new Index2D(4,3); 

		Pixel2D[]expected2= {po_1,po_2,po_3,po_4,po_5,po_6,po_7}; 
		assertArrayEquals(expected2, shortestPath2);
	}


	@Test
	void testIsInside() {
		// Create a new map with dimensions 4x5 and all pixels initialized to 0
		Map map= new Map(4,5,0);
		
		 // Define two Index2D objects representing pixel coordinates
		Index2D pixel1=new Index2D(1,2);
		Index2D pixel2=new Index2D(5,6);

		 // Check if the first and second pixel is inside the map
		boolean result1= map.isInside(pixel1);
		boolean result2= map.isInside(pixel2);
		assertTrue(result1);
		assertFalse(result2);


	}
	@Test
	void testAllDistance() {
		 // Define two 2D arrays representing maps
		int[][]mat= {
				{1,1,1,1},
				{0,0,0,0},
				{0,1,0,0},
				{1,1,1,1},
		};

		int[][]mat2= {
				{1,0,0,1},
				{0,0,0,0},
				{0,1,0,0},
				{1,0,0,1},
		};
		  // Create Map objects based on the defined maps
		Map map = new Map(mat);
		Map map2 = new Map(mat2);

		Pixel2D p1 = new Index2D(1,0);//start point
		Pixel2D p2 = new Index2D(0,1);//start point

		 //Calculate the distances from the start points to all other pixels
		Map2D map_1 = map.allDistance(p1,1); 
		Map2D map_2 = map2.allDistance(p2,1);

		 // Get the resulting maps as 2D array
		int [][] result= map_1.getMap();
		int [][] result2= map_2.getMap();

		 // Define the expected maps
		int [][]expected= {
				{-1, -1, -1, -1},
				{0 , 1 , 2  , 3}, 
				{1 , -1 , 3 , 4}, 
				{-1, -1, -1, -1}
		};
		int [][]expected2= {
				{ -1 , 0 , 1, -1},
				{ 2 , 1 , 2  , 3}, 
				{ 3 , -1 , 3 , 4},
				{-1 , 5 , 4 , -1}
		};
		assertArrayEquals(expected,result);
		assertArrayEquals(expected2,result2);
	}

}
