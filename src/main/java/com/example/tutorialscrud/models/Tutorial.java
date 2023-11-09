package com.example.tutorialscrud.models;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "tutorials-table")
@Schema(description = "Tutorial model")
public class Tutorial implements Serializable {
    private static final long serialVersionUID  = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Schema(accessMode = Schema.AccessMode.READ_ONLY, description = "Tutorial ID", example = "10")
    private long idTutorial;
    @Schema(description = "Tutorial title", example = "Spring Back-end Example")
    private String title;
    @Schema(description = "Tutorial description", example = "An CRUD example with Spring.")
    private String description;
    @Schema(description = "Tutorial published status", example = "false")
    private boolean published;

    public long getIdTutorial() {
        return idTutorial;
    }

    public void setIdTutorial(long idTutorial) {
        this.idTutorial = idTutorial;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isPublished() {
        return published;
    }

    public void setPublished(boolean published) {
        this.published = published;
    }
}
