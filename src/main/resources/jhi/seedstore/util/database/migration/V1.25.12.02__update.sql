ALTER TABLE `container_attributes` DROP FOREIGN KEY `container_attributes_ibfk_1`;

ALTER TABLE `container_attributes`
DROP COLUMN `attribute_id`,
CHANGE COLUMN `attribute_value` `attribute_values` json NOT NULL AFTER `container_id`,
ADD COLUMN `id` int NOT NULL AUTO_INCREMENT FIRST,
DROP PRIMARY KEY,
ADD PRIMARY KEY (`id`) USING BTREE;