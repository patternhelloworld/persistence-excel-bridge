CREATE TABLE `excel_db_read_task` (
      `id` BIGINT NOT NULL AUTO_INCREMENT,
      `group_id` BIGINT DEFAULT NULL,
      `startRow` INT DEFAULT NULL,
      `endRow` INT DEFAULT NULL,
      `description` VARCHAR(255) DEFAULT NULL,
      `status` TINYINT DEFAULT 0,
      `error_reason` TINYINT DEFAULT NULL,
      `error_count` INT DEFAULT 0,
      `warning_reason` TINYINT DEFAULT NULL,
      `error_message` TEXT DEFAULT NULL,
      `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
      `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
      PRIMARY KEY (`id`)
);
