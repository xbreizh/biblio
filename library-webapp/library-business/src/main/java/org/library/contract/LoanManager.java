package org.library.contract;


public interface LoanManager {


    boolean renewLoan(String token, int id);

    boolean isRenewable(String token, int id);

    String getStatus(String token, int id);


}
