package com.example.tutorialscrud.controllers;

import com.example.tutorialscrud.dtos.TutorialRecordDto;
import com.example.tutorialscrud.models.Tutorial;
import com.example.tutorialscrud.repositories.TutorialRepository;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@CrossOrigin
@RestController
@RequestMapping("/api")
public class TurorialController {

    @Autowired
    TutorialRepository tutorialRepository;

    @GetMapping("/tutorials")
    public ResponseEntity<Map<String, Object>> getAllTutorials(
            @RequestParam(required = false) String title,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "3") int size)
    {
        try {
            List<Tutorial> tutorials = new ArrayList<Tutorial>();
            Pageable paging = PageRequest.of(page, size);

            Page<Tutorial> pageTutorials;
            if (title == null)
                pageTutorials = tutorialRepository.findAll(paging);
            else
                pageTutorials = tutorialRepository.findByTitleContaining(title, paging);

            tutorials = pageTutorials.getContent();

            Map<String, Object> response = new HashMap<>();
            response.put("tutorials", tutorials);
            response.put("currentPage", pageTutorials.getNumber());
            response.put("totalItems", pageTutorials.getTotalElements());
            response.put("totalPages", pageTutorials.getTotalPages());

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
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
    public ResponseEntity<Map<String, Object>> findByPublished(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "3") int size
    ) {
        try {
            List<Tutorial> tutorials = new ArrayList<Tutorial>();
            Pageable paging = PageRequest.of(page, size);

            Page<Tutorial> pageTutorials = tutorialRepository.findByPublished(true, paging);
            tutorials = pageTutorials.getContent();

            Map<String, Object> response = new HashMap<>();
            response.put("tutorials", tutorials);
            response.put("currentPage", pageTutorials.getNumber());
            response.put("totalItems", pageTutorials.getTotalElements());
            response.put("totalPages", pageTutorials.getTotalPages());

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
