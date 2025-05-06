-- Insertion des championnats
INSERT INTO championship (name) VALUES ('PREMIER_LEAGUE');
INSERT INTO championship (name) VALUES ('LA_LIGA');
INSERT INTO championship (name) VALUES ('BUNDESLIGA');
INSERT INTO championship (name) VALUES ('SERIA');
INSERT INTO championship (name) VALUES ('LIGUE_1');


-- Insertion de joueurs fictifs
INSERT INTO player (name, position, nationality, age) VALUES
                                                          ('Player 1', 'STRIKER', 'English', 28),
                                                          ('Player 2', 'MIDFIELDER', 'Spanish', 24),
                                                          ('Player 3', 'DEFENSE', 'German', 30),
                                                          ('Player 4', 'STRIKER', 'French', 27),
                                                          ('Player 5', 'GOAL_KEEPER', 'Italian', 31),
                                                          ('Player 6', 'MIDFIELDER', 'Brazilian', 22);


-- Insertion du temps de jeu des joueurs
INSERT INTO playing_time (value, duration_unit) VALUES
                                                    (900, 'MINUTE'),  -- 15 heures
                                                    (1200, 'MINUTE'), -- 20 heures
                                                    (1800, 'MINUTE'), -- 30 heures
                                                    (1500, 'MINUTE'), -- 25 heures
                                                    (600, 'MINUTE'),  -- 10 heures
                                                    (1000, 'MINUTE'); -- 16 heures 40 minutes


-- Insertion des classements des joueurs
INSERT INTO player_ranking (player_id, rank, championship_id, scored_goals, playing_time_id, season_year) VALUES
                                                                                                              ((SELECT id FROM player WHERE name = 'Player 1'), 1, (SELECT id FROM championship WHERE name = 'PREMIER_LEAGUE'), 15, (SELECT id FROM playing_time WHERE value = 900), 2024),
                                                                                                              ((SELECT id FROM player WHERE name = 'Player 2'), 2, (SELECT id FROM championship WHERE name = 'PREMIER_LEAGUE'), 12, (SELECT id FROM playing_time WHERE value = 1200), 2024),
                                                                                                              ((SELECT id FROM player WHERE name = 'Player 3'), 3, (SELECT id FROM championship WHERE name = 'LA_LIGA'), 10, (SELECT id FROM playing_time WHERE value = 1800), 2024),
                                                                                                              ((SELECT id FROM player WHERE name = 'Player 4'), 4, (SELECT id FROM championship WHERE name = 'LA_LIGA'), 18, (SELECT id FROM playing_time WHERE value = 1500), 2024),
                                                                                                              ((SELECT id FROM player WHERE name = 'Player 5'), 5, (SELECT id FROM championship WHERE name = 'BUNDESLIGA'), 8, (SELECT id FROM playing_time WHERE value = 600), 2024),
                                                                                                              ((SELECT id FROM player WHERE name = 'Player 6'), 6, (SELECT id FROM championship WHERE name = 'SERIA'), 20, (SELECT id FROM playing_time WHERE value = 1000), 2024);


