import java.awt.Color;

public class oldStyle implements BoardStyleStrategy {
    @Override
    public void stylePit(Pit pit) {
        pit.setStyle(new Color(210, 180, 140), Color.BLACK);
    }
} 