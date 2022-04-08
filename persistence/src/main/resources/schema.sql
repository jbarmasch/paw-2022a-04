CREATE TABLE IF NOT EXISTS users (
    userId SERIAL PRIMARY KEY,
    username VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL
--     mail VARCHAR(100) UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS events (
    eventId SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description VARCHAR(100) NOT NULL,
    location VARCHAR(100),
    maxCapacity INTEGER NOT NULL,
    price DOUBLE PRECISION NOT NULL,
    date TIMESTAMP NOT NULL,
    CHECK (location IN ('Adrogué', 'San Isidro', 'Don Torcuato', 'Belgrano', 'Recoleta', 'Turdera'))
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