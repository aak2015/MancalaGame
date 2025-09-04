import java.awt.Color;

public class brightStyle implements BoardStyleStrategy {
    @Override
    public void stylePit(Pit pit) {
        pit.setStyle(new Color(255, 255, 200), Color.BLACK);
    }
} 