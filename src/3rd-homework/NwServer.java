import java.net.*;

public class NwServer { 
	
	public NwServer(int port, IOStrategy ios) { 
		try {
			ServerSocket ss = new ServerSocket(port);
			System.out.println("server is ready");
			while (true) {
				Socket socket = ss.accept(); 
				System.out.println("connection established.");
				ios.service(socket); 
			} 
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
	}
}
