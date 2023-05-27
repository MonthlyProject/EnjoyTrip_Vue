-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
-- -----------------------------------------------------
-- Schema tripdb
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema tripdb
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `tripdb` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci ;
USE `tripdb` ;

-- -----------------------------------------------------
-- Table `tripdb`.`content_type`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `tripdb`.`content_type` (
  `content_type_id` INT NOT NULL,
  `cnt` INT NULL DEFAULT '0',
  PRIMARY KEY (`content_type_id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `tripdb`.`sido`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `tripdb`.`sido` (
  `sido_code` INT NOT NULL,
  `sido_name` VARCHAR(30) NULL DEFAULT NULL,
  `cnt` INT NULL DEFAULT '0',
  PRIMARY KEY (`sido_code`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `tripdb`.`gugun`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `tripdb`.`gugun` (
  `gugun_code` INT NOT NULL,
  `gugun_name` VARCHAR(30) NULL DEFAULT NULL,
  `sido_code` INT NOT NULL,
  `cnt` INT NULL DEFAULT '0',
  PRIMARY KEY (`gugun_code`, `sido_code`),
  INDEX `gugun_to_sido_sido_code_fk_idx` (`sido_code` ASC) VISIBLE,
  CONSTRAINT `gugun_to_sido_sido_code_fk`
    FOREIGN KEY (`sido_code`)
    REFERENCES `tripdb`.`sido` (`sido_code`)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `tripdb`.`attraction_info`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `tripdb`.`attraction_info` (
  `content_id` INT NOT NULL,
  `content_type_id` INT NULL DEFAULT NULL,
  `title` VARCHAR(100) NULL DEFAULT NULL,
  `addr1` VARCHAR(100) NULL DEFAULT NULL,
  `addr2` VARCHAR(50) NULL DEFAULT NULL,
  `zipcode` VARCHAR(50) NULL DEFAULT NULL,
  `tel` VARCHAR(50) NULL DEFAULT NULL,
  `first_image` VARCHAR(200) NULL DEFAULT NULL,
  `first_image2` VARCHAR(200) NULL DEFAULT NULL,
  `readcount` INT NULL DEFAULT NULL,
  `sido_code` INT NULL DEFAULT NULL,
  `gugun_code` INT NULL DEFAULT NULL,
  `latitude` DECIMAL(20,17) NULL DEFAULT NULL,
  `longitude` DECIMAL(20,17) NULL DEFAULT NULL,
  `mlevel` VARCHAR(2) NULL DEFAULT NULL,
  PRIMARY KEY (`content_id`),
  INDEX `attraction_to_content_type_id_fk_idx` (`content_type_id` ASC) VISIBLE,
  INDEX `attraction_to_sido_code_fk_idx` (`sido_code` ASC) VISIBLE,
  INDEX `attraction_to_gugun_code_fk_idx` (`gugun_code` ASC) VISIBLE,
  CONSTRAINT `attraction_to_content_type_id_fk`
    FOREIGN KEY (`content_type_id`)
    REFERENCES `tripdb`.`content_type` (`content_type_id`),
  CONSTRAINT `attraction_to_gugun_code_fk`
    FOREIGN KEY (`gugun_code`)
    REFERENCES `tripdb`.`gugun` (`gugun_code`),
  CONSTRAINT `attraction_to_sido_code_fk`
    FOREIGN KEY (`sido_code`)
    REFERENCES `tripdb`.`sido` (`sido_code`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `tripdb`.`attraction_description`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `tripdb`.`attraction_description` (
  `content_id` INT NOT NULL,
  `homepage` VARCHAR(100) NULL DEFAULT NULL,
  `overview` VARCHAR(10000) NULL DEFAULT NULL,
  `telname` VARCHAR(45) NULL DEFAULT NULL,
  PRIMARY KEY (`content_id`),
  CONSTRAINT `attraction_detail_to_attraciton_id_fk`
    FOREIGN KEY (`content_id`)
    REFERENCES `tripdb`.`attraction_info` (`content_id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `tripdb`.`attraction_detail`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `tripdb`.`attraction_detail` (
  `content_id` INT NOT NULL,
  `cat1` VARCHAR(3) NULL DEFAULT NULL,
  `cat2` VARCHAR(5) NULL DEFAULT NULL,
  `cat3` VARCHAR(9) NULL DEFAULT NULL,
  `created_time` VARCHAR(14) NULL DEFAULT NULL,
  `modified_time` VARCHAR(14) NULL DEFAULT NULL,
  `booktour` VARCHAR(5) NULL DEFAULT NULL,
  PRIMARY KEY (`content_id`),
  CONSTRAINT `attraction_detail_to_basic_content_id_fk`
    FOREIGN KEY (`content_id`)
    REFERENCES `tripdb`.`attraction_info` (`content_id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `tripdb`.`user`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `tripdb`.`user` (
  `user_id` VARCHAR(16) NOT NULL,
  `user_pwd` VARCHAR(16) NOT NULL,
  `user_name` VARCHAR(20) NOT NULL,
  `email_id` VARCHAR(20) NULL DEFAULT NULL,
  `email_domain` VARCHAR(20) NULL DEFAULT NULL,
  `token` VARCHAR(1000) NULL DEFAULT NULL,
  `status` VARCHAR(50) NULL DEFAULT NULL,
  `save_folder` VARCHAR(45) NULL DEFAULT NULL,
  `save_file` VARCHAR(45) NULL DEFAULT NULL,
  `original_file` VARCHAR(45) NULL DEFAULT NULL,
  PRIMARY KEY (`user_id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `tripdb`.`board`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `tripdb`.`board` (
  `article_no` INT NOT NULL AUTO_INCREMENT,
  `user_id` VARCHAR(16) NULL DEFAULT NULL,
  `subject` VARCHAR(100) NULL DEFAULT NULL,
  `content` VARCHAR(2000) NULL DEFAULT NULL,
  `hit` INT NULL DEFAULT '0',
  `register_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`article_no`),
  INDEX `board_to_members_user_id_fk` (`user_id` ASC) VISIBLE,
  CONSTRAINT `board_to_members_user_id_fk`
    FOREIGN KEY (`user_id`)
    REFERENCES `tripdb`.`user` (`user_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB
AUTO_INCREMENT = 15
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `tripdb`.`comments`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `tripdb`.`comments` (
  `comment_no` INT NOT NULL AUTO_INCREMENT,
  `article_no` INT NOT NULL,
  `user_id` VARCHAR(16) NOT NULL,
  `comment` VARCHAR(1000) NULL DEFAULT NULL,
  `register_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`comment_no`),
  INDEX `comments_to_board_article_no_fk_idx` (`article_no` ASC) VISIBLE,
  INDEX `comments_to_user_user_id_fk_idx` (`user_id` ASC) VISIBLE,
  CONSTRAINT `comments_to_board_article_no_fk`
    FOREIGN KEY (`article_no`)
    REFERENCES `tripdb`.`board` (`article_no`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `comments_to_user_user_id_fk`
    FOREIGN KEY (`user_id`)
    REFERENCES `tripdb`.`user` (`user_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB
AUTO_INCREMENT = 2
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `tripdb`.`file_info`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `tripdb`.`file_info` (
  `idx` INT NOT NULL AUTO_INCREMENT,
  `article_no` INT NOT NULL,
  `save_folder` VARCHAR(45) NOT NULL,
  `original_file` VARCHAR(50) NOT NULL,
  `save_file` VARCHAR(50) NULL DEFAULT NULL,
  PRIMARY KEY (`idx`),
  INDEX `file_info_to_board_article_no_fk_idx` (`article_no` ASC) VISIBLE,
  CONSTRAINT `file_info_to_board_article_no_fk`
    FOREIGN KEY (`article_no`)
    REFERENCES `tripdb`.`board` (`article_no`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB
AUTO_INCREMENT = 17
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `tripdb`.`stargroup`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `tripdb`.`stargroup` (
  `user_id` VARCHAR(16) NOT NULL,
  `group_title` VARCHAR(45) NOT NULL,
  `idx` INT NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`idx`),
  INDEX `stargroup_user_user_id_fk_idx` (`user_id` ASC) VISIBLE,
  CONSTRAINT `stargroup_user_user_id_fk`
    FOREIGN KEY (`user_id`)
    REFERENCES `tripdb`.`user` (`user_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB
AUTO_INCREMENT = 21
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `tripdb`.`star`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `tripdb`.`star` (
  `title_idx` INT NOT NULL,
  `content_id` INT NOT NULL,
  `number` INT NULL DEFAULT NULL,
  `idx` INT NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`idx`),
  INDEX `star_to_stargroup_idx_fk_idx` (`title_idx` ASC) VISIBLE,
  INDEX `star_to_attraction_info_content_id_fk_idx` (`content_id` ASC) VISIBLE,
  CONSTRAINT `star_to_stargroup_idx_fk`
    FOREIGN KEY (`title_idx`)
    REFERENCES `tripdb`.`stargroup` (`idx`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `star_to_attraction_info_content_id_fk`
    FOREIGN KEY (`content_id`)
    REFERENCES `tripdb`.`attraction_info` (`content_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB
AUTO_INCREMENT = 41
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;

SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;

-- 관리자, 회원자
insert into user(user_id, user_pwd, user_name, email_id, email_domain)
values
( 'admin', '1234', '관리자', 'admin', 'naver.com'),
( 'ssafy', '1234', '김싸피', 'ssafy', 'ssafy.com'),
( 'coding', '1234', '박코딩', 'coding', 'google.com');

-- 필수 데이터
insert into content_type(content_type_id) values ('12'), ('14'), ('15'), ('25'), ('28'), ('32'), ('38'), ('39');

-- 더미데이터(게시판)
insert into board(user_id, subject, content)
values
('ssafy', '대구 여행 어때요??', '6월에 대구에 놀러가려고 하는데 어떻나요? 많이 더워요?'),
('ssafy', '6월 대구 여행.....', '대구 폼 미쳤다');

-- 더미데이터(공지사항)
insert into board(user_id, subject, content)
values
('admin', '비밀번호 찾기', '비밀번호 찾기 안됩니다. 비밀번호를 잊었다면 010-0000-0000으로 연락 주세요');

-- 더미데이터(댓글)
insert into comments(article_no, user_id, comment)
values
('1', 'coding', '별로 안더워요^^ 6월이면 아직 초여름이니 즐겁게 놀 수 있을거에요^0^'),
('1', 'ssafy', 'ㄳㄳ'),
('2', 'coding', 'ㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋ');

-- 더미데이터(즐겨찾기 그룹)
insert into stargroup(user_id, group_title)
values
('ssafy', '대구여행 맛집'),
('ssafy', '대구여행지');

-- 더미데이터(즐겨찾기 내용)
insert into star(title_idx, content_id)
values
('1', '133262'),
('1', '133306'),
('1', '133324'),
('2', '125406'),
('2', '126080');