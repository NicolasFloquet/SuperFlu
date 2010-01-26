package connexion;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

public class Send {

	private ObjectOutputStream ooStream;
	private Socket socket;

	public Send(Socket sock) {
		this.socket = sock;
	}

	public void sendData(Object o) {
		OutputStream oStream;
		try {
			oStream = socket.getOutputStream();
			ooStream = new ObjectOutputStream(oStream);
			ooStream.writeObject(o);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
}
