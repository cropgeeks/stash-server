-- Make sure the 'Description' attribute exists
insert into `attributes` (`name`, `datatype`)
    select 'Description', 'text'
    from dual
    where not exists (select 1 from `attributes` where `name` = 'Description');

-- Now convert any existing description into an attribute value
INSERT INTO `container_attributes` ( `container_id`, `attribute_values`, `created_on` ) SELECT `containers`.`id`, json_object(( SELECT id FROM `attributes` WHERE `attributes`.`name` = 'Description' ),
	`containers`.`description`
),
`containers`.`created_on`
FROM
	`containers`
WHERE
	NOT isnull( `containers`.`description` );

-- Remove the description column
ALTER TABLE `containers`
DROP COLUMN `description`;