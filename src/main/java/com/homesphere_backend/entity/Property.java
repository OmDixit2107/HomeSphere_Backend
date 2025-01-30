package com.homesphere_backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "properties")
@Getter
@Setter
@NoArgsConstructor
public class Property {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Foreign key reference to User entity
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String title;

    @Column(length = 1000)
    private String description;

    @Column(nullable = false)
    private Float price;

    @Column(nullable = false)
    private String location;

    @Column(nullable = false)
    private String type; // "buy" or "rent"

    @Column(nullable = false)
    private String status; // "available", "sold", "rented"

    @Column(nullable = false)
    private Boolean emiAvailable;

    // Constructor
    public Property(User user, String title, String desc, Float price, String location, String type, String status, Boolean emiAvailable) {
        this.user = user;
        this.title = title;
        this.description = desc;
        this.price = price;
        this.location = location;
        this.type = type;
        this.status = status;
        this.emiAvailable = emiAvailable;
    }
}
