import java.io.*;
import java.net.*;

public class FileTransferProtocol implements IOStrategy {

	@Override
	public void service(Socket socket) {
		try {
			DataInputStream dis = new DataInputStream(socket.getInputStream());
			DataOutputStream dos = new DataOutputStream(socket.getOutputStream());

			int command;
			while (true) {
				command = dis.readInt();
				switch (command) {
//				case 0:
//					System.out.println("close connection.");
//					try {
//						socket.getInputStream().close();
//						socket.getOutputStream().close();
//						socket.close();
//					} catch (Exception e) {
//					}
//					return;
				case 1: //从客户端读文件并上传
					String serverFileName = dis.readUTF();

					File serverFile = null;

					// process / or \
					if (serverFileName.startsWith("/")) {//文件夹路径
						serverFileName = serverFileName.replace('/', File.separatorChar);//跨平台的转换路径格式 路径表示方法不一样
						serverFileName = serverFileName.substring(1);
						serverFile = new File(GoFileServer.rootFolder, serverFileName); //启动服务器时创建的根文件夹

					} else {//文件路径
						serverFile = new File(GoFileServer.rootFolder, serverFileName);
					}

					FileOutputStream fos = new FileOutputStream(serverFile);//服务器端的字节输出流
					BufferedOutputStream bos = new BufferedOutputStream(fos);

					long length = dis.readLong();

					while (true) {//读文件的长度
						int len = dis.readInt();
						if (len == -1) {//全部传完
							break;
						}
						length = length - len;
						byte[] buffer = new byte[len];
						if (len > 0) {
							dis.readFully(buffer);
							bos.write(buffer);

						}
					}
					if (length != 0) {
						System.out.println("receive file wrongly.");
					}

					bos.flush();
					fos.flush();
					bos.close();
					fos.close();

					break;

				case 2://创建文件夹
					String serverFolderName = dis.readUTF();

					serverFolderName = serverFolderName.replace('/', File.separatorChar);
					File sfn = new File(GoFileServer.rootFolder, serverFolderName);
					sfn.mkdirs(); //与mkdir的区别

					break;

				case 3://下载文件

					String filename = dis.readUTF();

					String myFilename = filename;
					File f = new File(GoFileServer.rootFolder,myFilename);
					if(f.exists()){
						dos.writeLong(f.length());
						FileInputStream fis = new FileInputStream(f);
						byte[] buffer = new byte[1024];
						int r = 0;
						while((r=fis.read(buffer)) != -1){
							dos.write(buffer, 0, r);
						}
					}
					dos.flush();
					dis.close();
					dos.close();

					break;
				}

			}

		} catch (Exception e) {
			e.printStackTrace();

		}
	}

}
