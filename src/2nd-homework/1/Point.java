public class Point {
    private double x;
    private double y;
    public Point(double x, double y){
        this.x = x;
        this.y = y;
    }
    public boolean equals(Object obj){
        if(obj == null){
            return false;
        }
        else if (obj.getClass() != this.getClass()){
            return false;
        }
        else{
            Point point = (Point) obj;
            return (this.x == point.x && this.y == point.y);
        }
    }
    public Point clone(){
        return new Point(x,y);
    }
    public double getX(){
        return this.x;
    }
    public double getY(){
        return this.y;
    }
}
