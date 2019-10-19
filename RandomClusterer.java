package clustering;

import algorithms.Clusterer;
import data.DataSet;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import settings.AppPropertyTypes;
import vilij.templates.ApplicationTemplate;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

public class RandomClusterer extends Clusterer {
    private DataSet dataset;

    private final int                           maxIterations;
    private final int                           updateInterval;

    private final AtomicBoolean                 tocontinue;
    private final ArrayList<XYChart.Series>     series              =   new ArrayList<XYChart.Series>();
    private Button                              displayButton;
    private Button                              scrnshotButton;
    private Button                              exitButton;
    private LineChart                           chart;
    private int                                 currentIteration    = 1;

    public RandomClusterer(DataSet dataset, int maxIterations, int updateInterval, int numberOfClusters, boolean tocontinue) {
        super(numberOfClusters);
        this.dataset = dataset;
        this.maxIterations = maxIterations;
        this.updateInterval = updateInterval;
        this.tocontinue = new AtomicBoolean(tocontinue);

    }

    @Override
    public int getMaxIterations() {
        return maxIterations;
    }

    @Override
    public int getUpdateInterval() {
        return updateInterval;
    }

    @Override
    public boolean tocontinue() {
        return tocontinue.get();
    }

    @Override
    public void run() {
        ApplicationTemplate applicationTemplate = new ApplicationTemplate();

        exitButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (scrnshotButton.isDisabled() || currentIteration <= maxIterations) {
                    Alert algorithmRun = new Alert(Alert.AlertType.CONFIRMATION);
                    algorithmRun.setTitle(applicationTemplate.manager.getPropertyValue(AppPropertyTypes.EXIT_ALGO_TITLE.name()));
                    algorithmRun.setHeaderText(applicationTemplate.manager.getPropertyValue(AppPropertyTypes.EXIT_ALGO_CONTENT.name()));
                    Optional<ButtonType> result = algorithmRun.showAndWait();
                    if (result.get() == ButtonType.OK) System.exit(0);
                }
                else System.exit(0);
            }
        });

        if (!(series.size() > 0)) {
            for (int i = 1; i <= numberOfClusters; i++) {
                XYChart.Series serie = new XYChart.Series();
                serie.setName(i + applicationTemplate.manager.getPropertyValue(AppPropertyTypes.EMPTY_STRING.name()));
                series.add(serie);
            }
        }

        if (tocontinue.get()) {
            scrnshotButton.setDisable(true);
            displayButton.setVisible(false);
            for (int i = updateInterval; i <= maxIterations; i += updateInterval) {
                System.out.println(applicationTemplate.manager.getPropertyValue(AppPropertyTypes.ITERATION.name()) + i);
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        for (XYChart.Series s: series) {
                            s.getData().clear();
                        }

                        chart.getData().clear();

                        Object[] keySet = dataset.getLabels().keySet().toArray();

                        for (int i = 0; i < dataset.getLocations().size(); i++) {
                            Random random = new Random();
                            int index = random.nextInt(series.size());

                            Point2D point = dataset.getLocations().get(keySet[i].toString());
                            XYChart.Data tempPoint = new XYChart.Data(point.getX(), point.getY());
                            series.get(index).getData().add(tempPoint);
                        }

                        for (int j = 0; j < series.size(); j++) chart.getData().add(series.get(j));
                    }
                });

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {}
            }
            series.clear();
            scrnshotButton.setDisable(false);
            displayButton.setVisible(true);
        }
        else {
            scrnshotButton.setDisable(true);
            displayButton.setVisible(false);

            if (currentIteration <= maxIterations) {
                System.out.println(applicationTemplate.manager.getPropertyValue(AppPropertyTypes.ITERATION.name()) + currentIteration);
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        chart.getData().clear();
                        for (XYChart.Series s: series) {
                            s.getData().clear();
                        }
                        Object[] keySet = dataset.getLabels().keySet().toArray();

                        for (int i = 0; i < dataset.getLocations().size(); i++) {
                            Random random = new Random();
                            int index = random.nextInt(series.size());

                            Point2D point = dataset.getLocations().get(keySet[i].toString());
                            XYChart.Data tempPoint = new XYChart.Data(point.getX(), point.getY());
                            series.get(index).getData().add(tempPoint);
                        }

                        for (int j = 0; j < series.size(); j++) chart.getData().add(series.get(j));
                    }
                });

                try {
                    Thread.sleep(500);
                } catch (InterruptedException ex) {}

                currentIteration += updateInterval;

                scrnshotButton.setDisable(false);
                displayButton.setVisible(true);
            }
        }
    }

    public void setDisplayButton(Button displayButton){this.displayButton = displayButton; }

    public void setLineChart(LineChart chart) {this.chart = chart;}

    public void setScrnshotButton(Button scrnshotButton) {this.scrnshotButton = scrnshotButton;}

    public void setExitButton(Button exitButton) {this.exitButton = exitButton;}

    public ArrayList<XYChart.Series> getSeries() {return series;}

    public int getCurrentIteration() {return currentIteration;}

    public void setCurrentIteration(int currentIteration) {this.currentIteration = currentIteration;}
}
