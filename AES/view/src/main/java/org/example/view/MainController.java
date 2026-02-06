package org.example.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import org.example.model.AES;
import org.example.model.Dao;
import org.example.model.FileDao;
import org.example.model.MyFile;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;


public class MainController {

    AES aes = new AES();
    byte[] Key = new byte[16];

    @FXML
    private TextField textField;
    @FXML
    private TextArea textArea;
    @FXML
    private TextArea output;
    @FXML
    private ComboBox<Integer> combo;

    @FXML
    public void initialize() {
        combo.getItems().addAll(128, 192, 256);
        combo.setValue(128);  // Ustawienie domyślnej wartości
    }

    @FXML
    private void Zaszyfruj() throws AES.AESKeyException, AES.AESException {
        byte[] message = textArea.getText().getBytes();
        byte[] encrypted = aes.encode(message,Key);
        String text = Base64.getEncoder().encodeToString(encrypted);
        output.setText(text);
    }

    @FXML
    public void Deszyfruj() throws AES.AESKeyException, AES.AESException {
        byte[] message = Base64.getDecoder().decode(textArea.getText());
        byte[] decrypted = aes.decode(message,Key);
        String text = new String(decrypted, StandardCharsets.UTF_8);
        output.setText(text);
    }

    public void Wczytaj(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Wczytaj plik");
        fileChooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("Plik", "*.*")
        );
        File file = fileChooser.showOpenDialog(null);
        if(file != null) {
            String path = file.getAbsolutePath();
            try(Dao<MyFile> dao = new FileDao(path)) {
                MyFile myFile = dao.read(path);
                String content = myFile.getContent();
                textArea.setText(content);

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Sukces");
                alert.setHeaderText(null);
                alert.setContentText("Plik został pomyślnie wczytany.");
                alert.showAndWait();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void Zapisz(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Plik", "*.*")
        );
        File file = fileChooser.showSaveDialog(null);

        if (file != null) {
            String path = file.getAbsolutePath();
            MyFile myFile = new MyFile(file.getName(), output.getText());
            try(Dao<MyFile> dao = new FileDao(path)) {
                dao.write(path, myFile);

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

    public void Generuj() {
        SecureRandom random = new SecureRandom();
        int byteSize = combo.getValue() / 8;

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < byteSize; i++) {
            int randomChar = random.nextInt(94) + 33;
            sb.append((char) (randomChar));
        }

        textField.setText(sb.toString());
    }

    public void Zastosuj() throws AES.AESKeyException {
        byte[] key = textField.getText().getBytes(StandardCharsets.UTF_8);
        aes.testKey(key);
        Key = key;
    }

}
