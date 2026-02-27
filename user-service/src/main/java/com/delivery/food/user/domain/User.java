package com.delivery.food.user.domain;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;           // used later by auth for login

    @Column(nullable = false)
    private String fullName;

    @Column(name = "password", nullable = false)
    private String password;

    private String phone;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role = Role.CUSTOMER;

    private boolean active = true;

    // Add more profile fields as needed: address, avatarUrl, birthDate, preferences...

    public enum Role {
        CUSTOMER,
        RESTAURANT_OWNER,
        DELIVERY_PERSON,
        ADMIN
    }
}