package com.epam.gym.trainingreport.controller.openapi;

import com.epam.gym.trainingreport.dto.TrainerWorkloadRequest;
import com.epam.gym.trainingreport.dto.TrainerWorkloadResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Trainer Workload", description = "API for managing trainer workloads")
@SuppressWarnings("unused")
public interface TrainerWorkloadApi {

    @Operation(summary = "Handle a new training report",
            description = "Process training data by adding or deleting training details.")
    void createTrainerWorkload(TrainerWorkloadRequest request);

    @Operation(summary = "Get Trainer Summary", description = "Fetches training summary for a specified trainer.")
    TrainerWorkloadResponse getTrainerWorkload(@Parameter(example = "Super.Trainer") String username);
}
