package org.troparo.business.impl;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.troparo.business.contract.LoanManager;
import org.troparo.business.contract.MailManager;
import org.troparo.business.contract.MemberManager;
import org.troparo.model.Loan;
import org.troparo.model.Mail;
import org.troparo.model.Member;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.*;


@Named
@Transactional
@PropertySource("classpath:config.properties")
public class MailManagerImpl implements MailManager {
    @Inject
    LoanManager loanManager;


    @Inject
    MemberManager memberManager;

    public void setMemberManager(MemberManager memberManager) {
        this.memberManager = memberManager;
    }

    @Value("${daysReminder}")
    private int daysReminder;

    public MailManagerImpl() {
        if (daysReminder == 0) daysReminder = 5;
    }

    private static Logger logger = Logger.getLogger(MailManagerImpl.class);

    @Override
    public int calculateDaysBetweenDates(Date d1, Date d2) {
        long diff = d2.getTime() - d1.getTime();
        return (int) diff / (24 * 60 * 60 * 1000);
    }


    @Override
    public List<Mail> getOverdueEmailList() {
        Map<String, String> criterias = new HashMap<>();
        criterias.put("status", "OVERDUE");
        logger.info("getting overdue list");
        List<Loan> loans = loanManager.getLoansByCriteria(criterias);

        logger.info("loans: " + loans.size());
        return gettingDataForLoan(loans);
    }

    @Override
    public List<Mail> getLoansReadyForStart(String token) {
        List<Mail> list = new ArrayList<>();
        if (!memberManager.checkAdmin(token)) return list;
        logger.info("getting loans ready for start");
        List<Loan> loans = loanManager.getLoansReadyForStart();
        return gettingDataForLoan(loans);
    }

    @Override
    public List<Mail> getLoansReminder(String token) {
        List<Mail> list = new ArrayList<>();
        if (!memberManager.checkAdmin(token)) return list;
        List<Loan> loans = loanManager.getReminderLoans(daysReminder);
        logger.info("size of list found: " + loans.size());
        return gettingDataForLoan(loans);
    }

    List<Mail> gettingDataForLoan(List<Loan> loans) {
        logger.info("preparing mail list");
        List<Mail> mailList = new ArrayList<>();
        for (Loan loan : loans
        ) {
            Mail mail = new Mail();
            mail.setEmail(loan.getBorrower().getEmail());
            mail.setFirstName(loan.getBorrower().getFirstName());
            mail.setLastName(loan.getBorrower().getLastName());
            mail.setIsbn(loan.getBook().getIsbn());
            mail.setTitle(loan.getBook().getTitle());
            mail.setAuthor(loan.getBook().getAuthor());
            mail.setEdition(loan.getBook().getEdition());
            mail.setDueDate(loan.getPlannedEndDate());
            if (loan.getAvailableDate() != null) {
                mail.setEndAvailableDate(calculateEndAvailableDate(loan, loanManager.getNbDaysReservation()));
            }
            if (loan.getPlannedEndDate() != null) {
                int overDays = calculateDaysBetweenDates(getTodaySDate(), loan.getPlannedEndDate());
                mail.setDiffDays(overDays);
            }
            mailList.add(mail);
        }
        return mailList;
    }


    @Override
    public Date calculateEndAvailableDate(Loan loan, int nbDays) {
        logger.debug("calculating endAvailable date");
        Date availableDate = loan.getAvailableDate();
        Calendar c = Calendar.getInstance();
        c.setTime(availableDate);
        c.add(Calendar.DATE, nbDays);  // number of days to add
        return c.getTime();

    }

    Date getTodaySDate() {
        return new Date();
    }

    @Override
    public LoanManager getLoanManager() {
        return loanManager;
    }

    @Override
    public void setLoanManager(LoanManager loanManager) {
        this.loanManager = loanManager;
    }

    @Override
    public List<Mail> getPasswordResetList(String token) {
        List<Mail> list = new ArrayList<>();
        if (!memberManager.checkAdmin(token)) return list;
        List<Member> memberList = memberManager.getPasswordResetList();
        logger.info("number of member pwd to reset: " + memberList.size());
        for (Member member : memberList
        ) {
            if (member.getToken().startsWith("TEMP")) {
                member.setToken(removeTempFromToken(member.getToken()));
                member.setTokenexpiration(getTodaySDate());
                memberManager.updateMember(member);
            }
        }
        return convertMemberListIntoMailList(memberList);
    }

    public String removeTempFromToken(String token) {
        return token.replace("TEMP", "");
    }

    public List<Mail> convertMemberListIntoMailList(List<Member> memberList) {
        List<Mail> mailList = new ArrayList<>();
        logger.info("size of memberList received: " + memberList.size());
        for (Member member : memberList
        ) {
            Mail mail = new Mail();
            mail.setToken(member.getToken());
            mail.setLogin(member.getLogin());
            mail.setEmail(member.getEmail());
            mailList.add(mail);
        }
        return mailList;
    }

}
