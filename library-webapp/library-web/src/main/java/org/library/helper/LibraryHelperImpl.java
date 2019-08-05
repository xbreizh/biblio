package org.library.helper;

import org.apache.log4j.Logger;
import org.library.model.Loan;
import org.library.model.Member;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.ModelAndView;

import javax.inject.Named;
import java.util.*;

@Named
public class LibraryHelperImpl implements LibraryHelper {
    private static final int MAX_RESERVATION = 3;
    private static Logger logger = Logger.getLogger(LibraryHelperImpl.class);

    @Override
    public void addingPopup(ModelAndView mv, String error) {
        mv.addObject("popup", true);
        mv.addObject("error", error);
    }

    @Override
    public void checkMaxReserved(Member member, ModelAndView mv) {

        int nbReserved = 0;
        mv.addObject("maxReservation", false);
        for (Loan loan : member.getLoanList()
        ) {
            if (loan.getStartDate() == null) {
                nbReserved++;
            }
        }
        if (nbReserved >= MAX_RESERVATION) {
            mv.addObject("maxReservation", true);
        }

    }

    @Override
    public void checkOverdue(Member member, ModelAndView mv) {
        mv.addObject("overdue", false);
        for (Loan loan : member.getLoanList()
        ) {
            if (loan.getStatus().equalsIgnoreCase("OVERDUE")) {
                mv.addObject("overdue", true);
                logger.info("overdue found");
                break;
            }
        }

    }

    public void getIsbnRentedList(Member member, ModelAndView mv) {
        List<String> loanList = new ArrayList<>();
        String[] isbnList;
        if (!member.getLoanList().isEmpty()) {
            logger.info("nb loans to add: " + member.getLoanList().size());
            for (Loan loan : member.getLoanList()
            ) {
                logger.info("adding: " + loan.getIsbn());
                loanList.add(loan.getIsbn());
            }
        }
        logger.info("isbn passed: " + loanList.size());

        isbnList = Arrays.copyOf(loanList.toArray(), loanList.size(),
                String[].class);
        logger.info(Arrays.toString(isbnList));
        mv.addObject("isbnList", isbnList);
    }

    @Override
    public Map<String, String> generateSearchMap(String isbn, String author, String title) {
        Map<String, String> criteria = new HashMap<>();
        criteria.put("ISBN", isbn);
        criteria.put("TITLE", title);
        criteria.put("AUTHOR", author);

        return criteria;

    }


    //  Authentication

    @Override
    public String getConnectedLogin() {
        Authentication authentication = getAuthentication();
        return authentication.getPrincipal().toString();


    }

    @Override
    public String getConnectedToken() {
        Authentication authentication = getAuthentication();
        return authentication.getDetails().toString();
    }

    private Authentication getAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        logger.info("getting authentication: " + authentication.getName());
        return authentication;
    }
}
