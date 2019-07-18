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
				case 1: //�ӿͻ��˶��ļ����ϴ�
					String serverFileName = dis.readUTF();

					File serverFile = null;

					// process / or \
					if (serverFileName.startsWith("/")) {//�ļ���·��
						serverFileName = serverFileName.replace('/', File.separatorChar);//��ƽ̨��ת��·����ʽ ·����ʾ������һ��
						serverFileName = serverFileName.substring(1);
						serverFile = new File(GoFileServer.rootFolder, serverFileName); //����������ʱ�����ĸ��ļ���

					} else {//�ļ�·��
						serverFile = new File(GoFileServer.rootFolder, serverFileName);
					}

					FileOutputStream fos = new FileOutputStream(serverFile);//�������˵��ֽ������
					BufferedOutputStream bos = new BufferedOutputStream(fos);

					long length = dis.readLong();

					while (true) {//���ļ��ĳ���
						int len = dis.readInt();
						if (len == -1) {//ȫ������
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

				case 2://�����ļ���
					String serverFolderName = dis.readUTF();

					serverFolderName = serverFolderName.replace('/', File.separatorChar);
					File sfn = new File(GoFileServer.rootFolder, serverFolderName);
					sfn.mkdirs(); //��mkdir������

					break;

				case 3://�����ļ�

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
