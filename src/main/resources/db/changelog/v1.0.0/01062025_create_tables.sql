CREATE SCHEMA IF NOT EXISTS account;

-- Таблица ролей
CREATE TABLE IF NOT EXISTS account.roles (
    id UUID PRIMARY KEY,
    type VARCHAR(50) NOT NULL UNIQUE
);

-- Таблица аккаунтов
CREATE TABLE IF NOT EXISTS account.accounts (
    id UUID PRIMARY KEY,
    email VARCHAR(50) NOT NULL UNIQUE,
    password_hash VARCHAR(100) NOT NULL,
    role_id UUID REFERENCES account.roles(id) ON DELETE SET NULL
);

-- Таблица понравившихся релизов
CREATE TABLE IF NOT EXISTS account.liked_releases (
    account_id UUID NOT NULL REFERENCES account.accounts(id) ON DELETE CASCADE,
    release_id UUID NOT NULL,
    PRIMARY KEY (account_id, release_id)
);

-- Таблица понравившихся треков
CREATE TABLE IF NOT EXISTS account.liked_tracks (
    account_id UUID NOT NULL REFERENCES account.accounts(id) ON DELETE CASCADE,
    track_id UUID NOT NULL,
    PRIMARY KEY (account_id, track_id)
);