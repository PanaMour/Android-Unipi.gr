package com.unipi.softeng.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    //@Column(name="date",updatable = false)
    private Date date;

    @NotEmpty
    private String fullname;

    private String phone;

    @NotEmpty
    private String email;

    @NotEmpty
    private String message;

    //@PrePersist
    //void date() {
      //  this.date = new Date();
    //}

}
