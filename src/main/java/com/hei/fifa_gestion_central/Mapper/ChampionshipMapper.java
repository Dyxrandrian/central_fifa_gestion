package com.hei.fifa_gestion_central.Mapper;

import com.hei.fifa_gestion_central.enums.Championship;

import java.util.Map;
import java.util.UUID;

public class ChampionshipMapper {
        private static final Map<UUID, Championship> ID_TO_ENUM = Map.of(
                UUID.fromString("..."), Championship.PREMIER_LEAGUE,
                UUID.fromString("..."), Championship.LA_LIGA,
                UUID.fromString("..."), Championship.BUNDESLIGA,
                UUID.fromString("..."), Championship.SERIA,
                UUID.fromString("..."), Championship.LIGUE_1
        );

        public static Championship mapToEnum(UUID id) {
            return ID_TO_ENUM.get(id);
        }

}
