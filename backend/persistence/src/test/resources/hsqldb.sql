-- SET DATABASE SQL SYNTAX PGS TRUE;
CREATE TABLE IF NOT EXISTS users
(
    userId IDENTITY PRIMARY KEY,
    username VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(100)        NOT NULL,
    mail     VARCHAR(100) UNIQUE NOT NULL
    );

CREATE TABLE IF NOT EXISTS locations
(
    locationId IDENTITY PRIMARY KEY,
    name VARCHAR(100)
    );

CREATE TABLE IF NOT EXISTS types
(
    typeId IDENTITY PRIMARY KEY,
    name    VARCHAR(100),
    name_en VARCHAR(100)
    );

CREATE TABLE IF NOT EXISTS tags
(
    tagId IDENTITY PRIMARY KEY,
    name    VARCHAR(100),
    name_en VARCHAR(100)
    );

CREATE TABLE IF NOT EXISTS images
(
    imageId IDENTITY PRIMARY KEY,
    image VARBINARY(10000)
    );

CREATE TABLE IF NOT EXISTS events
(
    eventId IDENTITY PRIMARY KEY,
    name        VARCHAR(100) NOT NULL,
    description VARCHAR(100) NOT NULL,
    locationId  INTEGER REFERENCES locations ON DELETE CASCADE,
    date        TIMESTAMP    NOT NULL,
    typeId      INTEGER REFERENCES types ON DELETE CASCADE,
    userId      INTEGER REFERENCES users ON DELETE CASCADE,
    bouncerId   INTEGER REFERENCES users (userId) ON DELETE CASCADE,
    imageId     INTEGER REFERENCES images ON DELETE CASCADE,
    state       INTEGER      NOT NULL,
    minage      INTEGER CHECK (minage >= 14 AND minage <= 27)
    );

CREATE TABLE IF NOT EXISTS eventTags
(
    eventId INTEGER REFERENCES events ON DELETE CASCADE,
    tagId   INTEGER REFERENCES tags ON DELETE CASCADE,
    PRIMARY KEY (eventId, tagId)
    );

CREATE TABLE IF NOT EXISTS tickets
(
    ticketId IDENTITY PRIMARY KEY,
    eventId    INTEGER REFERENCES events ON DELETE CASCADE,
    name       VARCHAR(100),
    price      DOUBLE PRECISION,
    booked     INTEGER,
    qty        INTEGER,
    maxPerUser INTEGER DEFAULT 6,
    starting   TIMESTAMP,
    until      TIMESTAMP,
    UNIQUE (eventId, name),
    CHECK (maxPerUser >= 1 AND maxPerUser <= 10)
    );

CREATE TABLE IF NOT EXISTS eventbookings
(
    id IDENTITY PRIMARY KEY,
    code      VARCHAR(100) UNIQUE NOT NULL,
    userId    INTEGER REFERENCES users ON DELETE CASCADE,
    eventId   INTEGER REFERENCES events ON DELETE CASCADE,
    confirmed BOOLEAN             NOT NULL,
    UNIQUE (userId, eventId)
    );

CREATE TABLE IF NOT EXISTS ticketbookings
(
    id       INTEGER REFERENCES eventbookings ON DELETE CASCADE,
    ticketId INTEGER REFERENCES tickets ON DELETE CASCADE,
    qty      INTEGER CHECK (qty >= 0),
    PRIMARY KEY (id, ticketId)
    );

CREATE TABLE IF NOT EXISTS ratings
(
    userId      INTEGER REFERENCES users ON DELETE CASCADE,
    organizerId INTEGER REFERENCES users ON DELETE CASCADE,
    rating      DOUBLE PRECISION NOT NULL,
    PRIMARY KEY (userId, organizerId)
    );

CREATE TABLE IF NOT EXISTS roles
(
    roleId IDENTITY PRIMARY KEY,
    name varchar(100) NOT NULL
    );

CREATE TABLE IF NOT EXISTS userRoles
(
    userId INTEGER REFERENCES users ON DELETE CASCADE,
    roleId INTEGER REFERENCES roles ON DELETE CASCADE,
    PRIMARY KEY (userId, roleId)
);
