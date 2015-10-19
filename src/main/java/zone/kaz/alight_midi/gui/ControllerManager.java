package zone.kaz.alight_midi.gui;

import com.google.inject.Singleton;
import javafx.fxml.Initializable;

import java.util.HashMap;

@Singleton
public class ControllerManager {

    HashMap<Class<? extends Initializable>, Initializable> controllers = new HashMap<>();

    public void register(Initializable controller) {
        controllers.put(controller.getClass(), controller);
    }

    public Initializable get(Class<? extends Initializable> clazz) {
        return controllers.get(clazz);
    }

}
