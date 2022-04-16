-- SET DATABASE SQL SYNTAX PGS TRUE;

CREATE TABLE IF NOT EXISTS users (
    userId IDENTITY PRIMARY KEY,
    username VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL,
    mail VARCHAR(100) UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS locations (
    locationId IDENTITY PRIMARY KEY,
    name VARCHAR(100)
);

CREATE TABLE IF NOT EXISTS images (
    imageId IDENTITY PRIMARY KEY,
    image VARBINARY(10000)
);

CREATE TABLE IF NOT EXISTS events (
    eventId IDENTITY PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description VARCHAR(100) NOT NULL,
    locationId INTEGER REFERENCES locations,
    maxCapacity INTEGER NOT NULL,
    price DOUBLE PRECISION NOT NULL,
    date TIMESTAMP NOT NULL,
    type VARCHAR(100),
    userId INTEGER REFERENCES users
);

CREATE TABLE IF NOT EXISTS eventTags (
    eventId INTEGER REFERENCES events,
    tag VARCHAR(100),
    PRIMARY KEY (eventId, tag)
);

CREATE TABLE IF NOT EXISTS tickets (
    eventId INTEGER REFERENCES events,
    name VARCHAR(100),
    price DOUBLE PRECISION,
    booked INTEGER,
    maxQty INTEGER,
    PRIMARY KEY (eventId, name)
);

CREATE TABLE IF NOT EXISTS bookings (
    userId INTEGER REFERENCES users,
    eventId INTEGER REFERENCES events,
    name VARCHAR(100),
    qty INTEGER,
    PRIMARY KEY (userId, eventId, name)
);
