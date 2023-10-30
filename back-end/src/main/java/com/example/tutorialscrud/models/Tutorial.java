package com.example.tutorialscrud.models;

import jakarta.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "tutorials-table")
public class Tutorial implements Serializable {
    private static final long serialVersionUID  = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long idTutorial;
    private String title;
    private String description;
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
