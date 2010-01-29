package connexion;

public interface ConnexionController {
	public boolean connect();

	public void send(Object o);

	public void deconnection();
}
