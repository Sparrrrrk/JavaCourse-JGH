public class Line {
    private Point point1,point2;
    public Line(Point point1,Point point2) throws SamePointException{
        if(point1.equals(point2)){
            throw new SamePointException();
        }
        else{
            this.point1 = point1;
            this.point2 = point2;
        }
    }
    public Point getPoint1(){
        return this.point1;
    }
    public Point getPoint2(){
        return this.point2;
    }
    public void setPoint1(Point point1) throws SamePointException{
        if(point1.equals(point2)){
            throw new SamePointException();
        }
        else{
            this.point1 = point1;
        }
    }
    public void setPoint2(Point point2) throws SamePointException{
        if(point2.equals(point1)){
            throw new SamePointException();
        }
        else{
            this.point2 = point2;
        }
    }
    public String getEquation(){
        final double x1 = point1.getX();
        final double y1 = point1.getY();
        final double x2 = point2.getX();
        final double y2 = point2.getY();

        if(Double.compare(x1,x2) != 0){
            double k = (y2 - y1) / (x2 - x1);
            double b = y1 - k * x1;
            if(Double.compare(k,0) == 0){
                return String.format("y=%f",b);
            }
            else if(Double.compare(k,1) == 0){
                return String.format("y=x+%f",b);
            }
            else{
                return String.format("y=%fx+%f",k,b);
            }
        }
        else{
            return String.format("x=%f",x1);
        }
    }
    public static void main(String[] args){
        try{
            Line line = new Line(new Point(1,2),new Point(6,4));
            System.out.println(line.getEquation());

        }catch (SamePointException e){
            e.printStackTrace();
        }
    }
}
