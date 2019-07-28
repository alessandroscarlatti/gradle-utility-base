package groovydemo;

import java.awt.AWTException;
import java.awt.CheckboxMenuItem;
import java.awt.Image;
import java.awt.Menu;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;


public class TrayIconDemo2 {

    public TrayIconDemo2() throws Exception {
        initComponents();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    new TrayIconDemo2();
                } catch (Exception ex) { System.out.println("Error - " + ex.getMessage()); }
            }
        });
    }

    private void initComponents() throws Exception {
        createAndShowTray();
    }

    private void createAndShowTray() throws Exception {
        //Check the SystemTray is supported
        if (!SystemTray.isSupported()) {
            System.out.println("SystemTray is not supported");
            return;
        }

        //get the system tray
        final SystemTray tray = SystemTray.getSystemTray();

        //retrieve icon form url and scale it to 32 x 32
        final TrayIcon trayIcon = new TrayIcon(ImageIO.read(getClass().getResourceAsStream("/jenkins.png"))
                .getScaledInstance(tray.getTrayIconSize().width, tray.getTrayIconSize().height, Image.SCALE_SMOOTH), null);

        PopupMenu popup = new PopupMenu();

        // Create a pop-up menu components
        MenuItem aboutItem = new MenuItem("About");
        aboutItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                JOptionPane.showMessageDialog(null, "About");
            }
        });

        final  CheckboxMenuItem cb1 = new CheckboxMenuItem("Show Tooltip");
        cb1.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent ie) {
                if(cb1.getState()==true) {
                trayIcon.setToolTip("Hello, world");
                }else {
                trayIcon.setToolTip("");
                }
            }
        });

        Menu displayMenu = new Menu("Display");

        MenuItem infoItem = new MenuItem("Info");
        //add actionlistner to Info menu item
        infoItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                JOptionPane.showMessageDialog(null, "Display Info of some sort :D");
            }
        });

        MenuItem exitItem = new MenuItem("Exit");
        //add actionlistner to Exit menu item
        exitItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {

                tray.remove(trayIcon);

//                System.exit(0);
            }
        });

        //Add components to pop-up menu
        popup.add(aboutItem);
        popup.addSeparator();
        popup.add(cb1);
        popup.addSeparator();
        popup.add(displayMenu);
        displayMenu.add(infoItem);
        popup.add(exitItem);

        trayIcon.setPopupMenu(popup);

        try {
            tray.add(trayIcon);


            trayIcon.displayMessage("Jenkins Build SUCCESS", "/**\n" +
                " * @author Alessandro Scarlatti\n" +
                " * @since Saturday , 7/27/2019\n" +
                " */\n" +
                "class LibClass1 {\n" +
                "\n" +
                "    void doStuff() {\n" +
                "        println \"stuff and things\"\n" +
                "    }\n" +
                "}\n", TrayIcon.MessageType.ERROR);
        } catch (AWTException e) {
            System.out.println("TrayIcon could not be added.");
        }
    }
}