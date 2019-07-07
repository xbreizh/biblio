package org.library.spring.controller;

import org.apache.log4j.Logger;
import org.library.business.contract.BookManager;
import org.library.business.contract.LoanManager;
import org.library.business.contract.MemberManager;
import org.library.model.Book;
import org.library.model.Member;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.troparo.services.bookservice.BusinessExceptionBook;
import org.troparo.services.connectservice.BusinessExceptionConnect;
import org.troparo.services.loanservice.BusinessExceptionLoan;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

@Controller
public class UserController {
    @Inject
    MemberManager memberManager;
    @Inject
    BookManager bookManager;
    @Inject
    LoanManager loanManager;


    private Logger logger = Logger.getLogger(UserController.class);


    @GetMapping("/")
    public ModelAndView home() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String token = authentication.getDetails().toString();
        String login = authentication.getPrincipal().toString();
        logger.info("controller: " + authentication.getName());
        Member member = memberManager.getMember(token, login);
        ModelAndView mv = new ModelAndView();
        if (member != null) {
            logger.info("Member retrieved: " + member);
            logger.info("loan list: " + member.getLoanList());

            mv.addObject("loanList", member.getLoanList());
            mv.addObject("member", member);
            mv.setViewName("home");
        } else {
            mv.setViewName("login");
        }
        return mv;
    }

    @GetMapping("/login")
    public ModelAndView login() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        ModelAndView mv = new ModelAndView();
        mv.setViewName("login");

        // check if user already logged in
        if (!authentication.getPrincipal().toString().equals("anonymousUser")){
            mv.addObject("login",authentication.getPrincipal().toString());
            mv.setViewName("connected");
        }

        return mv;
    }

    @RequestMapping("/error")
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
        mv.addObject("login", login);
        mv.addObject("token", token);

        mv.setViewName("passwordReset/passwordReset");
        return mv;
    }

    @PostMapping("/passwordResetSendEmail")
    public ModelAndView passwordResetSendEmail1(String login, String email) throws BusinessExceptionConnect {
        logger.info("login: " + login);
        logger.info("email: " + email);
        ModelAndView mv = new ModelAndView();
        mv.addObject("login", login);
        mv.addObject("email", email);
        if (memberManager.sendResetPasswordlink(login, email)) {

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
        mv.addObject("login", login);
        mv.addObject("token", token);
        if (!passwordCheck(password, confirmPassword)) {
            mv.setViewName("redirect:/passwordReset");
            System.out.println("View  /d login: " + login + " / password: " + password + " / password2: " + confirmPassword + " / token: " + token);
            return mv;
        } else {
            try {
                memberManager.resetPassword(login, password, token);
            } catch (BusinessExceptionConnect businessExceptionConnect) {
                businessExceptionConnect.printStackTrace();
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
        ModelAndView mv = new ModelAndView();

        mv.setViewName("mySpace");

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

        HashMap criterias = new HashMap<String, String>();
        criterias.put("isbn", isbn);
        criterias.put("TITLE", title);
        criterias.put("AUTHOR", author);
        books = bookManager.searchBooks(token, criterias);

        mv.addObject("loanList", member.getLoanList());
        mv.addObject("member", member);
        mv.addObject("books", books);
        mv.addObject("isbn", isbn);
        mv.addObject("title", title);
        mv.addObject("author", author);
        mv.setViewName("home");
        logger.info("going back to home");

        return mv;
    }
}
