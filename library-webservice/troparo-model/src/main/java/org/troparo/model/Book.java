package org.troparo.model;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "BOOK")
@Getter
@Setter
public class Book {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private int id;

    @Column(name = "ISBN")
    private String isbn;

    @Column(name = "TITLE")
    private String title;

    @Column(name = "AUTHOR")
    private String author;

    @Column(name = "INSERT_DATE")
    private Date insertDate;

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


    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", isbn='" + isbn + '\'' +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", insert_date=" + insertDate +
                ", publicationYear=" + publicationYear +
                ", edition='" + edition + '\'' +
                ", nbPages=" + nbPages +
                ", keywords='" + keywords + '\'' +
                ", loanList=" + loanList.size() +
                '}';
    }
}