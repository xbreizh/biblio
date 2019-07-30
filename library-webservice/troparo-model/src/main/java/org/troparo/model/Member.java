package org.troparo.model;


import lombok.Getter;
import lombok.Setter;
import org.springframework.lang.NonNull;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "MEMBER")
@Getter
@Setter
public class Member {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    @NonNull
    private int id;

    @Column(name = "LOGIN")
    private String login;

    @Column(name = "FIRSTNAME")
    private String firstName;

    @Column(name = "LASTNAME")
    private String lastName;

    @Column(name = "PASSWORD")
    private String password;

    @Column(name = "Role")
    private String role;

    @Column(name = "TOKEN")
    private String token;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "DATEJOIN")
    private Date dateJoin;

    @Column(name = "DATECONNECT")
    private Date dateConnect;

    @Column(name = "TOKENEXPIRATION")
    private Date tokenexpiration;

    @Column(name = "REMINDER")
    private boolean reminder;

    @OneToMany(mappedBy = "borrower", fetch = FetchType.EAGER, cascade = {CascadeType.REMOVE})
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
                ", loanList=" + loanList.size() +
                ", tokenExpiration=" + tokenexpiration +
                ", reminder=" + reminder +
                '}';
    }
}