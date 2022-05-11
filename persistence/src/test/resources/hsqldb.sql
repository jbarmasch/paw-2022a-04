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


CREATE TABLE IF NOT EXISTS types (
    typeId IDENTITY PRIMARY KEY,
    name VARCHAR(100)
);

CREATE TABLE IF NOT EXISTS types_en (
    typeId IDENTITY PRIMARY KEY,
    name VARCHAR(100)
);

CREATE TABLE IF NOT EXISTS tags (
    tagId IDENTITY PRIMARY KEY,
    name VARCHAR(100)
);

CREATE TABLE IF NOT EXISTS tags_en (
    tagId IDENTITY PRIMARY KEY,
    name VARCHAR(100)
);

CREATE TABLE IF NOT EXISTS events (
    eventId IDENTITY PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description VARCHAR(100),
    locationId INTEGER REFERENCES locations ON DELETE CASCADE,
    date TIMESTAMP NOT NULL,
    typeId INTEGER REFERENCES types ON DELETE CASCADE,
    userId INTEGER REFERENCES users ON DELETE CASCADE,
    imageId INTEGER REFERENCES images ON DELETE CASCADE,
    state INTEGER NOT NULL
);

CREATE TABLE IF NOT EXISTS eventTags (
    eventId INTEGER REFERENCES events ON DELETE CASCADE,
    tagId INTEGER REFERENCES tags ON DELETE CASCADE,
    PRIMARY KEY (eventId, tagId)
);

CREATE TABLE IF NOT EXISTS tickets (
    ticketId IDENTITY PRIMARY KEY,
    eventId INTEGER REFERENCES events ON DELETE CASCADE,
    name VARCHAR(100),
    price DOUBLE PRECISION,
    booked INTEGER,
    maxTickets INTEGER,
    UNIQUE (eventId, name)
);

CREATE TABLE IF NOT EXISTS bookings (
    userId INTEGER REFERENCES users ON DELETE CASCADE,
    eventId INTEGER REFERENCES events ON DELETE CASCADE,
    ticketId INTEGER REFERENCES tickets ON DELETE CASCADE,
    qty INTEGER CHECK (qty >= 0),
    PRIMARY KEY (userId, eventId, ticketId)
);

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
    roleId INTEGER REFERENCES roles ON DELETE CASCADE,
    PRIMARY KEY (userId, roleId)
);

CREATE VIEW event_complete AS (
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

CREATE VIEW event_complete_en AS (
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