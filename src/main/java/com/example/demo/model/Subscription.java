package com.example.demo.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "subscriptions")
@Getter
@Setter
@NoArgsConstructor
public class Subscription {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "users_id", nullable = false)
    private User userId;

    @Column(name = "name", nullable = false)
    private String subscriptionName;

    public Subscription(User userId, String subscriptionName) {
        this.userId = userId;
        this.subscriptionName = subscriptionName;
    }

}
