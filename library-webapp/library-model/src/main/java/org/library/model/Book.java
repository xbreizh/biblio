package org.library.model;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class Book {

    // member variables

    private int Id;


    private String isbn;


    private String title;


    private String author;


    private Date insert_date;


    private int publicationYear;


    private String edition;


    private int nbPages;

    private int nbAvailable;


    private String keywords;


    private List<Loan> loanList = new ArrayList<>();

    // getters & setters

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public int getNbAvailable() {
        return nbAvailable;
    }

    public void setNbAvailable(int nbAvailable) {
        this.nbAvailable = nbAvailable;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Date getInsert_date() {
        return insert_date;
    }

    public void setInsert_date(Date insert_date) {
        this.insert_date = insert_date;
    }

    public int getPublicationYear() {
        return publicationYear;
    }

    public void setPublicationYear(int publicationYear) {
        this.publicationYear = publicationYear;
    }

    public String getEdition() {
        return edition;
    }

    public void setEdition(String edition) {
        this.edition = edition;
    }

    public int getNbPages() {
        return nbPages;
    }

    public void setNbPages(int nbPages) {
        this.nbPages = nbPages;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public List<Loan> getLoanList() {
        return loanList;
    }

    public void setLoanList(List<Loan> loanList) {
        this.loanList = loanList;
    }

    @Override
    public String toString() {
        return "Book{" +
                "Id=" + Id +
                ", isbn='" + isbn + '\'' +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", insert_date=" + insert_date +
                ", publicationYear=" + publicationYear +
                ", edition='" + edition + '\'' +
                ", nbPages=" + nbPages +
                ", keywords='" + keywords + '\'' +
                ", nbAvailable='" + nbAvailable + '\'' +
                ", loanList=" + loanList.size() +
                '}';
    }
}