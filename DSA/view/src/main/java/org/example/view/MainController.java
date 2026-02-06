package org.example.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import org.example.model.DSA;
import org.example.model.Dao;
import org.example.model.FileDao;
import org.example.model.MyFile;

import java.io.File;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;


public class MainController {



    @FXML
    private TextArea textArea;
    @FXML
    private TextArea output;
    DSA dsa = new DSA();

    @FXML
    public void initialize() {
   // Ustawienie domyślnej wartości
    }

    @FXML
    private void Podpisz(){
        BigInteger[] podpis = dsa.podpisuj(textArea.getText());
        String text = podpis[0].toString(16) + "\n" + podpis[1].toString(16);
        output.setText(text);

    }

    @FXML
    public void Sprawdz(){
        if(dsa.weryfikujString(textArea.getText(),output.getText())){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Sukces");
            alert.setHeaderText(null);
            alert.setContentText("Podpis jest poprawny");
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Sukces");
            alert.setHeaderText(null);
            alert.setContentText("Podpis nie jest poprawny");
            alert.showAndWait();
        }
    }

    public void Wczytaj(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Wczytaj plik");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Plik podpisany", "*.*")
        );
        File file = fileChooser.showOpenDialog(null);
        if(file != null) {
            String path = file.getAbsolutePath();
            try(Dao<MyFile> dao = new FileDao(path)) {
                MyFile myFile = dao.read(path);
                String content = myFile.getContent();

                textArea.setText(content);

            } catch (Exception e) {
                throw new RuntimeException("Błąd odczytu lub weryfikacji", e);
            }
        }
    }

    public void WczytajKlucz(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Wczytaj klucz");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Klucz", "*.key")
        );
        File file = fileChooser.showOpenDialog(null);
        if(file != null) {
            String path = file.getAbsolutePath();
            try(Dao<MyFile> dao = new FileDao(path)) {
                MyFile myFile = dao.read(path);
                String content = myFile.getContent();

                dsa.importPublicKey(content);

            } catch (Exception e) {
                throw new RuntimeException("Błąd odczytu lub weryfikacji", e);
            }
        }
    }
    public void WczytajSygn(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Wczytaj plik");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Plik podpisany", "*.sig")
        );
        File file = fileChooser.showOpenDialog(null);
        if(file != null) {
            String path = file.getAbsolutePath();
            try(Dao<MyFile> dao = new FileDao(path)) {

                MyFile myFile1 = dao.read(path);
                String signature = myFile1.getContent();

                output.setText(signature);

            } catch (Exception e) {
                throw new RuntimeException("Błąd odczytu lub weryfikacji", e);
            }
        }
    }
    public void Zapisz(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Plik podpisany", "*.*")
        );
        File file = fileChooser.showSaveDialog(null);

        if (file != null) {
            String path = file.getAbsolutePath();
            String path2 = file.getAbsolutePath() + ".sig";

            MyFile myFile1 = new MyFile(file.getName(), textArea.getText());
            MyFile myFile2 = new MyFile(file.getName(), output.getText());
            try (Dao<MyFile> dao = new FileDao(path)) {
                dao.write(path, myFile1);
                dao.write(path2, myFile2);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Sukces");
                alert.setHeaderText(null);
                alert.setContentText("Plik został pomyślnie zapisany.");
                alert.showAndWait();
            } catch (Exception e) {
                throw new RuntimeException("Błąd zapisu pliku", e);
            }
        }
    }

    public void ZapiszKlucz(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Klucz", "*.key")
        );
        File file = fileChooser.showSaveDialog(null);

        if (file != null) {
            String path = file.getAbsolutePath();

            MyFile myFile = new MyFile(file.getName(), dsa.exportPublicKey());
            try (Dao<MyFile> dao = new FileDao(path)) {
                dao.write(path, myFile);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Sukces");
                alert.setHeaderText(null);
                alert.setContentText("Klucz został pomyślnie zapisany.");
                alert.showAndWait();
            } catch (Exception e) {
                throw new RuntimeException("Błąd zapisu klucza", e);
            }
        }
    }

}
