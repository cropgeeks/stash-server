/*
 * This file is generated by jOOQ.
 */
package jhi.seedstore.database.codegen.tables;


import java.sql.Timestamp;

import jhi.seedstore.database.binding.ContainerAttributeValueBinding;
import jhi.seedstore.database.codegen.SeedstoreDb;
import jhi.seedstore.database.codegen.tables.records.ViewTableContainersRecord;
import jhi.seedstore.pojo.ContainerAttributeValue;

import org.jooq.Field;
import org.jooq.Name;
import org.jooq.Schema;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableOptions;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.TableImpl;


// @formatter:off
/**
 * VIEW
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class ViewTableContainers extends TableImpl<ViewTableContainersRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>seedstore_db.view_table_containers</code>
     */
    public static final ViewTableContainers VIEW_TABLE_CONTAINERS = new ViewTableContainers();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<ViewTableContainersRecord> getRecordType() {
        return ViewTableContainersRecord.class;
    }

    /**
     * The column <code>seedstore_db.view_table_containers.container_id</code>.
     */
    public final TableField<ViewTableContainersRecord, Integer> CONTAINER_ID = createField(DSL.name("container_id"), SQLDataType.INTEGER.nullable(false).defaultValue(DSL.inline("0", SQLDataType.INTEGER)), this, "");

    /**
     * The column
     * <code>seedstore_db.view_table_containers.container_barcode</code>.
     */
    public final TableField<ViewTableContainersRecord, String> CONTAINER_BARCODE = createField(DSL.name("container_barcode"), SQLDataType.VARCHAR(100).nullable(false), this, "");

    /**
     * The column
     * <code>seedstore_db.view_table_containers.container_description</code>.
     */
    public final TableField<ViewTableContainersRecord, String> CONTAINER_DESCRIPTION = createField(DSL.name("container_description"), SQLDataType.VARCHAR(255), this, "");

    /**
     * The column
     * <code>seedstore_db.view_table_containers.container_type_id</code>.
     */
    public final TableField<ViewTableContainersRecord, Integer> CONTAINER_TYPE_ID = createField(DSL.name("container_type_id"), SQLDataType.INTEGER.defaultValue(DSL.inline("0", SQLDataType.INTEGER)), this, "");

    /**
     * The column
     * <code>seedstore_db.view_table_containers.container_type_name</code>.
     */
    public final TableField<ViewTableContainersRecord, String> CONTAINER_TYPE_NAME = createField(DSL.name("container_type_name"), SQLDataType.VARCHAR(255), this, "");

    /**
     * The column
     * <code>seedstore_db.view_table_containers.container_type_description</code>.
     */
    public final TableField<ViewTableContainersRecord, String> CONTAINER_TYPE_DESCRIPTION = createField(DSL.name("container_type_description"), SQLDataType.CLOB, this, "");

    /**
     * The column
     * <code>seedstore_db.view_table_containers.container_type_icon</code>.
     */
    public final TableField<ViewTableContainersRecord, byte[]> CONTAINER_TYPE_ICON = createField(DSL.name("container_type_icon"), SQLDataType.BLOB, this, "");

    /**
     * The column <code>seedstore_db.view_table_containers.parent_id</code>.
     */
    public final TableField<ViewTableContainersRecord, Integer> PARENT_ID = createField(DSL.name("parent_id"), SQLDataType.INTEGER.defaultValue(DSL.inline("0", SQLDataType.INTEGER)), this, "");

    /**
     * The column
     * <code>seedstore_db.view_table_containers.parent_barcode</code>.
     */
    public final TableField<ViewTableContainersRecord, String> PARENT_BARCODE = createField(DSL.name("parent_barcode"), SQLDataType.VARCHAR(100), this, "");

    /**
     * The column
     * <code>seedstore_db.view_table_containers.parent_description</code>.
     */
    public final TableField<ViewTableContainersRecord, String> PARENT_DESCRIPTION = createField(DSL.name("parent_description"), SQLDataType.VARCHAR(255), this, "");

    /**
     * The column
     * <code>seedstore_db.view_table_containers.parent_container_type_id</code>.
     */
    public final TableField<ViewTableContainersRecord, Integer> PARENT_CONTAINER_TYPE_ID = createField(DSL.name("parent_container_type_id"), SQLDataType.INTEGER.defaultValue(DSL.inline("0", SQLDataType.INTEGER)), this, "");

    /**
     * The column
     * <code>seedstore_db.view_table_containers.parent_container_type_name</code>.
     */
    public final TableField<ViewTableContainersRecord, String> PARENT_CONTAINER_TYPE_NAME = createField(DSL.name("parent_container_type_name"), SQLDataType.VARCHAR(255), this, "");

    /**
     * The column
     * <code>seedstore_db.view_table_containers.parent_container_type_description</code>.
     */
    public final TableField<ViewTableContainersRecord, String> PARENT_CONTAINER_TYPE_DESCRIPTION = createField(DSL.name("parent_container_type_description"), SQLDataType.CLOB, this, "");

    /**
     * The column
     * <code>seedstore_db.view_table_containers.parent_container_type_icon</code>.
     */
    public final TableField<ViewTableContainersRecord, byte[]> PARENT_CONTAINER_TYPE_ICON = createField(DSL.name("parent_container_type_icon"), SQLDataType.BLOB, this, "");

    /**
     * The column
     * <code>seedstore_db.view_table_containers.container_is_active</code>.
     */
    public final TableField<ViewTableContainersRecord, Boolean> CONTAINER_IS_ACTIVE = createField(DSL.name("container_is_active"), SQLDataType.BOOLEAN.nullable(false).defaultValue(DSL.inline("1", SQLDataType.BOOLEAN)), this, "");

    /**
     * The column <code>seedstore_db.view_table_containers.trial_id</code>.
     */
    public final TableField<ViewTableContainersRecord, Integer> TRIAL_ID = createField(DSL.name("trial_id"), SQLDataType.INTEGER.defaultValue(DSL.inline("0", SQLDataType.INTEGER)), this, "");

    /**
     * The column <code>seedstore_db.view_table_containers.trial_name</code>.
     */
    public final TableField<ViewTableContainersRecord, String> TRIAL_NAME = createField(DSL.name("trial_name"), SQLDataType.VARCHAR(255), this, "");

    /**
     * The column
     * <code>seedstore_db.view_table_containers.trial_description</code>.
     */
    public final TableField<ViewTableContainersRecord, String> TRIAL_DESCRIPTION = createField(DSL.name("trial_description"), SQLDataType.CLOB, this, "");

    /**
     * The column <code>seedstore_db.view_table_containers.project_id</code>.
     */
    public final TableField<ViewTableContainersRecord, Integer> PROJECT_ID = createField(DSL.name("project_id"), SQLDataType.INTEGER.defaultValue(DSL.inline("0", SQLDataType.INTEGER)), this, "");

    /**
     * The column <code>seedstore_db.view_table_containers.project_name</code>.
     */
    public final TableField<ViewTableContainersRecord, String> PROJECT_NAME = createField(DSL.name("project_name"), SQLDataType.VARCHAR(255), this, "");

    /**
     * The column
     * <code>seedstore_db.view_table_containers.project_description</code>.
     */
    public final TableField<ViewTableContainersRecord, String> PROJECT_DESCRIPTION = createField(DSL.name("project_description"), SQLDataType.CLOB, this, "");

    /**
     * The column
     * <code>seedstore_db.view_table_containers.container_attributes</code>.
     */
    public final TableField<ViewTableContainersRecord, ContainerAttributeValue[]> CONTAINER_ATTRIBUTES = createField(DSL.name("container_attributes"), SQLDataType.JSON, this, "", new ContainerAttributeValueBinding());

    /**
     * The column
     * <code>seedstore_db.view_table_containers.sub_container_count</code>.
     */
    public final TableField<ViewTableContainersRecord, Long> SUB_CONTAINER_COUNT = createField(DSL.name("sub_container_count"), SQLDataType.BIGINT, this, "");

    /**
     * The column <code>seedstore_db.view_table_containers.created_on</code>.
     */
    public final TableField<ViewTableContainersRecord, Timestamp> CREATED_ON = createField(DSL.name("created_on"), SQLDataType.TIMESTAMP(0), this, "");

    private ViewTableContainers(Name alias, Table<ViewTableContainersRecord> aliased) {
        this(alias, aliased, null);
    }

    private ViewTableContainers(Name alias, Table<ViewTableContainersRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment("VIEW"), TableOptions.view("create view `view_table_containers` as select `c`.`id` AS `container_id`,`c`.`barcode` AS `container_barcode`,`c`.`description` AS `container_description`,`ct`.`id` AS `container_type_id`,`ct`.`name` AS `container_type_name`,`ct`.`description` AS `container_type_description`,`ct`.`icon` AS `container_type_icon`,`cp`.`id` AS `parent_id`,`cp`.`barcode` AS `parent_barcode`,`cp`.`description` AS `parent_description`,`ctp`.`id` AS `parent_container_type_id`,`ctp`.`name` AS `parent_container_type_name`,`ctp`.`description` AS `parent_container_type_description`,`ctp`.`icon` AS `parent_container_type_icon`,`c`.`is_active` AS `container_is_active`,`seedstore`.`trials`.`id` AS `trial_id`,`seedstore`.`trials`.`name` AS `trial_name`,`seedstore`.`trials`.`description` AS `trial_description`,`seedstore`.`projects`.`id` AS `project_id`,`seedstore`.`projects`.`name` AS `project_name`,`seedstore`.`projects`.`description` AS `project_description`,(select json_arrayagg(json_object('attributeId',`seedstore`.`attributes`.`id`,'attributeName',`seedstore`.`attributes`.`name`,'attributeValue',`seedstore`.`container_attributes`.`attribute_value`)) from (`seedstore`.`container_attributes` left join `seedstore`.`attributes` on((`seedstore`.`attributes`.`id` = `seedstore`.`container_attributes`.`attribute_id`))) where (`seedstore`.`container_attributes`.`container_id` = `c`.`id`)) AS `container_attributes`,(select count(1) from `seedstore`.`containers` `sc` where (`sc`.`parent_container_id` = `c`.`id`)) AS `sub_container_count`,`c`.`created_on` AS `created_on` from (((((`seedstore`.`containers` `c` left join `seedstore`.`containers` `cp` on((`c`.`parent_container_id` = `cp`.`id`))) left join `seedstore`.`container_types` `ct` on((`c`.`container_type_id` = `ct`.`id`))) left join `seedstore`.`container_types` `ctp` on((`cp`.`container_type_id` = `ctp`.`id`))) left join `seedstore`.`trials` on((`seedstore`.`trials`.`id` = `c`.`trial_id`))) left join `seedstore`.`projects` on((`seedstore`.`projects`.`id` = `c`.`project_id`)))"));
    }

    /**
     * Create an aliased <code>seedstore_db.view_table_containers</code> table
     * reference
     */
    public ViewTableContainers(String alias) {
        this(DSL.name(alias), VIEW_TABLE_CONTAINERS);
    }

    /**
     * Create an aliased <code>seedstore_db.view_table_containers</code> table
     * reference
     */
    public ViewTableContainers(Name alias) {
        this(alias, VIEW_TABLE_CONTAINERS);
    }

    /**
     * Create a <code>seedstore_db.view_table_containers</code> table reference
     */
    public ViewTableContainers() {
        this(DSL.name("view_table_containers"), null);
    }

    @Override
    public Schema getSchema() {
        return aliased() ? null : SeedstoreDb.SEEDSTORE_DB;
    }

    @Override
    public ViewTableContainers as(String alias) {
        return new ViewTableContainers(DSL.name(alias), this);
    }

    @Override
    public ViewTableContainers as(Name alias) {
        return new ViewTableContainers(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public ViewTableContainers rename(String name) {
        return new ViewTableContainers(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public ViewTableContainers rename(Name name) {
        return new ViewTableContainers(name, null);
    }
    // @formatter:on
}
