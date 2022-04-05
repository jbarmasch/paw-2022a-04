CREATE TABLE IF NOT EXISTS users (
    userId SERIAL PRIMARY KEY,
    username VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS events (
    eventId SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description VARCHAR(100) NOT NULL,
    location VARCHAR(100),
    maxCapacity INTEGER NOT NULL,
    price DOUBLE PRECISION NOT NULL,
    CHECK (location IN ('Adrogué', 'San Isidro', 'Don Torcuato', 'Belgrano', 'Recoleta', 'Turdera'))
);

