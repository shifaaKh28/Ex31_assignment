package exe.ex3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * This class represents a 2D map as a "screen" or a raster matrix or maze over integers.
 * @author boaz.benmoshe
 *
 */
public class Map implements Map2D {
	private int[][] _map;
	private boolean _cyclicFlag = true;

	/**
	 * Constructs a w*h 2D raster map with an init value v.
	 * @param w
	 * @param h
	 * @param v
	 */
	public Map(int w, int h, int v) {
		init(w,h, v);
	}
	/**
	 * Constructs a square map (size*size).
	 * @param size
	 */
	public Map(int size) {
		this(size,size, 0);
	}

	/**
	 * Constructs a map from a given 2D array.
	 * @param data
	 */
	public Map(int[][] data) {
		init(data);
	}
	@Override
	public void init(int w, int h, int v) {
		_map = new int[h][w];
		for (int i = 0; i < h; i++) {// rows
			for (int j = 0; j < w; j++) {//columns
				_map[i][j] = v;
			}
		}
	}
	@Override
	public void init(int[][] arr) {
		if(arr==null) {// checks if the arr parameter is null
			throw new RuntimeException();
		}
		int h = arr.length;
		int w = arr[0].length;
		_map = new int[h][w];
		for (int i = 0; i < h; i++) {
			System.arraycopy(arr[i], 0, _map[i], 0, w);
		}
	}
	@Override
	public int[][] getMap() {
		int[][] ans = new int[_map.length][_map[0].length];
		for (int i = 0; i < _map.length; i++) {
			System.arraycopy(_map[i], 0, ans[i], 0, _map[0].length);// to copy the elements from each row of _map
		}
		return ans;
	}

	@Override
	public int getWidth() {

		return _map.length ;
	}
	@Override

	public int getHeight() {
		if (_map.length == 0) {// checks if the _map array is empty (has a length of 0)
			return 0;
		}else {

		}
		return _map[0].length;
	}
	@Override

	public int getPixel(int x, int y) { 
		 return _map[x][y];
	}
	@Override

	public int getPixel(Pixel2D p) {
		return this.getPixel(p.getX(),p.getY());
	}
	@Override

	public void setPixel(int x, int y, int v) {
		_map[x][y]=v;
	}
	@Override

	public void setPixel(Pixel2D p, int v) {	
		this.setPixel(p.getX(), p.getY(),v);
	}
	@Override
	/** 
	 * Fills this map with the new color (new_v) starting from p.
	 * https://en.wikipedia.org/wiki/Flood_fill
	 */
	public int fill(Pixel2D xy, int new_v) {
		int ans = 0; // Initialize the count of filled pixels to 0

		int old_v = getPixel(xy); // Get the current color at pixel p
		if (old_v == new_v) {
			// If the current color is already the new color, no need to fill

			return ans;
		}
		return floodFill(xy, old_v, new_v);
	}

	// Recursive helper method for flood-fill algorithm
	private int floodFill(Pixel2D p,int old_v,int new_v) {
		// Check if the pixel is inside the map and has the old color
		if (!isInside(p) || getPixel(p) != old_v) {
			return 0;
		}
		setPixel(p, new_v); // Set the pixel color to the new color
		int counter = 1; // Counter to count the current pixel as filled

		Pixel2D[] neighbors = getNeighbors(p); // Get the neighboring pixels

		for (int i = 0; i < neighbors.length; i++) {
			Pixel2D neighbor = neighbors[i];
			counter += floodFill(neighbor, old_v, new_v); // Recursively fill the neighboring pixels
		}

		return counter; // Return the total count of filled pixels
	}
	@Override
	/**
	 * BFS like shortest the computation based on iterative raster implementation of BFS, see:
	 * https://en.wikipedia.org/wiki/Breadth-first_search
	 */
	public Pixel2D[] shortestPath(Pixel2D p1, Pixel2D p2, int obsColor) {
		// Check if p1 or p2 are outside the boundaries or null,or euqals 
		if(p1==null||p2==null||!isInside(p1)||!isInside(p2)) {
			return null;
		}
		Pixel2D[] ans = null;  // initialize our result to be null 

		//Create an ArrayList to keep tracking the visited pixels.
		List <Pixel2D> track= new ArrayList<Pixel2D>(); 

		// Create a 2D array to store the origin of each pixel in the shortest path
		Pixel2D[][] origin=  new Pixel2D[getWidth()][getHeight()];

		// Create a 2D boolean array to sign the visited pixels
		boolean[][] visited = new boolean[getHeight()][getWidth()];

		// Add the start pixel (p1) to the track  
		track.add(p1);
		visited[p1.getX()][p1.getY()] = true;

		//find the shortest path
		while (!track.isEmpty()) {
			/*removes and retrieves the first element from the track ArrayList and assigns it to the variable.
			 * and selecting the next pixel to be processed in the breadth-first search (BFS) algorithm.
			 */
			Pixel2D current_pixel = track.remove(0);

			// Check if we have reached pixel (p2)
			if (current_pixel.equals(p2)) {
				break;
			}
			// Get the neighbors of the current pixel
			Pixel2D[] neigh = getNeighbors(current_pixel);

			// Iterate through the neighbors and add them to the track if they are valid moves
			for (int i = 0; i < neigh.length; i++) {
				Pixel2D neighbors = neigh[i];

				if (validMove(current_pixel, neighbors, obsColor) && !visited[neighbors.getX()][neighbors.getY()]) {
					track.add(neighbors);
					visited[neighbors.getX()][neighbors.getY()] = true;
					origin[neighbors.getX()][neighbors.getY()] = current_pixel;
				}
			}
		}
		// If the destination pixel (p2) was visited, construct the shortest path
		if (visited[p2.getX()][p2.getY()]) {
			ArrayList<Pixel2D> shortest_path = new ArrayList<>();
			Pixel2D curr = p2;

			// Traverse the parent array to construct the path from p1 to p2
			while (curr != null) {
				shortest_path.add(curr);
				curr = origin[curr.getX()][curr.getY()];
			}

			// Convert the path to array and reverse it
			ans = new Pixel2D[shortest_path.size()];
			for (int i = shortest_path.size() - 1, j = 0; i >= 0; i--, j++) {
				ans[j] = shortest_path.get(i);
			}
		}
		return ans;
	}
	/*
	 * this method helps us in the shortestpath method:
	 * method determines if a move between two pixels is valid by comparing 
	 * their colors with the obstacle color.
	 *  It returns true if both pixels have different colors from the obstacle color and false otherwise.
	 */
	private boolean validMove(Pixel2D p1, Pixel2D p2, int obsColor) {
		int color_1 = getPixel(p1);//retrieves the color of p1
		int color_2 = getPixel(p2);//retrieves the color of p2

		//checks if both color1 and color2 are different from obsColor
		boolean isValid = (color_1 != obsColor) && (color_2 != obsColor);
		return isValid;
	}
	@Override
	public boolean isInside(Pixel2D p) {
		int x=p.getX();//retrieves the x-coordinate of the p pixel 
		int y= p.getY();//retrieves the y-coordinate of the p pixel 

		int width=getWidth();//retrieves the width of the map
		int height = getHeight();//retrieves the height of the map

		/*checks if the x-coordinate x is greater than or equal to 0, less than the width,
		 *  the y-coordinate y is greater than or equal to 0,
		 *  and less than the height 
		 */
		if(x>=0&&x<width && y>=0&&y<height) {
			return true;
		}
		return false;
	}

