package org.library.model;


import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class Loan implements Comparable<Loan> {

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


    @Override
    public int compareTo(Loan loan) {
        return this.status.compareTo(loan.status);
    }

    @Override
    public boolean equals(Object obj) {
        if(obj==null)return false;
        if (getClass() != obj.getClass())
            return false;
       Loan loan = (Loan) obj;
       if(startDate ==null || startDate != loan.startDate)return false;
       if(!isbn.equals(loan.getIsbn()))return false;
       if(book!=loan.getBook())return false;
       if(borrower!=loan.getBorrower())return false;
       return endDate==loan.getEndDate();
    }

    @Override
    public int hashCode() {
        return 1;
    }


}