package org.mail.contract;


import org.troparo.services.connectservice.BusinessExceptionConnect;

public interface ConnectManager {


    String authenticate() throws BusinessExceptionConnect;


}