	@Override
	public boolean isCyclic() {
		return _cyclicFlag;
	}
	@Override

	public void setCyclic(boolean cy) {
		this._cyclicFlag=cy;
	}
	@Override

	public Map2D allDistance(Pixel2D start, int obsColor) {
		Map2D ans = null; 

		int h = getHeight();
		int w= getWidth();

		// Create a new map with the same dimensions as the current map, using the obstacle color.
		ans = new Map(w, h,-1);
		// Create a list to store the pixels to be processed.
		List<Pixel2D> pixels= new ArrayList<Pixel2D>();

		// Set the distance of the start pixel to 0 in the new map.
		ans.setPixel(start, 0); 

		// Add the start pixel to the list of pixels to be processed.
		pixels.add(start);

		// Perform (BFS) to compute the shortest path distances.
		while (!pixels.isEmpty()) {

			// Remove the first pixel from the list of pixels.
			Pixel2D current_pix= pixels.remove(0);	 

			// Get the current distance of the pixel from the new map.
			int current_Dis= ans.getPixel(current_pix);

			// Get the neighboring 
			Pixel2D[] neigh= getNeighbors( current_pix);

			// Iterate through the neighbors
			for (int i = 0; i < neigh.length; i++) {
				// Access the neighbor at the current index.
				Pixel2D neighbor = neigh[i];

				// Check if the neighbor pixel is inside the map and has not been visited.
				if(isInside(neighbor)&&ans.getPixel(neighbor)==-1) {

					// Check if the neighbor pixel is not an obstacle.
					if (getPixel(neighbor) != obsColor) {
						// Update the distance of the neighbor pixel in the new map.
						ans.setPixel(neighbor, current_Dis + 1);

						// Add the neighbor pixel to the list of pixels to be processed.     
						pixels.add(neighbor);
					}
				}
			}
		}
		return ans;
	}
	/*
	 * this function help us in the shortestPath and all distnce methods : 
	 * takes a Pixel2D object and finds its neighboring
	 *  pixels by checking the boundaries of the map.
	 *  It returns a list of valid neighboring pixels.
	 */
	private Pixel2D[] getNeighbors(Pixel2D pixel) {
		//initializes an empty ArrayList to store the neighboring pixels.
		List<Pixel2D> neighbors = new ArrayList<>();

		int x = pixel.getX();
		int y = pixel.getY();
		int width = getWidth();
		int height = getHeight();

		// Check top neighbor
		if (x > 0) {
			neighbors.add(new Index2D(x - 1, y));
		}

		// Check bottom neighbor
		if (x < width - 1) {
			neighbors.add(new Index2D(x + 1, y));
		}

		// Check left neighbor
		if (y > 0) {
			neighbors.add(new Index2D(x, y - 1));
		}

		// Check right neighbor
		if (y < height - 1) {
			neighbors.add(new Index2D(x, y + 1));
		}
		// Convert the ArrayList to an array
		Pixel2D[] neighborsArray = new Pixel2D[neighbors.size()];
		neighborsArray = neighbors.toArray(neighborsArray);

		return neighborsArray;
	}

}





