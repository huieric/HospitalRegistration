package main.java.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.stage.StageStyle;
import main.java.Main;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Login implements Initializable {
    public ImageView boxImage;
    public JFXTextField idInput;
    public JFXPasswordField passwordInput;
    public JFXRadioButton doctorRadioButton;
    public JFXRadioButton patientRadioButton;
    public JFXButton button;
    public Label userLabel;
    public Label passwordLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
//        idInput.setText("000002");
//        passwordInput.setText("12345");

        userLabel.setVisible(false);
        passwordLabel.setVisible(false);
        Image image = new Image(getClass().getResourceAsStream("../../resources/image/LoginTitleIcon.png"));
        boxImage.setImage(image);
        patientRadioButton.selectedProperty().addListener((o, oldVal, newVal) -> {
            if (newVal)
                doctorRadioButton.setSelected(false);
            else
                doctorRadioButton.setSelected(true);
        });
        doctorRadioButton.selectedProperty().addListener((o, oldVal, newVal) -> {
            if (newVal)
                patientRadioButton.setSelected(false);
            else
                patientRadioButton.setSelected(true);
        });
        idInput.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!passwordInput.getText().isEmpty()) {
                    passwordInput.clear();
                }
            }
        });
        idInput.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        if (idInput.isFocused()) {
                            userLabel.setVisible(false);
                        }
                        if (idInput.isFocused() && !idInput.getText().isEmpty()) {
                            idInput.selectAll();
                        }
                    }
                });
            }
        });
        passwordInput.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (passwordInput.isFocused()) {
                    passwordLabel.setVisible(false);
                }
            }
        });
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (idInput.getText().isEmpty()) {
                    userLabel.setVisible(true);
                    userLabel.setText("User id empty");
                }
                else if (passwordInput.getText().isEmpty()) {
                    passwordLabel.setVisible(true);
                    passwordLabel.setText("Password empty");
                }
                else {
                    String id = idInput.getText();
                    String password = passwordInput.getText();
                    Session session = Main.factory.openSession();
                    Transaction tx = null;
                    try {
                        tx = session.beginTransaction();
                        Query query = session.createQuery("select count(*) from TBrxxEntity where brbh=:brbh and dlkl=:dlkl");
                        query.setParameter("brbh", id);
                        query.setParameter("dlkl", password);
                        Long exists = (Long) query.list().get(0);
                        if (exists==0) {
                            System.out.println("User not exists or incorrect password");
                            Alert alert = new Alert(Alert.AlertType.WARNING, "User not exists or incorrect password", ButtonType.OK);
                            alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
                            alert.initStyle(StageStyle.UNDECORATED);
                            alert.setHeaderText(null);
                            alert.setGraphic(null);
                            alert.show();
                            return;
                        }
                        Main.id = idInput.getText();
                        if (Main.curStage != null)
                            Main.curStage.close();
                        if (patientRadioButton.isSelected()) {
                            Main.isPatient = true;
                            try {
                                FXMLLoader loader = new FXMLLoader(getClass().getResource("../../resources/view/MainWindow_Patient.fxml"));
                                BorderPane borderPane = loader.load();
                                Main.curStage.setScene(new Scene(borderPane, borderPane.getPrefWidth(), borderPane.getPrefHeight()));
                                Main.curStage.show();
                            }
                            catch (IOException ioexception) {
                                System.out.print(ioexception.toString());
                                System.exit(1);
                            }
                        }
                        else {
                            Main.isPatient = false;
                            try {
                                FXMLLoader loader = new FXMLLoader(getClass().getResource("../../resources/view/MainWindow_Doctor.fxml"));
                                BorderPane borderPane = loader.load();
                                Main.curStage.setScene(new Scene(borderPane, borderPane.getPrefWidth(), borderPane.getPrefHeight()));
                                Main.curStage.show();
                            }
                            catch (IOException ioexception) {
                                System.out.print(ioexception.toString());
                                System.exit(1);
                            }
                        }
                    }
                    catch (HibernateException e) {
                        if (tx!=null)
                            tx.rollback();
                        e.printStackTrace();
                    }
                    finally {
                        session.close();
                    }
                }
            }
        });
    }
}
