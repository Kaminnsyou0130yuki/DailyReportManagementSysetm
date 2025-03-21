package com.techacademy.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.hibernate.annotations.SQLRestriction;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Entity
@Table(name = "reports")
@SQLRestriction("delete_flg = false")
public class DailyReport {

//  ID
    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

//    日付
    @Column(nullable = false)
    @NotNull
    private LocalDate reportDate;

//    タイトル
    @Column(length = 100, nullable = false)
    @Size(max = 100)
    private String title;

//    内容
    @Column(columnDefinition = "LONGTEXT", nullable = false)
    @Size(max = 600)
    private String content;

//    社員番号
    @ManyToOne
    @JoinColumn(name = "employee_code", referencedColumnName = "code")
    private Employee employee;

//    削除フラグ
    @Column(columnDefinition="TINYINT", nullable = false)
    private boolean deleteFlg;

//    登録日時
    @Column(nullable = false)
    private LocalDateTime createdAt;

//    更新日時
    @Column(nullable = false)
    private LocalDateTime updatedAt;

}
