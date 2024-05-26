package com.warehouse.warehouse.util;

import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;

import java.util.regex.Pattern;

public class FieldValidation {

    public static void setTextFieldLimit(TextField textField, int maxLength) {
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.length() > maxLength) {
                textField.setText(oldValue);
            }
        });
    }

    public static void setNumericField(TextField textField) {
        Pattern validEditingState = Pattern.compile("\\d*");
        TextFormatter<?> textFormatter = new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            if (validEditingState.matcher(newText).matches()) {
                return change;
            } else {
                return null;
            }
        });
        textField.setTextFormatter(textFormatter);
    }
}