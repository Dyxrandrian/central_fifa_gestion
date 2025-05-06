/*package com.hei.fifa_gestion_central.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/syncrhonization")
public class SynchronizationController {
    private final Synchr synchronizationService;

    public SynchronizationController(SynchronizationService synchronizationService) {
        this.synchronizationService = synchronizationService;
    }

    @PostMapping
    public ResponseEntity<Void> synchronizeChampionshipData() {
        synchronizationService.synchronize();
        return ResponseEntity.ok().build();
    }
}*/
