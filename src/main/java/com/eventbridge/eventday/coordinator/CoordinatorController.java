package com.eventbridge.eventday.coordinator;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/coordinators")
public class CoordinatorController {

    private final CoordinatorRepository coordinatorRepository;

    public CoordinatorController(CoordinatorRepository coordinatorRepository) {
        this.coordinatorRepository = coordinatorRepository;
    }

    @GetMapping
    public List<Coordinator> getAll() {
        return coordinatorRepository.findAll();
    }

    @PostMapping
    public Coordinator create(@RequestBody Coordinator coordinator) {
        return coordinatorRepository.save(coordinator);
    }

    @GetMapping("/{id}")
    public Coordinator getOne(@PathVariable Integer id) {
        return coordinatorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Coordinator not found"));
    }
}
