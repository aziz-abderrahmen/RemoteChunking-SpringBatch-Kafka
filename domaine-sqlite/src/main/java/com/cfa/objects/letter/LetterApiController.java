package com.cfa.objects.letter;

import io.micrometer.core.instrument.util.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(value = "/letter")
public class LetterApiController {
    @Autowired
    private LetterRepository _repo;
    @GetMapping("/getAll")
    public List<Letter> getAll() {
        return _repo.findAll();
    }


    @PostMapping("/newLetter")
    public Letter createLetter(@RequestBody String msg){
        Letter s = new Letter();
        s.setMessage( msg);
        s.setCreationDate(new Date());
        s.setTreatmentDate(new Date());

        return _repo.save(s);
    }



}

