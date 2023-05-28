package exe.ex3;



public class Index2D implements Pixel2D{
    private int _x, _y;
    public Index2D() {
    	this(0,0);
    	}
    public Index2D(int x, int y) {
    	_x=x;_y=y;
    	}
    
    public Index2D(Pixel2D t) {
    	this(t.getX(), t.getY());
    	}
    
    @Override
    public int getX() {
        return _x;
    }
    @Override
    public int getY() {
        return _y;
    }
    @Override
    public double distance2D(Pixel2D t) {
        double ans = 0;
       if(t==null) {//check if the parameter 't' is null
    	   throw new RuntimeException("invalid paramter");
       }
       double dx = _x- t.getX();// Calculate the difference in X coordinates between the current pixel and 't'
       double dy =_y-t.getY(); // Calculate the difference in Y coordinates between the current pixel and 't'
       double dis=((dx*dx)+(dy*dy)); //Compute the squared distance using the formula (dx * dx) + (dy * dy)
       
       ans=Math.sqrt(dis);
        return ans;
    }
    @Override
    public String toString() {
        return getX()+","+getY();
    }
    @Override
    public boolean equals(Object t) {
        boolean ans = false;
        // Check if the object is null or not an instance of Index2D
       if(t==null || !(t instanceof Index2D)) {
    	   
    	   return false;
    	   }
       // Cast the object to Index2D type for further comparison
       Index2D t2 = (Index2D)t;
       ans = (_x == t2._x) && (_y == t2._y);
       
        return ans;
    }

}
