package zone.kaz.alight_midi.inject;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import zone.kaz.alight_midi.device.MidiDeviceManager;

public class AlightModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(MidiDeviceManager.class).in(Singleton.class);
    }
}
