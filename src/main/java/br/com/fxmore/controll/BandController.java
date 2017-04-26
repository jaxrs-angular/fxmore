package br.com.fxmore.controll;

import br.com.fxmore.component.FXMBand;
import br.com.fxmore.component.element.*;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.Border;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.PopupWindow;
import javafx.stage.Stage;

import java.io.File;
import java.util.List;

/**
 * Created by dmartinez on 24/04/2017.
 */
public class BandController {
    private final GraphicsContext gc;
    private final Canvas cv;
    private final Controller controller;
    private List<FXMElement> children;
    private FXMBand band;
    private Font font;

    public BandController(Canvas cv, FXMBand band, Controller controller) {
        this.cv = cv;
        this.band = band;
        this.controller = controller;
        this.children = band.getElements();
        this.gc = cv.getGraphicsContext2D();

        font = Font.font("Arial Rounded MT Bold", 18);
        this.draw();
    }

    public void draw() {
        gc.clearRect(0, 0, cv.getWidth(), cv.getHeight());
        drawBackground();
        drawElements();
    }

    private void drawElements() {
        for (FXMElement element : band.getElements()) {
            element.draw(gc);
        }

    }

    private void drawBackground() {
        gc.setFill(this.band.getColor());
        gc.fillRect(0, 0, cv.getWidth(), cv.getHeight());
        gc.setFill(Color.BLACK);
        gc.setFont(font);
        gc.fillText(band.getName(), cv.getWidth() - getTextWidth(band.getName()) - 10, 20);
    }

    private double getTextWidth(String text) {
        final Text t = new Text(text);
        t.setFont(font);
        return t.getLayoutBounds().getWidth();
    }

    public void add(FXMElement t) {
        for (FXMElement element : band.getElements()) {
            element.setSelected(false);
        }
        int last = band.getElements().size() + 1;
        t.setIndex(last);
        band.getElements().add(t);

    }

    public FXMElement click(Integer x, Integer y, Boolean secondary, Canvas canvas, Integer screenX, Integer screenY) {
        for (FXMElement element : band.getElements()) {
            if (element.isTouched(x, y)) {
                if (element.getSelected()) {
                    if (secondary) {
                        if (element instanceof FXMText || element instanceof FXMLabel) {
                            showFontDialog(x, y, canvas, screenX, screenY, (FXMText) element);
                        }else if (element instanceof FXMImage) {
                            File url = controller.getFileChooser().showOpenDialog(controller.getTextArea().getScene().getWindow());
                            Image image = new Image("File:/" + url.getAbsolutePath());
                            ((FXMImage) element).changeImage(image);
                        }else if (element instanceof FXMLine) {
                            showLineDialog(x, y, canvas, screenX, screenY, (FXMLine) element);
                        }
                    } else {
                        if (element instanceof FXMLabel) {
                            showTextField(element);
                        }else if (element instanceof FXMText) {
                            showTextArea(element);
                        }
                    }
                    return element;
                } else {
                    element.setSelected(true);
                    return element;
                }
            } else {
                element.setSelected(false);
            }
        }
        return null;
    }

    private void showFontDialog(Integer x, Integer y, Canvas canvas, Integer screenX, Integer screenY, FXMText element) {
        String fontname = element.getFont().getFamily();
        String style = element.getFont().getStyle();
        String size = ((Integer) ((Double) element.getFont().getSize()).intValue()).toString();
        controller.getFontBox().setValue(fontname);
        controller.getFontSize().setValue(size);
        controller.getFontBold().setSelected((style.indexOf("Bold") >= 0));
        controller.getFontItalic().setSelected((style.indexOf("Italic") >= 0));

        Integer sx = controller.getSelected().getX() + controller.getSelected().getWidth() + (screenX - x) + 5;
        Integer sy = controller.getSelected().getY() + (screenY - y);
        //controller.getTextCMenu().setAnchorLocation(PopupWindow.AnchorLocation.CONTENT_BOTTOM_RIGHT);
        controller.getTextCMenu().show(canvas, sx, sy);
    }

    private void showLineDialog(Integer x, Integer y, Canvas canvas, Integer screenX, Integer screenY, FXMLine element) {
        controller.getLineSingle().setSelected(element.getSingle());
        controller.getLineDouble().setSelected(!element.getSingle());
        controller.getLineSlider().setValue(element.getLineWidth());
        Integer sx = controller.getSelected().getX() + controller.getSelected().getWidth() + (screenX - x) + 5;
        Integer sy = controller.getSelected().getY() + (screenY - y);
        controller.getLineCMenu().show(canvas, sx, sy);
    }


    private void showTextField(FXMElement element) {
        controller.getTextField().relocate(element.getX(), element.getY() + cv.getBoundsInParent().getMinY());
        controller.getTextField().setPrefWidth(element.getWidth());
        controller.getTextField().setPrefHeight(element.getHeight());
        controller.getTextField().setText(((FXMLabel) element).getText());
        controller.getTextField().setFont(((FXMLabel) element).getFont());
        controller.getTextField().setBorder(Border.EMPTY);
        controller.getTextField().toFront();
        controller.setTextFieldVisible(true);
        controller.getTextField().requestFocus();
    }

    private void showTextArea(FXMElement element) {
        controller.getTextArea().relocate(element.getX(), element.getY() + cv.getBoundsInParent().getMinY());
        controller.getTextArea().setPrefWidth(element.getWidth());
        controller.getTextArea().setPrefHeight(element.getHeight());
        controller.getTextArea().setText(((FXMText) element).getText());
        controller.getTextArea().setFont(((FXMText) element).getFont());
        controller.getTextArea().setBorder(Border.EMPTY);
        controller.getTextArea().toFront();
        controller.setTextAreaVisible(true);
        controller.getTextArea().requestFocus();
    }

    public void unselectAll() {
        for (FXMElement element : band.getElements()) {
            element.setSelected(false);
        }
    }

    public void resize(Integer newWidth){
        for (FXMElement element : band.getElements()) {
            if(element.getX()+element.getWidth()>newWidth){
                Integer dif = element.getX()+element.getWidth()-newWidth;
                element.setX(element.getX()-dif);
            }
        }
        cv.setWidth(newWidth);
    }

    public boolean delete(FXMElement selected) {
        if(band.getElements().contains(selected)) {
            band.getElements().remove(selected);
            int i=0;
            for (FXMElement element : band.getElements()) {
                element.setIndex(i);
                i++;
            }
            return true;
        }
        return false;
    }
}
