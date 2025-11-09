CREATE TABLE `equipment` (
	`equipmentid` INT NOT NULL AUTO_INCREMENT,
	`name` VARCHAR(50) NULL DEFAULT NULL COLLATE 'utf8mb4_unicode_ci',
	`category` VARCHAR(50) NULL DEFAULT NULL COLLATE 'utf8mb4_unicode_ci',
	`equipmentcondition` VARCHAR(200) NULL DEFAULT NULL COLLATE 'utf8mb4_unicode_ci',
	`totalquantity` INT NULL DEFAULT NULL,
	`availablequantity` INT NULL DEFAULT NULL,
	`createts` TIMESTAMP NOT NULL DEFAULT (CURRENT_TIMESTAMP),
	`updatets` TIMESTAMP NOT NULL DEFAULT (CURRENT_TIMESTAMP) ON UPDATE CURRENT_TIMESTAMP,
	PRIMARY KEY (`equipmentid`) USING BTREE
)
COLLATE='utf8mb4_unicode_ci'
ENGINE=InnoDB
AUTO_INCREMENT=45
;

CREATE TABLE `orders` (
	`orderid` INT NOT NULL AUTO_INCREMENT,
	`userid` INT NOT NULL,
	`equipmentid` INT NOT NULL,
	`status` VARCHAR(5) NOT NULL DEFAULT 'PAP' COMMENT 'AP/RJ/PAP/RET' COLLATE 'utf8mb4_unicode_ci',
	`approveddate` DATE NULL DEFAULT NULL,
	`approvedbyuserid` INT NULL DEFAULT NULL,
	`returndate` DATE NULL DEFAULT NULL,
	`quantity` INT NOT NULL DEFAULT '0',
	`createts` TIMESTAMP NOT NULL DEFAULT (now()),
	PRIMARY KEY (`orderid`) USING BTREE
)
COLLATE='utf8mb4_unicode_ci'
ENGINE=InnoDB
AUTO_INCREMENT=36
;

CREATE TABLE `users` (
	`userid` INT NOT NULL AUTO_INCREMENT,
	`username` VARCHAR(50) NULL DEFAULT NULL COLLATE 'utf8mb4_unicode_ci',
	`email` VARCHAR(50) NULL DEFAULT NULL COLLATE 'utf8mb4_unicode_ci',
	`password` VARCHAR(50) NULL DEFAULT NULL COLLATE 'utf8mb4_unicode_ci',
	`phonenumber` VARCHAR(50) NULL DEFAULT NULL COLLATE 'utf8mb4_unicode_ci',
	`role` VARCHAR(50) NULL DEFAULT NULL COLLATE 'utf8mb4_unicode_ci',
	`createts` TIMESTAMP NOT NULL DEFAULT (now()),
	PRIMARY KEY (`userid`) USING BTREE
)
COLLATE='utf8mb4_unicode_ci'
ENGINE=InnoDB
AUTO_INCREMENT=13
;

INSERT INTO `users` (`username`, `email`, `password`, `phonenumber`, `role`) VALUES ('Shambhavi', 'shambhavi@admin.bits.edu', 'abc', '9878088811', 'A');

INSERT INTO `users` (`username`, `email`, `password`, `phonenumber`, `role`) VALUES ('Dr. Kavita Rao', 'kavita.rao@faculty.bits.edu', 'teacher123', '9876534567', 'T');

INSERT INTO `users` (`username`, `email`, `password`, `phonenumber`, `role`) VALUES ('Aarav Mehta', 'aarav.mehta@student.bits.edu', 'student123', '9876543210', 'S');

