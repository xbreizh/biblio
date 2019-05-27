package org.troparo.business.contract;

import org.troparo.model.Mail;

import java.util.List;


public interface EmailManager {

    List<Mail> getOverdueEmailList();


}