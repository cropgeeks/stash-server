/*
 * This file is generated by jOOQ.
 */
package jhi.seedstore.database.codegen;


import java.util.Arrays;
import java.util.List;

import jhi.seedstore.database.codegen.tables.Attributes;
import jhi.seedstore.database.codegen.tables.ContainerAttributes;
import jhi.seedstore.database.codegen.tables.ContainerTypes;
import jhi.seedstore.database.codegen.tables.Containers;
import jhi.seedstore.database.codegen.tables.Projects;
import jhi.seedstore.database.codegen.tables.TransferLogs;
import jhi.seedstore.database.codegen.tables.Trials;
import jhi.seedstore.database.codegen.tables.Users;
import jhi.seedstore.database.codegen.tables.ViewTableContainers;
import jhi.seedstore.database.codegen.tables.ViewTableTransfers;

import org.jooq.Catalog;
import org.jooq.Table;
import org.jooq.impl.SchemaImpl;


// @formatter:off
/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class SeedstoreDb extends SchemaImpl {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>seedstore_db</code>
     */
    public static final SeedstoreDb SEEDSTORE_DB = new SeedstoreDb();

    /**
     * No further instances allowed
     */
    private SeedstoreDb() {
        super("seedstore_db", null);
    }


    @Override
    public Catalog getCatalog() {
        return DefaultCatalog.DEFAULT_CATALOG;
    }

    @Override
    public final List<Table<?>> getTables() {
        return Arrays.asList(
            Attributes.ATTRIBUTES,
            ContainerAttributes.CONTAINER_ATTRIBUTES,
            ContainerTypes.CONTAINER_TYPES,
            Containers.CONTAINERS,
            Projects.PROJECTS,
            TransferLogs.TRANSFER_LOGS,
            Trials.TRIALS,
            Users.USERS,
            ViewTableContainers.VIEW_TABLE_CONTAINERS,
            ViewTableTransfers.VIEW_TABLE_TRANSFERS
        );
    }
    // @formatter:on
}
