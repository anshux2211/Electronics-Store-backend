package com.lcwd.electronic.store.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder

@Entity
@Table(name = "users")
public class User {
    @Id
    private String userId;

    private String name;

    @Column(unique = true)
    private String email;

    @Column(length = 10)
    private String password;

    private String gender;

    @Column(unique = true)
    private String phone;

    @Column(length = 1000)
    private String about;

    private String imageName;
}
