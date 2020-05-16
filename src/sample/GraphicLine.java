package sample;

import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

import java.io.IOException;

public class GraphicLine extends HBox {

    public static final String CARTESIAN = "f(x) = ";
    public static final String POLAR = "ρ(t) = ";

    @FXML
    private Label label;
    @FXML
    private JFXTextField textField;

    public GraphicLine(String labelText) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("GraphicLine.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
        label.setText(labelText);
    }

    public JFXTextField getTextField() {
        return textField;
    }
}
