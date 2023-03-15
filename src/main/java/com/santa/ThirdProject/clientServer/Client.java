package com.santa.ThirdProject.clientServer;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.knowm.xchart.XChartPanel;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.XYSeries;
import org.knowm.xchart.style.Styler;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import javax.swing.*;
import java.awt.*;
import java.util.*;

public class Client {

    public static void main(String[] args) throws JsonProcessingException {

        final String nameSensor = "Viktor";

        registerSensor(nameSensor);

        double minTemperature = -100;
        double maxTemperature = 100;

        Random random = new Random();

        for (int i = 0; i < 10; i++) {

            add(minTemperature + (random.nextDouble() * (maxTemperature - minTemperature)),
                    random.nextBoolean(), "Alex");

        }

        get();

    }

    private static void registerSensor(String registerName) {
        String url = "http://localhost:8080/sensors/registration";

        Map<String, Object> jsonNewSensor = new HashMap<>();
        jsonNewSensor.put("name", registerName);

        makePostRequestWithJSON(url, jsonNewSensor);

    }

    private static void add(double temperature, boolean raining, String nameSensor) {
        String url = "http://localhost:8080/measurements/add";

        Map<String, Object> jsonMeasurements = new HashMap<>();
        jsonMeasurements.put("value", temperature);
        jsonMeasurements.put("raining", raining);
        jsonMeasurements.put("sensor", Map.of("name", nameSensor));

        makePostRequestWithJSON(url, jsonMeasurements);

    }

    private static void get() throws JsonProcessingException {
        String url = "http://localhost:8080/measurements";

        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        xchart(Objects.requireNonNull(response.getBody()));

    }

    private static void makePostRequestWithJSON(String url, Map<String, Object> data) {

        final RestTemplate restTemplate = new RestTemplate();

        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Object> request = new HttpEntity<>(data, headers);

        try {

            restTemplate.postForObject(url, request, String.class);
            System.out.println("Запрос выполнен успешно");

        } catch (HttpClientErrorException e) {

            System.out.println("Ошибка запроса");
            System.out.println(e.getMessage());

        }

    }

    private static void xchart(String response) throws JsonProcessingException {

        String str = response.replace("[", "");
        str = str.replace("]", "");
        String[] valuesStr = str.split(",");
        double[] values = Arrays.stream(valuesStr).mapToDouble(Double::parseDouble).toArray();
        double[] x = new double[values.length];

        for (int i = 0; i < values.length; i++) {
            x[i] = i + 1;
        }

        // Create Chart
        final XYChart chart = new XYChartBuilder().width(600).height(400).title("Temperature chart").xAxisTitle("Days").yAxisTitle("Temperature").build();

        // Customize Chart
        chart.getStyler().setLegendPosition(Styler.LegendPosition.InsideNE);
        chart.getStyler().setDefaultSeriesRenderStyle(XYSeries.XYSeriesRenderStyle.Area);
        chart.addSeries("Temperature", x, values);

        // Schedule a job for the event-dispatching thread:
        // creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {

                // Create and set up the window.
                JFrame frame = new JFrame("Temperature chart");
                frame.setLayout(new BorderLayout());
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

                // chart
                JPanel chartPanel = new XChartPanel<XYChart>(chart);
                frame.add(chartPanel, BorderLayout.CENTER);

                // Display the window.
                frame.pack();
                frame.setVisible(true);
            }
        });
    }

}
