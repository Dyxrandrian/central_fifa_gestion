DROP DATABASE IF EXISTS central_fifa_gestion;

CREATE DATABASE central_fifa_gestion;

\c central_fifa_gestion;

-- Table des championnats (e.g., Premier League, La Liga, etc.)
CREATE TABLE championship (
                              id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                              name TEXT NOT NULL CHECK (name IN ('PREMIER_LEAGUE', 'LA_LIGA', 'BUNDESLIGA', 'SERIA', 'LIGUE_1'))
);

-- Table pour les classements des clubs dans un championnat
CREATE TABLE club_ranking (
                              id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                              championship_id UUID REFERENCES championship(id),
                              club_id UUID,
                              rank INTEGER,
                              ranking_points INTEGER,
                              scored_goals INTEGER,
                              conceded_goals INTEGER,
                              difference_goals INTEGER,
                              clean_sheet_number INTEGER,
                              season_year INTEGER
);

-- Table pour les classements des joueurs dans un championnat
CREATE TABLE player_ranking (
                                id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                                player_id UUID,
                                rank INTEGER,
                                championship_id UUID REFERENCES championship(id),
                                scored_goals INTEGER,
                                playing_time_id UUID,
                                season_year INTEGER
);

-- Table pour les statistiques de match
CREATE TABLE match_score (
                             id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                             match_id UUID,
                             home_score INTEGER,
                             away_score INTEGER
);

-- Table pour les positions des joueurs (relié à PlayerPosition dans OpenAPI)
CREATE TYPE player_position AS ENUM ('STRIKER', 'MIDFIELDER', 'DEFENSE', 'GOAL_KEEPER');

-- Table pour le temps de jeu des joueurs
CREATE TABLE playing_time (
                              id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                              value DOUBLE PRECISION,
                              duration_unit TEXT CHECK (duration_unit IN ('SECOND', 'MINUTE', 'HOUR'))
);

-- Table pour les joueurs
CREATE TABLE player (
                        id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                        name TEXT NOT NULL,
                        position TEXT CHECK (position IN ('STRIKER', 'MIDFIELDER', 'DEFENSE', 'GOAL_KEEPER')),
                        nationality TEXT NOT NULL,
                        age INTEGER,
                        club_id UUID REFERENCES club(id)
);

-- Table pour les clubs (référence vers le club du championnat)
CREATE TABLE club (
                      id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                      name TEXT NOT NULL,
                      acronym TEXT,
                      year_creation INTEGER,
                      stadium TEXT
);
