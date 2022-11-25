package jhi.seedstore;

import jakarta.ws.rs.ApplicationPath;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;

@ApplicationPath("/api/")
public class Seedstore extends ResourceConfig
{
	public Seedstore()
	{
		packages("jhi.seedstore");

		register(MultiPartFeature.class);
	}
}
