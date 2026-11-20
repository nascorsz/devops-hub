-- DevOps Hub - Vitrine de Projetos e Talentos Acadêmicos
-- Banco de dados: SQLite

PRAGMA foreign_keys = ON;

DROP TABLE IF EXISTS applications;
DROP TABLE IF EXISTS opportunities;
DROP TABLE IF EXISTS mentorships;
DROP TABLE IF EXISTS connection_requests;
DROP TABLE IF EXISTS users;

-- Usuários da plataforma (estudante, mentor, empresa e administrador)
CREATE TABLE users (
    id            INTEGER PRIMARY KEY AUTOINCREMENT,
    name          TEXT    NOT NULL,
    email         TEXT    NOT NULL UNIQUE COLLATE NOCASE,
    password_hash TEXT    NOT NULL,
    role          TEXT    NOT NULL CHECK (role IN ('estudante', 'mentor', 'empresa', 'admin')),
    area          TEXT    NOT NULL DEFAULT '',
    skills        TEXT    NOT NULL DEFAULT '',   -- habilidades separadas por vírgula
    bio           TEXT    NOT NULL DEFAULT '',
    created_at    TEXT    NOT NULL DEFAULT (datetime('now'))
);

-- Oportunidades / Projetos Acadêmicos cadastrados pelos squads
CREATE TABLE opportunities (
    id          INTEGER PRIMARY KEY AUTOINCREMENT,
    owner_id    INTEGER NOT NULL,
    title       TEXT    NOT NULL,
    description TEXT    NOT NULL DEFAULT '',
    type        TEXT    NOT NULL DEFAULT 'Projeto UC',
    area        TEXT    NOT NULL DEFAULT 'Tecnologia',
    created_at  TEXT    NOT NULL DEFAULT (datetime('now')),
    FOREIGN KEY (owner_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Candidaturas ou conexões com os projetos
CREATE TABLE applications (
    id               INTEGER PRIMARY KEY AUTOINCREMENT,
    opportunity_id   INTEGER NOT NULL,
    user_id          INTEGER NOT NULL,
    status           TEXT    NOT NULL DEFAULT 'Recebida',
    created_at       TEXT    NOT NULL DEFAULT (datetime('now')),
    FOREIGN KEY (opportunity_id) REFERENCES opportunities(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id)        REFERENCES users(id)        ON DELETE CASCADE,
    UNIQUE (opportunity_id, user_id)
);