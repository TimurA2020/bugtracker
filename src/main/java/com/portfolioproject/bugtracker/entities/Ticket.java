package com.portfolioproject.bugtracker.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "tickets")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String description;

    private String type;

    private String priority;

    private String status;

    @ManyToOne(fetch = FetchType.LAZY)
    private User ticketAuthor;

    @ManyToOne(fetch = FetchType.LAZY)
    private User assignedDeveloper;

    @ManyToOne(fetch = FetchType.LAZY)
    private Project project;

}
