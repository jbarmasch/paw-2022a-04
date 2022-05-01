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
    name VARCHAR(100)
);

CREATE TABLE IF NOT EXISTS tags (
    tagId SERIAL PRIMARY KEY,
    name VARCHAR(100)
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
    ticketsLeft INTEGER NOT NULL CHECK (ticketsLeft >= 0),
    attendance INTEGER NOT NULL,
    price DOUBLE PRECISION NOT NULL,
    date TIMESTAMP NOT NULL,
    typeId INTEGER REFERENCES types,
    userId INTEGER REFERENCES users,
    imageId INTEGER REFERENCES images,
    state INTEGER NOT NULL
);

CREATE TABLE IF NOT EXISTS eventTags (
    eventId INTEGER REFERENCES events,
    tagId INTEGER REFERENCES tags,
    PRIMARY KEY (eventId, tagId)
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
    qty INTEGER CHECK (qty >= 0),
    PRIMARY KEY (userId, eventId, name)
);

CREATE OR REPLACE VIEW event_complete AS (
    SELECT events.eventid, events.name, events.description, events.locationid, events.attendance, events.ticketsleft, events.price,
           events.typeid, events.date, events.imageid, events.userid, events.state, locations.name AS locName, images.image, types.name AS typeName, ARRAY_AGG(t.tagId) AS tagIds, ARRAY_AGG(t.name) AS tagNames
    FROM events JOIN locations ON events.locationid = locations.locationid JOIN images ON events.imageid = images.imageid
                JOIN types ON events.typeid = types.typeid LEFT OUTER JOIN eventTags eT on events.eventId = eT.eventId LEFT OUTER JOIN tags t on eT.tagId = t.tagId
    GROUP BY events.eventId, locations.locationid, images.imageid, types.typeid
);
