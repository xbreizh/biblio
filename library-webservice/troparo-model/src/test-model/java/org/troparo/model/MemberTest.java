package org.troparo.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MemberTest {

    private Member member;

    @BeforeEach
    void init() {
        member = new Member();
    }

    @Test
    void getId() {
        member.setId(123);
        assertEquals(123, member.getId());
    }

    @Test
    void setId() {
        member.setId(123);
        assertEquals(123, member.getId());
    }

    @Test
    void getLogin() {
        member.setLogin("Momo45");
        assertEquals("Momo45", member.getLogin());
    }

    @Test
    void setLogin() {
        member.setLogin("Momo45");
        assertEquals("Momo45", member.getLogin());
    }

    @Test
    void getFirstName() {
        member.setFirstName("Maurice");
        assertEquals("Maurice", member.getFirstName());
    }

    @Test
    void setFirstName() {
        member.setFirstName("Maurice");
        assertEquals("Maurice", member.getFirstName());
    }

    @Test
    void getLastName() {
        member.setLastName("Logalo");
        assertEquals("Logalo", member.getLastName());
    }

    @Test
    void setLastName() {
        member.setLastName("Logalo");
        assertEquals("Logalo", member.getLastName());
    }

    @Test
    void getPassword() {
        member.setPassword("123frf");
        assertEquals("123frf", member.getPassword());
    }

    @Test
    void setPassword() {
        member.setPassword("123frf");
        assertEquals("123frf", member.getPassword());
    }

    @Test
    void getRole() {
        member.setRole("boss");
        assertEquals("boss", member.getRole());
    }


    @Test
    void setRole() {
        member.setRole("boss");
        assertEquals("boss", member.getRole());
    }

    @Test
    void getToken() {
        member.setToken("121213fefd4545345");
        assertEquals("121213fefd4545345", member.getToken());
    }

    @Test
    void setToken() {
        member.setToken("121213fefd4545345");
        assertEquals("121213fefd4545345", member.getToken());
    }

    @Test
    void getEmail() {
        member.setEmail("tofo@gmail.fr");
        assertEquals("tofo@gmail.fr", member.getEmail());
    }

    @Test
    void setEmail() {
        member.setEmail("tofo@gmail.fr");
        assertEquals("tofo@gmail.fr", member.getEmail());
    }

    @Test
    void getDateJoin() throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = format.parse("2009-12-31");
        member.setDateJoin(date);
        assertEquals(date, member.getDateJoin());
    }

    @Test
    void setDateJoin() throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = format.parse("2009-12-31");
        member.setDateJoin(date);
        assertEquals(date, member.getDateJoin());
    }

    @Test
    void getDateConnect() throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = format.parse("2009-12-31");
        member.setDateConnect(date);
        assertEquals(date, member.getDateConnect());
    }

    @Test
    void setDateConnect() throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = format.parse("2009-12-31");
        member.setDateConnect(date);
        assertEquals(date, member.getDateConnect());
    }

    @Test
    void getLoanList() {
        List<Loan> loanList = new ArrayList<>();
        member.setLoanList(loanList);
        assertEquals(loanList, member.getLoanList());
    }

    @Test
    void setLoanList() {
        List<Loan> loanList = new ArrayList<>();
        member.setLoanList(loanList);
        assertEquals(loanList, member.getLoanList());
    }

    @Test
    void toString1() throws ParseException {
        List<Loan> loanList = new ArrayList<>();
        member.setLoanList(loanList);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = format.parse("2009-12-31");
        member.setDateConnect(format.parse("2009-12-31"));
        member.setDateJoin(format.parse("2010-12-31"));
        member.setEmail("bob@gmail.com");
        member.setToken("bnjnj3222");
        member.setRole("boss");
        member.setPassword("bhbh222");
        member.setLastName("Boki");
        member.setFirstName("Paul");
        member.setLogin("koko");
        member.setId(123);
        assertEquals("Member{id=123, login='koko', firstName='Paul', lastName='Boki', password='bhbh222', role='boss', token='bnjnj3222', email='bob@gmail.com', dateJoin=Fri Dec 31 00:00:00 UTC 2010, dateConnect=Thu Dec 31 00:00:00 UTC 2009, loanList=0, tokenExpiration=null}", member.toString());
    }


}