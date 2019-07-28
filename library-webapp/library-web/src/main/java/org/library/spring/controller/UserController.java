package org.library.spring.controller;

import org.apache.log4j.Logger;
import org.library.business.contract.BookManager;
import org.library.business.contract.LoanManager;
import org.library.business.contract.MemberManager;
import org.library.model.Book;
import org.library.model.Loan;
import org.library.model.Member;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.troparo.services.bookservice.BusinessExceptionBook;
import org.troparo.services.connectservice.BusinessExceptionConnect;
import org.troparo.services.loanservice.BusinessExceptionLoan;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.xml.ws.soap.SOAPFaultException;
import java.net.UnknownHostException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@Controller
@ControllerAdvice
public class UserController {
    private static final String LOGIN = "login";
    @Inject
    MemberManager memberManager;
    @Inject
    BookManager bookManager;
    @Inject
    LoanManager loanManager;

    private Logger logger = Logger.getLogger(this.getClass().getName());

    @ExceptionHandler({IndexOutOfBoundsException.class, NoHandlerFoundException.class, SOAPFaultException.class, BusinessExceptionConnect.class, UnknownHostException.class, NullPointerException.class})
    public ModelAndView handleNoHandlerFoundException(BusinessExceptionConnect ex) {
        ModelAndView model = new ModelAndView();
        model.addObject("exception", ex.getMessage());


        if (ex.getMessage().startsWith("No handler found")) {
            model.setViewName("404");
        } else {
            model.setViewName("error");
        }

        return model;
    }


