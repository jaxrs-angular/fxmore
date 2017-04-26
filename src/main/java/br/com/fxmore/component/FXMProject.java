package br.com.fxmore.component;

import javafx.scene.shape.Rectangle;

/**
 * Created by dmartinez on 23/04/2017.
 */
public class FXMProject {
    private FXMPageOrientation pageOrientation;
    private FXMBand bandA;
    private FXMBand bandB;

    public FXMProject(FXMPageOrientation pageOrientation, FXMBand bandA, FXMBand bandB) {
        this.pageOrientation = pageOrientation;
        this.bandA = bandA;
        this.bandB = bandB;
    }

    public FXMPageOrientation getPageOrientation() {
        return pageOrientation;
    }

    public void setPageOrientation(FXMPageOrientation pageOrientation) {
        this.pageOrientation = pageOrientation;
    }

    public FXMBand getBandA() {
        return bandA;
    }

    public void setBandA(FXMBand bandA) {
        this.bandA = bandA;
    }

    public FXMBand getBandB() {
        return bandB;
    }

    public void setBandB(FXMBand bandB) {
        this.bandB = bandB;
    }
}
