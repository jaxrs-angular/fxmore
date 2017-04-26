package br.com.fxmore.component.element;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Created by dmartinez on 24/04/2017.
 */
public class FXMImage extends FXMElement {
    private Image image;
    private Integer imageWidth;
    private Integer imageHeight;

    public FXMImage(Image image,Integer x, Integer y, Integer width, Integer height, Integer index) {
        this.setX(x);
        this.setY(y);
        this.setWidth(width);
        this.setHeight(height);
        this.setIndex(index);
        this.image = image;
        this.imageWidth = ((Double)image.getWidth()).intValue();
        this.imageHeight = ((Double)image.getHeight()).intValue();
        this.setSelected(true);

    }

    @Override
    public FXMElementType getType() {
        return FXMElementType.IMAGE;
    }

    @Override
    public void draw(GraphicsContext gc) {
        gc.drawImage(image,getX(),getY(),getWidth(),getHeight());
        if(getSelected()){
            gc.strokeRect(getX(),getY(),getWidth(),getHeight());
            gc.fillRect(getX()+getWidth()-5,getY()+getHeight()-5,11,11);
        }
    }

    @Override
    public void redim(int x, int y) {
        if(x>getX()+10 && y>getY()+10) {
            Integer maxWitdh = x - getX();
            Integer maxHeight = y - getY();
            // redimensionando pela largura
            Double factor = (new Double(maxWitdh))/(new Double(getWidth()));
            if(getHeight()*factor<=maxHeight){
                Integer nw = ((Double)(getWidth()*factor)).intValue();
                Integer dif = nw-getWidth();
                setWidth(nw);
                setHeight(getHeight()+dif);
            }else{
                factor = new Double(maxHeight)/new Double(getHeight());
                Integer nh = ((Double)(getHeight()*factor)).intValue();
                Integer dif = nh-getHeight();
                setHeight(nh);
                setWidth(getWidth()+dif);
            }

        }

    }

    public void changeImage(Image image){
        this.image = image;
        Double ih = new Double(image.getHeight());
        Double iw = new Double(image.getWidth());
        // redimensionando pela largura
        Double factor = (new Double(getWidth())/iw);
        if(factor*ih>getHeight()){
            factor = new Double(getHeight())/ih;
        }
        setWidth(((Double)(iw*factor)).intValue());
        setHeight(((Double)(ih*factor)).intValue());

    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }
}
