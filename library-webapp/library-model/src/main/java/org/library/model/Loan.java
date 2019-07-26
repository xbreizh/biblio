package org.library.model;


import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class Loan {

    // member variables

    private int id;


    private Date startDate;


    private Date plannedEndDate;


    private Date endDate;


    private Member borrower;


    private Book book;

    private boolean renewable;

    private String status;

    private boolean checked;

    private String isbn;



    @Override
    public String toString() {
        return "Loan{" +
                "id=" + id +
                ", startDate=" + startDate +
                ", plannedEndDate=" + plannedEndDate +
                ", endDate=" + endDate +
                ", renewable=" + renewable +
                ", status=" + status +
                ", book=" + book.getTitle() +
                '}';
    }


}