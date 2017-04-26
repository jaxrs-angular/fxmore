package br.com.fxmore.component.element;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

/**
 * Created by dmartinez on 24/04/2017.
 */
public class FXMLine extends FXMElement{
    private Boolean single;
    private Integer lineWidth;

    public FXMLine(Integer x, Integer y, Integer index) {
        this.setX(x);
        this.setY(y);
        this.setIndex(index);
        this.lineWidth=2;
        this.setWidth(60);
        this.setHeight(12);
        this.single = true;
        this.setSelected(true);
    }

    @Override
    public FXMElementType getType() {
        return FXMElementType.LINE;
    }

    @Override
    public void draw(GraphicsContext gc) {
        gc.setLineWidth(lineWidth);
        if(single){
            gc.setStroke(Color.BLACK);
            gc.beginPath();
            gc.moveTo(getX(),getY()+5);
            gc.lineTo(getX()+getWidth(),getY()+5);
            gc.closePath();
            gc.stroke();

        }else{
            gc.setStroke(Color.BLACK);
            gc.beginPath();
            gc.moveTo(getX(),getY()+lineWidth+2);
            gc.lineTo(getX()+getWidth(),getY()+lineWidth+2);
            gc.closePath();
            gc.stroke();
            gc.beginPath();
            gc.moveTo(getX()+getWidth(),getY()+2+(lineWidth*3));
            gc.lineTo(getX(),getY()+2+(lineWidth*3));
            gc.closePath();
            gc.stroke();
        }
        if(getSelected()){
            gc.setLineWidth(1);
            gc.strokeRect(getX(),getY(),getWidth(),getHeight());
            gc.fillRect(getX()+getWidth()-5,getY()+getHeight()-5,11,11);
        }
    }

    @Override
    public void redim(int x, int y) {
        if(y-getY()>10) {
            setWidth(x - getX());
        }
    }

    public Boolean getSingle() {
        return single;
    }

    public void setSingle(Boolean single) {
        this.single = single;
    }

    public Integer getLineWidth() {
        return lineWidth;
    }

    public void setLineWidth(Integer lineWidth) {
        this.lineWidth = lineWidth;
        this.setHeight(2+lineWidth*5);
    }
}
