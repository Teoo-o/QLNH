package com.nhahang.qlnh.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MaNlKey implements Serializable {

    @Column(name = "MaMon")
    private String maMon;

    @Column(name = "MaNL")
    private String maNL;
}