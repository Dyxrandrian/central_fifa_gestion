package com.hei.fifa_gestion_central.DTO;

import lombok.Data;

import java.util.UUID;
@Data
public class ClubDTO {
    private UUID id;
    private String name;
    private String acronym;
    private int yearCreation;
    private String stadium;
    private CoachDTO coach;

}

