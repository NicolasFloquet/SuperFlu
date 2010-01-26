package connexion;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

public class Send {

	private ObjectOutputStream ooStream;

	public Send(Socket sock) {
		OutputStream oStream;
		try {
			oStream = sock.getOutputStream();
			ooStream = new ObjectOutputStream(oStream);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void sendData(Object o) {
		try {
			ooStream.writeObject(o);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
