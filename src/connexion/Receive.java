import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.Socket;

public class Receive {
	public final static String SERVER_HOSTNAME = "localhost";
	public final static int COMM_PORT = 5050; // socket port for client comms

	/** Default constructor. */
	public Receive() {
	}

	public Object getData(Socket socket){
		System.out.println("receive");
		try {
			InputStream iStream = socket.getInputStream();
			ObjectInputStream oiStream = null;
			oiStream = new ObjectInputStream(iStream);
			return oiStream.readObject();
		} catch (IOException e) {
			e.printStackTrace();
		}catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
}