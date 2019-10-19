/* I like this bit of code because it was the first time I used threads in order to plot out data points on a graph */
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
