package main.java.controllers;

import com.jfoenix.controls.*;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import main.java.Main;
import main.resources.model.*;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.net.URL;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;

public class MainWindow_Doctor implements Initializable {
    public JFXTreeTableView<Patient> patientTable;
    public JFXTreeTableView<Income> incomeTable;
    public DatePicker startDatePicker;
    public DatePicker endDatePicker;
    public JFXTimePicker startTimePicker;
    public JFXTimePicker endTimePicker;
    public Label timeLabel2;
    public JFXTextField filterField;
    public Label welcomeLabel;
    public Pane titlePane;

    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;

    class Delta { double x,y; }
    private final Delta dragDelta = new Delta();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // set titlePane drag event
        titlePane.setOnMousePressed(e -> {
            dragDelta.x = Main.curStage.getX() - e.getScreenX();
            dragDelta.y = Main.curStage.getY() - e.getScreenY();
        });
        titlePane.setOnMouseDragged(e -> {
            Main.curStage.setX(e.getScreenX()+dragDelta.x);
            Main.curStage.setY(e.getScreenY()+dragDelta.y);
        });

        // Create tables
        JFXTreeTableColumn<Patient, String> idColumn = new JFXTreeTableColumn<>("挂号编号");
        idColumn.setCellValueFactory((TreeTableColumn.CellDataFeatures<Patient, String> param) -> {
            if (idColumn.validateValue(param)) {
                return param.getValue().getValue().id;
            } else {
                return idColumn.getComputedValue(param);
            }
        });
        JFXTreeTableColumn<Patient, String> nameColumn = new JFXTreeTableColumn<>("病人名称");
        nameColumn.setCellValueFactory((TreeTableColumn.CellDataFeatures<Patient, String> param) -> {
            if (nameColumn.validateValue(param)) {
                return param.getValue().getValue().name;
            } else {
                return nameColumn.getComputedValue(param);
            }
        });
        JFXTreeTableColumn<Patient, String> timeColumn = new JFXTreeTableColumn<>("挂号日期时间");
        timeColumn.setCellValueFactory((TreeTableColumn.CellDataFeatures<Patient, String> param) -> {
            if (timeColumn.validateValue(param)) {
                return param.getValue().getValue().time;
            } else {
                return timeColumn.getComputedValue(param);
            }
        });
        JFXTreeTableColumn<Patient, String> typeColumn = new JFXTreeTableColumn<>("号种类别");
        typeColumn.setCellValueFactory((TreeTableColumn.CellDataFeatures<Patient, String> param) -> {
            if (typeColumn.validateValue(param)) {
                return param.getValue().getValue().type;
            } else {
                return typeColumn.getComputedValue(param);
            }
        });
        patientTable.setId("tablePatient");

        JFXTreeTableColumn<Income, String> departmentColumn = new JFXTreeTableColumn<>("科室名称");
        departmentColumn.setCellValueFactory((TreeTableColumn.CellDataFeatures<Income, String> param) -> {
            if (departmentColumn.validateValue(param)) {
                return param.getValue().getValue().department;
            } else {
                return departmentColumn.getComputedValue(param);
            }
        });
        JFXTreeTableColumn<Income, String> idColumn2 = new JFXTreeTableColumn<>("医生编号");
        idColumn2.setCellValueFactory((TreeTableColumn.CellDataFeatures<Income, String> param) -> {
            if (idColumn2.validateValue(param)) {
                return param.getValue().getValue().id;
            } else {
                return idColumn2.getComputedValue(param);
            }
        });
        JFXTreeTableColumn<Income, String> nameColumn2 = new JFXTreeTableColumn<>("医生名称");
        nameColumn2.setCellValueFactory((TreeTableColumn.CellDataFeatures<Income, String> param) -> {
            if (nameColumn2.validateValue(param)) {
                return param.getValue().getValue().name;
            } else {
                return nameColumn2.getComputedValue(param);
            }
        });
        JFXTreeTableColumn<Income, String> typeColumn2 = new JFXTreeTableColumn<>("号种类别");
        typeColumn2.setCellValueFactory((TreeTableColumn.CellDataFeatures<Income, String> param) -> {
            if (typeColumn2.validateValue(param)) {
                return param.getValue().getValue().type;
            } else {
                return typeColumn2.getComputedValue(param);
            }
        });
        JFXTreeTableColumn<Income, Number> numberColumn = new JFXTreeTableColumn<>("挂号人次");
        numberColumn.setCellValueFactory((TreeTableColumn.CellDataFeatures<Income, Number> param) -> {
            if (numberColumn.validateValue(param)) {
                return param.getValue().getValue().number;
            } else {
                return numberColumn.getComputedValue(param);
            }
        });
        JFXTreeTableColumn<Income, Number> incomeColumn = new JFXTreeTableColumn<>("收入合计");
        incomeColumn.setCellValueFactory((TreeTableColumn.CellDataFeatures<Income, Number> param) -> {
            if (incomeColumn.validateValue(param)) {
                return param.getValue().getValue().income;
            } else {
                return incomeColumn.getComputedValue(param);
            }
        });
        incomeTable.setId("tableIncome");

