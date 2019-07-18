import java.io.*;
import java.util.*;
import java.net.*;

public class GoFileClient {

	public static Socket socket = null;
	public static DataInputStream dis = null;
	public static DataOutputStream dos = null;

	public static File rootFolder = null;
	public static boolean isSystemRootFolder = false;

	public static void main(String[] args) throws Exception {
		System.setSecurityManager(null);
		if (args.length != 4) {
			System.out.println("Usage: java GoFileClient download|upload ip port file|folder");
			System.exit(0);
		}

		File file ;
//		System.out.println(args[3]);
//		if (!file.exists()) {
//			System.out.println("file not found");
//			System.exit(0);
//		}

		if ("download".equalsIgnoreCase(args[0])) {
			socket = new Socket(args[1], Integer.parseInt(args[2]));
			dis = new DataInputStream(socket.getInputStream());
			dos = new DataOutputStream(socket.getOutputStream());


			String savePath = "ClientFolder/";
			String downloadFileName = args[3];
			savePath += downloadFileName;

			String[] splitFileName = downloadFileName.split("\\.");

			String unitFileName = splitFileName[0];
			int number = 0;

			File myFile = new File(savePath);
			while(myFile.exists()){
				number++;
				splitFileName[0] = unitFileName + "(" + number + ")";
//				System.out.println(splitFileName[0]);
				String path = "ClientFolder/" + splitFileName[0] + "." + splitFileName[1];
				savePath = path;
				System.out.println(path);
				myFile = new File(savePath);
			}

			try {
				myFile.createNewFile();
			}catch (IOException e){
				e.printStackTrace();
			}


			rootFolder = null;
			downloadFile(downloadFileName,myFile);

		} else if ("upload".equalsIgnoreCase(args[0])) {//upload localhost 23333 ClientFolder/test.txt
			file = new File(args[3]);
			if (!file.exists()) {
				System.out.println("file not found");
				System.exit(0);
			}
			socket = new Socket(args[1], Integer.parseInt(args[2]));
			dis = new DataInputStream(socket.getInputStream());
			dos = new DataOutputStream(socket.getOutputStream());// 输出到文件

			if (file.isFile()) {
				rootFolder = null;
				uploadFile(file);

			} else if (file.isDirectory()) {
				File parent = file.getParentFile();
				if (parent == null) {
					isSystemRootFolder = true;
				} else {
					isSystemRootFolder = false;
				}

				rootFolder = file;
				uploadFolder(file);

			}
		} else {
			System.out.println("wrong operation!");
			System.out.println("Usage: java GoFileClient download|upload ip port file|folder");
			System.exit(0);

		}

		// say bye-bye and close the connection

//		dos.writeInt(0);
		dos.flush();
		dos.close();
		dis.close();
		try {
			Thread.sleep(50);
		} catch (Exception e) {
		}
//		try {
//			socket.close();
//		} catch (Exception e) {
//		}

		System.out.println("finished.");
		System.exit(0);

	}

	private static void uploadFile(File file) throws Exception {
		sendFile(file.getName(), file);
	}
	private static void downloadFile(String unitFileName,File file) throws Exception{
		receiveFile(unitFileName,file);

	}

	private static void uploadFolder(File folder) throws Exception {

		if (folder.getCanonicalPath().length() > rootFolder.getCanonicalPath().length()) {
			createFolder(folder);
		} else {
			if (!isSystemRootFolder) {
				createFolder(folder);
			}
		}

		File[] children = folder.listFiles();
		if (children == null) { // maybe permission?
			// System.out.println("debug: " + folder.getCanonicalPath());
			return;
		}

		for (int i = 0; i < children.length; i++) {
			if (children[i].isFile()) {

				String serverFileName = "";

				String rname = rootFolder.getName();

				serverFileName = children[i].getCanonicalPath().substring(rootFolder.getCanonicalPath().length());
				serverFileName = serverFileName.replace(File.separatorChar, '/');
				if (!serverFileName.startsWith("/")) {
					serverFileName = "/" + serverFileName;
				}

				serverFileName = rootFolder.getName() + serverFileName;

				if (!serverFileName.startsWith("/")) {
					serverFileName = "/" + serverFileName;
				}

				sendFile(serverFileName, children[i]);

			} else {

				uploadFolder(children[i]);
			}
		}

	}

	private static void createFolder(File folder) throws Exception {

		String r = rootFolder.getCanonicalPath();
		String f = folder.getCanonicalPath();

		String serverFolderName = "";

		if (r.equals(f)) {
			serverFolderName = "/" + folder.getName();
		} else {
			serverFolderName = f.substring(r.length());
			String rname = rootFolder.getName();

			serverFolderName = serverFolderName.replace(File.separatorChar, '/');
			serverFolderName = "/" + rname + serverFolderName;
		}

		dos.writeInt(2); // command, ask to create a folder
		dos.writeUTF(serverFolderName);
		dos.flush();

	}

	// low-level operation, serverFileName should be a.file, /a.file,
	// /xyz/abc/a.file
	private static void sendFile(String serverFileName, File localFile) throws Exception {

		dos.writeInt(1); // command, ask to send file to server
		dos.writeUTF(serverFileName);
		dos.writeLong(localFile.length());
		dos.flush();
		byte[] buffer = new byte[8 * 16 * 1024];

		FileInputStream fis = new FileInputStream(localFile);
		BufferedInputStream bis = new BufferedInputStream(fis);
		int len = 0;
		while ((len = bis.read(buffer)) != -1) {
			dos.writeInt(len);
			dos.write(buffer, 0, len);
			dos.flush();
		}

		dos.writeInt(-1);
		dos.flush();
		bis.close();
		fis.close();

	}
	private static void receiveFile(String clientFileName, File serverFile) throws Exception{
		dos.writeInt(3); // command, ask to download file to client
		dos.writeUTF(clientFileName);
		dos.flush();
		long fileLength = dis.readLong();
		System.out.println(fileLength);

		if(fileLength != -1)
		{
			byte[] buffer = new byte[1024];

			FileOutputStream fos = new FileOutputStream(serverFile);
			int len = 0;
			while ((len = dis.read(buffer)) != -1){
				System.out.println("write buffer."+len);
				fos.write(buffer, 0, len);
			}
			fos.close();
		}else{
			System.out.println("file not exist.");
		}
//		dos.writeInt(-1);

	}

}
