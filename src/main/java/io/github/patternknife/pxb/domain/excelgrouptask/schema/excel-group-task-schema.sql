CREATE TABLE `excel_group_task` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `status` TINYINT NOT NULL DEFAULT 0,
    `row_count_per_task` INT NOT NULL DEFAULT 0,
    `total_row` BIGINT DEFAULT NULL,
    `description` VARCHAR(255) DEFAULT NULL,
    `relative_file_path` VARCHAR(255) DEFAULT NULL,
    `saved_file_ext` VARCHAR(255) DEFAULT NULL,
    `original_file_name` VARCHAR(255) DEFAULT NULL,
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `excel_updated_at` DATETIME DEFAULT NULL,
    `read_or_write` TINYINT NOT NULL DEFAULT 0,
    PRIMARY KEY (`id`)
);
