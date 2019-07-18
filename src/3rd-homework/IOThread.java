import java.net.Socket;

public class IOThread extends Thread { 
	private Socket socket; 
	private IOStrategy ios; 

	public IOThread(Socket socket, IOStrategy ios) {
		this.socket = socket;
		this.ios = ios;
	}

	public void run() {
		ios.service(socket); 
	}
}
