package jhi.seedstore.pojo;

import jhi.seedstore.database.codegen.tables.pojos.ViewTableContainers;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.List;

@Getter
@Setter
@Accessors(chain = true)
public class ContainerImport
{
	private Integer                   parentContainerId;
	private List<ViewTableContainers> items;
}
