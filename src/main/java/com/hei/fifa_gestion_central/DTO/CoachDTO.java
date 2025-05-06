package com.hei.fifa_gestion_central.DTO;


import lombok.Data;

import java.util.UUID;
@Data
public class CoachDTO {
    private UUID id;
    private String name;
    private String nationality;
}
