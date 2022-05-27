package com.cfa.objects.letter;

import com.cfa.objects.person.Person;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "letter")
@Getter
@Setter
@ToString
@NoArgsConstructor

public class Letter implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "message")
    private String message;

    @Column(name = "creationDate")
    private Date creationDate;

    @Column(name = "treatmentDate")
    private Date treatmentDate;

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || Hibernate.getClass(this) != Hibernate.getClass(obj)) {
            return false;
        }
        final Letter letter = (Letter) obj;
        return id != null && Objects.equals(id, letter.id);
    }
}
