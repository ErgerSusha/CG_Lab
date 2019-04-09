package sample;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import sample.adaptive.AdaptiveMethod;
import sample.local.BernsenMethod;
import sample.local.NiblackMethod;
import sample.low_frequency_filters.AveragingFilter;
import sample.low_frequency_filters.AveragingFilterModificated;
import sample.low_frequency_filters.GaussianFilter;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

public class Main extends Application {

    @FXML
    private TextField text_path;
    @FXML
    private ImageView img_frame;
    @FXML
    private ImageView img_result;
    @FXML
    private TextField valueTF;
    @FXML
    private TextField radiusTF;

    private int choosenMethod = 0;

    private File file;

    @Override
    public void start(Stage primaryStage) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
            Scene scene = new Scene(root, 1700, 1000);
            primaryStage.setTitle("JavaFX FileChooser");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Application.launch(args);
    }

    @FXML
    public void handleLoad() throws MalformedURLException {
        JFileChooser chooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
        int returnValue = chooser.showOpenDialog(null);

        if (returnValue == JFileChooser.APPROVE_OPTION) {
            text_path.setText(chooser.getSelectedFile().getAbsolutePath());
            file = new File(chooser.getSelectedFile().getAbsolutePath());
            String localURL = file.toURI().toURL().toString();
            img_frame.setImage(new Image(localURL));
            this.processImage();
        }
    }

    public void adaptiveMethod() {
        this.choosenMethod = 0;
    }

    public void averagingMethod() {
        this.choosenMethod = 1;
    }

    public void bernsenMethod() {
        this.choosenMethod = 2;
    }

    public void niblackMethod() {
        this.choosenMethod = 3;
    }

    public void averagingMethodModification() {
        this.choosenMethod = 4;
    }

    public void gaussianFilter() {
        this.choosenMethod = 5;
    }

    private void processImage() {
        try {
            BufferedImage image = ImageIO.read(file);
            int width = image.getWidth();
            int height = image.getHeight();

            int[][] rgbOldArray = new int[width][height];

            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    rgbOldArray[i][j] = image.getRGB(i, j);
                }
            }

            switch (this.choosenMethod) {
                case 0:
                    AdaptiveMethod.filter(width, height, rgbOldArray, image,
                            Float.valueOf(valueTF.getText()));
                    break;
                case 1:
                    AveragingFilter.filter(width, height, rgbOldArray, image,
                            Integer.valueOf(radiusTF.getText()));
                    break;
                case 2:
                    BernsenMethod.filter(width, height, rgbOldArray, image,
                            Integer.valueOf(radiusTF.getText()));
                    break;
                case 3:
                    NiblackMethod.filter(width, height, rgbOldArray, image,
                            Double.valueOf(valueTF.getText()), Integer.valueOf(radiusTF.getText()));
                    break;
                case 4:
                    AveragingFilterModificated.filter(width, height, rgbOldArray, image,
                            Integer.valueOf(valueTF.getText()));
                    break;
                case 5:
                    GaussianFilter.filter(width, height, rgbOldArray, image,
                            Integer.valueOf(valueTF.getText()));
                    break;
            }

            File newFile = new File("C:\\Users\\erger\\Desktop\\output.jpg");
            try {
                ImageIO.write(image, "jpg", newFile);
            } catch (IOException e) {
                e.printStackTrace();
            }

            img_result.setImage(new Image(newFile.toURI().toURL().toString()));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void perform() {
        this.processImage();
    }
}
