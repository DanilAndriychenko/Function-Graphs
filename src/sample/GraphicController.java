package sample;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Side;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.VBox;
import net.objecthunter.exp4j.ExpressionBuilder;
import org.gillius.jfxutils.chart.ChartPanManager;
import org.gillius.jfxutils.chart.JFXChartUtil;

import java.util.HashMap;

public class GraphicController {

    HashMap<GraphicLine, XYChart.Series> functions = new HashMap<>();
    @FXML
    private ScrollPane scrollPaneProperties;
    @FXML
    private VBox vBoxProperties;
    @FXML
    private JFXButton buttonPlus;
    @FXML
    private ContextMenu contextMenuPlus;
    @FXML
    private MenuItem menuItemCartesian;
    @FXML
    private MenuItem menuItemPolar;
    @FXML
    private LineChart lineChart;
    @FXML
    private NumberAxis axisX;
    @FXML
    private NumberAxis axisY;

    @FXML
    private void initialize() {
        /*Set actions for buttons: plus and both systems*/
        buttonPlus.setOnAction(actionEvent -> contextMenuPlus.show(buttonPlus, Side.BOTTOM, 0, 0));
        menuItemCartesian.setOnAction(actionEvent -> addFunction(GraphicLine.CARTESIAN));
        menuItemPolar.setOnAction(actionEvent -> addFunction(GraphicLine.POLAR));
        //TODO: remove functions from vBox
        axisX.setForceZeroInRange(false);
        axisY.setForceZeroInRange(false);

        axisX.setLowerBound(-1000);
        axisX.setUpperBound(500);
        ChartPanManager panner = new ChartPanManager(lineChart);
        //while presssing the left mouse button, you can drag to navigate
        panner.setMouseFilter(mouseEvent -> {
            if (mouseEvent.getButton() == MouseButton.PRIMARY) {//set your custom combination to trigger navigation
                // let it through

                System.out.println(axisX.getLowerBound() + " , " + axisX.getUpperBound());
                System.out.println(axisY.getLowerBound() + " , " + axisY.getUpperBound());
            } else {
                mouseEvent.consume();
            }
        });
        panner.start();

        //holding the right mouse button will draw a rectangle to zoom to desired location
        JFXChartUtil.setupZooming(lineChart, mouseEvent -> {
            if (mouseEvent.getButton() != MouseButton.SECONDARY)//set your custom combination to trigger rectangle zooming
                mouseEvent.consume();
        });
    }

    /*
    This method takes type of function that have to be created and add container to vBox on scrollPane
     */
    private void addFunction(String type) {
        GraphicLine graphicLine = new GraphicLine(type);

        XYChart.Series series = new XYChart.Series<>();
        lineChart.getData().add(series);
        functions.put(graphicLine, series);

        graphicLine.getTextField().setOnKeyTyped(keyEvent -> keyTyped(keyEvent));

        vBoxProperties.getChildren().add(graphicLine);
    }

    private void keyTyped(KeyEvent keyEvent) {
        GraphicLine graphicLine = (GraphicLine) ((JFXTextField) (keyEvent.getSource())).getParent();
        String expression = graphicLine.getTextField().getText();
        ObservableList<XYChart.Data> dataList = getData(expression);
        functions.get(graphicLine).setName(expression);
        functions.get(graphicLine).setData(dataList);
    }

    private ObservableList<XYChart.Data> getData(String expression) {
        ObservableList<XYChart.Data> dataList = FXCollections.observableArrayList();
        for (double i = -200; i <= 200; i += 0.5) {
            //TODO: handle exception
            //TODO: add minus tp GraphicLine and textarea below to tell about error;
            double y = new ExpressionBuilder(expression)
                    .variable("x")
                    .build()
                    .setVariable("x", i)
                    .evaluate();
            dataList.add(new XYChart.Data(i, y));
        }
        return dataList;
    }
}
