package org.troparo.model;

import java.util.Date;

public class Mail {


    private Date dueDate;
    private int diffDays;
    private Book book;
    private Member member;
    private Date endAvailableDate;


    public Mail() {
        book = new Book();
        member = new Member();
    }

    public Date getEndAvailableDate() {
        return endAvailableDate;
    }

    public void setEndAvailableDate(Date endAvailableDate) {
        this.endAvailableDate = endAvailableDate;
    }

    public String getToken() {
        return member.getToken();
    }

    public void setToken(String token) {
        member.setToken(token);
    }

    public String getLogin() {
        return member.getLogin();
    }

    public void setLogin(String login) {
        member.setLogin(login);
    }

    public String getEmail() {
        return member.getEmail();
    }

    public void setEmail(String email) {
        member.setEmail(email);
    }

    public String getFirstName() {
        return member.getFirstName();
    }

    public void setFirstName(String firstName) {
        member.setFirstName(firstName);
    }

    public String getLastName() {
        return member.getLastName();
    }

    public void setLastName(String lastName) {
        member.setLastName(lastName);
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public int getDiffDays() {
        return diffDays;
    }

    public void setDiffDays(int diffDays) {
        this.diffDays = diffDays;
    }

    public String getIsbn() {
        return book.getIsbn();
    }

    public void setIsbn(String isbn) {
        book.setIsbn(isbn);
    }

    public String getTitle() {
        return book.getTitle();
    }

    public void setTitle(String title) {
        book.setTitle(title);
    }

    public String getAuthor() {
        return book.getAuthor();
    }

    public void setAuthor(String author) {
        book.setAuthor(author);
    }

    public String getEdition() {
        return book.getEdition();
    }

    public void setEdition(String edition) {
        book.setEdition(edition);
    }

    @Override
    public String toString() {
        return "Mail{" +
                "email='" + member.getEmail() + '\'' +
                ", firstName='" + member.getFirstName() + '\'' +
                ", lastName='" + member.getLastName() + '\'' +
                ", dueDate=" + dueDate +
                ", diffDays=" + diffDays +
                ", isbn='" + book.getIsbn() + '\'' +
                ", title='" + book.getTitle() + '\'' +
                ", author='" + book.getAuthor() + '\'' +
                ", edition='" + book.getEdition() + '\'' +
                '}';
    }
}
