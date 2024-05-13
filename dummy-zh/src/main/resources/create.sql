CREATE TABLE IF NOT EXISTS dummy (
    `id` INTEGER NOT NULL,
    `text_value` TEXT NOT NULL,
    `integer_value` INT NOT NULL,
    `double_value` DOUBLE NOT NULL,
    `bool_value` BOOLEAN NOT NULL CHECK (bool_value IN (0, 1)),
    PRIMARY KEY (`id`)
);
