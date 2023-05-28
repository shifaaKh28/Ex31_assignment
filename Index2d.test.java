package exe.ex3;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class Index2DTest {

	@Test
	void testDistance2D() {
		 // Create two Index2D instances for testing
        Index2D pixel1 = new Index2D(1, 2);
        Index2D pixel2 = new Index2D(5, 6);
        
        
        double dx= pixel1.getX()-pixel2.getX();
        double dy= pixel1.getY()-pixel2.getY();
        
        // Calculate the actual distance using the distance2D method
        double dis1=pixel1.distance2D(pixel2); 
        
        // Calculate the expected distance using the Euclidean formula
        double dis2= Math.sqrt(dx*dx+dy*dy);
        assertEquals(dis1,dis2);
        
     // Create an Index2D instance with the same coordinates
        Index2D pixel3 = new Index2D(1, 2);
        double dis3=pixel3 .distance2D(pixel3); 
        double dis4 = 0.0;
        assertEquals(dis3,dis4);

	}

	@Test
	void testEqualsObject() {
		  // Create two Index2D instances with different coordinates(semytric)
        Index2D pixel1 = new Index2D(2, 3);
        Index2D pixel2 = new Index2D(5, 7);

        // Test the equals method
        boolean result = pixel1.equals(pixel2);
        assertFalse(result);
        
        // Create two Index2D instances with the same coordinates(reflexive)
        Index2D pixel3 = new Index2D(2, 3);
        Index2D pixel4 = new Index2D(2, 3);
        boolean result2 = pixel3.equals(pixel4);
        assertTrue(result2);
        
     // Create two Index2D instances with the same coordinates(transitive)
        Index2D pixel5 = new Index2D(2, 3);
        Index2D pixel6 = new Index2D(3, 5);
        Index2D pixel7 = new Index2D(2, 5);
        
        boolean result3 = pixel3.equals(pixel5);
        assertTrue(result3);
      
	}

}
