package com.cfa.objects.letter;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Date;

@Repository

public interface LetterRepository extends JpaRepository <Letter, Integer> {

    Letter getLetterByCreationDate(final Date creation_date);
    Letter getLetterByTreatmentDate(final Date treatment_date);

}