        patientTable.setShowRoot(false);
        patientTable.setEditable(false);
        patientTable.getColumns().setAll(idColumn, nameColumn, timeColumn, typeColumn);
        patientTable.setColumnResizePolicy(JFXTreeTableView.CONSTRAINED_RESIZE_POLICY);

        incomeTable.setShowRoot(false);
        incomeTable.setEditable(false);
        incomeTable.getColumns().setAll(departmentColumn, idColumn2, nameColumn2, typeColumn2, numberColumn, incomeColumn);
        incomeTable.setColumnResizePolicy(JFXTreeTableView.CONSTRAINED_RESIZE_POLICY);

        //Create data
        ObservableList<Patient> patients = FXCollections.observableArrayList();
        Session session = Main.factory.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            // set welcome text
            Query query = session.createQuery("from TKsysEntity where ysbh=:ysbh");
            query.setParameter("ysbh", Main.id);
            TKsysEntity tKsysEntity = (TKsysEntity) query.list().get(0);
            welcomeLabel.setText(tKsysEntity.getYsmc().substring(0, 1)+"医生, 您好");
            query = session.createQuery("from TGhxxEntity where ysbh=:ysbh and thbz=0");
            query.setParameter("ysbh", Main.id);
            List ghxxs = query.list();
            for (Iterator it = ghxxs.iterator(); it.hasNext(); ) {
                TGhxxEntity tGhxxEntity = (TGhxxEntity) it.next();
                query = session.createQuery("from TBrxxEntity where brbh=:brbh");
                query.setParameter("brbh", tGhxxEntity.getBrbh());
                TBrxxEntity tBrxxEntity = (TBrxxEntity) query.list().get(0);
                query = session.createQuery("from THzxxEntity where hzbh=:hzbh");
                query.setParameter("hzbh", tGhxxEntity.getHzbh());
                THzxxEntity tHzxxEntity = (THzxxEntity) query.list().get(0);
                String type = tHzxxEntity.getSfzj()==1?"专家号":"普通号";
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Patient patient = new Patient(tGhxxEntity.getGhbh(), tBrxxEntity.getBrmc(), dateFormat.format(tGhxxEntity.getRqsj()).toString(), type);
                patients.add(patient);
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
        final TreeItem<Patient> patientRoot = new RecursiveTreeItem<>(patients, RecursiveTreeObject::getChildren);
        patientTable.setRoot(patientRoot);
        updateIncome(Timestamp.valueOf(LocalDate.EPOCH.atStartOfDay()), new Timestamp(System.currentTimeMillis()));

        DateFormat datetimeFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        final Timeline timeline = new Timeline(
                new KeyFrame(
                        Duration.millis(500),
                        event -> {
                            // Dispaly current time and refresh in every second
                            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                            String curTimeStr = datetimeFormat.format(timestamp);
                            timeLabel2.setText(curTimeStr);
                        }
                )
        );
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();

        // Text search
        filterField.textProperty().addListener((o, oldVal, newVal) -> {
            patientTable.setPredicate(patientProp -> {
                final Patient patient = patientProp.getValue();
                return patient.getId().contains(newVal)
                        || patient.getName().contains(newVal)
                        || patient.getType().contains(newVal);
            });
            incomeTable.setPredicate(incomeProp -> {
                final Income income = incomeProp.getValue();
                return income.getDepartment().contains(newVal)
                        || income.getId().contains(newVal)
                        || income.getName().contains(newVal)
                        || income.getType().contains(newVal);
            });
        });

        // Time search
        startDatePicker.valueProperty().addListener((o, oldVal, newVal) -> {
            LocalDate startDate = LocalDate.EPOCH;
            if (newVal!=null)
                startDate = newVal;
            startDateTime = startDate.atStartOfDay();
            LocalTime startTime = startTimePicker.getValue();
            if (startTime!=null)
                startDateTime = startDate.atTime(startTime.getHour(), startTime.getMinute());
            if (endDateTime!=null)
                updateIncome(Timestamp.valueOf(startDateTime), Timestamp.valueOf(endDateTime));
            else
                updateIncome(Timestamp.valueOf(startDateTime), new Timestamp(System.currentTimeMillis()));
        });
        startTimePicker.valueProperty().addListener((o, oldVal, newVal) -> {
            LocalTime startTime = LocalTime.MIN;
            if (newVal!=null)
                startTime = newVal;
            LocalDate startDate = startDatePicker.getValue();
            if (startDate!=null)
                startDateTime = startTime.atDate(startDate);
            else
                startDateTime = startTime.atDate(LocalDate.EPOCH);
            if (endDateTime!=null)
                updateIncome(Timestamp.valueOf(startDateTime), Timestamp.valueOf(endDateTime));
            else
                updateIncome(Timestamp.valueOf(startDateTime), new Timestamp(System.currentTimeMillis()));
        });
        endDatePicker.valueProperty().addListener((o, oldVal, newVal) -> {
            LocalDate endDate = LocalDate.now();
            if (newVal!=null)
                endDate = newVal;
            endDateTime = endDate.atStartOfDay();
            LocalTime endTime = endTimePicker.getValue();
            if (endTime!=null)
                endDateTime = endDate.atTime(endTime);
            if (startDateTime!=null)
                updateIncome(Timestamp.valueOf(startDateTime), Timestamp.valueOf(endDateTime));
            else
                updateIncome(Timestamp.valueOf(LocalDate.EPOCH.atStartOfDay()), Timestamp.valueOf(endDateTime));
        });
        endTimePicker.valueProperty().addListener((o, oldVal, newVal) -> {
            LocalTime endTime = LocalTime.MAX;
            if (newVal!=null)
                endTime = newVal;
            LocalDate endDate = endDatePicker.getValue();
            if (endDate!=null)
                endDateTime = endTime.atDate(endDate);
            else
                endDateTime = endTime.atDate(LocalDate.now());
            if (startDateTime!=null)
                updateIncome(Timestamp.valueOf(startDateTime), Timestamp.valueOf(endDateTime));
            else
                updateIncome(Timestamp.valueOf(LocalDate.EPOCH.atStartOfDay()), Timestamp.valueOf(endDateTime));
        });
    }

