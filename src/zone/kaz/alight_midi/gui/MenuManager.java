package zone.kaz.alight_midi.gui;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;

import java.util.HashMap;

/**
 * Created by kazz on 2015/09/28.
 */
public class MenuManager {

    private MenuBar menuBar = new MenuBar();
    private HashMap<String, MenuItem> itemMap = new HashMap<String, MenuItem>();
    private Menu menuFile = new Menu("_File");
    private Menu menuEdit = new Menu("_Edit");
    private Menu menuView = new Menu("_View");
    static MenuManager menuManager = new MenuManager();

    private MenuManager() {
        prepare();
    }

    private void prepare() {
        menuBar.getMenus().addAll(menuFile, menuEdit, menuView);
        menuFile.getItems().addAll(createItem("Preferences"));
        menuBar.setUseSystemMenuBar(true);
    }

    public static MenuManager getInstance() {
        return menuManager;
    }

    public MenuBar getMenuBar() {
        return menuBar;
    }

    public MenuItem getItem(String name) {
        return itemMap.get(name);
    }

    private MenuItem createItem(String name) {
        MenuItem item = new MenuItem(name);
        itemMap.put(name, item);
        return item;
    }
}
