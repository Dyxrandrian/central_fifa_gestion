package com.hei.fifa_gestion_central.entity;

import com.hei.fifa_gestion_central.enums.DurationUnit;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode
public class PlayingTime {
    private double value;
    private DurationUnit durationUnit;
}
