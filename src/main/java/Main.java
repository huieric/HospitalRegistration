package main.java;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import main.resources.model.TBrxxEntity;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

import java.sql.Timestamp;

public class Main extends Application {
    public static Stage curStage;
    public static SessionFactory factory;
    public static String id;
    public static boolean isPatient = false;

    @Override
    public void start(Stage primaryStage) throws Exception{
        try {
            factory = new Configuration().configure().buildSessionFactory();
        }
        catch (Throwable ex) {
            System.err.println("Failed to create sessionFactory object." + ex);
            throw new ExceptionInInitializerError(ex);
        }

        FXMLLoader loader = new FXMLLoader(getClass().getResource("../resources/view/Login.fxml"));
//        TabPane tabPane = loader.load();
        Pane pane = loader.load();
        Scene scene = new Scene(pane, pane.getPrefWidth(), pane.getPrefHeight());
//        scene.getStylesheets().add(getClass().getResource("../resources/css/Login.css").toExternalForm());

        curStage = primaryStage;
        primaryStage.setTitle("Hospital");
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("../resources/image/Icon.png")));
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
        if (isPatient) {
            Session session = factory.openSession();
            Transaction transaction = null;
            try {
                transaction = session.beginTransaction();
                Query query = session.createQuery("from TBrxxEntity where brbh=:brbh");
                query.setParameter("brbh", id);
                TBrxxEntity tBrxxEntity = (TBrxxEntity) query.list().get(0);
                tBrxxEntity.setDlrq(new Timestamp(System.currentTimeMillis()));
                session.update(tBrxxEntity);
                transaction.commit();
            }
            catch (HibernateException e) {
                System.out.println(e.toString());
                if (transaction!=null)
                    transaction.rollback();
            }
            finally {
                session.close();
            }
        }
        factory.close();
    }
}
