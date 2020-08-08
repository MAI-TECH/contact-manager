package com.maitech.models;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.validation.annotation.Validated;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Validated
@Entity
@Table(name = "contacts")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Data @NoArgsConstructor @ToString @EqualsAndHashCode
public class ContactModel implements Serializable {

    private static final long serialVersionUID = 4048798961366546485L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 100)
    private String name;

    @Pattern(regexp ="^\\+?[0-9. ()-]{7,25}$", message = "Phone number")
    @Size(max = 25)
    private String phone;

    @Email(message = "Email Address")
    @Size(max = 100)
    private String email;

    @Size(max = 50)
    private String address1;

    @Size(max = 50)
    private String address2;

    @Size(max = 50)
    private String address3;

    @Size(max = 50)
    private String postalCode;

    @Column(length = 4000)
    private String note;
}
