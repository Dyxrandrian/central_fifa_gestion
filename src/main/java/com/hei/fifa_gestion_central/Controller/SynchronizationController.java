package com.hei.fifa_gestion_central.Controller;

import com.hei.fifa_gestion_central.Service.SynchronizationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/syncrhonization")
@CrossOrigin(origins = "http://localhost:8080")
public class SynchronizationController {
    private final SynchronizationService synchronizationService;

    public SynchronizationController(SynchronizationService synchronizationService) {
        this.synchronizationService = synchronizationService;
    }

    @PostMapping
    public ResponseEntity<Void> synchronizeChampionshipData() {
        synchronizationService.synchronize();
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public String str (){
        return "pong";
    }
}
