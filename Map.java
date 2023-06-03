import java.util.*;


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
		_map = new int[w][h];
		for (int i = 0; i < w; i++) {// rows
			for (int j = 0; j < h; j++) {//columns
				_map[i][j] = v;
			}
		}
	}
	@Override
	public void init(int[][] arr) {
		if(arr==null) {// checks if the array null
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
			for (int j = 0; j < _map[0].length; j++) {
				ans[i][j] = this._map[i][j];
			}

		}

		return ans;
	}
	@Override
	public int getWidth() {
		return _map.length ;
	}
	@Override

	public int getHeight() {
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

		Pixel2D[] neighbors = findTheNeighbors(p); // Get the neighboring pixels

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
		Pixel2D[] ans = null;  // the result.

		// Create a sorted map of distances from p1 to all other pixels
		Map2D sort_map = this.allDistance(p1, obsColor);

		// Get the target point value from the sorted map
		int target_point = sort_map.getPixel(p2);

		//check if the target point is an obstacle
		if ((target_point == -1) || (target_point == -2)){
			return null;
		}
		int width = this.getWidth();
		int heigth = this.getHeight();

		ans = new Index2D[target_point + 1];


		// Use a queue to BFS search from the target to the start point
		Queue<Pixel2D> start_point = new LinkedList<Pixel2D>();

		start_point.add(p2);
		ans[target_point] = p2;
		while (!start_point.isEmpty()) {

			Pixel2D current = start_point.remove();

			// If the current pixel is the start pixel, break the loop
			if (current.equals(p1)) {
				break;
			}

			// Get the neighbors of the current point
			Pixel2D[] neighbors = findTheNeighbors(current);

			for (Pixel2D neighbor : neighbors) {
				if ((this.isCyclic() || isInside(neighbor)) && sort_map.getPixel(neighbor) == sort_map.getPixel(current) - 1) {
					start_point.add(neighbor);

					// store the neighbor pixel at the correct position in the shortest path sequence
					ans[sort_map.getPixel(current) - 1] = neighbor;
					break;
				}
			}
		}

		return ans;
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
			Pixel2D[] neigh= findTheNeighbors( current_pix);

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
	 *this is a helper method that help us in the shortest path method and allditance
	 */
	private Pixel2D[] findTheNeighbors(Pixel2D pixel) {
		Pixel2D[] neighbors = new Pixel2D[4];

		int w=getWidth();
		int h= getHeight();
		int x = pixel.getX();
		int y = pixel.getY();

		neighbors[3] = new Index2D(x, (y - 1 + h) % h);//left
		neighbors[2] = new Index2D(x, (y + 1) % h);//right
		neighbors[0] = new Index2D((x - 1 + w) % w, y);//up
		neighbors[1] = new Index2D((x + 1) % w, y);//down


		return neighbors;
	}
}





