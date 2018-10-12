package zone.kaz.alight_midi.sequencer.animation.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import zone.kaz.alight_midi.sequencer.Animation;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class ParamsLoader<T> {

    private final Animation animation;
    protected final static ObjectMapper objectMapper = new ObjectMapper();

    public ParamsLoader(Animation animation) {
        this.animation = animation;
    }

    public T load(Class<T> clazz) {
        if (!animation.getParams().equals("")) {
            try {
                return objectMapper.readValue(animation.getParams(), clazz);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            return clazz.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }
}
