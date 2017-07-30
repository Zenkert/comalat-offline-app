package comalat.Application.RestAPI.Filters;

import comalat.Services.FolderServices.FolderManager;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.ext.Provider;

/**
 *
 * @author SyleSakis
 */
@Provider
public class MainFolderFilter implements ContainerRequestFilter {

    private static final String RESTAPI_URL_PREFIX1 = "languages";
    private static final String RESTAPI_URL_PREFIX2 = "data";

    @Override
    public void filter(ContainerRequestContext requestContext) {
        if (requestContext.getUriInfo().getPath().contains(RESTAPI_URL_PREFIX1)
                || requestContext.getUriInfo().getPath().contains(RESTAPI_URL_PREFIX2)) {
            FolderManager.createMainFolders();
        }
    }

}
