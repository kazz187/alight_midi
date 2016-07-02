package zone.kaz.alight_midi.sequencer.animation.util;

import java.util.Random;

public class RandomColor {

    private final Color[] colorArray;
    private Random rand = new Random();

    public RandomColor(Color[] colorArray) {
        this.colorArray = colorArray;
    }

    public Color getNext() {
        int randInt = rand.nextInt(colorArray.length);
        return colorArray[randInt];
    }

}
