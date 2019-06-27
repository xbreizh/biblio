package org.library.model;


import java.util.Date;


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


    // getters & setters


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    Date getPlannedEndDate() {
        return plannedEndDate;
    }

    public void setPlannedEndDate(Date plannedEndDate) {
        this.plannedEndDate = plannedEndDate;
    }

    Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    Member getBorrower() {
        return borrower;
    }

    void setBorrower(Member borrower) {
        this.borrower = borrower;
    }

    public Book getBook() {
        return book;
    }

    boolean isRenewable() {
        return renewable;
    }

    public void setRenewable(boolean renewable) {
        this.renewable = renewable;
    }

    String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setBook(Book book) {
        this.book = book;


    }

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