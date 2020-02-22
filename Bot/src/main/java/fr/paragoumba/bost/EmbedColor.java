package fr.paragoumba.bost;

import java.awt.*;

public class EmbedColor extends Color {

    public static final EmbedColor SUCCESS = new EmbedColor(119, 178, 85);
    public static final EmbedColor INFO = new EmbedColor(0, 170, 255);
    public static final EmbedColor ERROR = new EmbedColor(Color.RED);

    public EmbedColor(Color color){

        super(color.getRGB());

    }

    public EmbedColor(int r, int g, int b){

        super(r, g, b);

    }
}
