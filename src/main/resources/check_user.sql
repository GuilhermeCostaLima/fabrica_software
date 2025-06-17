-- Verificar usuários
SELECT id, username, email, password, full_name FROM users;

-- Verificar roles
SELECT * FROM roles;

-- Verificar associações user_roles
SELECT u.email, r.name as role_name
FROM users u
JOIN user_roles ur ON u.id = ur.user_id
JOIN roles r ON r.id = ur.role_id; 