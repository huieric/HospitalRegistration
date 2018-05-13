package main.java.controllers;

import com.jfoenix.controls.*;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;
import javafx.util.Duration;
import main.java.layout.AutoCompleteComboBox;
import main.java.Main;
import main.resources.model.*;
import org.controlsfx.control.StatusBar;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.math.BigDecimal;
import java.net.URL;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("deprecation")
public class MainWindow_Patient implements Initializable {

    public Label feeLabel;
    public Label accountLabel;
    public JFXComboBox<String> deptBox;
    public JFXComboBox<String> doctBox;
    public JFXComboBox<String> typeBox;
    public JFXComboBox<String> nameBox;
    public StatusBar statusBar;
    public JFXButton confirmButton;
    public JFXButton resetButton;
    public Label timeLabel;
    public JFXTreeTableView<Record> recordTable;
    public JFXTextField filterField;
    public Label welcomeLabel;
    public JFXSpinner spinner;

    private TGhxxEntity tGhxxEntity;

    @FXML
    private void handleExit(ActionEvent event) {
        Session session = Main.factory.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            TBrxxEntity tBrxxEntity = (TBrxxEntity) session.get(TBrxxEntity.class, Main.id);
            tBrxxEntity.setDlrq(new Timestamp(System.currentTimeMillis()));
            session.update(tBrxxEntity);
            tx.commit();
        }
        catch (HibernateException e) {
            if (tx!=null)
                tx.rollback();
            e.printStackTrace();
        }
        finally {
            session.close();
        }
        if (Main.curStage!=null)
            Main.curStage.close();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        spinner.setVisible(false);
        // set patient id
        tGhxxEntity = new TGhxxEntity();
        tGhxxEntity.setBrbh(Main.id);
        // get dept list
        Session session = Main.factory.openSession();
        Transaction tx = session.beginTransaction();
        try {
            List ksxxs = session.createQuery("from TKsxxEntity").list();
            deptBox.getItems().clear();
            for (Iterator it=ksxxs.iterator(); it.hasNext(); ) {
                TKsxxEntity ksxx = (TKsxxEntity)it.next();
                deptBox.getItems().add(ksxx.getKsbh() + " " + ksxx.getKsmc() + " " + ksxx.getPyzs());
            }
            tx.commit();
        }
        catch (HibernateException e) {
            if (tx!=null)
                tx.rollback();
            e.printStackTrace();
        }
        finally {
            session.close();
        }
        /*
        comboBox列表中的值被选中时，去除编号和拼音字首，只留下名称。
         */
        deptBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (newValue!=null) {
                    String[] colList = newValue.split(" ");
                    if (colList.length==3)
                        Platform.runLater(()->deptBox.getEditor().setText(colList[1]));
                }
            }
        });
        doctBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (newValue!=null) {
                    String[] colList = newValue.split(" ");
                    if (colList.length==3)
                        Platform.runLater(()->doctBox.getEditor().setText(colList[1]));
                }
            }
        });
        nameBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (newValue!=null) {
                    String[] colList = newValue.split(" ");
                    if (colList.length==3)
                        Platform.runLater(()->nameBox.getEditor().setText(colList[1]));
                }
            }
        });

        /*
        deptBox的值改变（从列表中选中或编辑完成）时，清空后续comboBox，检查当前comboBox的值是否合法，不合法则清空。
         */
        deptBox.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (newValue!=null && !newValue.equals(oldValue)) {
                    doctBox.getEditor().clear();
                    typeBox.getEditor().clear();
                    nameBox.getEditor().clear();
                    feeLabel.setText("") ;
                    statusBar.setText("");
                    List itemList = deptBox.getItems();
                    int i;
                    for (i=0; i<itemList.size(); i++) {
                        if (newValue.equals(itemList.get(i).toString().split(" ")[1])) {
                            break;
                        }
                    }
                    if (i==itemList.size())
                        deptBox.getEditor().clear();
                }
            }
        });
        doctBox.getEditor().focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (oldValue==false && newValue==true) {
                    if (deptBox.getEditor().getText().isEmpty()) {
                        Session session = Main.factory.openSession();
                        Transaction tx = session.beginTransaction();
                        try {
                            List ksyss = session.createQuery("from TKsysEntity ").list();
                            doctBox.getItems().clear();
                            for (Iterator it=ksyss.iterator(); it.hasNext(); ) {
                                TKsysEntity ksys = (TKsysEntity)it.next();
                                doctBox.getItems().add(ksys.getYsbh() + " " + ksys.getYsmc() + " " + ksys.getPtzs());
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
                    else {
                        Session session = Main.factory.openSession();
                        Transaction tx = session.beginTransaction();
                        try {
                            Query query = session.createQuery("from TKsysEntity where ksbh in (from TKsxxEntity where ksmc = :ksmc)");
                            query.setParameter("ksmc", deptBox.getEditor().getText());
                            List ksyss = query.list();
                            doctBox.getItems().clear();
                            for (Iterator it=ksyss.iterator(); it.hasNext(); ) {
                                TKsysEntity ksys = (TKsysEntity)it.next();
                                doctBox.getItems().add(ksys.getYsbh() + " " + ksys.getYsmc() + " " + ksys.getPtzs());
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
            }
        });
        doctBox.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (newValue!=null && !newValue.equals(oldValue)) {
                    typeBox.getEditor().clear();
                    nameBox.getEditor().clear();
                    feeLabel.setText("");
                    statusBar.setText("");
                    List itemList = doctBox.getItems();
                    int i;
                    String ysbh=null;
                    for (i=0; i<itemList.size(); i++) {
                        String[] colList = itemList.get(i).toString().split(" ");
                        if (newValue.equals(colList[1])) {
                            ysbh = colList[0];
                            break;
                        }
                    }
                    if (i==itemList.size()) {
                        doctBox.getEditor().clear();
                    }
                    else {
                        Session session = Main.factory.openSession();
                        Transaction tx = session.beginTransaction();
                        try {
                            Query query = session.createQuery("from TKsysEntity where ysbh=:ysbh");
                            query.setParameter("ysbh", ysbh);
                            tGhxxEntity.setYsbh(ysbh);
                            TKsysEntity ksys = (TKsysEntity)query.list().get(0);
                            typeBox.getItems().clear();
                            if (ksys.getSfzj()==1) {
                                typeBox.getItems().addAll("普通号", "专家号");
                            }
                            else {
                                typeBox.getItems().addAll("普通号");
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
            }
        });
        typeBox.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (newValue!=null && !newValue.equals(oldValue)) {
                    List itemList = typeBox.getItems();
                    int i;
                    for (i=0; i<itemList.size(); i++) {
                        String item = itemList.get(i).toString();
                        if (newValue.equals(item)) {
                            break;
                        }
                    }
                    if (i==itemList.size()) {
                        typeBox.getEditor().clear();
                    }
                    else {
                        nameBox.getItems().clear();
                        nameBox.getItems().add(deptBox.getValue()+newValue);
                    }
                }
            }
        });
        nameBox.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (newValue==null) {
                    feeLabel.setText("");
                    return;
                }
                if (newValue.isEmpty()) {
                    feeLabel.setText("");
                }
                else {
                    byte isExpert = typeBox.getValue().equals("专家号")?(byte)1:(byte)0;
                    Session session = Main.factory.openSession();
                    Transaction tx = session.beginTransaction();
                    try {
                        Query query = session.createQuery("from THzxxEntity where sfzj=:sfzj and ksbh in (select ksbh from TKsxxEntity where ksmc=:ksmc)");
                        query.setParameter("sfzj", isExpert);
                        query.setParameter("ksmc", deptBox.getValue());
                        THzxxEntity hzxx = (THzxxEntity)query.list().get(0);
                        feeLabel.setText(hzxx.getGhfy().toString()+"元");
                        tGhxxEntity.setGhfy(hzxx.getGhfy());
                        tGhxxEntity.setHzbh(hzxx.getHzbh());
                        tx.commit();
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
        // Customize combobox to autocomplete
        new AutoCompleteComboBox<String>(deptBox);
        new AutoCompleteComboBox<String>(doctBox);
        new AutoCompleteComboBox<String>(typeBox);
        new AutoCompleteComboBox<String>(nameBox);

        // Confirm button clicked
        confirmButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                // recharge
                if (confirmButton.getText().equals("充值")) {
                    spinner.setVisible(true);
                    try {
                        TimeUnit.SECONDS.sleep(3);
                    }
                    catch (InterruptedException e) {
                        System.out.println(e.toString());
                        return;
//                        System.exit(1);
                    }
                    Session session = Main.factory.openSession();
                    Transaction tx = session.beginTransaction();
                    try {
                        Query query = session.createQuery("from TBrxxEntity where brbh=:brbh");
                        query.setParameter("brbh", Main.id);
                        TBrxxEntity tBrxxEntity = (TBrxxEntity) query.list().get(0);
                        tBrxxEntity.setYcje(tBrxxEntity.getYcje().add(BigDecimal.valueOf(50)));
                        session.update(tBrxxEntity);
                        tx.commit();
                        accountLabel.setText(tBrxxEntity.getYcje()+"元");
                    }
                    catch (HibernateException e) {
                        System.out.println(e.toString());
                        if (tx==null)
                            tx.rollback();
                    }
                    finally {
                        session.close();
                    }
                    spinner.setVisible(false);
                    statusBar.setText("充值成功！");
                    confirmButton.setText("确定");
                    return;
                }
                // Check
                if (deptBox.getValue()==null
                        || doctBox.getValue()==null
                        || typeBox.getValue()==null
                        || nameBox.getValue()==null) {
                    JFXAlert<Void> alert = new JFXAlert<>(Main.curStage);
                    alert.show();
                    statusBar.setText("挂号失败！请完善挂号信息");
                    return;
                }
                long id = 0;
                Session session = Main.factory.openSession();
                Transaction tx = session.beginTransaction();
                try {
                    Query query = session.createQuery("from TKsxxEntity where ksmc=:ksmc");
                    query.setParameter("ksmc", deptBox.getValue());
                    if (query.list().size()==0) {
                        JFXAlert<Void> alert = new JFXAlert<>(Main.curStage);
                        alert.show();
                        statusBar.setText("挂号失败！科室不存在");
                        return;
                    }
                    TKsxxEntity tKsxxEntity = (TKsxxEntity) query.list().get(0);
                    query = session.createQuery("from TKsysEntity where ksbh=:ksbh and ysmc=:ysmc");
                    query.setParameter("ksbh", tKsxxEntity.getKsbh());
                    query.setParameter("ysmc", doctBox.getValue());
                    if (query.list().size()==0) {
                        JFXAlert<Void> alert = new JFXAlert<>(Main.curStage);
                        alert.show();
                        statusBar.setText("挂号失败！医生不存在");
                        return;
                    }
                    TKsysEntity tKsysEntity = (TKsysEntity) query.list().get(0);
                    String type = typeBox.getValue();
                    if (!type.equals("专家号") && !type.equals("普通号")) {
                        JFXAlert<Void> alert = new JFXAlert<>(Main.curStage);
                        alert.show();
                        statusBar.setText("挂号失败！号种类别不存在");
                        return;
                    }
                    if (type.equals("专家号") && tKsysEntity.getSfzj()==0) {
                        JFXAlert<Void> alert = new JFXAlert<>(Main.curStage);
                        alert.show();
                        statusBar.setText("挂号失败！该医生不是专家");
                        return;
                    }
                    if (!nameBox.getValue().equals(deptBox.getValue()+type)) {
                        JFXAlert<Void> alert = new JFXAlert<>(Main.curStage);
                        alert.show();
                        statusBar.setText("挂号失败！号种名称不存在");
                        return;
                    }
                    // check ok
                    query = session.createQuery("select count(*) from TGhxxEntity ");
                    id = (Long) query.list().get(0) + 1;
                    query = session.createQuery("select count(*) from TGhxxEntity where hzbh=:hzbh");
                    query.setParameter("hzbh", tGhxxEntity.getHzbh());
                    Long num = (Long) query.list().get(0)+1;
                    tGhxxEntity.setGhbh(String.format("%6d", id).replace(" ", "0"));
                    tGhxxEntity.setGhrc(Math.toIntExact(num));
                    tGhxxEntity.setThbz((byte)0);
                    tGhxxEntity.setRqsj(new Timestamp(System.currentTimeMillis()));
                    session.save(tGhxxEntity);
                    query = session.createQuery("from TBrxxEntity where brbh=:brbh");
                    query.setParameter("brbh", Main.id);
                    TBrxxEntity tBrxxEntity = (TBrxxEntity) query.list().get(0);
                    if (tBrxxEntity.getYcje().compareTo(tGhxxEntity.getGhfy())<0) {
                        statusBar.setText("余额不足！请充值");
                        confirmButton.setText("充值");
                        return;
                    }
                    tBrxxEntity.setYcje(tBrxxEntity.getYcje().subtract(tGhxxEntity.getGhfy()));
                    session.save(tBrxxEntity);
                    tx.commit();
                }
                catch (HibernateException e) {
                    if (tx!=null)
                        tx.rollback();
                    e.printStackTrace();
                }
                finally {
                    session.close();
                }
                statusBar.setText("挂号成功！挂号编号："+id);
                updateRecordTable();
            }
        });
        // Reset button clicked
        resetButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                deptBox.getEditor().clear();
                doctBox.getEditor().clear();
                typeBox.getEditor().clear();
                nameBox.getEditor().clear();
                feeLabel.setText("");
                statusBar.setText("");
            }
        });
        // Display up-center timeLabel and refresh in every one second
        DateFormat datetimeFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        final Timeline timeline = new Timeline(
                new KeyFrame(
                        Duration.millis(500),
                        event -> {
                            // Dispaly current time and refresh in every second
                            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                            String curTimeStr = datetimeFormat.format(timestamp);
                            timeLabel.setText(curTimeStr);
                        }
                )
        );
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
        // Display table
        JFXTreeTableColumn<Record, String> idColumn = new JFXTreeTableColumn<>("挂号编号");
        idColumn.setPrefWidth(70);
        idColumn.setCellValueFactory((TreeTableColumn.CellDataFeatures<Record, String> param) -> {
            if (idColumn.validateValue(param)) {
                return param.getValue().getValue().id;
            } else {
                return idColumn.getComputedValue(param);
            }
        });
        JFXTreeTableColumn<Record, String> patientColumn = new JFXTreeTableColumn<>("病人姓名");
        patientColumn.setPrefWidth(70);
        patientColumn.setCellValueFactory((TreeTableColumn.CellDataFeatures<Record, String> param) -> {
            if (patientColumn.validateValue(param)) {
                return param.getValue().getValue().patientName;
            } else {
                return patientColumn.getComputedValue(param);
            }
        });
        JFXTreeTableColumn<Record, String> deptColumn = new JFXTreeTableColumn<>("科室名称");
        deptColumn.setPrefWidth(70);
        deptColumn.setCellValueFactory((TreeTableColumn.CellDataFeatures<Record, String> param) -> {
            if (deptColumn.validateValue(param)) {
                return param.getValue().getValue().departName;
            } else {
                return deptColumn.getComputedValue(param);
            }
        });
        JFXTreeTableColumn<Record, String> doctorColumn = new JFXTreeTableColumn<>("医生姓名");
        doctorColumn.setPrefWidth(70);
        doctorColumn.setCellValueFactory((TreeTableColumn.CellDataFeatures<Record, String> param) -> {
            if (doctorColumn.validateValue(param)) {
                return param.getValue().getValue().doctorName;
            } else {
                return doctorColumn.getComputedValue(param);
            }
        });
        JFXTreeTableColumn<Record, String> typeColumn = new JFXTreeTableColumn<>("号种类别");
        typeColumn.setPrefWidth(70);
        typeColumn.setCellValueFactory((TreeTableColumn.CellDataFeatures<Record, String> param) -> {
            if (typeColumn.validateValue(param)) {
                return param.getValue().getValue().type;
            } else {
                return typeColumn.getComputedValue(param);
            }
        });
        JFXTreeTableColumn<Record, Number> numberColumn = new JFXTreeTableColumn<>("挂号人次");
        numberColumn.setPrefWidth(70);
        numberColumn.setCellValueFactory((TreeTableColumn.CellDataFeatures<Record, Number> param) -> {
            if (numberColumn.validateValue(param)) {
                return param.getValue().getValue().number;
            } else {
                return numberColumn.getComputedValue(param);
            }
        });
        JFXTreeTableColumn<Record, Number> feeColumn = new JFXTreeTableColumn<>("挂号费用");
        feeColumn.setPrefWidth(70);
        feeColumn.setCellValueFactory((TreeTableColumn.CellDataFeatures<Record, Number> param) -> {
            if (feeColumn.validateValue(param)) {
                return param.getValue().getValue().fee;
            } else {
                return feeColumn.getComputedValue(param);
            }
        });
        JFXTreeTableColumn<Record, String> timeColumn = new JFXTreeTableColumn<>("挂号时间");
        timeColumn.setPrefWidth(150);
        timeColumn.setCellValueFactory((TreeTableColumn.CellDataFeatures<Record, String> param) -> {
            if (timeColumn.validateValue(param)) {
                return param.getValue().getValue().time;
            } else {
                return timeColumn.getComputedValue(param);
            }
        });
        recordTable.setShowRoot(false);
        recordTable.setEditable(true);
        recordTable.getColumns().setAll(idColumn, patientColumn, deptColumn, doctorColumn, typeColumn, numberColumn, feeColumn, timeColumn);
        recordTable.setColumnResizePolicy(JFXTreeTableView.UNCONSTRAINED_RESIZE_POLICY);
        updateRecordTable();
        // Text search
        filterField.textProperty().addListener((o, oldVal, newVal) -> {
            recordTable.setPredicate(recordProp -> {
                final Record record = recordProp.getValue();
                return record.getId().contains(newVal)
                        || record.getPatientName().contains(newVal)
                        || record.getDepartName().contains(newVal)
                        || record.getDoctorName().contains(newVal)
                        || record.getType().contains(newVal)
                        || record.getTime().contains(newVal);
            });
        });

        recordTable.setRowFactory(new Callback<TreeTableView<Record>, TreeTableRow<Record>>() {
            @Override
            public TreeTableRow<Record> call(TreeTableView<Record> param) {
                final TreeTableRow<Record> row = new TreeTableRow<>();
                // cancel record
                final ContextMenu menu = new ContextMenu();
                MenuItem cancelItem = new MenuItem("退号");
                menu.getItems().add(cancelItem);
                cancelItem.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        TreeItem<Record> item = recordTable.getSelectionModel().getSelectedItem();
                        Session session = Main.factory.openSession();
                        Transaction tx = null;
                        try {
                            tx = session.beginTransaction();
                            Query query = session.createQuery("from TGhxxEntity where ghbh=:ghbh");
                            query.setParameter("ghbh", item.getValue().getId());
                            TGhxxEntity tGhxxEntity = (TGhxxEntity) query.list().get(0);
                            tGhxxEntity.setThbz((byte)1);
                            session.update(tGhxxEntity);
                            tx.commit();
                            recordTable.getRoot().getChildren().remove(item);
                            statusBar.setText("退号成功！");
                        }
                        catch (HibernateException e) {
                            System.out.println(e.toString());
                            if (tx!=null)
                                tx.rollback();
                        }
                        finally {
                            session.close();
                        }
                    }
                });
                // Only display context menu for non-null items
                row.contextMenuProperty().bind(
                        Bindings.when(javafx.beans.binding.Bindings.isNotNull(row.itemProperty()))
                                .then(menu)
                                .otherwise((ContextMenu)null));
                return row;
            }
        });
    }

    private static final class Record extends RecursiveTreeObject<Record> {
        final StringProperty id;
        final StringProperty patientName;
        final StringProperty departName;
        final StringProperty doctorName;
        final StringProperty type;
        final IntegerProperty number;
        final DoubleProperty fee;
        final StringProperty time;

        Record(String id, String patientName, String departName, String doctorName, String type, Integer number, Double fee, String time) {
            this.id = new SimpleStringProperty(id);
            this.patientName = new SimpleStringProperty(patientName);
            this.departName = new SimpleStringProperty(departName);
            this.doctorName = new SimpleStringProperty(doctorName);
            this.type = new SimpleStringProperty(type);
            this.number = new SimpleIntegerProperty(number);
            this.fee = new SimpleDoubleProperty(fee);
            this.time = new SimpleStringProperty(time);
        }

        public String getId() {
            return id.get();
        }

        public String getPatientName() {
            return patientName.get();
        }

        public String getDepartName() {
            return departName.get();
        }

        public String getDoctorName() {
            return doctorName.get();
        }

        public String getType() {
            return type.get();
        }

        public Integer getNumber() {
            return number.get();
        }

        public Double getFee() {
            return fee.get();
        }

        public String getTime() {
            return time.get();
        }
    }

    private void updateRecordTable() {
        ObservableList<Record> records = FXCollections.observableArrayList();
        Session session = Main.factory.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Query query = session.createQuery("from TGhxxEntity where brbh=:brbh and thbz=0");
            query.setParameter("brbh", Main.id);
            List recordList = query.list();
            query = session.createQuery("from TBrxxEntity where brbh=:brbh");
            query.setParameter("brbh", Main.id);
            TBrxxEntity tBrxxEntity = (TBrxxEntity) query.list().get(0);
            welcomeLabel.setText(tBrxxEntity.getBrmc()+"，您好");
            accountLabel.setText(String.valueOf(tBrxxEntity.getYcje())+"元");
            for (Iterator it=recordList.iterator(); it.hasNext(); ) {
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
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Record record = new Record(
                        tGhxxEntity.getGhbh(),
                        tBrxxEntity.getBrmc(),
                        tKsxxEntity.getKsmc(),
                        tKsysEntity.getYsmc(),
                        tHzxxEntity.getSfzj()==1?"专家号":"普通号",
                        tGhxxEntity.getGhrc(),
                        tGhxxEntity.getGhfy().doubleValue(),
                        dateFormat.format(tGhxxEntity.getRqsj())
                );
                records.add(record);
            }
        }
        catch (HibernateException e) {
            System.out.println(e.toString());
            if (tx!=null)
                tx.rollback();
        }
        finally {
            session.close();
        }
        final TreeItem<Record> incomeRoot = new RecursiveTreeItem<>(records, RecursiveTreeObject::getChildren);
        recordTable.setRoot(incomeRoot);
    }
}