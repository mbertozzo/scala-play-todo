# User schema

# --- !Ups
CREATE TABLE IF NOT EXISTS `scala_todo`.`user` (
    `id` INT(11) NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(45) NULL DEFAULT NULL,
    `surname` VARCHAR(45) NULL DEFAULT NULL,
    `isAdmin` TINYINT(4) NULL DEFAULT NULL,
    PRIMARY KEY (`id`)
);

# --- !Downs
DROP TABLE `user`