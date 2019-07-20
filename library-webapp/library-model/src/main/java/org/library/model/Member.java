package org.library.model;


import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
public class Member {

    // member variables

    private int id;


    private String login;


    private String firstName;


    private String lastName;


    private String password;


    private String role;


    private String token;


    private String email;


    private Date dateJoin;


    private Date dateConnect;


    private List<Loan> loanList = new ArrayList<>();


    @Override
    public String toString() {
        return "Member{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", password='" + password + '\'' +
                ", role='" + role + '\'' +
                ", token='" + token + '\'' +
                ", email='" + email + '\'' +
                ", dateJoin=" + dateJoin +
                ", dateConnect=" + dateConnect +
                ", loanList=" + loanList +
                '}';
    }
}