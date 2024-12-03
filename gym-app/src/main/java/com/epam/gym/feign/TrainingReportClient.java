package com.epam.gym.feign;

import com.epam.gym.trainingreport.dto.TrainerWorkloadRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "training-report")
public interface TrainingReportClient {
    @PostMapping("/api/v1/workload/report")
    void handleTraining(@RequestBody TrainerWorkloadRequest request);
}
