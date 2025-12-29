-- Enable UUID generation
CREATE EXTENSION IF NOT EXISTS pgcrypto;

-- Create table
CREATE TABLE IF NOT EXISTS patient (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    address VARCHAR(255) NOT NULL,
    date_of_birth DATE NOT NULL,
    registration_date DATE NOT NULL
);

-- Insert dummy records if they don't exist
INSERT INTO patient (id, name, email, address, date_of_birth, registration_date)
SELECT gen_random_uuid(), 'Alice Johnson', 'alice@example.com', '123 Main St, Springfield',
       DATE '1990-05-14', CURRENT_DATE
WHERE NOT EXISTS (SELECT 1 FROM patient WHERE email = 'alice@example.com');

INSERT INTO patient (id, name, email, address, date_of_birth, registration_date)
SELECT gen_random_uuid(), 'Bob Smith', 'bob@example.com', '456 Oak Ave, Riverdale',
       DATE '1985-09-22', CURRENT_DATE
WHERE NOT EXISTS (SELECT 1 FROM patient WHERE email = 'bob@example.com');

INSERT INTO patient (id, name, email, address, date_of_birth, registration_date)
SELECT gen_random_uuid(), 'Charlie Davis', 'charlie@example.com', '789 Pine Rd, Lakeside',
       DATE '1992-12-03', CURRENT_DATE
WHERE NOT EXISTS (SELECT 1 FROM patient WHERE email = 'charlie@example.com');

INSERT INTO patient (id, name, email, address, date_of_birth, registration_date)
SELECT gen_random_uuid(), 'Diana Miller', 'diana@example.com', '321 Maple St, Hilltown',
       DATE '1978-03-19', CURRENT_DATE
WHERE NOT EXISTS (SELECT 1 FROM patient WHERE email = 'diana@example.com');

INSERT INTO patient (id, name, email, address, date_of_birth, registration_date)
SELECT gen_random_uuid(), 'Ethan Brown', 'ethan@example.com', '654 Cedar Ln, Brookfield',
       DATE '2000-11-27', CURRENT_DATE
WHERE NOT EXISTS (SELECT 1 FROM patient WHERE email = 'ethan@example.com');
