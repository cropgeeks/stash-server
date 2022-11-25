package jhi.seedstore.resource.base;

import jakarta.servlet.http.*;
import jakarta.ws.rs.core.*;

public class ContextResource
{
	@Context
	protected SecurityContext     securityContext;
	@Context
	protected HttpServletRequest  req;
	@Context
	protected HttpServletResponse resp;
}