    @RequestMapping("/")
    public ModelAndView home(String error) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String token = authentication.getDetails().toString();
        String login = authentication.getPrincipal().toString();
        logger.info("controller: " + authentication.getName());
        Member member = memberManager.getMember(token, login);
        ModelAndView mv = new ModelAndView();
        if (member != null) {
            logger.info("Member retrieved: " + member);
            checkOverdue(member, mv);
            getIsbnRentedList(member, mv);
            mv.addObject("member", member);
            addingPopup(mv, error);
            mv.setViewName("home");
        } else {
            mv.setViewName(LOGIN);
        }
        return mv;
    }

    private void getIsbnRentedList(Member member, ModelAndView mv){
        List<String> loanList = new ArrayList<>();
        String[] isbnList;
        if(!member.getLoanList().isEmpty()) {
            logger.info("nb loans to add: "+member.getLoanList().size());
            for (Loan loan : member.getLoanList()
            ) {
                logger.info("adding: "+loan.getIsbn());
                loanList.add(loan.getIsbn());
            }
        }
        logger.info("isbn passed: "+loanList.size());

        isbnList = Arrays.copyOf(loanList.toArray(), loanList.size(),
                String[].class);
        logger.info(Arrays.toString(isbnList));
        mv.addObject("isbnList", isbnList);
    }

    private void checkOverdue(Member member, ModelAndView mv) {
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

    private void addingPopup(ModelAndView mv, String error) {
        mv.addObject("popup", true);
        mv.addObject("error", error);
    }

    @GetMapping("/login")
    public ModelAndView login() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        ModelAndView mv = new ModelAndView();
        mv.setViewName(LOGIN);

        // check if user already logged in
        if (!authentication.getPrincipal().toString().equals("anonymousUser")) {
            mv.addObject(LOGIN, authentication.getPrincipal().toString());
            mv.setViewName("connected");
        }

        return mv;
    }

    @RequestMapping("/denied")
    public ModelAndView error(Principal user, HttpServletRequest req) {
        logger.info("error");
        ModelAndView model = new ModelAndView();
        model.addObject("errorCode", "Error 403");

        logger.info(req.getAttribute("javax.servlet.error.status_code"));
        if (user != null) {
            model.addObject("msg", "Hi " + user.getName()
                    + ", you do not have permission to access this page!");
        } else {
            model.addObject("msg",
                    "You do not have permission to access this page!");
        }

        model.setViewName("403");
        return model;
    }


    @GetMapping("/passwordReset")
    public ModelAndView passwordReset(String login, String token) {
        ModelAndView mv = new ModelAndView();
        mv.addObject(LOGIN, login);
        mv.addObject("token", token);

        mv.setViewName("passwordReset/passwordReset");
        return mv;
    }

    @PostMapping("/passwordResetSendEmail")
    public ModelAndView passwordResetSendEmail1(String login, String email) throws BusinessExceptionConnect {
        logger.info(LOGIN + login);
        logger.info("email: " + email);
        ModelAndView mv = new ModelAndView();
        mv.addObject(LOGIN, login);
        mv.addObject("email", email);
        if (memberManager.sendResetPasswordLink(login, email)) {

            mv.setViewName("passwordReset/passwordResetLinkOk");
        } else {
            mv.setViewName("passwordReset/passwordResetLinkKo");
        }
        return mv;
    }

    @GetMapping("/recover")
    public ModelAndView passwordResetSendEmail() {

        ModelAndView mv = new ModelAndView();
        mv.setViewName("passwordReset/passwordResetSendEmail");
        return mv;
    }


    @PostMapping("/passwordReset1")
    public ModelAndView passwordReset1(String login, String password, String confirmPassword, String token) {
        ModelAndView mv = new ModelAndView();
        mv.addObject(LOGIN, login);
        mv.addObject("token", token);
        if (!passwordCheck(password, confirmPassword)) {
            mv.setViewName("redirect:/passwordReset");
            logger.info("View  /d login: " + login + " / password: " + password + " / password2: " + confirmPassword + " / token: " + token);
            return mv;
        } else {
            try {
                memberManager.resetPassword(login, password, token);
            } catch (BusinessExceptionConnect businessExceptionConnect) {
                logger.error(businessExceptionConnect.getMessage());
            }
            return new ModelAndView("passwordReset/passwordResetOk");
        }

    }

    private boolean passwordCheck(String password, String confirmPassword) {
        if (password.isEmpty() || !password.equals(confirmPassword)) return false;
        return true;

    }


    @PostMapping("/renew")
    public ModelAndView renew(ModelAndView mv, String id) throws BusinessExceptionLoan {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String token = authentication.getDetails().toString();
        logger.info("trying to renew: " + id);
        int idLoan = Integer.parseInt(id);
        loanManager.renewLoan(token, idLoan);

        return new ModelAndView("redirect:/");

    }

    @PostMapping("/remove")
    public ModelAndView remove(ModelAndView mv, String loanId) throws BusinessExceptionLoan {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String token = authentication.getDetails().toString();
        logger.info("trying to remove: " + loanId);
        int idLoan = Integer.parseInt(loanId);
        loanManager.removeLoan(token, idLoan);

        return new ModelAndView("redirect:/");

    }


    @GetMapping("/connect")
    public String user(Principal principal) {
        // Get authenticated user name from Principal
        logger.info("trying to get to user");
        logger.info(principal.getName());
        logger.info("role: " + principal.toString());

        logger.info("principal: " + principal);
        return "home";
    }


    @GetMapping("/mySpace")
    public ModelAndView mySpace() {

        return new ModelAndView("mySpace");
    }

    @PostMapping("/reservePreForm")
    public ModelAndView reservePreForm(String isbn) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String token = authentication.getDetails().toString();
        logger.info("trying to get loans for: " + isbn);
        String reserveResult = loanManager.reserve(token, isbn);
        ModelAndView mv = new ModelAndView();

        mv.addObject("error", reserveResult);
        logger.info("error returned: " + reserveResult);
        mv.setViewName("403");

        return mv;

    }

    @PostMapping("/reserve")
    public ModelAndView reserve(ModelAndView mv, String isbn) {
        logger.info("getting into search");
        // Get authenticated user name from SecurityContext
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String token = authentication.getDetails().toString();
        String login = authentication.getPrincipal().toString();
        Member member = memberManager.getMember(token, login);
        logger.info("token: " + token);
        logger.info(authentication.getName());
        logger.info("isbn received: " + isbn);
        logger.info(loanManager.reserve(token, isbn));
        checkOverdue(member, mv);
        getIsbnRentedList(member, mv);
        mv.setViewName("home");
        logger.info("going back to home");

        return mv;

    }


    @PostMapping("/search")
    public ModelAndView search(ModelAndView mv, String isbn, String author, String title) throws BusinessExceptionBook {
        logger.info("getting into search");
        List<Book> books;
        // Get authenticated user name from SecurityContext
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String token = authentication.getDetails().toString();
        String login = authentication.getPrincipal().toString();
        Member member = memberManager.getMember(token, login);
        logger.info("token: " + token);
        logger.info(authentication.getName());
        logger.info("isbn received: " + isbn);
        logger.info("title received: " + title);
        logger.info("author received: " + author);

        checkOverdue(member, mv);
        getIsbnRentedList(member, mv);

        HashMap criteria = new HashMap<String, String>();
        criteria.put("ISBN", isbn);
        criteria.put("TITLE", title);
        criteria.put("AUTHOR", author);
        books = bookManager.searchBooks(token, criteria);

        mv.setViewName("home");
        mv.addObject("loanList", member.getLoanList());
        mv.addObject("member", member);
        mv.addObject("books", books);
        mv.addObject("isbn", isbn);
        mv.addObject("title", title);
        mv.addObject("author", author);
        logger.info("going back to home");

        return mv;
    }
}
