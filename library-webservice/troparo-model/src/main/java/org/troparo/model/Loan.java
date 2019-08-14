package org.troparo.model;


import lombok.Getter;
import lombok.Setter;
import org.springframework.lang.NonNull;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "LOAN")
@Getter
@Setter
public class Loan {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    @NonNull
    private int id;

    @Column(name = "RESERVATION_DATE")
    private Date reservationDate;

    @Column(name = "AVAILABLE_DATE")
    private Date availableDate;

    @Column(name = "START_DATE")
    private Date startDate;

    @Column(name = "PLANNED_END_DATE")
    private Date plannedEndDate;

    @Column(name = "END_DATE")
    private Date endDate;

    @Column(name = "ISBN")
    @NonNull
    private String isbn;

    @ManyToOne
    @NonNull
    private Member borrower;

    @ManyToOne
    private Book book;


    @Override
    public String toString() {
        String title = "no book";
        if (book != null) title = book.getTitle();
        return "Loan{" +
                "id=" + id +
                ", reservationDate=" + reservationDate +
                ", availableDate=" + availableDate +
                ", startDate=" + startDate +
                ", plannedEndDate=" + plannedEndDate +
                ", endDate=" + endDate +
                ", isbn=" + isbn +
                ", borrower=" + borrower +
                ", book=" + title +
                '}';
    }


}