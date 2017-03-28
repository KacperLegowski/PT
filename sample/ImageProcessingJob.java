package sample;

/**
 * Created by Karoll0 on 2017-03-24.
 */

import javafx.application.Platform;
import javafx.beans.binding.BooleanExpression;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

import javafx.concurrent.Task;

import javax.imageio.ImageIO;
import java.io.*;
import java.net.ConnectException;
import java.net.Socket;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by Karoll0 on 2017-03-24.
 */
public class ImageProcessingJob  {
    File file;
    SimpleStringProperty status;
    DoubleProperty progres;
    public ImageProcessingJob(File file) {
        this.file = file;
        this.status = new SimpleStringProperty("Waitng");
        this.progres = new SimpleDoubleProperty(0);
    }


    public File getFile() {
        return file;
    }

    public SimpleStringProperty getStatusProperty() {
        return status;
    }

    public DoubleProperty getProgressProperty() {

        return progres;
    }
    public void convertToGrayscale(
            File originalFile, //oryginalny plik graficzny
            File outputDir, //katalog docelowy
            DoubleProperty progressProp,
            SimpleStringProperty statusProp//własność określająca postęp operacji
    ) {
        try {
            //wczytanie oryginalnego pliku do pamięci
            BufferedImage original = ImageIO.read(originalFile);
            //przygotowanie bufora na grafikę w skali szarości
            BufferedImage grayscale = new BufferedImage(
                    original.getWidth(), original.getHeight(), original.getType());
            //przetwarzanie piksel po pikselu
           //System.out.println("Zaczynam");
            for (int i = 0; i < original.getWidth(); i++) {
                for (int j = 0; j < original.getHeight(); j++) {
            //pobranie składowych RGB
                    int red = new Color(original.getRGB(i, j)).getRed();
                    int green = new Color(original.getRGB(i, j)).getGreen();
                    int blue = new Color(original.getRGB(i, j)).getBlue();
            //obliczenie jasności piksela dla obrazu w skali szarości
                    int luminosity = (int) (0.21*red + 0.71*green + 0.07*blue);
            //przygotowanie wartości koloru w oparciu o obliczoną jaskość
                    int newPixel =
                            new Color(luminosity, luminosity, luminosity).getRGB();
            //zapisanie nowego piksela w buforze
                    grayscale.setRGB(i, j, newPixel);
                }
            //obliczenie postępu przetwarzania jako liczby z przedziału [0, 1]
                double progress = (1.0 + i) / original.getWidth();
            //aktualizacja własności zbindowanej z paskiem postępu w tabeli
                Platform.runLater(() -> progressProp.set(progress));
                Platform.runLater(() -> statusProp.set("Processing"));
            }
            Platform.runLater(() -> statusProp.set("Sent"));
            //przygotowanie ścieżki wskazującej na plik wynikowy
           // System.out.println("Gotowe");
            Path outputPath =
                    Paths.get(outputDir.getAbsolutePath(), originalFile.getName());
            //zapisanie zawartości bufora do pliku na dysku
            ImageIO.write(grayscale, "jpg", outputPath.toFile());
        } catch (IOException ex) {
            //translacja wyjątku
            throw new RuntimeException(ex);
        }
    }
}

