-- MySQL Script generated by MySQL Workbench
-- Sat May 26 22:36:25 2018
-- Model: New Model    Version: 1.0
-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Table `credentials`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `credentials` ;

CREATE TABLE IF NOT EXISTS `credentials` (
  `user_id` INT NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(20) NOT NULL,
  `hashed` CHAR(32) NOT NULL,
  PRIMARY KEY (`user_id`),
  UNIQUE INDEX `username_UNIQUE` (`username` ASC))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `topics`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `topics` ;

CREATE TABLE IF NOT EXISTS `topics` (
  `topic_id` INT NOT NULL AUTO_INCREMENT,
  `topic_name` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`topic_id`),
  UNIQUE INDEX `topic_name_UNIQUE` (`topic_name` ASC))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `subscribes`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `subscribes` ;

CREATE TABLE IF NOT EXISTS `subscribes` (
  `subscribe_id` INT NOT NULL AUTO_INCREMENT,
  `user_id` INT NOT NULL,
  `topic_id` INT NOT NULL,
  PRIMARY KEY (`subscribe_id`),
  INDEX `fk_user_id_idx` (`user_id` ASC),
  INDEX `fk_topic_id_idx` (`topic_id` ASC),
  UNIQUE INDEX `subscribe_uq` (`user_id` ASC, `topic_id` ASC),
  CONSTRAINT `fk_user_id`
    FOREIGN KEY (`user_id`)
    REFERENCES `credentials` (`user_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_topic_id2`
    FOREIGN KEY (`topic_id`)
    REFERENCES `topics` (`topic_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `posts`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `posts` ;

CREATE TABLE IF NOT EXISTS `posts` (
  `post_id` INT NOT NULL AUTO_INCREMENT,
  `text` VARCHAR(255) NULL,
  `topic_id` INT NOT NULL,
  PRIMARY KEY (`post_id`),
  INDEX `fk_topic_id_idx` (`topic_id` ASC),
  CONSTRAINT `fk_topic_id`
    FOREIGN KEY (`topic_id`)
    REFERENCES `topics` (`topic_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- procedure subscribe_to_topic
-- -----------------------------------------------------
DROP procedure IF EXISTS `subscribe_to_topic`;

DELIMITER $$
CREATE PROCEDURE `subscribe_to_topic` (IN username VARCHAR(20), IN topic_name VARCHAR(45))
BEGIN
	SET @user_id=(SELECT c.user_id FROM credentials c WHERE c.username=username);
    SET @topic_id=(SELECT t.topic_id FROM topics t WHERE t.topic_name=topic_name);
	INSERT INTO subscribes (user_id, topic_id) VALUES (@user_id, @topic_id);
END$$

DELIMITER ;

SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
