package fr.paragoumba.bost;

public class Color extends java.awt.Color {

    public static final Color SUCCESS = new Color(119, 178, 85);
    public static final Color INFO = new Color(0, 170, 255);
    public static final Color ERROR = new Color(java.awt.Color.RED);

    public Color(java.awt.Color color){

        super(color.getRGB());

    }

    public Color(int r, int g, int b){

        super(r, g, b);

    }
}
