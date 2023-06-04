import java.awt.*;

import exe.ex3.game.Game;
import exe.ex3.game.GhostCL;
import exe.ex3.game.PacManAlgo;
import exe.ex3.game.PacmanGame;

/**
 * This is the major algorithmic class for Ex3 - the PacMan game:
 *
 * This code is a very simple example (random-walk algorithm).
 * Your task is to implement (here) your PacMan algorithm.
 */
//------------------------
/**
 * the algorithm selects a random direction for Pacman by finding the closest food pixel and determining the appropriate movement direction.
 * It repeats this process in each iteration of the move method.
 * The algorithm aims to guide Pacman to eat all the food pixels while avoiding collisions with ghosts.
 *  i  add a three helper methods and used it in the randomDir method
 *  1.convertString
 *  2.findClosestFood
 *  3. determineDirection
 */
public class Ex3Algo implements PacManAlgo{
	private int _count;

	public Ex3Algo() {_count=0;}

	@Override
	/**
	 *  Add a short description for the algorithm as a String.
	 */
	public String getInfo() {
		return null;
	}

	@Override
	/**
	 * This ia the main method - that you should design, implement and test.
	 */
	public int move(PacmanGame game) {
		if(_count==0 || _count==300) {
			int code = 0;
			int[][] board = game.getGame(0);
			printBoard(board);
			int blue = Game.getIntColor(Color.BLUE, code);
			int pink = Game.getIntColor(Color.PINK, code);
			int black = Game.getIntColor(Color.BLACK, code);
			int green = Game.getIntColor(Color.GREEN, code);
			System.out.println("Blue=" + blue + ", Pink=" + pink + ", Black=" + black + ", Green=" + green);
			String pos = game.getPos(code).toString();
			System.out.println("Pacman coordinate: "+pos);
			GhostCL[] ghosts = game.getGhosts(code);
			printGhosts(ghosts);
			int up = Game.UP, left = Game.LEFT, down = Game.DOWN, right = Game.RIGHT;
		}
		_count++;
		int dir = randomDir(game);
		return dir;
	}

	private static void printBoard(int[][] b) {
		for(int y =0;y<b[0].length;y++){
			for(int x =0;x<b.length;x++){
				int v = b[x][y];
				System.out.print(v+"\t");
			}
			System.out.println();
		}
	}

	private static void printGhosts(GhostCL[] gs) {
		for(int i=0;i<gs.length;i++){
			GhostCL g = gs[i];
			System.out.println(i+") status: "+g.getStatus()+",  type: "+g.getType()+",  pos: "+g.getPos(0)+",  time: "+g.remainTimeAsEatable(0));
		}
	}
/*
in this method we add some operation by using the helper methods we make , it is  returns the randomly chosen direction for Pacman.
 */
	private static int randomDir(PacmanGame game) {

		int[][] board = game.getGame(0);
		Map map = new Map(board);

		// Get the current position of Pacman as a string
		String position = game.getPos(0);

		// Convert the string position to a Pixel2D object
		Pixel2D pos = convertString(position);

		// Calculate the distances to all reachable pixels from the current position
		Map2D dis = map.allDistance(pos, 1);

		// Find the closest food pixel to the current position
		Pixel2D pink = findClosestFood(pos, map, dis);

		// Find the shortest path from the current position to the closest food pixel
		Pixel2D[] path = map.shortestPath(pos, pink, 1);

		// Determine the direction to move based on the map and path
		int direction = determineDirection(map, pos, path[1]);

		// Return the randomly chosen direction for Pacman
		return direction;
	}
	/**
	 * Converts a string representation of coordinates into a Pixel2D object.
	 * The string should be in the format "x,y".
	 *
	 * @param st The string representation of coordinates.
	 * @return A Pixel2D object representing the coordinates.
	 */
	private static Pixel2D convertString(String st) {

		// Split the string by comma to separate the x and y coordinates
		String[] a = st.split(",");
		int x = Integer.parseInt(a[0]); // Parse the x coordinate from the first element of the array
		int y = Integer.parseInt(a[1]); // Parse the y coordinate from the second element of the array

		// Create a new Index2D object with the parsed x and y coordinates
		return new Index2D(x, y);
	}

	/**
	 * Finds the closest food pixel to the given position on the map.
	 *
	 * @param current       The current position.
	 * @param mat           The map containing the food pixels.
	 * @param dist The map containing the distance values from each pixel to the target.
	 * @return The closest food pixel to the given position.
	 */
	private static Pixel2D findClosestFood(Pixel2D current, Map mat, Map2D dist) {
		int dis = Integer.MAX_VALUE; // Initialize the minimum distance to a very large value
		Pixel2D closest_p = new Index2D(0, 0); // Initialize the closest pixel to the top-left corner
		int w = mat.getWidth();
		int h= mat.getHeight();

		// Iterate over all pixels on the map
		for (int i = 0; i < w; i++) {
			for (int j = 0; j < h; j++) {

				// Check if the pixel is food and the pixel has a valid distance value
				if (mat.getPixel(i, j) == 3 && dist.getPixel(i, j) != -1) {

						int current_Dist = dist.getPixel(i, j); // Get the distance value of the pixel
						// Check if the current distance is smaller than the minimum distance found so far
						if (dis> current_Dist) {
							dis = current_Dist; // Update the minimum distance
							closest_p = new Index2D(i, j); // Update the closest pixel
						}
					}
				}
			}

		return closest_p; // Return the closest food pixel
	}
	/*
       this method determines the appropriate movement direction for Pacman based
       on the map's cyclic property and the relative positions of from and to.
     */
	private static int  determineDirection(Map mat, Pixel2D current_Pos,Pixel2D target_Pos) {
		int w=mat.getWidth();
		int h=mat.getHeight();

		int x1=current_Pos.getX();
		int y1=current_Pos.getY();

		int x2=target_Pos.getX();
		int y2=target_Pos.getY();

		// Check if the map is cyclic and handle wrapping around
		if (mat.isCyclic()) {

			if (y1 == y2) {

				// Check if Pacman needs to move right to wrap around the map
				if (x1 == w- 1 && x2 == 0) {
					return Game.RIGHT;
				}

				// Check if Pacman needs to move left to wrap around the map
				if (x1 == 0 && x2 == w- 1) {
					return Game.LEFT;
				}
			}
			if (x1 == x2) {

				// Check if Pacman needs to move upward to wrap around the map
				if (y1 == h - 1 && y2 == 0) {
					return Game.UP;
				}
				// Check if Pacman needs to move downward to wrap around the map
				if (y1== 0 && y2== h- 1) {
					return Game.DOWN;
				}
			}

		}
		// Determine movement direction based on relative positions
		if (x2 < x1 && y2 == y1) {
			return Game.LEFT;
		}

		if (x2> x1 && y2 == y1) {
			return Game.RIGHT;

		}

		if (x2 == x1 && y2 < y1) {
			return Game.DOWN;

		}
		if (x2 == x1 && y2 > y1) {
			return Game.UP;

		}
		// Return the default direction (pause the game)
		return Game.PAUSE;
	}

}
