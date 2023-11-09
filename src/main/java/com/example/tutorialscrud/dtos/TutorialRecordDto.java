package com.example.tutorialscrud.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record TutorialRecordDto(@NotBlank String title, @NotNull String description, @NotNull boolean published) {
}
