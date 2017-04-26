package br.com.fxmore.controll;

import br.com.fxmore.component.FXMBand;
import br.com.fxmore.component.FXMBandType;
import br.com.fxmore.component.FXMPageOrientation;
import br.com.fxmore.component.FXMProject;
import br.com.fxmore.component.element.*;
import com.thoughtworks.xstream.XStream;
import javafx.collections.MapChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Screen;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Controller {

    final int REDIM = 1;
    final int MOVE = 2;
    final int NONE = 3;

    // dados do projeto
    private FXMProject data;

    // padrao de texto
    private Font defaultFont;
    private List<String> fontList;

    // dimensões para A4
    private Integer portWidth;
    private Integer landWidth;

    // imagens para drag-n-drop
    private Image imgTexto;
    private Image imgLabel;
    private Image imgImagem;
    private Image imgLinha;

    // imagem padrao para FXMImage
    private Image fxmImageDefault;


    // controladores dos canvas
    private BandController pnACtrl;
    private BandController pnBCtrl;

    // elemento selecionado
    private FXMElement selected;
    private int mouseAction;
    private int lastMouseX;
    private int lastMouseY;


    @FXML
    private Canvas painelA;

    @FXML
    private Canvas painelB;

    @FXML
    private RadioButton portrait;

    @FXML
    private TextArea textArea;

    @FXML
    private TextField textField;

    private Boolean textAreaVisible;
    private boolean textFieldVisible;
    private ChoiceBox<String> fontBox;
    private ChoiceBox<String> fontSize;
    private CheckBox fontBold;
    private CheckBox fontItalic;
    private CheckBox fontUnderline;
    private ContextMenu textCMenu;
    private ContextMenu lineCMenu;
    private FileChooser fileChooser;
    private RadioButton lineSingle;
    private RadioButton lineDouble;
    private Slider lineSlider;

    private void clear() {
        Integer height = 250;
        Integer width = (portrait.isSelected()) ? portWidth : landWidth;

        painelA.setWidth(width);
        painelA.setHeight(height);

        painelB.setWidth(width);
        painelB.setHeight(height);

        FXMBand a = new FXMBand(FXMBandType.BAND_A, "Banda A", Color.rgb(230, 230, 230), width, height);
        FXMBand b = new FXMBand(FXMBandType.BAND_A, "Banda B", Color.rgb(240, 240, 240), width, height);

        data = new FXMProject((portrait.isSelected()) ? FXMPageOrientation.PORTRAIT : FXMPageOrientation.LANDSCAP, a, b);

        // criando controladores de apoio
        pnACtrl = new BandController(painelA, a, this);
        pnBCtrl = new BandController(painelB, b, this);

    }

    public void initialize() {
        mouseAction = NONE;
        // definindo fonte padrão para text/label
        defaultFont = Font.font("Arial", FontWeight.NORMAL, FontPosture.REGULAR, 18);
        fontList = Font.getFamilies();

        // carregando imagens drag-n-drop
        imgTexto = new Image("images/lbTexto.png");
        imgLabel = new Image("images/lbLabel.png");
        imgImagem = new Image("images/lbImagem.png");
        imgLinha = new Image("images/lbLinha.png");
        // carregando imagem padrao
        fxmImageDefault = new Image("images/fxmImageDefault.png");

        // calculando largura folha a4(210x297mm) para a tela corrente
        Double dpi = Screen.getPrimary().getDpi();
        portWidth = new Double(dpi * (210 / 25.4)).intValue();
        landWidth = new Double(dpi * (297 / 25.4)).intValue();

        textArea.toBack();
        textAreaVisible = false;

        textField.toBack();
        textFieldVisible = false;

        // inicializando novo projeto em branco
        clear();

        //contextmenu
        textCMenu = new ContextMenu();
        fontBox = new ChoiceBox<String>();
        fontBox.getItems().addAll(fontList);
        fontBox.setValue(fontList.get(0));
        EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                updateTextElement();
            }
        };
        fontBox.setOnAction(event);

        fontSize = new ChoiceBox<String>();
        for (Integer x = 8; x < 97; x++) {
            fontSize.getItems().add(x.toString());
        }
        fontSize.setOnAction(event);

        HBox item1Group = new HBox();
        item1Group.getChildren().addAll(new Text("Fonte: "), fontBox, fontSize);
        CustomMenuItem item1 = new CustomMenuItem(item1Group);
        item1.setHideOnClick(false);

        fontBold = new CheckBox("Negrito: ");
        fontBold.setSelected(false);
        CustomMenuItem item3 = new CustomMenuItem(fontBold);
        item3.setHideOnClick(false);
        fontBold.setOnAction(event);

        fontItalic = new CheckBox("Itálico: ");
        fontItalic.setSelected(false);
        CustomMenuItem item4 = new CustomMenuItem(fontItalic);
        item4.setHideOnClick(false);
        fontItalic.setOnAction(event);

        /*fontUnderline = new CheckBox("Sublinhado: ");
        fontUnderline.setSelected(false);
        CustomMenuItem item5 = new CustomMenuItem(fontUnderline);
        item5.setHideOnClick(false);*/

        textCMenu.getItems().addAll(item1, item3, item4);

        lineCMenu = new ContextMenu();
        HBox rbBox = new HBox();
        ToggleGroup rbGroup = new ToggleGroup();
        lineSingle = new RadioButton("Simples ");
        lineSingle.setToggleGroup(rbGroup);
        EventHandler<ActionEvent> lineEvent = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ((FXMLine) selected).setSingle(lineSingle.isSelected());
                pnACtrl.draw();
                pnBCtrl.draw();
            }
        };
        lineSingle.setOnAction(lineEvent);
        lineDouble = new RadioButton("Dupla ");
        lineDouble.setToggleGroup(rbGroup);
        lineDouble.setOnAction(lineEvent);
        rbBox.getChildren().addAll(new Text("Tipo: "), lineSingle, lineDouble);
        item1 = new CustomMenuItem(rbBox);
        item1.setHideOnClick(false);

        HBox sizeBox = new HBox();
        lineSlider = new Slider(1, 10, 2);
        lineSlider.setShowTickLabels(true);
        lineSlider.setShowTickMarks(true);
        lineSlider.setMinorTickCount(0);
        lineSlider.setMajorTickUnit(1);
        lineSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            ((FXMLine) selected).setLineWidth(((Double) newValue).intValue());
            pnACtrl.draw();
            pnBCtrl.draw();
        });
        sizeBox.getChildren().addAll(new Text("Espessura: "), lineSlider);
        item3 = new CustomMenuItem(sizeBox);
        item3.setHideOnClick(false);

        lineCMenu.getItems().addAll(item1, item3);


        fileChooser = new FileChooser();
        fileChooser.setTitle("Adicionar Imagem");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All Images", "*.jpg;*.png"),
                new FileChooser.ExtensionFilter("JPG", "*.jpg"),
                new FileChooser.ExtensionFilter("PNG", "*.png")
        );


    }

    private void updateTextElement() {
        if (selected != null && textCMenu.isShowing()) {
            FontWeight weight = (fontBold.isSelected()) ? FontWeight.BOLD : FontWeight.NORMAL;
            FontPosture posture = (fontItalic.isSelected()) ? FontPosture.ITALIC : FontPosture.REGULAR;
            Font font = Font.font(fontBox.getValue(), weight, posture, new Double(fontSize.getValue()));
            ((FXMText) selected).setFont(font);
            if (selected instanceof FXMLabel) {
                FXMLabel lb = ((FXMLabel) selected);
                lb.setHeight(((Double) lb.getTextHeight(lb.getText())).intValue());
            }
            pnACtrl.draw();
            pnBCtrl.draw();
        }
    }


    public void textDragDetect(MouseEvent mouseEvent) {
        ImageView img = (ImageView) mouseEvent.getTarget();
        Dragboard db = img.startDragAndDrop(TransferMode.ANY);
        ClipboardContent content = new ClipboardContent();
        content.putString(img.getId());
        db.setContent(content);
        db.setDragView(getDragImage(img));
        mouseEvent.consume();
    }

    private Image getDragImage(ImageView img) {
        if (img.getId().equals("textButtom")) return imgTexto;
        else if (img.getId().equals("labelButtom")) return imgLabel;
        else if (img.getId().equals("imageButtom")) return imgImagem;
        else if (img.getId().equals("lineButtom")) return imgLinha;
        else return null;
    }

    public void bandDragOver(DragEvent dragEvent) {
        if (dragEvent.getGestureSource() == dragEvent.getGestureTarget()) {
            int x = ((Double) dragEvent.getX()).intValue();
            int y = ((Double) dragEvent.getY()).intValue();
            if (selected != null && mouseAction != NONE) {
                dragEvent.acceptTransferModes(TransferMode.ANY);
                BandController ctrl = (((Canvas) dragEvent.getTarget()).getId().equals("painelA")) ? pnACtrl : pnBCtrl;
                // System.out.println("lastX: " + ((Integer) lastMouseX) + " X: " + ((Integer) lastMouseX) + " lastY: " + ((Integer) lastMouseY) + " Y:" + ((Integer) y));
                if ((Math.abs(lastMouseX - x) > 3 || Math.abs(lastMouseY - y) > 3)) {
                    if (mouseAction == REDIM) {
                        selected.redim(x, y);
                        ctrl.draw();
                    } else { //MOVE
                        int maxWidth = ((Double) ((Canvas) dragEvent.getTarget()).getWidth()).intValue();
                        int maxHeight = ((Double) ((Canvas) dragEvent.getTarget()).getHeight()).intValue();
                        if (selected.move(x - lastMouseX, y - lastMouseY, maxWidth, maxHeight)) {
                            lastMouseX = x;
                            lastMouseY = y;
                            ctrl.draw();
                        }
                    }
                }
            }
        } else if (dragEvent.getGestureSource() != dragEvent.getGestureTarget() &&
                dragEvent.getDragboard().hasString()) {
                    /* allow for both copying and moving, whatever user chooses */
            dragEvent.acceptTransferModes(TransferMode.ANY);
        }

        dragEvent.consume();
    }


    public void bandDragDetected(MouseEvent mouseEvent) {
        int x = ((Double) mouseEvent.getX()).intValue();
        int y = ((Double) mouseEvent.getY()).intValue();
        if (selected == null) {
            this.bandClicked(mouseEvent);
        }
        if (selected != null) {
            lastMouseX = x;
            lastMouseY = y;

            Canvas cv = (Canvas) mouseEvent.getTarget();
            Dragboard db = cv.startDragAndDrop(TransferMode.ANY);
            ClipboardContent content = new ClipboardContent();
            content.putString("");
            db.setContent(content);

            //System.out.println("x: "+((Integer)x)+" y: "+((Integer)y)+"    selx: "+selected.getX()+" sely: "+selected.getY()+" selw: "+selected.getWidth()+" selh: "+selected.getHeight()+" dotx: "+(selected.getX()+selected.getWidth())+" doty: "+(selected.getY()+selected.getHeight()));
            if (selected.isDotTouched(x, y)) {
                mouseAction = REDIM;
            } else if (selected.isTouched(x, y)) {
                mouseAction = MOVE;
            } else {
                mouseAction = NONE;
                return;
            }
        }
        mouseEvent.consume();
    }

    public void bandDragDroped(DragEvent dragEvent) {
        if (dragEvent.getGestureSource() == dragEvent.getGestureTarget()) {
            Canvas cv = (Canvas) dragEvent.getGestureSource();
            mouseAction = NONE;
            cv.setCursor(Cursor.DEFAULT);

        } else if (dragEvent.getGestureSource() != dragEvent.getGestureTarget() && dragEvent.getDragboard().hasString()) {

            String tp = dragEvent.getDragboard().getString();
            BandController ctrl = (((Canvas) dragEvent.getGestureTarget()).getId().equals("painelA")) ? pnACtrl : pnBCtrl;
            FXMElement t = null;

            hideLineDialogs();
            hideTextDialogs();
            if (selected != null)
                selected.setSelected(false);

            if (tp.equals("textButtom"))
                t = new FXMText("texto", ((Double) dragEvent.getX()).intValue(), ((Double) dragEvent.getY()).intValue(), defaultFont, false, false, false, 0);
            else if (tp.equals("labelButtom"))
                t = new FXMLabel("Label", ((Double) dragEvent.getX()).intValue(), ((Double) dragEvent.getY()).intValue(), defaultFont, false, false, false, 0);
            else if (tp.equals("imageButtom"))
                t = new FXMImage(fxmImageDefault, ((Double) dragEvent.getX()).intValue(), ((Double) dragEvent.getY()).intValue(), 60, 60, 0);
            else if (tp.equals("lineButtom"))
                t = new FXMLine(((Double) dragEvent.getX()).intValue(), ((Double) dragEvent.getY()).intValue(), 0);

            selected = t;
            ctrl.add(t);
            pnACtrl.draw();
            pnBCtrl.draw();


        }
        dragEvent.consume();
    }

    public void bandClicked(MouseEvent mouseEvent) {
        BandController ctrl = (((Canvas) mouseEvent.getTarget()).getId().equals("painelA")) ? pnACtrl : pnBCtrl;

        FXMElement element = ctrl.click(((Double) mouseEvent.getX()).intValue(), ((Double) mouseEvent.getY()).intValue(), mouseEvent.getButton().name().equals("SECONDARY"), ((Canvas) mouseEvent.getTarget()), ((Double) mouseEvent.getScreenX()).intValue(), ((Double) mouseEvent.getScreenY()).intValue());
        if (selected != null && element != selected) { // tem seleciona e nao clicou nele
            selected.setSelected(false);
            if (selected instanceof FXMText || selected instanceof FXMLabel) { // selecionado eh text/label
                hideTextDialogs();

            } else if (selected instanceof FXMLine) {
                hideLineDialogs();
            }

        }
        selected = element;
        pnACtrl.draw();
        pnBCtrl.draw();
    }

    private void hideLineDialogs() {
        if (lineCMenu.isShowing()) lineCMenu.hide();
    }

    private void hideTextDialogs() {
        if (textCMenu.isShowing()) textCMenu.hide();

        if (textAreaVisible) { // text editor esta visivel
            ((FXMText) selected).setText(textArea.getText());
            textArea.toBack();
            textAreaVisible = false;
        }

        if (textFieldVisible) { // text editor esta visivel
            ((FXMLabel) selected).setText(textField.getText());
            textField.toBack();
            textFieldVisible = false;
        }
    }

    public TextArea getTextArea() {
        return textArea;
    }

    public void textAreaChanged(InputMethodEvent inputMethodEvent) {
        if (selected instanceof FXMText) {
            ((FXMText) selected).setText(textArea.getText());
        }
    }

    public void setTextAreaVisible(boolean textAreaVisible) {
        this.textAreaVisible = textAreaVisible;
    }

    public void setTextFieldVisible(boolean textFieldVisible) {
        this.textFieldVisible = textFieldVisible;
    }


    public ContextMenu getTextCMenu() {
        return textCMenu;
    }

    public ChoiceBox<String> getFontBox() {
        return fontBox;
    }

    public ChoiceBox<String> getFontSize() {
        return fontSize;
    }

    public CheckBox getFontItalic() {
        return fontItalic;
    }

    public CheckBox getFontBold() {
        return fontBold;
    }

    public FXMElement getSelected() {
        return selected;
    }

    public TextField getTextField() {
        return textField;
    }

    public FileChooser getFileChooser() {
        return fileChooser;
    }

    public void textFieldChanged(InputMethodEvent inputMethodEvent) {
        if (selected instanceof FXMLabel) {
            ((FXMLabel) selected).setText(textField.getText());
        }
    }

    public ContextMenu getLineCMenu() {
        return lineCMenu;
    }

    public Slider getLineSlider() {
        return lineSlider;
    }

    public RadioButton getLineSingle() {
        return lineSingle;
    }

    public RadioButton getLineDouble() {
        return lineDouble;
    }

    public void limpar(MouseEvent mouseEvent) {
        clear();
    }

    public void setPortrait(MouseEvent mouseEvent) {
        pnACtrl.resize(portWidth);
        pnBCtrl.resize(portWidth);
        pnACtrl.draw();
        pnBCtrl.draw();

    }

    public void setLandscap(MouseEvent mouseEvent) {
        pnACtrl.resize(landWidth);
        pnBCtrl.resize(landWidth);
        pnACtrl.draw();
        pnBCtrl.draw();
    }

    public void keyPressed(KeyEvent keyEvent) {
        if (selected != null && keyEvent.getCode() == KeyCode.DELETE) {
            if (!pnACtrl.delete(selected)) {
                pnBCtrl.delete(selected);
                pnBCtrl.draw();
            } else
                pnACtrl.draw();
        }
        keyEvent.consume();
    }

    public void save(MouseEvent mouseEvent) {
        XStream xt = new XStream();
        String xml = xt.toXML(this.data);
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Salvar Projeto");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("XML", "*.xml")
        );
        File file = fileChooser.showSaveDialog(getTextArea().getScene().getWindow());
        if (file != null) {
            try {
                Files.write(Paths.get(file.toURI()), xml.getBytes());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

    }

    public void open(MouseEvent mouseEvent) {
        XStream xt = new XStream();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Abrir Projeto");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("XML", "*.xml")
        );
        File file = fileChooser.showOpenDialog(getTextArea().getScene().getWindow());
        if (file != null) {
            try {
                FXMProject project = (FXMProject) xt.fromXML(file);
                clear();
                portrait.setSelected((project.getPageOrientation() == FXMPageOrientation.PORTRAIT));
                data = project;
                pnACtrl = new BandController(painelA, data.getBandA(), this);
                pnBCtrl = new BandController(painelB, data.getBandB(), this);
                pnACtrl.draw();
                pnBCtrl.draw();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

    }
}
