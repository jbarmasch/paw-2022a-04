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

CREATE TABLE IF NOT EXISTS types_en (
    typeId SERIAL PRIMARY KEY,
    name VARCHAR(100)
);

CREATE TABLE IF NOT EXISTS tags (
    tagId SERIAL PRIMARY KEY,
    name VARCHAR(100)
);

CREATE TABLE IF NOT EXISTS tags_en (
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
    date TIMESTAMP NOT NULL,
    typeId INTEGER REFERENCES types,
    userId INTEGER REFERENCES users,
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
    UNIQUE (eventId, name)
);

CREATE TABLE IF NOT EXISTS eventbookings (
    id SERIAL PRIMARY KEY,
    code VARCHAR(100) UNIQUE NOT NULL,
    userId INTEGER REFERENCES users,
    eventId INTEGER REFERENCES events,
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

DROP VIEW IF EXISTS event_complete;
DROP VIEW IF EXISTS event_complete_en;

CREATE OR REPLACE VIEW event_complete AS (
    SELECT aux.*,  ARRAY_AGG(t.tagId) AS tagIds, ARRAY_AGG(t.name) AS tagNames
    FROM
        (SELECT events.eventid, events.name, events.description, events.locationid, SUM(COALESCE(ti.booked, 0)) AS attendance,
                MIN(CASE WHEN ti.maxTickets - ti.booked > 0 THEN ti.price END) AS minPrice,
                (SUM(COALESCE(ti.maxTickets, 0)) - SUM(COALESCE(ti.booked, 0))) AS ticketsLeft,
                events.typeid, events.date, events.imageid, events.userid, events.state, locations.name AS locName, types.name AS typeName, users.username,
                ARRAY_AGG(ti.ticketId) AS ticketIds, ARRAY_AGG(ti.maxTickets) AS ticketQtys, ARRAY_AGG(ti.booked) AS ticketBookeds, ARRAY_AGG(ti.name) AS ticketNames, ARRAY_AGG(ti.price) AS ticketPrices
         FROM events JOIN locations ON events.locationid = locations.locationid
                     LEFT OUTER JOIN tickets ti ON events.eventid = ti.eventid
                     JOIN types ON events.typeid = types.typeid JOIN users ON events.userid = users.userid
         GROUP BY events.eventId, locations.locationid, types.typeid, users.username) AS aux LEFT OUTER JOIN eventTags eT ON aux.eventId = eT.eventId LEFT OUTER JOIN tags t ON eT.tagId = t.tagId
    GROUP BY aux.eventid, aux.name, aux.description, aux.attendance, aux.locationid, aux.minPrice, aux.ticketsLeft, aux.typeid, aux.date, aux.imageid, aux.userid, aux.state, aux.locName,
             aux.typeName, aux.username, aux.ticketIds, aux.ticketQtys, aux.ticketBookeds, aux.ticketNames, aux.ticketPrices
);

CREATE OR REPLACE VIEW event_complete_en AS (
    SELECT aux.*,  ARRAY_AGG(t.tagId) AS tagIds, ARRAY_AGG(t.name) AS tagNames
    FROM
        (SELECT events.eventid, events.name, events.description, events.locationid, SUM(COALESCE(ti.booked, 0)) AS attendance,
                MIN(CASE WHEN ti.maxTickets - ti.booked > 0 THEN ti.price END) AS minPrice,
                (SUM(COALESCE(ti.maxTickets, 0)) - SUM(COALESCE(ti.booked, 0))) AS ticketsLeft,
                events.typeid, events.date, events.imageid, events.userid, events.state, locations.name AS locName, types_en.name AS typeName, users.username,
                ARRAY_AGG(ti.ticketId) AS ticketIds, ARRAY_AGG(ti.maxTickets) AS ticketQtys, ARRAY_AGG(ti.booked) AS ticketBookeds, ARRAY_AGG(ti.name) AS ticketNames, ARRAY_AGG(ti.price) AS ticketPrices
         FROM events JOIN locations ON events.locationid = locations.locationid
                     LEFT OUTER JOIN tickets ti ON events.eventid = ti.eventid
                     JOIN types_en ON events.typeid = types_en.typeid JOIN users ON events.userid = users.userid
         GROUP BY events.eventId, locations.locationid, types_en.typeid, users.username) AS aux LEFT OUTER JOIN eventTags eT ON aux.eventId = eT.eventId LEFT OUTER JOIN tags_en t ON eT.tagId = t.tagId
    GROUP BY aux.eventid, aux.name, aux.description, aux.attendance, aux.locationid, aux.minPrice, aux.ticketsLeft, aux.typeid, aux.date, aux.imageid, aux.userid, aux.state, aux.locName,
             aux.typeName, aux.username, aux.ticketIds, aux.ticketQtys, aux.ticketBookeds, aux.ticketNames, aux.ticketPrices
);
