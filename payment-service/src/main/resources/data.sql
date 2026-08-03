-- Insertion des règles tarifaires initiales (Lignes de bus Abu Dhabi Transport simulation)
INSERT INTO fare_rules (line_id, line_name, passenger_type, base_fare, active) VALUES
('LINE_01', 'Ligne 01 - Express Centre Ville - Aéroport', 'REGULAR', 500.00, true),
('LINE_01', 'Ligne 01 - Express Centre Ville - Aéroport', 'STUDENT', 250.00, true),
('LINE_01', 'Ligne 01 - Express Centre Ville - Aéroport', 'SENIOR', 200.00, true),
('LINE_02', 'Ligne 02 - Corniche & Université', 'REGULAR', 350.00, true),
('LINE_02', 'Ligne 02 - Corniche & Université', 'STUDENT', 150.00, true),
('LINE_02', 'Ligne 02 - Corniche & Université', 'SENIOR', 100.00, true),
('LINE_03', 'Ligne 03 - Périphérique & Zone Industrielle', 'REGULAR', 400.00, true);

-- Insertion de portefeuilles utilisateurs de test (Solde initial positif pour USR_1001)
INSERT INTO wallets (user_id, card_id, balance, currency, status) VALUES
('USR_1001', 'CARD_NFC_883921', 5000.00, 'XOF', 'ACTIVE'),
('USR_1002', 'CARD_NFC_441209', 2000.00, 'XOF', 'ACTIVE'),
('USR_1003', 'CARD_NFC_772183', 1000.00, 'XOF', 'ACTIVE');

-- Historique initial des transactions de démonstration
INSERT INTO payment_transactions (transaction_ref, user_id, card_id, type, amount, balance_after, currency, status, payment_method, ride_id, line_id, bus_id, description, timestamp) VALUES
('TX_WAVE_9981273', 'USR_1001', 'CARD_NFC_883921', 'RECHARGE_WAVE', 5000.00, 5000.00, 'XOF', 'SUCCESS', 'WAVE', NULL, NULL, NULL, 'Recharge via Wave Mobile Money (+221)', CURRENT_TIMESTAMP),
('TX_WAVE_4481920', 'USR_1002', 'CARD_NFC_441209', 'RECHARGE_WAVE', 2000.00, 2000.00, 'XOF', 'SUCCESS', 'WAVE', NULL, NULL, NULL, 'Recharge via Wave Mobile Money (+221)', CURRENT_TIMESTAMP);

-- Alertes de fraude de démonstration
INSERT INTO fraud_alerts (user_id, card_id, fraud_type, risk_level, description, timestamp, resolved) VALUES
('USR_1003', 'CARD_NFC_772183', 'INSUFFICIENT_FUNDS_REPEATED', 'MEDIUM', 'Tentative de validation de trajet avec solde insuffisant', CURRENT_TIMESTAMP, false);
