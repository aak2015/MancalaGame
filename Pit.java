import javax.swing.*;
import java.awt.*;

public class Pit extends JPanel {
    private String name;
    private int beadCount;
    private static final int BEAD_DIAM = 20;
    private static final Point[] SLOTS = {
            new Point(10, 20),
            new Point(40, 20),
            new Point(70, 20),
            new Point(25, 55),
            new Point(55, 55)
    };


    public Pit(String name, int initialBeadCount) {
        this.name = name;
        this.beadCount = initialBeadCount;
        setLayout(null);
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
        setPreferredSize(new Dimension(100, 100));
        setOpaque(true);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        g.setColor(Color.BLACK);
        g.setFont(new Font("Serif", Font.BOLD, 14));
        FontMetrics fm = g.getFontMetrics();
        int textW = fm.stringWidth(name);
        g.drawString(name, (getWidth() - textW) / 2, fm.getAscent() + 2);

        g.setColor(Color.LIGHT_GRAY);
        for (Point p : SLOTS) {
            g.drawOval(p.x, p.y, BEAD_DIAM, BEAD_DIAM);
        }
        g.setColor(Color.BLUE);
        for (int i = 0; i < beadCount && i < SLOTS.length; i++) {
            Point p = SLOTS[i];
            g.fillOval(p.x, p.y, BEAD_DIAM, BEAD_DIAM);
        }

        if(getBeadCount() > SLOTS.length){
            g.setColor(Color.BLACK);
            g.setFont(new Font("Serif", Font.BOLD, 16));
            String beadCount = "" + getBeadCount();
            FontMetrics beadCountFM = g.getFontMetrics();
            int countWidth = beadCountFM.stringWidth(beadCount);

            int x = getWidth() - countWidth - 5;
            int y = getHeight() - 5;

            g.drawString(beadCount, x, y);
        }
    }

    public void updateBeadCount(int count) {
        this.beadCount = count;
        repaint();
    }

    public int getBeadCount() {
        return beadCount;
    }

    public void setStyle(Color backgroundColor, Color textColor) {
        setBackground(backgroundColor);
    }
}