    private void updateIncome(Timestamp startTime, Timestamp endTime) {
        ObservableList<Income> incomes = FXCollections.observableArrayList();
        Session session = Main.factory.openSession();
        Transaction tx = null;
        try {
            Query query = session.createQuery("from TGhxxEntity where rqsj>=:startTime and rqsj<=:endTime and thbz=0 order by ysbh");
            query.setParameter("startTime", startTime);
            query.setParameter("endTime", endTime);
            List ghList = query.list();
            ArrayList<Income> incomeArrayList = new ArrayList<Income>();
            int i=0;
            for (Iterator it=ghList.iterator(); it.hasNext(); ) {
                TGhxxEntity tGhxxEntity = (TGhxxEntity) it.next();
                query = session.createQuery("from TKsysEntity where ysbh=:ysbh");
                query.setParameter("ysbh", tGhxxEntity.getYsbh());
                TKsysEntity tKsysEntity = (TKsysEntity) query.list().get(0);
                query = session.createQuery("from TKsxxEntity where ksbh=:ksbh");
                query.setParameter("ksbh", tKsysEntity.getKsbh());
                TKsxxEntity tKsxxEntity = (TKsxxEntity) query.list().get(0);
                query = session.createQuery("from THzxxEntity where hzbh=:hzbh");
                query.setParameter("hzbh", tGhxxEntity.getHzbh());
                THzxxEntity tHzxxEntity = (THzxxEntity) query.list().get(0);
                if (incomeArrayList.isEmpty() || !tGhxxEntity.getYsbh().equals(incomeArrayList.get(i-1).getId())) {
                    if (!incomeArrayList.isEmpty()) {
                        if (incomeArrayList.get(i-1).getNumber()!=0)
                            incomes.add(incomeArrayList.get(i-1));
                        if (incomeArrayList.get(i-2).getNumber()!=0)
                            incomes.add(incomeArrayList.get(i-2));
                    }
                    incomeArrayList.add(new Income(
                            tKsxxEntity.getKsmc(),
                            tGhxxEntity.getYsbh(),
                            tKsysEntity.getYsmc(),
                            "普通号",
                            0,
                            0.
                    ));
                    incomeArrayList.add(new Income(
                            tKsxxEntity.getKsmc(),
                            tGhxxEntity.getYsbh(),
                            tKsysEntity.getYsmc(),
                            "专家号",
                            0,
                            0.
                    ));
                    i+=2;
                    if (tHzxxEntity.getSfzj()==1) {
                        incomeArrayList.get(i-1).setNumber(1);
                        incomeArrayList.get(i-1).setIncome(tGhxxEntity.getGhfy().doubleValue());
                    }
                    else {
                        incomeArrayList.get(i-2).setNumber(1);
                        incomeArrayList.get(i-2).setIncome(tGhxxEntity.getGhfy().doubleValue());
                    }
                }
                else {
                    if (tHzxxEntity.getSfzj()==1) {
                        incomeArrayList.get(i-1).setNumber(incomeArrayList.get(i-1).getNumber()+1);
                        incomeArrayList.get(i-1).setIncome(incomeArrayList.get(i-1).getIncome()+tGhxxEntity.getGhfy().intValue());
                    }
                    else {
                        incomeArrayList.get(i-2).setNumber(incomeArrayList.get(i-2).getNumber()+1);
                        incomeArrayList.get(i-2).setIncome(incomeArrayList.get(i-2).getIncome()+tGhxxEntity.getGhfy().intValue());
                    }
                }
            }
            if (!incomeArrayList.isEmpty()) {
                if (incomeArrayList.get(i-1).getNumber()!=0)
                    incomes.add(incomeArrayList.get(i-1));
                if (incomeArrayList.get(i-2).getNumber()!=0)
                    incomes.add(incomeArrayList.get(i-2));
            }
            // build tree
        }
        catch (HibernateException e) {
            System.out.println(e.toString());
            tx.rollback();
        }
        finally {
            session.close();
        }
        final TreeItem<Income> incomeRoot = new RecursiveTreeItem<>(incomes, RecursiveTreeObject::getChildren);
        incomeTable.setRoot(incomeRoot);
    }

