package com.unipi.softeng.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.sql.Date;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class News {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="id")
    private Long id;

    private Date date;

    @NotEmpty
    private String title;

    private String author;

    private String message;

    @Enumerated(EnumType.STRING)
    private NewsType type;

    public News(Date date, String title, String type) {
        this.date = date;
        this.title = title;
        this.type = NewsType.valueOf(type);
        //this.attachedFile = null;
    }

    public String getType() {
        return type.toString();
    }

}
