package org.troparo.model;


import lombok.Getter;
import lombok.Setter;
import org.springframework.lang.NonNull;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "LOAN")
@Getter @Setter
public class Loan {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    @NonNull
    private int id;

    @Column(name = "START_DATE")
    private Date startDate;

    @Column(name = "PLANNED_END_DATE")
    private Date plannedEndDate;

    @Column(name = "END_DATE")
    private Date endDate;

    @Column(name = "CHECKED")
    private boolean checked;

    @ManyToOne
    private Member borrower;

    @ManyToOne
    private Book book;



    @Override
    public String toString() {
        return "Loan{" +
                "id=" + id +
                ", startDate=" + startDate +
                ", plannedEndDate=" + plannedEndDate +
                ", endDate=" + endDate +
                ", borrower=" + borrower.getLogin() +
                ", book=" + book.getTitle() +
                ", checked=" + checked +
                '}';
    }


}