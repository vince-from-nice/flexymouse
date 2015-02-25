package org.flexymouse.listener.behavior;

import javax.media.j3d.Behavior;

import org.flexymouse.listener.Application;
import org.flexymouse.listener.view.AbstractModelView;

public abstract class AbstractBehavior extends Behavior {
    
    private Application app;
    
    public AbstractBehavior(Application a) {
        this.app = a;
    }

    public Application getApplication() {
        return this.app;
    }
    
    public AbstractModelView getCurrentView() {
        return this.app.getCurrentView();
    }

}
