package br.com.fxmore.component.element;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.text.Font;

/**
 * Created by dmartinez on 24/04/2017.
 */
public class FXMLabel extends FXMText {

    public FXMLabel(String text, Integer x, Integer y, Font font,  Boolean bold, Boolean underline, Boolean italic, Integer index) {
        super(text,x,y,font,bold,underline,italic,index);
        this.setWidth(((Double) getTextWidth(text)).intValue()+10);
        this.setHeight(((Double) getTextHeight(text)).intValue()+10);

    }

    @Override
    public FXMElementType getType() {
        return FXMElementType.LABEL;
    }

    @Override
    public void redim(int x, int y) {
        if(y-getY()>10) {
            setWidth(x - getX());
        }

    }
}
