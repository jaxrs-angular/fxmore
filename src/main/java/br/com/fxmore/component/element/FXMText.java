package br.com.fxmore.component.element;

import com.sun.org.apache.xpath.internal.operations.Bool;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 * Created by dmartinez on 23/04/2017.
 */
public class FXMText extends FXMElement {

    private String text;
    private Font font;
    private boolean bold;
    private boolean underline;
    private boolean italic;

    public FXMText(String text, Integer x, Integer y, Font font, Boolean bold, Boolean underline, Boolean italic, Integer index) {
        this.setX(x);
        this.setY(y);
        this.setIndex(index);
        this.text = text;
        this.font = font;
        this.bold = bold;
        this.underline = underline;
        this.italic = italic;
        this.setWidth(((Double)getTextWidth(text)).intValue()+10);
        this.setHeight(60);
        this.setSelected(true);

    }


    @Override
    public FXMElementType getType() {
        return FXMElementType.TEXT;
    }

    @Override
    public void draw(GraphicsContext gc) {
        gc.save();
        gc.beginPath();
        gc.rect(this.getX(),this.getY(),this.getWidth(),this.getHeight());
        gc.closePath();
        gc.clip();
        gc.setFont(font);
        gc.setFill(Color.BLACK);
        gc.fillText(text,getX(),getY()+getFont().getSize());
        gc.restore();
        if(getSelected()){
            gc.strokeRect(getX(),getY(),getWidth(),getHeight());
            gc.fillRect(getX()+getWidth()-5,getY()+getHeight()-5,11,11);
        }

    }

    @Override
    public void redim(int x, int y) {
        if(y-getY()>10 && x-getX()>10) {
            setWidth(x - getX());
            setHeight(y - getY());
        }
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Font getFont() {
        return font;
    }

    public void setFont(Font font) {
        this.font = font;
    }

    public boolean isBold() {
        return bold;
    }

    public void setBold(boolean bold) {
        this.bold = bold;
    }

    public boolean isUnderline() {
        return underline;
    }

    public void setUnderline(boolean underline) {
        this.underline = underline;
    }

    public boolean isItalic() {
        return italic;
    }

    public void setItalic(boolean italic) {
        this.italic = italic;
    }

    public double getTextWidth(String text){
        final Text t = new Text(text);
        t.setFont(font);
        return t.getLayoutBounds().getWidth();
    }

    public double getTextHeight(String text){
        final Text t = new Text(text);
        t.setFont(font);
        return t.getLayoutBounds().getHeight();
    }

}
