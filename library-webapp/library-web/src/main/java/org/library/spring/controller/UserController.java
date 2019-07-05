package org.library.spring.controller;

import org.apache.log4j.Logger;
import org.library.business.contract.BookManager;
import org.library.business.contract.LoanManager;
import org.library.business.contract.MemberManager;
import org.library.model.Book;
import org.library.model.Member;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.troparo.services.bookservice.BusinessExceptionBook;
import org.troparo.services.connectservice.BusinessExceptionConnect;
import org.troparo.services.loanservice.BusinessExceptionLoan;

import javax.inject.Inject;
import java.security.Principal;
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
        logger.info("Member retrieved: " + member);
        logger.info("loan list: " + member.getLoanList());

        ModelAndView mv = new ModelAndView();
        mv.addObject("loanList", member.getLoanList());
        mv.addObject("member", member);
        mv.setViewName("home");
        return mv;
    }

    @GetMapping("/login")
    public ModelAndView login() {
        logger.info("login");
        ModelAndView mv = new ModelAndView();

        mv.setViewName("login");
        return mv;
    }


    @GetMapping("/passwordReset")
    public ModelAndView passwordReset(String login,  String token) throws BusinessExceptionLoan {
       /* Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String token = authentication.getDetails().toString();*/
       /* System.out.println("View  /d login: "+login+" / password: "+password+" / token: "+token);
        logger.info("trying to resetPassword: " + login);
       // memberManager.renewLoan(token, login);
        memberManager.resetPassword(login, password, token);*/
        System.out.println("login: "+login);
        System.out.println("token: "+token);
        ModelAndView mv = new ModelAndView();
        mv.addObject("login", login);
        mv.addObject("token", token);

        mv.setViewName("passwordReset");
        return mv;
    }

    @PostMapping("/passwordResetSendEmail")
    public ModelAndView passwordResetSendEmail1(String login,  String email) throws BusinessExceptionConnect {
        System.out.println("login: "+login);
        System.out.println("email: "+email);
        ModelAndView mv = new ModelAndView();
        mv.addObject("login", login);
        mv.addObject("email", email);
        if(memberManager.sendResetPasswordlink(login, email)){

            mv.setViewName("passwordResetLinkOk");
        }else{
            mv.setViewName("passwordResetLinkKo");
        }
        return mv;
    }


    @GetMapping("/passwordResetSendEmail")
    public ModelAndView passwordResetSendEmail(String login,  String email) throws BusinessExceptionConnect {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("passwordResetSendEmail");
        return mv;
    }



    @PostMapping("/passwordReset1")
    public ModelAndView passwordReset1( String login, String password, String confirmPassword, String token){
        ModelAndView mv = new ModelAndView();
        mv.addObject("login", login);
        mv.addObject("token", token);
        if(!passwordCheck(password, confirmPassword)){
            mv.setViewName("redirect:/passwordReset");
            System.out.println("View  /d login: "+login+" / password: "+password+" / password2: "+confirmPassword+" / token: "+token);
            return mv;
        }else{
            try {
                memberManager.resetPassword(login, password, token);
            } catch (BusinessExceptionConnect businessExceptionConnect) {
                businessExceptionConnect.printStackTrace();
            }
            return new ModelAndView("passwordResetOk");
        }

    }

    private boolean passwordCheck(String password, String confirmPassword) {
        if(password.isEmpty() || !password.equals(confirmPassword))return false;
        return true;

    }

   /* @GetMapping("/passwordResetOk")
    public ModelAndView passwordResetOk() {
        logger.info("passwordResetOk");
        ModelAndView mv = new ModelAndView();

        mv.setViewName("passwordResetOk");
        return mv;
    }*/


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
        // Get authenticated user name from SecurityContext

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
