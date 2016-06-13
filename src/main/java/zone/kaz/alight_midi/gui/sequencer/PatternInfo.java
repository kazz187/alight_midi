package zone.kaz.alight_midi.gui.sequencer;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FilenameUtils;
import zone.kaz.alight_midi.sequencer.StepSequencerManager;
import zone.kaz.alight_midi.sequencer.StepSequencerPattern;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.Semaphore;
import java.util.stream.Collectors;

public class PatternInfo implements SequencerInfo {

    private String filePath;
    private String patternName;

    public PatternInfo(String filePath) {
        this.filePath = filePath;
        this.patternName = FilenameUtils.getBaseName(filePath);
    }

    public void loadPattern(StepSequencerController controller, StepSequencerManager manager) {
        if (patternName.equals(controller.getPatternName())) {
            return;
        }
        Semaphore stepSequencerSemaphore = manager.getStepSequencerSemaphore();
        try {
            stepSequencerSemaphore.acquire();
            StepSequencerPattern currentPattern = manager.getPattern();
            String json = Files.readAllLines(Paths.get(filePath)).stream().collect(Collectors.joining());
            ObjectMapper objectMapper = new ObjectMapper();
            StepSequencerPattern pattern = objectMapper.readValue(json, StepSequencerPattern.class);
            while (currentPattern.getSize() != 0) {
                currentPattern.remove();
            }
            // TODO: Refactor
            int i = 0;
            for (StepSequencer stepSequencer : pattern.getStepSequencerList()) {
                stepSequencer.setController(controller);
                stepSequencer.setRowIndex(i++);
                stepSequencer.initGridPane();
                stepSequencer.setButtonWidth(controller.getColWidth());
                stepSequencer.updateSequencerInfo();
                stepSequencer.setClock(pattern.getCalcClock(), pattern.getBeats());
                stepSequencer.updateButtons();
            }
            pattern.setClock(pattern.getClock());

            manager.setPattern(pattern);
            controller.setRate(pattern.getRate());
            controller.setClock(pattern.getClock());
            controller.setPatternName(toString());
            stepSequencerSemaphore.release();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return patternName;
    }

}
