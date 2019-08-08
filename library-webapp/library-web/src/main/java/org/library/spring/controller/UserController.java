package org.library.spring.controller;

import org.apache.log4j.Logger;
import org.library.business.contract.BookManager;
import org.library.business.contract.LoanManager;
import org.library.business.contract.MemberManager;
import org.library.helper.LibraryHelper;
import org.library.model.Book;
import org.library.model.Member;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.troparo.services.bookservice.BusinessExceptionBook;
import org.troparo.services.connectservice.BusinessExceptionConnect;
import org.troparo.services.loanservice.BusinessExceptionLoan;
import org.troparo.services.memberservice.BusinessExceptionMember;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.xml.ws.soap.SOAPFaultException;
import java.net.UnknownHostException;
import java.security.Principal;
import java.util.List;
import java.util.Map;

@Controller
@ControllerAdvice
public class UserController {
    private static final String LOGIN = "login";
    private static final String HOME = "home";
    private static final String RESET = "passwordReset/passwordReset";
    private static final String ERROR = "403";
    private static final String NOT_FOUND = "404";
    private static final String REDIRECT_HOME = "redirect:/";
    private static Logger logger = Logger.getLogger(UserController.class);
    //@Inject
    private MemberManager memberManager;
    //@Inject
    private BookManager bookManager;
    //@Inject
    private LoanManager loanManager;
    //@Inject
    private LibraryHelper helper;

    @Inject
    public UserController(MemberManager memberManager, BookManager bookManager, LoanManager loanManager, LibraryHelper helper) {
        this.memberManager = memberManager;
        this.bookManager = bookManager;
        this.loanManager = loanManager;
        this.helper = helper;
    }

    @ExceptionHandler({IndexOutOfBoundsException.class, NoHandlerFoundException.class, SOAPFaultException.class, BusinessExceptionConnect.class, UnknownHostException.class, NullPointerException.class})
    public ModelAndView handleNoHandlerFoundException(BusinessExceptionConnect ex) {
        ModelAndView model = new ModelAndView(ERROR);
        model.addObject("exception", ex.getMessage());


        if (ex.getMessage().startsWith("No handler found")) {
            model.setViewName(NOT_FOUND);
        }

        return model;
    }


    @RequestMapping("/")
    public ModelAndView home(String error) {
        logger.info("getting home");
        String token = helper.getConnectedToken();
        String login = helper.getConnectedLogin();
        Member member = memberManager.getMember(token, login);

        ModelAndView mv = new ModelAndView(HOME);
        if (member != null) {
            logger.info("Member retrieved: " + member);

            //checking
            helper.checkOverdue(member, mv);
            helper.getIsbnRentedList(member, mv);
            helper.checkMaxReserved(member, mv);
            helper.addingPopup(mv, error);

            mv.addObject("member", member);

        } else {
            mv.setViewName(LOGIN);
        }
        return mv;
    }


    @GetMapping("/login")
    public ModelAndView login() {
        String login = helper.getConnectedLogin();
        ModelAndView mv = new ModelAndView(LOGIN);

        // check if user already logged in
        if (!login.equals("anonymousUser")) {
            mv.setViewName(REDIRECT_HOME);
        }

        return mv;
    }

    @RequestMapping("/denied")
    public ModelAndView error(Principal user, HttpServletRequest req) {
        logger.info("error");
        ModelAndView model = new ModelAndView(ERROR);
        model.addObject("errorCode", "Error 403");

        logger.info(req.getAttribute("javax.servlet.error.status_code"));
        if (user != null) {
            model.addObject("msg", "Hi " + user.getName()
                    + ", you do not have permission to access this page!");
        } else {
            model.addObject("msg",
                    "You do not have permission to access this page!");
        }

        return model;
    }


    @GetMapping("/passwordReset")
    public ModelAndView passwordReset(String login, String token) {
        ModelAndView mv = new ModelAndView(RESET);
        mv.addObject(LOGIN, login);
        mv.addObject("token", token);

        return mv;
    }

    @PostMapping("/passwordResetSendEmail")
    public ModelAndView passwordResetSendEmail1(String login, String email) throws BusinessExceptionConnect {
        logger.info(LOGIN +" "+ login);
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
        return !password.isEmpty() && password.equals(confirmPassword);

    }


    @PostMapping("/renew")
    public ModelAndView renew(String id) throws BusinessExceptionLoan {
        String token = helper.getConnectedToken();
        logger.info("trying to renew: " + id);
        int idLoan = Integer.parseInt(id);
        loanManager.renewLoan(token, idLoan);

        return new ModelAndView(REDIRECT_HOME);

    }

    @PostMapping("/reminder")
    public ModelAndView reminder(String login, boolean reminder) throws BusinessExceptionMember {
        String token = helper.getConnectedToken();
        logger.info("updating reminder");
        memberManager.switchReminder(token, login, reminder);

        return new ModelAndView(REDIRECT_HOME);

    }


    @PostMapping("/remove")
    public ModelAndView remove(String loanId) throws BusinessExceptionLoan {
        String token = helper.getConnectedToken();
        logger.info("trying to remove: " + loanId);

        loanManager.removeLoan(token, Integer.parseInt(loanId));

        return new ModelAndView(REDIRECT_HOME);

    }


    @PostMapping("/reserve")
    public ModelAndView reserve(String isbn) {
        logger.info("getting into search");

        String token = helper.getConnectedToken();
        logger.info("isbn received: " + isbn);
        logger.info(loanManager.reserve(token, isbn));

        return new ModelAndView(REDIRECT_HOME);

    }


    @PostMapping("/search")
    public ModelAndView search(String isbn, String author, String title) throws BusinessExceptionBook {
        logger.info("getting into search");
        ModelAndView mv = new ModelAndView(HOME);

        String token = helper.getConnectedToken();
        String login = helper.getConnectedLogin();

        logger.info("elements received: isbn" + isbn + " / title " + title + " / author: " + author);


        Map<String, String> criteria = helper.generateSearchMap(isbn, author, title);
        List<Book> books = bookManager.searchBooks(token, criteria);


        Member member = memberManager.getMember(token, login);
        mv.addObject("loanList", member.getLoanList());
        mv.addObject("member", member);
        mv.addObject("books", books);
        mv.addObject("isbn", isbn);
        mv.addObject("title", title);
        mv.addObject("author", author);


        helper.checkOverdue(member, mv);
        helper.getIsbnRentedList(member, mv);
        helper.checkMaxReserved(member, mv);

        return mv;
    }


}

