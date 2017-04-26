package br.com.fxmore.component.element;

import javafx.scene.canvas.GraphicsContext;

/**
 * Created by dmartinez on 23/04/2017.
 */
public abstract class FXMElement implements Comparable<FXMElement> {
    private Integer x;
    private Integer y;
    private Integer width;
    private Integer height;
    private Integer index;  //ordem de sobreposicao (maior valor = topo)
    private Boolean selected;

    abstract public FXMElementType getType();
    abstract public void draw(GraphicsContext gc);
    abstract public void redim(int x, int y);
    public Boolean move(int factorX, int factorY,int maxWidth,int maxHeight){
        if(getX()+factorX>0 && getY()+factorY>0 && getY()+getHeight()+factorY<maxHeight && getX()+getWidth()+factorX<maxWidth) {
            setX(getX() + factorX);
            setY(getY() + factorY);
            return true;
        }
        return false;
    }

    public Boolean isTouched(Integer x, Integer y){
        return (x>=getX() && x<=getX()+getWidth() && y>=getY() && y<=getY()+getHeight());
    }

    public boolean isDotTouched(int x, int y){
        int lx = (getX() + getWidth());
        int ly = (getY() + getHeight());
        //System.out.println("dotX: "+lx+"   dotY: "+ly);
        return (x>=lx-7 && x<=lx+9 && y>=ly-7 && y<=ly+9);
    };

    @Override
    public int compareTo(FXMElement other) {
        if (this.index < other.getIndex()) return -1;
        else if (this.index > other.getIndex()) return 1;
        else return 0;
    }

    public Integer getX() {
        return x;
    }

    public void setX(Integer x) {
        this.x = x;
    }

    public Integer getY() {
        return y;
    }

    public void setY(Integer y) {
        this.y = y;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }


    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Boolean getSelected() {
        return selected;
    }

    public void setSelected(Boolean selected) {
        this.selected = selected;
    }

}
