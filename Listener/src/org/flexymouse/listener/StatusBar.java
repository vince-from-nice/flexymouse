package org.flexymouse.listener;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

import org.flexymouse.listener.event.AbstractEvent;
import org.flexymouse.listener.event.ButtonClickEvent;
import org.flexymouse.listener.event.OrientationEvent;
import org.flexymouse.listener.view.EarthModelView;
import org.flexymouse.listener.view.PhoneModelView;

public class StatusBar extends JPanel implements ActionListener {

    private static final long serialVersionUID = 7757619774188142735L;

    Application listener;

    JLabel connectionLabel;

    JLabel orientationLabel;

    JLabel buttonsLabel;

    JComboBox<String> viewComboBox;

    private static String[] views = new String[] { EarthModelView.NAME, PhoneModelView.NAME };

    public StatusBar(Application listener) {
        super();

        this.listener = listener;

        BoxLayout layout = new BoxLayout(this, BoxLayout.X_AXIS);
        this.setBorder(BorderFactory.createEmptyBorder(2, 10, 0, 10));
        this.setLayout(layout);

        JLabel label1 = new JLabel("Select model : ");
        label1.setAlignmentX(LEFT_ALIGNMENT);
        this.add(label1);

        viewComboBox = new JComboBox<String>(views);
        viewComboBox.setMaximumSize(viewComboBox.getPreferredSize());
        viewComboBox.addActionListener(this);
        this.add(viewComboBox);
        this.add(Box.createRigidArea(new Dimension(20, 0)));

        orientationLabel = new JLabel("Yaw: 0° Pitch: 0° Roll: 0°");
        orientationLabel.setForeground(Color.BLUE);
        this.add(orientationLabel);
        this.add(Box.createRigidArea(new Dimension(20, 0)));

        buttonsLabel = new JLabel(" ");
        buttonsLabel.setForeground(Color.ORANGE);
        buttonsLabel.setSize(200, 30);
        this.add(buttonsLabel);
        this.add(Box.createRigidArea(new Dimension(20, 0)));

        connectionLabel = new JLabel("No FlexyMouse connected");
        connectionLabel.setForeground(Color.RED);
        // int w = 300;
        // connectionLabel.setMinimumSize(new Dimension(w, 0));
        // connectionLabel.setPreferredSize(new Dimension(w, 0));
        // connectionLabel.setMaximumSize(new Dimension(w, 0));
        // connectionLabel.setHorizontalTextPosition(JLabel.RIGHT);
        // connectionLabel.setAlignmentX(RIGHT_ALIGNMENT);
        this.add(Box.createHorizontalGlue());
        this.add(connectionLabel);

        // layout.putConstraint(SpringLayout.WEST, viewComboBox, 5, SpringLayout.WEST, this);
        // layout.putConstraint(SpringLayout.EAST, connectionLabel, 5, SpringLayout.EAST, this);
    }

    public void actionPerformed(ActionEvent e) {
        JComboBox cb = (JComboBox) e.getSource();
        this.listener.changeModelView((String) cb.getSelectedItem());
    }

    public void updateConnectionStatus(boolean connected, String address) {
        if (connected) {
            this.connectionLabel.setText("Connected to " + address);
            this.connectionLabel.setForeground(Color.GREEN);
        } else {
            this.connectionLabel.setText("Not connected");
            this.connectionLabel.setForeground(Color.RED);
        }
    }

    public void onFlexyMouseEvent(AbstractEvent event) {
        if (event instanceof ButtonClickEvent) {
            this.buttonsLabel.setText("Click on button" + ((ButtonClickEvent) event).getButtonNumber() + " !!");
            Timer t = new Timer(1000, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    buttonsLabel.setText(null);
                }
            });
            t.setRepeats(false);
            t.start();
        } else if (event instanceof OrientationEvent) {
            OrientationEvent e = (OrientationEvent) event;
            updateOrientationLabel("Yaw: " + (int) e.getAzimuth() + "° Pitch: " + (int) e.getPitch() + "° Roll: "
                    + (int) e.getRoll() + "°");
        }
    }

    public void updateOrientationLabel(String newLabel) {
        this.orientationLabel.setText(newLabel);
    }
}
