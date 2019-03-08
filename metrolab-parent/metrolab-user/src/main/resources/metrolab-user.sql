CREATE TABLE IF NOT EXISTS `metrolab_db_users`.`Account` (
  `id` SMALLINT(5) UNSIGNED NOT NULL AUTO_INCREMENT,
  `email` VARCHAR(255) NOT NULL,
  `password` VARCHAR(255) NOT NULL,
  `full_name` VARCHAR(60) NOT NULL,
  `security_role` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `User_u1` (`email` ASC))
ENGINE = InnoDB ;



