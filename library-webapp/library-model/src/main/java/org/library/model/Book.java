package org.library.model;


import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
public class Book {

    // member variables

    private int id;

    private String isbn;


    private String title;


    private String author;


    private Date insertDate;


    private int publicationYear;


    private String edition;


    private int nbPages;

    private int nbAvailable;


    private String keywords;


    private List<Loan> loanList = new ArrayList<>();



    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", isbn='" + isbn + '\'' +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", insertDate=" + insertDate +
                ", publicationYear=" + publicationYear +
                ", edition='" + edition + '\'' +
                ", nbPages=" + nbPages +
                ", keywords='" + keywords + '\'' +
                ", nbAvailable='" + nbAvailable + '\'' +
                ", loanList=" + loanList.size() +
                '}';
    }
}