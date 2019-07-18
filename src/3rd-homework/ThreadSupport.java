public class ThreadSupport implements IOStrategy { // ThreadSupport.java
	private IOStrategy ios; 

	public ThreadSupport(IOStrategy ios) { 
		this.ios = ios;
	}

	public void service(java.net.Socket socket) { 
		new IOThread(socket, ios).start(); 
	} 
}
