package assignment.instruments;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Ouda
 */
public class InstrumentsController implements Initializable {

    @FXML
    private MenuBar mainMenu;
    @FXML
    private ImageView image;
    @FXML
    private BorderPane InstrumentPortal;
    @FXML
    private Label title;
    @FXML
    private Label about;
    @FXML
    private Button play;
    @FXML
    private Button pause;
    @FXML
    private ComboBox type;
    @FXML
    private TextField name;
    Media media;
    MediaPlayer player;
    OrderedDictionary database = null;
    InstrumentRecord instrument = null;
    int instrumentType = 1;

    @FXML
    public void exit() {
        Stage stage = (Stage) mainMenu.getScene().getWindow();
        stage.close();
    }

    public void find() {
        DataKey key = new DataKey(this.name.getText(), instrumentType);
        try {
            instrument = database.find(key);
            showInstrument();
        } catch (DictionaryException ex) {
            displayAlert(ex.getMessage());
        }
    }

    public void delete() {
        InstrumentRecord previousInstrument = null;
        try {
            previousInstrument = database.predecessor(instrument.getDataKey());
        } catch (DictionaryException ex) {

        }
        InstrumentRecord nextInstrument = null;
        try {
            nextInstrument = database.successor(instrument.getDataKey());
        } catch (DictionaryException ex) {

        }
        DataKey key = instrument.getDataKey();
        try {
            database.remove(key);
        } catch (DictionaryException ex) {
            System.out.println("Error in delete "+ ex);
        }
        if (database.isEmpty()) {
            this.InstrumentPortal.setVisible(false);
            displayAlert("No more Instruments in the database to show");
        } else {
            if (previousInstrument != null) {
                instrument = previousInstrument;
                showInstrument();
            } else if (nextInstrument != null) {
                instrument = nextInstrument;
                showInstrument();
            }
        }
    }

    private void showInstrument() {
        play.setDisable(false);
        pause.setDisable(true);
        if (player != null) {
            player.stop();
        }
        String img = instrument.getImage();
        Image instrumentImage = new Image("file:src/main/resources/assignment/Instruments/images/" + img);
        image.setImage(instrumentImage);
        title.setText(instrument.getDataKey().getInstrumentName());
        about.setText(instrument.getAbout());
    }

    private void displayAlert(String msg) {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("Alert.fxml"));
            Parent ERROR = loader.load();
            AlertController controller = (AlertController) loader.getController();

            Scene scene = new Scene(ERROR);
            Stage stage = new Stage();
            stage.setScene(scene);

            stage.getIcons().add(new Image("file:src/main/resources/assignment/Instruments/images/INSicon.png"));
            stage.setTitle("Dictionary Exception");
            controller.setAlertText(msg);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

        } catch (IOException ex1) {

        }
    }

    public void getType() {
        switch (this.type.getValue().toString()) {
            case "String":
                this.instrumentType = 1;
                break;
            case "Wind":
                this.instrumentType = 2;
                break;
            case "Percussion":
                this.instrumentType = 3;
                break;
            default:
                break;
        }
    }

    public void first() {
        try{
            instrument = database.smallest();
            showInstrument();
        }
        catch(DictionaryException ex){
            System.out.println("Error in delete "+ ex);
        }
        // Write this method
    }

    public void last() {
        try{
            instrument = database.largest();
            showInstrument();
        }
        catch(DictionaryException ex){
            System.out.println("Error in delete "+ ex);
        }
        // Write this method
    }


    public void next() {
        try{
            instrument = database.successor(instrument.getDataKey());
            showInstrument();
        }
        catch (DictionaryException ex) {
            displayAlert(ex.getMessage());
        }
    }

    public void previous() {

        try {
            instrument = database.predecessor(instrument.getDataKey());
            showInstrument();
        } catch (DictionaryException ex) {
            displayAlert(ex.getMessage());
        }
        // Write this method
    }

    public void play() {
        String filename = "src/main/resources/assignment/Instruments/sounds/" + instrument.getSound();
        media = new Media(new File(filename).toURI().toString());
        player = new MediaPlayer(media);
        play.setDisable(true);
        pause.setDisable(false);
        player.play();
    }

    public void pause() {
        play.setDisable(false);
        pause.setDisable(true);
        if (player != null) {
            player.stop();
        }
    }

    public void loadDictionary() {
        Scanner input;
        int line = 0;
        try {
            String instrumentName = "";
            String description;
            int type = 0;
            input = new Scanner(new File("InstrumentsDatabase.txt"));
            while (input.hasNext()) // read until  end of file
            {
                String data = input.nextLine();
                switch (line % 3) {
                    case 0:
                        type = Integer.parseInt(data);
                        break;
                    case 1:
                        instrumentName = data;
                        break;
                    default:
                        description = data;
                        database.insert(new InstrumentRecord(new DataKey(instrumentName, type), description, instrumentName + ".mp3", instrumentName + ".jpg"));
                        break;
                }
                line++;
            }
        } catch (IOException e) {
            System.out.println("There was an error in reading or opening the file: InstrumentsDatabase.txt");
            System.out.println(e.getMessage());
        } catch (DictionaryException ex) {
            Logger.getLogger(InstrumentsController.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.InstrumentPortal.setVisible(true);
        this.first();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        database = new OrderedDictionary();
        type.setItems(FXCollections.observableArrayList(
                "String", "Wind", "Percussion"
        ));
        type.setValue("String");
    }

}
