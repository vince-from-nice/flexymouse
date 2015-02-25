package org.flexymouse.listener;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Map;

import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.Group;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.flexymouse.listener.behavior.KeyboardBehaviour;
import org.flexymouse.listener.behavior.MouseBehaviour;
import org.flexymouse.listener.view.AbstractModelView;
import org.flexymouse.listener.view.EarthModelView;
import org.flexymouse.listener.view.PhoneModelView;

import com.sun.j3d.utils.universe.SimpleUniverse;

public class Application extends JFrame {

    private static final long serialVersionUID = 0;

    private AbstractModelView currentView;

    private SimpleUniverse universe;

    private BranchGroup rootBranchGraph;

    private Canvas3D canvas3d;

    private StatusBar statusBar;

    private KeyboardBehaviour keyboardBehaviour;

    private MouseBehaviour mouseBehavior;

    private BranchGroup keyboardBehaviourBG, mouseBehaviorBG;

    private static final String NAME = "FlexyMouseListener";

    private static final String VERSION = "0.2";

    private static final String TITLE = NAME + " " + VERSION;

    private static final int DEFAULT_WINDOWS_WIDTH = 750;

    private static final int DEFAULT_WINDOWS_HEIGHT = 550;

    static private Map<String, AbstractModelView> views;

    public static void main(String[] args) {
        System.out.println(TITLE + " is starting..");
        // new MainFrame(app, , 550);
        Application app = new Application();
        app.setTitle(NAME);
        app.setSize(DEFAULT_WINDOWS_WIDTH, DEFAULT_WINDOWS_HEIGHT);
        app.init();
        app.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent winEvent) {
                System.exit(0);
            }
        });
    }

    public void init() {
        // Init the canvas
        this.canvas3d = new Canvas3D(SimpleUniverse.getPreferredConfiguration());
        // Map props = canvas3D.queryProperties();
        // System.out.println("Canvas3D properties: " + props);
        this.canvas3d.setStereoEnable(false);

        // Init the GUI
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());
        this.add("Center", this.canvas3d);
        this.statusBar = new StatusBar(this);
        add("South", statusBar);
        contentPane.validate();
        // this.pack();
        this.setVisible(true);

        // Create the universe, oh yeah :)
        this.universe = new SimpleUniverse(this.canvas3d);

        // Init the root BG
        this.rootBranchGraph = new BranchGroup();
        this.rootBranchGraph.setCapability(Group.ALLOW_CHILDREN_READ);
        this.rootBranchGraph.setCapability(Group.ALLOW_CHILDREN_WRITE);
        this.rootBranchGraph.setCapability(Group.ALLOW_CHILDREN_EXTEND);
        this.rootBranchGraph.setCapability(BranchGroup.ALLOW_DETACH);
        this.universe.addBranchGraph(this.rootBranchGraph);

        // Init the model views
        Application.views = new HashMap<String, AbstractModelView>();
        Application.views.put(EarthModelView.NAME, new EarthModelView());
        Application.views.put(PhoneModelView.NAME, new PhoneModelView());

        // Init behaviors with their branch group
        this.keyboardBehaviour = new KeyboardBehaviour(this);
        this.keyboardBehaviour.setSchedulingBounds(new BoundingSphere());
        this.mouseBehavior = new MouseBehaviour(this);
        this.mouseBehavior.setSchedulingBounds(new BoundingSphere());
        this.keyboardBehaviourBG = new BranchGroup();
        this.keyboardBehaviourBG.setCapability(BranchGroup.ALLOW_DETACH);
        this.keyboardBehaviourBG.addChild(this.keyboardBehaviour);
        this.mouseBehaviorBG = new BranchGroup();
        this.mouseBehaviorBG.setCapability(BranchGroup.ALLOW_DETACH);
        this.mouseBehaviorBG.addChild(this.mouseBehavior);

        // Set current view model
        this.changeModelView(EarthModelView.NAME);

        // Launch a thread for listening FlexyMouse
        try {
            (new Thread(new Connection(this, Connection.DEFAULT_PORT))).start();
        } catch (SocketException e) {
            System.out.println("Unable to open connection on port " + Connection.DEFAULT_PORT);
            e.printStackTrace();
        }
    }

    public void changeModelView(String viewName) {
        if (this.currentView != null && this.currentView.getName() == viewName) {
            return;
        }
        AbstractModelView newView = null;
        for (AbstractModelView v : Application.views.values()) {
            if (v.getName() == viewName) {
                newView = v;
            }
        }
        if (newView != null) {
            AbstractModelView oldView = this.currentView;
            this.currentView = newView;
            System.out.println("Replacing " + (oldView != null ? oldView.getName() : "null") + " by "
                    + newView.getName());

            // Remove old view BG from the root BG
            if (oldView != null) {
                oldView.getBranchGroup().detach();
                this.rootBranchGraph.removeChild(oldView.getBranchGroup());
            }

            // Add new view BG to root BG
            this.rootBranchGraph.addChild(this.currentView.getBranchGroup());

            // Remove behaviors from the branch group of the old view
            if (oldView != null) {
                oldView.getBranchGroup().removeChild(this.mouseBehaviorBG);
                oldView.getBranchGroup().removeChild(this.keyboardBehaviourBG);
            }

            // Add behaviors into the branch group of the new view
            this.currentView.getBranchGroup().addChild(this.mouseBehaviorBG);
            this.currentView.getBranchGroup().addChild(this.keyboardBehaviourBG);

            this.currentView.initviewPlatform(universe);
            this.revalidate();
        }
    }

    public void checkClose() {
        if (JOptionPane.YES_OPTION == JOptionPane.showConfirmDialog(this, "Sure you want to close?")) {
            // dispose();
            System.exit(0);
        }
    }

    public AbstractModelView getCurrentView() {
        return currentView;
    }

    public StatusBar getStatusBar() {
        return statusBar;
    }
}
