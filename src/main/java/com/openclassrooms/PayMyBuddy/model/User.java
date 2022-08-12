package com.openclassrooms.PayMyBuddy.model;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@Data
@ToString(exclude = { "bankAccounts" })
@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    private String email;
    private String password;

    @Column(name = "date_of_creation")
    private Timestamp date_of_creation;

    @Column(name = "last_online_time")
    private Timestamp lastOnlineTime;

    private float balance ;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    private String address;

    private String phone;

    // TODO à suppr
    // créer DTO, pas de model "persistant" dans la couche controller
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private List<BankAccount> bankAccounts;

}
