package sample;

import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.ProgressBarTableCell;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ForkJoinPool;

public class Controller implements Initializable {
    List<ImageProcessingJob> list = new ArrayList<>();
    String choice;
    File selectedDirectory;
    @FXML private TableView<ImageProcessingJob> tabela;
    @FXML TableColumn<ImageProcessingJob, String> imageNameColumn;
    @FXML TableColumn<ImageProcessingJob, Double> progressColumn;
    @FXML TableColumn<ImageProcessingJob, String> statusColumn;
    @FXML ChoiceBox<String> cb;


    @Override
    public void initialize(URL url, ResourceBundle rb) {

        ObservableList<ImageProcessingJob> dane = FXCollections.observableArrayList(list);
        tabela.itemsProperty().setValue(dane);
        cb.getItems().addAll("sekwencyjne","współbieżne domyślne","współbieżne określona");
        cb.setValue("sekwencyjne");
        imageNameColumn.setCellValueFactory( //nazwa pliku
                p -> new SimpleStringProperty(p.getValue().getFile().getName()));
        statusColumn.setCellValueFactory( //status przetwarzani
                p -> p.getValue().getStatusProperty());
        progressColumn.setCellValueFactory( //postęp przetwarzania
                p -> p.getValue().getProgressProperty().asObject());
        progressColumn.setCellFactory( //wykorzystanie paska postępu
                ProgressBarTableCell.<ImageProcessingJob>forTableColumn());



        //...dalsze inicjalizacje...
    }
    @FXML
    private void chooseFileAction()
    {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("JPG images", "*.jpg"));
        List<File> selectedFiles = fileChooser.showOpenMultipleDialog(null);
        for(File file:selectedFiles){
            ImageProcessingJob tmp = new ImageProcessingJob(file);
            list.add(tmp);
        }
        tabela.getItems().clear();
        tabela.getItems().addAll(list);
    }
    @FXML
    void sendFileAction() {
        choice = cb.getValue();
        System.out.println(choice);
        DirectoryChooser directoryChooser = new DirectoryChooser();
        selectedDirectory =
                directoryChooser.showDialog(null);
        if(choice=="sekwencyjne")
        {
            System.out.println("Elo");
            new Thread(this::backgroundJob).start();
        }
        else if(choice=="współbieżne domyślne")
        {
            ForkJoinPool.commonPool().submit(this::backgroundJob);
        }
        else
        {
            ForkJoinPool pool = new ForkJoinPool(2); //pożądana liczba wątków
            pool.submit(this::backgroundJob);
        }
    }
    private void backgroundJob(){
        //...operacje w tle...
        System.out.println("Siema");
        if(choice=="sekwencyjne") {
            long start = System.currentTimeMillis();
            list.stream().forEach(item -> item.convertToGrayscale(item.file, selectedDirectory, item.progres, item.status));
            long end = System.currentTimeMillis();
            System.out.println(end-start+"ms");
        }
        else {
            long start = System.currentTimeMillis();
            list.parallelStream().forEach(item -> item.convertToGrayscale(item.file, selectedDirectory, item.progres, item.status));
            long end = System.currentTimeMillis();
            System.out.println(end-start+"ms");
        }
    }



}
