DROP VIEW IF EXISTS `view_table_containers`;
CREATE ALGORITHM = UNDEFINED SQL SECURITY DEFINER VIEW `view_table_containers` AS select `c`.`id` as `container_id`, `c`.`barcode` as `container_barcode`, `c`.`description` as `container_description`, `seedstore`.`container_types`.`id` as `container_type_id`, `seedstore`.`container_types`.`name` as `container_type_name`, `seedstore`.`container_types`.`description` as `container_type_description`, `cp`.`id` as `parent_id`, `cp`.`barcode` as `parent_barcode`, `cp`.`description` as `parent_description`, `c`.`is_active` as `container_is_active`, `seedstore`.`trials`.`id` as `trial_id`, `seedstore`.`trials`.`name` as `trial_name`, `seedstore`.`trials`.`description` as `trial_description`, `seedstore`.`projects`.`id` as `project_id`, `seedstore`.`projects`.`name` as `project_name`, `seedstore`.`projects`.`description` as `project_description`, ( select json_objectagg(`seedstore`.`attributes`.`name`, `seedstore`.`container_attributes`.`attribute_value`) from (`seedstore`.`container_attributes` left join `seedstore`.`attributes` on ((`seedstore`.`attributes`.`id` = `seedstore`.`container_attributes`.`attribute_id`))) where (`seedstore`.`container_attributes`.`container_id` = `c`.`id`)) as `container_attributes`, (select count(1) from `containers` `sc` where `sc`.`parent_container_id` = `c`.`id`) as `sub_container_count`, `c`.`created_on` as `created_on` from `seedstore`.`containers` `c` left join `seedstore`.`containers` `cp` on `c`.`parent_container_id` = `cp`.`id` left join `seedstore`.`container_types` on `c`.`container_type_id` = `seedstore`.`container_types`.`id` left join `seedstore`.`trials` on `seedstore`.`trials`.`id` = `c`.`trial_id` left join `seedstore`.`projects` on `seedstore`.`projects`.`id` = `c`.`project_id`;

DROP VIEW IF EXISTS `view_table_transfers`;
CREATE ALGORITHM = UNDEFINED SQL SECURITY DEFINER VIEW `view_table_transfers` AS select `transfer_logs`.`id` as `transfer_log_id`, `c`.`id` as `container_id`, `c`.`barcode` as `container_barcode`, `c`.`description` as `container_description`, `s`.`id` as `source_id`, `s`.`barcode` as `source_barcode`, `s`.`description` as `source_description`, `t`.`id` as `target_id`, `t`.`barcode` as `target_barcode`, `t`.`description` as `target_description`, `transfer_logs`.`user_id` as `user_id`, `transfer_logs`.`created_on` as `created_on`, `transfer_logs`.`updated_on` as `updated_on` from `transfer_logs` left join `containers` `c` on `c`.`id` = `transfer_logs`.`container_id` left join `containers` `s` on `s`.`id` = `transfer_logs`.`source_id` left join `containers` `t` on `t`.`id` = `transfer_logs`.`target_id`;