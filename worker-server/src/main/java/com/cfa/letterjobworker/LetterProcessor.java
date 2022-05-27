package com.cfa.letterjobworker;

import com.cfa.objects.letter.Letter;

import org.springframework.batch.item.ItemProcessor;
import java.util.Date;

public class LetterProcessor implements ItemProcessor<String, Letter> {


    @Override
    public Letter process(String s) throws Exception {

        Letter l = new Letter();
        l.setCreationDate(new Date());
        l.setMessage(s);
        l.setTreatmentDate(new Date());

        return l;
    }

}
