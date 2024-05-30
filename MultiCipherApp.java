import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class MultiCipherAppp extends Application{
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage){
        primaryStage.setTitle("Cipher Application");

        Label label = new Label("Enter the text to be encrypted: ");
        TextField inputText = new TextField();

        Label label2 = new Label("Enter the shift amount (For Caesar Cipher): ");
        TextField shiftAmount = new TextField();

        Label label3 = new Label("Enter the key (for Vigenere Cipher): ");
        TextField vigenereKey = new TextField();

        ComboBox<String> cipherSelector = new ComboBox<>();
        cipherSelector.getItems().addAll("Caesar Cipher", "Vigenere Cipher");
        cipherSelector.setValue("Caesar Cipher");

        Button encryptButton = new Button("Encrypt");
        Button decryptButton = new Button("Decrypt");

        Button loadFileButton = new Button("Load from File");
        Button saveFileButton = new Button("Save to File");

        TextArea resultArea = new TextArea();
        resultArea.setEditable(false);

        FileChooser fileChooser = new FileChooser();

        loadFileButton.setOnAction(e -> {
            File file = fileChooser.showOpenDialog(primaryStage);
            if (file != null) {
                try {
                    String content = new String(Files.readAllBytes(Paths.get(file.toURI())));
                    inputText.setText(content);
                }
                catch (IOException ex){
                    ex.printStackTrace();
                }
            }
        });

        saveFileButton.setOnAction(e -> {
            File file = fileChooser.showSaveDialog(primaryStage);
            if (file != null) {
                try {
                    Files.write(Paths.get(file.toURI()), resultArea.getText().getBytes());
                }
                catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        encryptButton.setOnAction(e -> {
            String text = inputText.getText();
            String selectedCipher = cipherSelector.getValue();
            if (selectedCipher.equals("Caesar Cipher")){
                int shift = Integer.parseInt(shiftAmount.getText());
                String encrypted = encryptCaesar(text, shift);
                resultArea.setText("Encrypted: " + encrypted);
            }
            else if (selectedCipher.equals("Vigenere Cipher")){
                String key = vigenereKey.getText();
                String encrypted = encryptVigenere(text, key);
                resultArea.setText("Encrypted: " + encrypted);
            }
        });

        decryptButton.setOnAction(e -> {
            String text = inputText.getText();
            String selectedCipher = cipherSelector.getValue();
            if (selectedCipher.equals("Caesar Cipher")){
                int shift = Integer.parseInt(shiftAmount.getText());
                String decrypted = decryptCaesar(text, shift);
                resultArea.setText("Decrypted: " + decrypted);
            }
            else if (selectedCipher.equals("Vigenere Cipher")){
                String key = vigenereKey.getText();
                String decrypted = decryptVigenere(text, key);
                resultArea.setText("Decrypted: " + decrypted);
            }
        });

        VBox vbox = new VBox(10, label, inputText, loadFileButton, label2, shiftAmount, label3, vigenereKey, cipherSelector, encryptButton, decryptButton, resultArea, saveFileButton);
        Scene scene = new Scene(vbox, 400, 500);

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static String encryptCaesar(String text, int shift){
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < text.length(); i++){
            char ch = text.charAt(i);
            if (Character.isLetter(ch)){
                char base = Character.isLowerCase(ch) ? 'a' : 'A';
                ch = (char) ((ch - base + shift) % 26 + base);
            }
            result.append(ch);
        }
        return result.toString();
    }

    public static String decryptCaesar(String text, int shift){
        return encryptCaesar(text, 26 - shift);
    }

    public static String encryptVigenere(String text, String key) {
        StringBuilder result = new StringBuilder();
        key = key.toUpperCase();
        for (int i = 0, j = 0; i < text.length(); i++) {
            char ch = text.charAt(i);
            if (Character.isLetter(ch)) {
                char base = Character.isLowerCase(ch) ? 'a' : 'A';
                ch = (char) ((ch - base + (key.charAt(j % key.length()) - 'A')) % 26 + base);
                j++;
            }
            result.append(ch);
        }
        return result.toString();
    }

    public static String decryptVigenere(String text, String key) {
        StringBuilder result = new StringBuilder();
        key = key.toUpperCase();
        for (int i = 0, j = 0; i < text.length(); i++) {
            char ch = text.charAt(i);
            if (Character.isLetter(ch)) {
                char base = Character.isLowerCase(ch) ? 'a' : 'A';
                ch = (char) ((ch - base - (key.charAt(j % key.length()) - 'A') + 26) % 26 + base);
                j++;
            }
            result.append(ch);
        }
        return result.toString();
    }
}
