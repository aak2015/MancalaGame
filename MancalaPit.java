import javax.swing.*;
import java.awt.*;

public class MancalaPit extends Pit {
    public MancalaPit(String name, int initialBeadCount) {
        super(name, initialBeadCount);
        setPreferredSize(new Dimension(100, 300));
    }
} 