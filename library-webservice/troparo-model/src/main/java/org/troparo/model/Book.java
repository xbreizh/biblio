package org.troparo.model;


import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "BOOK")
public class Book {

    // member variables
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "ID")
    private int Id;

    @Column(name = "ISBN")
    private String isbn;

    @Column(name = "TITLE")
    private String title;

    @Column(name = "AUTHOR")
    private String author;

    @Column(name = "INSERT_DATE")
    private Date insert_date;

    @Column(name = "PUBLICATIONYEAR")
    private int publicationYear;

    @Column(name = "EDITION")
    private String edition;

    @Column(name = "NB_PAGES")
    private int nbPages;

    @Column(name = "KEYWORDS")
    private String keywords;

    @OneToMany(mappedBy = "book", fetch = FetchType.EAGER, cascade = {CascadeType.REMOVE})
    private List<Loan> loanList = new ArrayList<>();

    // getters & setters

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
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
                ", loanList=" + loanList.size() +
                '}';
    }
}