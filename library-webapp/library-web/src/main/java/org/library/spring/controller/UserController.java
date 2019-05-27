package org.library.spring.controller;

import org.apache.log4j.Logger;
import org.library.contract.BookManager;
import org.library.contract.LoanManager;
import org.library.contract.MemberManager;
import org.library.model.Book;
import org.library.model.Member;
import org.library.model.Search;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.inject.Inject;
import java.security.Principal;
import java.util.ArrayList;
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
        SecurityContext context = SecurityContextHolder.getContext();
        String token = context.getAuthentication().getDetails().toString();
        String login = context.getAuthentication().getPrincipal().toString();
        logger.info("controller: " + context.getAuthentication().getName());
     /* logger.info(context.getAuthentication().getCredentials().toString());
      logger.info(context.getAuthentication().toString());*/
        Member m = memberManager.getMember(token, login);
        logger.info("Member retrieved: " + m);
        logger.info("loan list: " + m.getLoanList());
        /* logger.info("Member retrieved: " + m.getLoanList().size());*/
        ModelAndView mv = new ModelAndView();
        /* List<Loan> loanList = m.getLoanList();*/
/*
        logger.info("loanlist: " + loanList);
        if (loanList.size() > 0) {
            logger.info("loanList > 0");
            loanList = m.getLoanList();
        }*/
        Search search = new Search();
        mv.addObject("loanList", m.getLoanList());
        mv.addObject("member", m);
        mv.setViewName("home");
        return mv;
    }

    @GetMapping("/login")
    public ModelAndView login() {
        logger.info("yuhuhuhuhu");
        ModelAndView mv = new ModelAndView();

        mv.setViewName("login");
        return mv;
    }

    @PostMapping("/renew")
    public ModelAndView renew(ModelAndView mv, String id) {
        SecurityContext context = SecurityContextHolder.getContext();
        String token = context.getAuthentication().getDetails().toString();
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
       /* SecurityContext context = SecurityContextHolder.getContext();
        String token = context.getAuthentication().getDetails().toString();
        String login = context.getAuthentication().getPrincipal().toString();
        logger.info("controller: "+context.getAuthentication().getName());
     *//* logger.info(context.getAuthentication().getCredentials().toString());
      logger.info(context.getAuthentication().toString());*//*
        Member m = memberManager.getMember(token, login);
        logger.info("Member retrieved: " + m);
       *//* logger.info("Member retrieved: " + m.getLoanList().size());*//*

        List<Loan> loanList = new ArrayList<>();

        logger.info("loanlist: "+loanList);
        if(loanList.size() > 0) {
            logger.info("loanList > 0");
            loanList =  m.getLoanList();
        }*/
      /*  mv.addObject("loanList", loanList);
        mv.addObject("member",m);*/
        mv.setViewName("mySpace");
        /*logger.info("loan sample from member: "+m.getLoanList().get(0));*/
        return mv;
    }

    @PostMapping("/search")
    public ModelAndView search(ModelAndView mv, String ISBN, String author, String title) {
        logger.info("getting into search");
        List<Book> books = new ArrayList<>();
        /*Search search = new Search();*/
        // Get authenticated user name from SecurityContext
        SecurityContext context = SecurityContextHolder.getContext();
        String token = context.getAuthentication().getDetails().toString();
        String login = context.getAuthentication().getPrincipal().toString();
        Member m = memberManager.getMember(token, login);
        logger.info("token: "+token);
        logger.info(context.getAuthentication().getName());
        logger.info("isbn received: " + ISBN);
        logger.info("title received: " + title);
        logger.info("author received: " + author);
        /* logger.info("search: "+search);*/
        HashMap criterias = new HashMap<String, String>();
        criterias.put("ISBN", ISBN);
        criterias.put("TITLE", title);
        criterias.put("AUTHOR", author);
        books = bookManager.searchBooks(token, criterias);
        /* ModelAndView mv = new ModelAndView();*/
        mv.addObject("loanList", m.getLoanList());
        mv.addObject("member", m);
        mv.addObject("books", books);
        mv.addObject("isbn", ISBN);
        mv.addObject("title", title);
        mv.addObject("author", author);
        mv.setViewName("home");
        logger.info("going back to home");
        /*logger.info("loan sample from member: "+m.getLoanList().get(0));*/
        return mv;
    }
}
