package com.csStudy.CardGame.domain;


import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int cid;

    @Column(name = "cname", unique = true)
    private String cname;

    @Column(name = "cnt")
    private int cnt;

}