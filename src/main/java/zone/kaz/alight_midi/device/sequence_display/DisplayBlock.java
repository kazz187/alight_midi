package zone.kaz.alight_midi.device.sequence_display;

public abstract class DisplayBlock {

    boolean isLightUp = false;

    public void light_up() {
        if (isLightUp) {
            return;
        }
        light_up_process();
        isLightUp = true;
    }
    public void light_down() {
        if (!isLightUp) {
            return;
        }
        light_down_process();
        isLightUp = false;
    }

    abstract void light_up_process();
    abstract void light_down_process();

}
