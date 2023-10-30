package com.example.tutorialscrud.controllers;

import com.example.tutorialscrud.dtos.TutorialRecordDto;
import com.example.tutorialscrud.models.Tutorial;
import com.example.tutorialscrud.repositories.TutorialRepository;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping("/api")
public class TurorialController {

    @Autowired
    TutorialRepository tutorialRepository;

    @GetMapping("/tutorials")
    public ResponseEntity<List<Tutorial>> getAllTutorial(@RequestParam(required = false)String title){
        List<Tutorial> tutorials = new ArrayList<Tutorial>();

        if (title == null)
            tutorialRepository.findAll().forEach(tutorials::add);
        else
            tutorialRepository.findByTitleContaining(title).forEach(tutorials::add);

        if (tutorials.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(tutorials, HttpStatus.OK);
    }

    @GetMapping("/tutorials/{id}")
    public ResponseEntity<Object> getTutorialById(@PathVariable("id")long id){
        Optional<Tutorial> tutorial = tutorialRepository.findById(id);
        if(tutorial.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Tutorial not found.");
        }
        return ResponseEntity.status(HttpStatus.OK).body(tutorial.get());
    }

    @PostMapping("/tutorials")
    public ResponseEntity<Tutorial> createTutorial(@RequestBody @Valid TutorialRecordDto tutorialRecordDto){
        var tutorial = new Tutorial();
        BeanUtils.copyProperties(tutorialRecordDto, tutorial);
        return ResponseEntity.status(HttpStatus.CREATED).body(tutorialRepository.save(tutorial));
    }

    @PutMapping("/tutorials/{id}")
    public ResponseEntity<Object> updateTutorial(@PathVariable("id")long id, @RequestBody TutorialRecordDto tutorialRecordDto){
        Optional<Tutorial> tutorialO = tutorialRepository.findById(id);
        if(tutorialO.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Tutorial not found.");
        }
        var tutorialUpdate = tutorialO.get();
        BeanUtils.copyProperties(tutorialRecordDto, tutorialUpdate);
        return ResponseEntity.status(HttpStatus.OK).body(tutorialRepository.save(tutorialUpdate));
    }

    @DeleteMapping("/tutorials/{id}")
    public ResponseEntity<Object> deleteTutorial(@PathVariable("id")long id){
        Optional<Tutorial> tutorial = tutorialRepository.findById(id);
        if(tutorial.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Tutorial not found.");
        }
        tutorialRepository.delete(tutorial.get());
        return ResponseEntity.status(HttpStatus.OK).body("Tutorial deleted successfully.");
    }

    @DeleteMapping("/tutorials")
    public ResponseEntity<Object> deleteAllTutorials(){
        tutorialRepository.deleteAll();
        return ResponseEntity.status(HttpStatus.OK).body("Tutorials deleted successfully.");
    }

    @GetMapping("/tutorials/published")
    public ResponseEntity<List<Tutorial>> findByPublished(){
        List<Tutorial> tutorials = tutorialRepository.findByPublished(true);
        if(tutorials.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(tutorials);
        }
        return ResponseEntity.status(HttpStatus.OK).body(tutorials);
    }

}
