CREATE TABLE IF NOT EXISTS usr (
    `id` INTEGER NOT NULL,
    `username` TEXT NOT NULL UNIQUE CHECK (LENGTH(`username`) > 0),
    `email` TEXT NOT NULL UNIQUE CHECK (LENGTH(`email`) > 0),
    `password` VARCHAR(1023) NOT NULL,
    PRIMARY KEY (`id`)
);

CREATE TABLE IF NOT EXISTS trip (
    `id` INTEGER NOT NULL,
    `name` INT NOT NULL,
    `half_board` BOOLEAN NOT NULL CHECK (`half_board` IN (0, 1)),
    `num_guests` INT NOT NULL,
    `num_nights` INT NOT NULL,
    `description` TEXT,
    PRIMARY KEY (`id`)
);

CREATE TABLE IF NOT EXISTS sight (
    `id` INTEGER NOT NULL,
    `name` TEXT NOT NULL,
    `price` NUMERIC NOT NULL CHECK (`price` >= 0),
    `opening` INT NOT NULL CHECK (`opening` BETWEEN 0 AND 24),
    `closing` INT NOT NULL CHECK (`closing` BETWEEN 0 AND 24),
    `description` TEXT,
    `popularity` INT NOT NULL CHECK (`popularity` BETWEEN 0 AND 10),
    PRIMARY KEY (`id`)
);
