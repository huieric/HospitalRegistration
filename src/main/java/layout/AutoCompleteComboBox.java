package main.java.layout;

import com.jfoenix.controls.JFXAutoCompletePopup;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

public class AutoCompleteComboBox<T> {
    public AutoCompleteComboBox(final ComboBox<T> comboBox) {
        JFXAutoCompletePopup<T> autoCompletePopup = new JFXAutoCompletePopup<>();
        autoCompletePopup.setSelectionHandler(e -> {
            comboBox.setValue(e.getObject());
        });
        TextField editor = comboBox.getEditor();
        editor.textProperty().addListener((o, oldVal, newVal) -> {
            autoCompletePopup.filter(item -> item.toString().toLowerCase().contains(editor.getText().toLowerCase()));
            if (autoCompletePopup.getFilteredSuggestions().isEmpty()
                    || comboBox.showingProperty().get())
                autoCompletePopup.hide();
            else
                autoCompletePopup.show(editor);
        });
//        editor.focusedProperty().addListener((o, oldVal, newVal) -> {
//            if (comboBox.showingProperty().get()) {
//                autoCompletePopup.hide();
//                return;
//            }
//            if (newVal) {
//                autoCompletePopup.getSuggestions().clear();
//                autoCompletePopup.getSuggestions().addAll(comboBox.getItems());
//                autoCompletePopup.show(editor);
//            }
//        });
        comboBox.getSelectionModel().selectedItemProperty().addListener((o, oldVal, newVal) -> {
            autoCompletePopup.hide();
        });
    }
}

//import javafx.collections.FXCollections;
//import javafx.collections.ObservableList;
//import javafx.event.EventHandler;
//import javafx.scene.control.ComboBox;
//import javafx.scene.input.KeyCode;
//import javafx.scene.input.KeyEvent;
//import javafx.scene.input.MouseEvent;
//
//public class AutoCompleteComboBoxListener<T> implements EventHandler<KeyEvent> {
//
//    private ComboBox<T> comboBox;
//    private StringBuilder sb;
//    private ObservableList<T> data;
//    private boolean moveCaretToPos = false;
//    private int caretPos;
//
//    public AutoCompleteComboBoxListener(final ComboBox comboBox) {
//        this.comboBox = comboBox;
//        sb = new StringBuilder();
//        data = comboBox.getItems();
//
//        this.comboBox.setEditable(true);
//        this.comboBox.getEditor().setOnMouseClicked(new EventHandler<MouseEvent>() {
//            @Override
//            public void handle(MouseEvent event) {
//                System.out.println("Clicked!");
////                comboBox.show();
//                ObservableList<T> list = FXCollections.observableArrayList();
//                for (int i=0; i<data.size(); i++) {
//                    if(data.get(i).toString().toLowerCase().startsWith(
//                            AutoCompleteComboBoxListener.this.comboBox
//                                    .getEditor().getText().toLowerCase())) {
//                        list.add(data.get(i));
//                    }
//                }
//                comboBox.setItems(list);
//                if(!list.isEmpty()) {
//                    comboBox.show();
//                }
//            }
//        });
//        this.comboBox.setOnKeyPressed(new EventHandler<KeyEvent>() {
//
//            @Override
//            public void handle(KeyEvent t) {
//                comboBox.hide();
//            }
//        });
//        this.comboBox.setOnKeyReleased(AutoCompleteComboBoxListener.this);
//    }
//
//    @Override
//    public void handle(KeyEvent event) {
//
//        if(event.getCode() == KeyCode.UP) {
//            caretPos = -1;
//            moveCaret(comboBox.getEditor().getText().length());
//            return;
//        } else if(event.getCode() == KeyCode.DOWN) {
//            if(!comboBox.isShowing()) {
//                comboBox.show();
//            }
//            caretPos = -1;
//            moveCaret(comboBox.getEditor().getText().length());
//            return;
//        } else if(event.getCode() == KeyCode.BACK_SPACE) {
//            moveCaretToPos = true;
//            caretPos = comboBox.getEditor().getCaretPosition();
//        } else if(event.getCode() == KeyCode.DELETE) {
//            moveCaretToPos = true;
//            caretPos = comboBox.getEditor().getCaretPosition();
//        }
//
//        if (event.getCode() == KeyCode.ENTER) {
//            Object selectedItemText = comboBox.getValue();
//            if (selectedItemText!=null)
//                comboBox.getEditor().setText(selectedItemText.toString());
//            return;
//        }
//        if (event.getCode() == KeyCode.RIGHT || event.getCode() == KeyCode.LEFT
//                || event.isControlDown() || event.getCode() == KeyCode.HOME
//                || event.getCode() == KeyCode.END || event.getCode() == KeyCode.TAB) {
//            return;
//        }
//
//        ObservableList<T> list = FXCollections.observableArrayList();
//        for (int i=0; i<data.size(); i++) {
//            if(data.get(i).toString().toLowerCase().startsWith(
//                    AutoCompleteComboBoxListener.this.comboBox
//                            .getEditor().getText().toLowerCase())) {
//                list.add(data.get(i));
//            }
//        }
//        comboBox.setItems(list);
//
//        String t = comboBox.getEditor().getText();
//        comboBox.getEditor().setText(t);
////        if (selectedItemText!=null)
////            comboBox.getEditor().setText(selectedItemText.toString());
////        else
////            comboBox.getEditor().setText(t);
//
//        if(!moveCaretToPos) {
//            caretPos = -1;
//        }
//        moveCaret(t.length());
//        if(!list.isEmpty()) {
//            comboBox.show();
//        }
//    }
//
//    private void moveCaret(int textLength) {
//        if(caretPos == -1) {
//            comboBox.getEditor().positionCaret(textLength);
//        } else {
//            comboBox.getEditor().positionCaret(caretPos);
//        }
//        moveCaretToPos = false;
//    }
//
//}

