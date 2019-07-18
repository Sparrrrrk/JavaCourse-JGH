public class SamePointException extends Exception {
    public SamePointException(){
        super();
    }
    public SamePointException(String message){
        super(message);
    }
    public SamePointException(String message,Throwable cause){
        super(message,cause);
    }
}
