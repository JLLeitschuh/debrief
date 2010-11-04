package org.mwc.asset.comms.restlet;

import java.io.File;

import org.mwc.asset.comms.restlet.test.data.ContactServerResource;
import org.restlet.Application;
import org.restlet.Component;
import org.restlet.Restlet;
import org.restlet.data.LocalReference;
import org.restlet.data.Protocol;
import org.restlet.resource.Directory;
import org.restlet.routing.Router;

public class TestServerApplication extends Application {

    /**
     * When launched as a standalone application.
     * 
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        Component component = new Component();
        component.getClients().add(Protocol.FILE);
        component.getServers().add(Protocol.HTTP, 8080);
        component.getDefaultHost().attach(new TestServerApplication());
        component.start();
    }

    @Override
    public Restlet createInboundRoot() {
        Router router = new Router(getContext());
        getConnectorService().getClientProtocols().add(Protocol.FILE);

        // Serve the files generated by the GWT compilation step.
        File warDir = new File("");
        if (!"war".equals(warDir.getName())) {
            warDir = new File(warDir, "war/");
        }

        Directory dir = new Directory(getContext(), LocalReference
                .createFileReference(warDir));
        router.attachDefault(dir);
        router.attach("/contacts/{user}", ContactServerResource.class);

        return router;
    }

}
