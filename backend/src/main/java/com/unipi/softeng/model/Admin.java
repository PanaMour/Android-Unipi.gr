package com.unipi.softeng.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

@Entity @Data @NoArgsConstructor @AllArgsConstructor
public class Admin {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique=true)
    @NotEmpty
    private String username;
    @NotEmpty
    private String password;
    private String firstname;
    private String lastname;

}