    private static final class Income extends RecursiveTreeObject<Income> {
        public StringProperty department;
        public StringProperty id;
        public StringProperty name;
        public StringProperty type;
        public IntegerProperty number;
        public DoubleProperty income;

        public Income() {

        }
        public Income(String department, String id, String name, String type, Integer number, Double income) {
            this.department = new SimpleStringProperty(department);
            this.id = new SimpleStringProperty(id);
            this.name = new SimpleStringProperty(name);
            this.type = new SimpleStringProperty(type);
            this.number = new SimpleIntegerProperty(number);
            this.income = new SimpleDoubleProperty(income);
        }

        public String getDepartment() {
            return department.get();
        }

        public String getId() {
            return id.get();
        }

        public String getName() {
            return name.get();
        }

        public String getType() {
            return type.get();
        }

        public Integer getNumber() {
            return number.get();
        }

        public Double getIncome() {
            return income.get();
        }

        public void setDepartment(String department) {
            this.department.set(department);
        }

        public void setId(String id) {
            this.id.set(id);
        }

        public void setName(String name) {
            this.name.set(name);
        }

        public void setType(String type) {
            this.type.set(type);
        }

        public void setNumber(Integer number) {
            this.number.set(number);
        }

        public void setIncome(Double income) {
            this.income.set(income);
        }
    }

    private static final class Patient extends RecursiveTreeObject<Patient> {
        public StringProperty id;
        public StringProperty name;
        public StringProperty time;
        public StringProperty type;

        public Patient() {

        }
        public Patient(String id, String name, String time, String type) {
            this.id = new SimpleStringProperty(id);
            this.name = new SimpleStringProperty(name);
            this.time = new SimpleStringProperty(time);
            this.type = new SimpleStringProperty(type);
        }

        public String getId() { return id.get(); }

        public String getName() {
            return name.get();
        }

        public String getTime() {
            return time.get();
        }

        public String getType() {
            return type.get();
        }

        public void setId(String id) {
            this.id.set(id);
        }

        public void setName(String name) {
            this.name.set(name);
        }

        public void setTime(String time) {
            this.time.set(time);
        }

        public void setType(String type) {
            this.type.set(type);
        }
    }
}