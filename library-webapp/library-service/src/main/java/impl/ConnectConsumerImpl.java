package impl;

import contract.ConnectConsumer;

public class ConnectConsumerImpl implements ConnectConsumer {
    @Override
    public String getToken(String login, String password) {
        return null;
    }

    @Override
    public boolean checkToken(String token) {
        return false;
    }

    @Override
    public boolean closeConnection(String login) {
        return false;
    }
}
