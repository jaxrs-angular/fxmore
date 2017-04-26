package br.com.fxmore.component;

import br.com.fxmore.component.element.FXMElement;
import javafx.scene.paint.Color;

import java.util.*;

/**
 * Created by dmartinez on 23/04/2017.
 */
public class FXMBand {
    private FXMBandType bandType;
    private String name;
    private Color color;
    private Integer width;
    private Integer height;
    private List<FXMElement> elements;

    public FXMBand(FXMBandType bandType, String name, Color color, Integer width, Integer height) {
        this.bandType = bandType;
        this.name = name;
        this.color = color;
        this.width = width;
        this.height = height;
        elements = new ArrayList<>();
    }

    public Color getColor() {
        return color;
    }

    public String getName() {
        return name;
    }

    public List<FXMElement> getElements() {
        return elements;
    }

}
