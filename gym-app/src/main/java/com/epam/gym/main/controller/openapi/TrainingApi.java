package com.epam.gym.main.controller.openapi;

import com.epam.gym.main.dto.TrainingRequest;
import com.epam.gym.main.dto.TrainingTypeResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Tag(name = "Training")
@SuppressWarnings("unused")
public interface TrainingApi {
    @Operation(summary = "Create a new training session", description = "Adds a new training session to the system.")
    void createTraining(@Valid @RequestBody TrainingRequest request);

    @Operation(summary = "List all training types", description = "Retrieves all available training types.")
    List<TrainingTypeResponse> listAllTrainingTypes();
}
