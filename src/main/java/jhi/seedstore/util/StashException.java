package jhi.seedstore.util;

import jakarta.ws.rs.core.Response;

public class StashException extends Exception
{
	private Response.Status status;

	public StashException(Response.Status status)
	{
		this.status = status;
	}

	public StashException(Response.Status status, String message)
	{
		super(message);
		this.status = status;
	}

	public Response.Status getStatus()
	{
		return status;
	}
}
