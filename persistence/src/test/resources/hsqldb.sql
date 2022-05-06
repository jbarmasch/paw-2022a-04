-- SET DATABASE SQL SYNTAX PGS TRUE;

CREATE TABLE IF NOT EXISTS users (
    userId IDENTITY PRIMARY KEY,
    username VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL,
    mail VARCHAR(100) UNIQUE NOT NULL
);

-- CREATE TABLE IF NOT EXISTS locations (
--     locationId IDENTITY PRIMARY KEY,
--     name VARCHAR(100)
-- );
--
-- CREATE TABLE IF NOT EXISTS images (
--     imageId IDENTITY PRIMARY KEY,
--     image VARBINARY(10000)
-- );
--
-- CREATE TABLE IF NOT EXISTS events (
--     eventId IDENTITY PRIMARY KEY,
--     name VARCHAR(100) NOT NULL,
--     description VARCHAR(100) NOT NULL,
--     locationId INTEGER REFERENCES locations,
--     attendance INTEGER NOT NULL,
--     date TIMESTAMP NOT NULL,
--     typeId INTEGER REFERENCES types,
--     userId INTEGER REFERENCES users,
--     imageId INTEGER REFERENCES images,
--     state INTEGER NOT NULL
-- );
--
-- CREATE TABLE IF NOT EXISTS types (
--     typeId IDENTITY PRIMARY KEY,
--     name VARCHAR(100)
-- );
--
-- CREATE TABLE IF NOT EXISTS tags (
--     tagId IDENTITY PRIMARY KEY,
--     name VARCHAR(100)
-- );
--
-- CREATE TABLE IF NOT EXISTS eventTags (
--     eventId INTEGER REFERENCES events,
--     tagId INTEGER REFERENCES tags,
--     PRIMARY KEY (eventId, tagId)
-- );
--
-- CREATE TABLE IF NOT EXISTS tickets (
--     ticketId IDENTITY PRIMARY KEY,
--     eventId INTEGER REFERENCES events,
--     name VARCHAR(100),
--     price DOUBLE PRECISION,
--     booked INTEGER,
--     ticketsLeft INTEGER,
--     UNIQUE (eventId, name)
-- );
--
-- CREATE TABLE IF NOT EXISTS bookings (
--     userId INTEGER REFERENCES users,
--     eventId INTEGER REFERENCES events,
--     ticketId INTEGER REFERENCES tickets,
--     qty INTEGER CHECK (qty >= 0),
--     PRIMARY KEY (userId, eventId, ticketId)
-- );
--
CREATE TABLE IF NOT EXISTS ratings (
    userId INTEGER REFERENCES users ON DELETE CASCADE,
    organizerId INTEGER REFERENCES users ON DELETE CASCADE,
    rating DOUBLE PRECISION NOT NULL,
    PRIMARY KEY (userId, organizerId)
);

CREATE TABLE IF NOT EXISTS roles (
    roleId IDENTITY PRIMARY KEY,
    name varchar(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS userRoles (
    userId INTEGER REFERENCES users ON DELETE CASCADE,
    roleId INTEGER REFERENCES roles,
    PRIMARY KEY (userId, roleId)
);
