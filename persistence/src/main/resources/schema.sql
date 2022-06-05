CREATE TABLE IF NOT EXISTS users (
    userId SERIAL PRIMARY KEY,
    username VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL,
    mail VARCHAR(100) UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS locations (
    locationId SERIAL PRIMARY KEY,
    name VARCHAR(100)
);

CREATE TABLE IF NOT EXISTS types (
    typeId SERIAL PRIMARY KEY,
    name VARCHAR(100),
    name_en VARCHAR(100)
);

CREATE TABLE IF NOT EXISTS tags (
    tagId SERIAL PRIMARY KEY,
    name VARCHAR(100),
    name_en VARCHAR(100)
);

CREATE TABLE IF NOT EXISTS images (
    imageId SERIAL PRIMARY KEY,
    image BYTEA
);

CREATE TABLE IF NOT EXISTS events (
    eventId SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description VARCHAR(100) NOT NULL,
    locationId INTEGER REFERENCES locations,
    date TIMESTAMP NOT NULL,
    typeId INTEGER REFERENCES types,
    userId INTEGER REFERENCES users,
    bouncerId INTEGER REFERENCES users(userId),
    imageId INTEGER REFERENCES images,
    state INTEGER NOT NULL,
    minage INTEGER CHECK(minage >= 14 AND minage <= 27)
);

CREATE TABLE IF NOT EXISTS eventTags (
    eventId INTEGER REFERENCES events,
    tagId INTEGER REFERENCES tags,
    PRIMARY KEY (eventId, tagId)
);

CREATE TABLE IF NOT EXISTS tickets (
    ticketId SERIAL PRIMARY KEY,
    eventId INTEGER REFERENCES events ON DELETE CASCADE,
    name VARCHAR(100),
    price DOUBLE PRECISION,
    booked INTEGER,
    maxTickets INTEGER,
    starting TIMESTAMP,
    until TIMESTAMP,
    UNIQUE (eventId, name)
);

CREATE TABLE IF NOT EXISTS eventbookings (
    id SERIAL PRIMARY KEY,
    code VARCHAR(100) UNIQUE NOT NULL,
    userId INTEGER REFERENCES users,
    eventId INTEGER REFERENCES events,
    confirmed BOOLEAN NOT NULL,
    UNIQUE (userId, eventId)
);

CREATE TABLE IF NOT EXISTS ticketbookings (
    id INTEGER REFERENCES eventbookings,
    ticketId INTEGER REFERENCES tickets ON DELETE CASCADE,
    qty INTEGER CHECK (qty >= 0),
    PRIMARY KEY (id, ticketId)
);

CREATE TABLE IF NOT EXISTS ratings (
    userId INTEGER REFERENCES users,
    organizerId INTEGER REFERENCES users,
    rating DOUBLE PRECISION NOT NULL,
    PRIMARY KEY (userId, organizerId)
);

CREATE TABLE IF NOT EXISTS roles (
    roleId SERIAL PRIMARY KEY,
    name varchar(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS userRoles (
    userId INTEGER REFERENCES users,
    roleId INTEGER REFERENCES roles,
    PRIMARY KEY (userId, roleId)
);
