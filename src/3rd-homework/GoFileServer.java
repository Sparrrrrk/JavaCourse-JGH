import java.io.*;

public class GoFileServer {
	
	public static File rootFolder = null;

	public static void main(String[] args) {
		if (args.length != 2) {
			System.out.println("Usage: java GoFileServer port folder");
			System.exit(0);
		}

		File file = new File(args[1]);
		
		if (!file.exists()) {
			System.out.println("folder not found!");
			System.exit(0);
		}

		rootFolder = file;

		new NwServer(Integer.parseInt(args[0]), new ThreadSupport(new FileTransferProtocol()));
	}

}
