USE blog;

    CREATE TABLE  IF NOT EXISTS `role`
    (
        `id` INT(11) NOT NULL AUTO_INCREMENT,
        `name` VARCHAR(255) NOT NULL,
        PRIMARY KEY(`id`)
    );
    CREATE TABLE  IF NOT EXISTS `user`
    (
        `id` INT(11) NOT NULL AUTO_INCREMENT,
        `user_name` VARCHAR(255) NOT NULL,
        `email` VARCHAR(255) NOT NULL,
        `password` VARCHAR(255) NOT NULL,
        `role_id` INT(11) NOT NULL,
        FOREIGN KEY (`role_id`) REFERENCES `role` (`id`),
        PRIMARY KEY(`id`)
    )

    ALTER TABLE `role`
    ADD UNIQUE (`name`)
