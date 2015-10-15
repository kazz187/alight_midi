package zone.kaz.alight_midi.inject;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;

public class DIContainer {

    private static DIContainer instance = new DIContainer();
    private AbstractModule module = null;
    private Injector injector = null;

    private DIContainer() {}

    private void _setModule(AbstractModule module) {
        this.module = module;
        injector = Guice.createInjector(module);
    }

    private Injector _getInjector() {
        return injector;
    }

    public static void setModule(AbstractModule module) {
        instance._setModule(module);
    }

    public static Injector getInjector() {
        return instance._getInjector();
    }

    public static <T> T get(Class<T> clazz) {
        return getInjector().getInstance(clazz);
    }

}
