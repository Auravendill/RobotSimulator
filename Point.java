class Point 
{ 
    double x,y; 
      
    public Point(double x, double y)  
    { 
        this.x = x; 
        this.y = y; 
    } 
      
    // Method used to display X and Y coordinates 
    // of a point 
    static void displayPoint(Point p) 
    { 
        System.out.println("(" + p.x + ", " + p.y + ")"); 
    } 
} 