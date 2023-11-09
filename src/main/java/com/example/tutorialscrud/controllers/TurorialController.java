package com.example.tutorialscrud.controllers;

import com.example.tutorialscrud.dtos.TutorialRecordDto;
import com.example.tutorialscrud.models.Tutorial;
import com.example.tutorialscrud.repositories.TutorialRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Tag(name="Tutorial", description = "Tutorial management API")
@CrossOrigin
@RestController
@RequestMapping("/api")
public class TurorialController {

    @Autowired
    TutorialRepository tutorialRepository;

    @Operation(
            summary = "Retrieve all tutorials from a specified data range.",
            description = "Get a list of tutorial objects, the items are separated by pages and can by filtered by title, " +
                    "if no parameter is passed, it will return the first three tutorials of the dataset by default.",
            tags = {"Tutorials", "Get"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = Tutorial.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "204", description = "There are no tutorials.", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "500", content = {@Content(schema = @Schema())})
    })
    @GetMapping("/tutorials")
    public ResponseEntity<Map<String, Object>> getAllTutorials(
            @Parameter(description = "Search tutorials by title") @RequestParam(required = false) String title,
            @Parameter(description = "Page number, starting from 0", required = true)@RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Number of tutorials per page", required = true) @RequestParam(defaultValue = "3") int size)
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


    @Operation(
            summary = "Retrieve a tutorial by Id.",
            description = "Get the tutorial object of specified ID. The object contains Id, title, description and published status.",
            tags = {"Tutorials", "Get", "Id"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = Tutorial.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "500", content = {@Content(schema = @Schema())})
    })
    @GetMapping("/tutorials/{id}")
    public ResponseEntity<Object> getTutorialById(@PathVariable("id")long id){
        Optional<Tutorial> tutorial = tutorialRepository.findById(id);
        if(tutorial.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Tutorial not found.");
        }
        return ResponseEntity.status(HttpStatus.OK).body(tutorial.get());
    }

    @Operation(
            summary = "Create a tutorial.",
            tags = {"Tutorials", "Post"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", content = {@Content(schema = @Schema(implementation = Tutorial.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "500", content = {@Content(schema = @Schema())})
    })
    @PostMapping("/tutorials")
    public ResponseEntity<Tutorial> createTutorial(@RequestBody @Valid TutorialRecordDto tutorialRecordDto){
        var tutorial = new Tutorial();
        BeanUtils.copyProperties(tutorialRecordDto, tutorial);
        return ResponseEntity.status(HttpStatus.CREATED).body(tutorialRepository.save(tutorial));
    }

    @Operation(
            summary = "Updates the tutorial of given Id.",
            tags = {"Tutorials", "Put"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = Tutorial.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "500", content = {@Content(schema = @Schema())}),
    })
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

    @Operation(
            summary = "Deletes a tutorial by Id.",
            tags = {"Tutorials", "Delete", "Id"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "500", content = {@Content(schema = @Schema())}),
    })
    @DeleteMapping("/tutorials/{id}")
    public ResponseEntity<Object> deleteTutorial(@PathVariable("id")long id){
        Optional<Tutorial> tutorial = tutorialRepository.findById(id);
        if(tutorial.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Tutorial not found.");
        }
        tutorialRepository.delete(tutorial.get());
        return ResponseEntity.status(HttpStatus.OK).body("Tutorial deleted successfully.");
    }

    @Operation(
            summary = "Deletes all tutorials.",
            tags = {"Tutorials", "Delete"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "500", content = {@Content(schema = @Schema())}),
    })
    @DeleteMapping("/tutorials")
    public ResponseEntity<Object> deleteAllTutorials(){
        tutorialRepository.deleteAll();
        return ResponseEntity.status(HttpStatus.OK).body("Tutorials deleted successfully.");
    }
    @Operation(
            summary = "Retrieve all published tutorials.",
            tags = {"Tutorials", "Get", "Filter"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "500", content = {@Content(schema = @Schema())}),
    })
    @GetMapping("/tutorials/published")
    public ResponseEntity<Map<String, Object>> findByPublished(
           @Parameter(description = "Page number, starting from 0", required = true) @RequestParam(defaultValue = "0") int page,
           @Parameter(description = "Number of tutorials per page", required = true) @RequestParam(defaultValue = "3") int size
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
