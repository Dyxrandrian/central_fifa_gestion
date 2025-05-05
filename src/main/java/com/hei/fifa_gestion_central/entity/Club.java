package com.hei.fifa_gestion_central.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode
public class Club {
    private String id;
    private String name;
    private String acronym;
    private Integer yearCreation;
    private String stadium;
    private Coach coach;
}
