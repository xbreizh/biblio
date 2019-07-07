package org.library.spring.controller;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;

@ControllerAdvice
public class NoHandlerFoundControllerAdvice {

    @ExceptionHandler(NoHandlerFoundException.class)
    public ModelAndView handleNoHandlerFoundException(NoHandlerFoundException ex) {
        ModelAndView model = new ModelAndView();
        model.addObject("exception", ex.getMessage());
        if(ex.getMessage().startsWith("No handler found")){
            model.setViewName("404");
        }else{
            model.setViewName("error");
        }

        return model;
    }

}