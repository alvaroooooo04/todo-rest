-- =============================================================================
-- DATOS DE EJEMPLO - se cargan al arrancar (ddl-auto=update / create)
-- Passwords codificados con BCrypt. Texto plano: "1234" para todos.
-- =============================================================================

-- ---------- USUARIOS ----------
-- admin / 1234
INSERT INTO user_entity (id, username, email, password, role, fullname)
VALUES (NEXTVAL('user_entity_seq'), 'admin', 'admin@laboral.es',
        '{bcrypt}$2a$10$wVN8OoaSnIQc6Tcq5acqQuwhYwFqEZ6wQyMOFmaTfa7nXTaYBSPnG',
        'ADMIN', 'Administrador del sistema');

-- gestor / 1234
INSERT INTO user_entity (id, username, email, password, role, fullname)
VALUES (NEXTVAL('user_entity_seq'), 'gestor', 'gestor@laboral.es',
        '{bcrypt}$2a$10$wVN8OoaSnIQc6Tcq5acqQuwhYwFqEZ6wQyMOFmaTfa7nXTaYBSPnG',
        'GESTOR', 'Gestor de categorias');

-- lorena / 1234
INSERT INTO user_entity (id, username, email, password, role, fullname)
VALUES (NEXTVAL('user_entity_seq'), 'lorena', 'lorena@laboral.es',
        '{bcrypt}$2a$10$wVN8OoaSnIQc6Tcq5acqQuwhYwFqEZ6wQyMOFmaTfa7nXTaYBSPnG',
        'USER', 'Lorena Diaz');

-- ---------- CATEGORIAS ----------
INSERT INTO category (id, title) VALUES (NEXTVAL('category_seq'), 'Trabajo');
INSERT INTO category (id, title) VALUES (NEXTVAL('category_seq'), 'Personal');
INSERT INTO category (id, title) VALUES (NEXTVAL('category_seq'), 'Estudios');
INSERT INTO category (id, title) VALUES (NEXTVAL('category_seq'), 'Casa');

-- ---------- TAGS ----------
INSERT INTO tag (id, name) VALUES (NEXTVAL('tag_seq'), 'urgente');
INSERT INTO tag (id, name) VALUES (NEXTVAL('tag_seq'), 'reunion');
INSERT INTO tag (id, name) VALUES (NEXTVAL('tag_seq'), 'compras');
INSERT INTO tag (id, name) VALUES (NEXTVAL('tag_seq'), 'salud');

-- ---------- TAREAS de lorena (id=3) ----------
INSERT INTO task (id, created_at, deadline, title, description, completed, priority, author_id, category_id)
VALUES (NEXTVAL('task_seq'), CURRENT_TIMESTAMP, TIMESTAMPADD(DAY, 7, CURRENT_TIMESTAMP),
        'Comprar alimentos', 'Hacer la lista de la compra semanal.', false, 'MEDIA', 3, 2);

INSERT INTO task (id, created_at, deadline, title, description, completed, priority, author_id, category_id)
VALUES (NEXTVAL('task_seq'), CURRENT_TIMESTAMP, TIMESTAMPADD(DAY, 2, CURRENT_TIMESTAMP),
        'Pagar facturas', 'Factura de electricidad antes de la fecha limite.', false, 'ALTA', 3, 1);

INSERT INTO task (id, created_at, deadline, title, description, completed, priority, author_id, category_id)
VALUES (NEXTVAL('task_seq'), CURRENT_TIMESTAMP, TIMESTAMPADD(DAY, 14, CURRENT_TIMESTAMP),
        'Estudiar examen DWES', 'Repasar Spring Security y Spring Data JPA.', false, 'ALTA', 3, 3);

INSERT INTO task (id, created_at, deadline, title, description, completed, priority, author_id, category_id)
VALUES (NEXTVAL('task_seq'), CURRENT_TIMESTAMP, TIMESTAMPADD(HOUR, 12, CURRENT_TIMESTAMP),
        'Reunion con el equipo', 'Preparar puntos de la reunion semanal.', false, 'MEDIA', 3, 1);

INSERT INTO task (id, created_at, deadline, title, description, completed, priority, author_id, category_id)
VALUES (NEXTVAL('task_seq'), CURRENT_TIMESTAMP, TIMESTAMPADD(DAY, 5, CURRENT_TIMESTAMP),
        'Llamar al medico', 'Cita anual para revision.', true, 'BAJA', 3, 2);

-- ---------- ASOCIACION TAGS <-> TAREAS ----------
INSERT INTO task_tag (task_id, tag_id) VALUES (1, 3);  -- comprar alimentos -> compras
INSERT INTO task_tag (task_id, tag_id) VALUES (2, 1);  -- pagar facturas -> urgente
INSERT INTO task_tag (task_id, tag_id) VALUES (3, 1);  -- estudiar examen -> urgente
INSERT INTO task_tag (task_id, tag_id) VALUES (4, 2);  -- reunion -> reunion
INSERT INTO task_tag (task_id, tag_id) VALUES (5, 4);  -- llamar al medico -> salud
