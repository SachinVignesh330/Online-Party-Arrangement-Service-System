-- ============================================================
-- V1: User Management authentication support
--
-- The original schema only gave `customer` a password column,
-- and had no table at all for the Admin/Owner role, even though
-- the requirements call for login for all four roles (Customer,
-- Admin/Owner, Vendor, Event Coordinator). This migration adds
-- what's needed for a shared authentication model without
-- touching any existing data.
-- ============================================================

ALTER TABLE customer ADD COLUMN IF NOT EXISTS status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE';

ALTER TABLE vendor ADD COLUMN IF NOT EXISTS password VARCHAR(255);
ALTER TABLE vendor ADD COLUMN IF NOT EXISTS created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP;

ALTER TABLE coordinator ADD COLUMN IF NOT EXISTS password VARCHAR(255);
ALTER TABLE coordinator ADD COLUMN IF NOT EXISTS created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP;

CREATE TABLE IF NOT EXISTS admin (
    admin_id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    phone VARCHAR(20),
    password VARCHAR(255) NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Note: password columns on vendor/coordinator are left nullable so this
-- migration never breaks existing seeded rows. The application layer
-- enforces "password required" only at registration time (see
-- RegisterRequest validation), and any pre-existing vendor/coordinator
-- rows will simply be unable to log in until an admin resets their
-- credentials.
