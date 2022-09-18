package com.openclassrooms.PayMyBuddy.model;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
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

    @NotBlank(message = "L''email ne peut pas être vide.")
    @Pattern(regexp = EmailAddress.EMAIL_ADDRESS_PATTERN, message = EmailAddress.EMAIL_INVALID_MESSAGE)
    private String email;
    private String password;

    @Column(name = "date_of_creation")
    private Timestamp date_of_creation;

    @Column(name = "last_online_time")
    private Timestamp lastOnlineTime;

    private double balance ;

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
