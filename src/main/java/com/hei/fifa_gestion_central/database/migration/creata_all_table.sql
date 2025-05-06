-- Supprime la base si elle existe
DROP DATABASE IF EXISTS central_fifa_gestion;

-- Crée la base
CREATE DATABASE central_fifa_gestion;

-- Connexion à la nouvelle base
\c central_fifa_gestion;

-- Définition des types ENUM
CREATE TYPE player_position AS ENUM ('STRIKER', 'MIDFIELDER', 'DEFENSE', 'GOAL_KEEPER');
CREATE TYPE duration_unit AS ENUM ('SECOND', 'MINUTE', 'HOUR');
CREATE TYPE championship_name AS ENUM ('PREMIER_LEAGUE', 'LA_LIGA', 'BUNDESLIGA', 'SERIA', 'LIGUE_1');

-- Table des championnats
CREATE TABLE championship (
                              id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                              name championship_name NOT NULL
);

-- Table des entraîneurs
CREATE TABLE coach (
                       id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                       name TEXT NOT NULL,
                       nationality TEXT NOT NULL
);

-- Table des clubs
CREATE TABLE club (
                      id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                      name TEXT NOT NULL,
                      acronym TEXT,
                      year_creation INTEGER,
                      stadium TEXT,
                      coach_id UUID UNIQUE REFERENCES coach(id)
);

-- Table des joueurs
CREATE TABLE player (
                        id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                        name TEXT NOT NULL,
                        number INTEGER NOT NULL,
                        position player_position NOT NULL,
                        nationality TEXT NOT NULL,
                        age INTEGER,
                        club_id UUID REFERENCES club(id)
);

-- Table des temps de jeu
CREATE TABLE playing_time (
                              id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                              value DOUBLE PRECISION NOT NULL,
                              duration_unit duration_unit NOT NULL
);

-- Table des classements de clubs dans un championnat
CREATE TABLE club_ranking (
                              id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                              championship_id UUID NOT NULL REFERENCES championship(id),
                              club_id UUID NOT NULL REFERENCES club(id),
                              rank INTEGER DEFAULT 0,
                              ranking_points INTEGER NOT NULL,
                              scored_goals INTEGER NOT NULL,
                              conceded_goals INTEGER NOT NULL,
                              difference_goals INTEGER NOT NULL,
                              clean_sheet_number INTEGER NOT NULL,
                              season_year INTEGER NOT NULL,
                              UNIQUE (championship_id, club_id, season_year)
);

-- Table des classements des joueurs dans un championnat
CREATE TABLE player_ranking (
                                id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                                player_id UUID UNIQUE REFERENCES player(id),
                                rank INTEGER,
                                championship_id UUID REFERENCES championship(id),
                                scored_goals INTEGER,
                                playing_time_id UUID REFERENCES playing_time(id),
                                season_year INTEGER
);

-- Table des scores de match
CREATE TABLE match_score (
                             id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                             match_id UUID,
                             home_score INTEGER,
                             away_score INTEGER
);
