package contract;

public interface ConnectConsumer {

    String getToken(String login, String password);

    boolean checkToken(String token);

    boolean closeConnection(String login);

}
