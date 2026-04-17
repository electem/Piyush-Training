package com.example.OneToOneMapping.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private UserAadhar userAadhar;

    // 🔥 Helper method (important for bidirectional mapping)
    public void addAadhar(UserAadhar aadhar) {
        this.userAadhar = aadhar;
        aadhar.setUser(this);
    }
}