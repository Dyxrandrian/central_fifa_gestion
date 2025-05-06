package com.hei.fifa_gestion_central.DTO;

import com.hei.fifa_gestion_central.enums.PlayerPosition;
import lombok.Data;

import java.util.UUID;
@Data
public class PlayerDTO {
    private UUID id;
    private String name;
    private int number;
    private PlayerPosition position;
    private String nationality;
    private int age;
    private ClubDTO club;
}

