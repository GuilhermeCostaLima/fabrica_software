-- Limpeza de dados com ordem correta para evitar erro de FK
DELETE FROM payments;
DELETE FROM reservations;
DELETE FROM user_roles;
DELETE FROM room_amenities;
DELETE FROM hotel_amenities;
DELETE FROM rooms;
DELETE FROM hotels;
DELETE FROM addresses;
DELETE FROM users WHERE email IN ('admin@123km.com');
DELETE FROM roles;

-- Inserir roles
INSERT INTO roles (name) VALUES ('ROLE_USER');
INSERT INTO roles (name) VALUES ('ROLE_ADMIN');

-- Inserir usuário admin (senha: 123456)
INSERT INTO users (username, email, password, full_name)
VALUES (
    'admin',
    'admin@123km.com',
    '$2a$10$rJoOYTeehRm6Gy.74nlUXeMdQWB00ILCKTNxcnyPu3B7lFr97IAy.', -- 123456 (BCrypt)
    'Administrador'
);

-- Relacionar admin com ROLE_ADMIN
INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id
FROM users u, roles r
WHERE u.email = 'admin@123km.com' AND r.name = 'ROLE_ADMIN';

-- Endereço do hotel
INSERT INTO addresses (street, number, neighborhood, city, state, country, zip_code)
VALUES ('Avenida Atlântica', '1702', 'Copacabana', 'Rio de Janeiro', 'RJ', 'Brasil', '22021-001');

-- Inserir hotel
INSERT INTO hotels (name, description, stars, address_id, phone_number, email, average_rating, active, featured)
VALUES (
    'Hotel Copacabana Palace',
    'O Copacabana Palace é um dos hotéis mais luxuosos e tradicionais do Rio de Janeiro. Localizado em frente à praia de Copacabana, oferece uma experiência única com serviço de primeira classe.',
    5,
    (SELECT id FROM addresses WHERE street = 'Avenida Atlântica' AND number = '1702'),
    '(21) 2548-7070',
    'reservas@copacabanapalace.com',
    4.8,
    true,
    true
);

-- Comodidades do hotel
INSERT INTO hotel_amenities (hotel_id, amenity)
SELECT id, amenity
FROM hotels h,
UNNEST(ARRAY[
    'Piscina', 'Academia', 'Spa', 'Restaurante',
    'Bar', 'Wi-Fi', 'Estacionamento', 'Serviço de Quarto 24h'
]) amenity
WHERE h.name = 'Hotel Copacabana Palace';

-- Inserir quartos
INSERT INTO rooms (hotel_id, number, type, capacity, daily_rate, description, active)
VALUES
    ((SELECT id FROM hotels WHERE name = 'Hotel Copacabana Palace'), '101', 'Standard', 2, 500.00, 'Quarto Standard com vista para a cidade', true),
    ((SELECT id FROM hotels WHERE name = 'Hotel Copacabana Palace'), '201', 'Luxo', 2, 800.00, 'Quarto Luxo com vista parcial para o mar', true),
    ((SELECT id FROM hotels WHERE name = 'Hotel Copacabana Palace'), '301', 'Suite', 3, 1200.00, 'Suite com vista para o mar', true),
    ((SELECT id FROM hotels WHERE name = 'Hotel Copacabana Palace'), '401', 'Suite Presidencial', 4, 2500.00, 'Suite Presidencial com vista panorâmica', true);

-- Comodidades dos quartos
INSERT INTO room_amenities (room_id, amenity)
SELECT r.id, amenity
FROM rooms r,
UNNEST(ARRAY['TV LCD', 'Ar Condicionado', 'Frigobar', 'Cofre']) amenity
WHERE r.hotel_id = (SELECT id FROM hotels WHERE name = 'Hotel Copacabana Palace');
