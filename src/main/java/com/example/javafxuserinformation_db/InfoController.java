package com.example.javafxuserinformation_db;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.PreparedStatement;

public class InfoController {
    @FXML
    private ComboBox<String> jobComboBox;
    @FXML private TextField nameTextField;
    @FXML private TextField scoreTextField;
    @FXML private ComboBox<String> jobComboBoxEdit;
    @FXML private TextField nameTextFieldEdit;
    @FXML private TextField scoreTextFieldEdit;
    @FXML private TableView<User> tableView;
    @FXML private TableColumn<User, Integer> idColumn;
    @FXML private TableColumn<User, String> nameColumn;
    @FXML private TableColumn<User, String> jobColumn;
    @FXML private TableColumn<User, Integer> scoreColumn;

    private UserInfoDao userInfoDao;

    int editId = -1;
    @FXML
    private void initialize() {
        var connection = DBUtil.getConnection();
        this.userInfoDao = new UserInfoDao(connection);

        ObservableList<String> items
                = FXCollections.observableArrayList(this.userInfoDao.list);
        jobComboBox.setItems(items);
        jobComboBoxEdit.setItems(items);

        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        jobColumn.setCellValueFactory(new PropertyValueFactory<>("job"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        scoreColumn.setCellValueFactory(new PropertyValueFactory<>("score"));
        jobColumn.setSortable(false);
        nameColumn.setSortable(false);
        scoreColumn.setSortable(false);

        this.userInfoDao.getRecord(1);
        tableView.getItems().add(this.userInfoDao.user);
        this.userInfoDao.getRecord(2);
        tableView.getItems().add(this.userInfoDao.user);
    }

    @FXML
    public void onAdd(ActionEvent actionEvent) {
        String job = jobComboBox.getValue();
        String name = nameTextField.getText();
        String scoreText = scoreTextField.getText();

        if (job == null || name.trim().isEmpty() || scoreText.trim().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "すべての項目を入力してください。", ButtonType.OK);
            alert.showAndWait();
            return;
        }

        int score;
        try {
            score = Integer.parseInt(scoreText);
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "スコアには0～100の整数を入力してください。", ButtonType.OK);
            alert.showAndWait();
            return;
        }
        if (score < 0 || score > 100) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "スコアには0～100の整数を入力してください。", ButtonType.OK);
            alert.showAndWait();
            return;
        }

        this.userInfoDao.insertInfo(job, name, score);
        this.userInfoDao.getRecord(3);
        tableView.getItems().add(this.userInfoDao.user);
    }

    @FXML
    private void onDelete(ActionEvent actionEvent) {
        int selectedIndex = tableView.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            tableView.getItems().remove(selectedIndex);
        }
    }
    @FXML
    private void onEdit(ActionEvent actionEvent) {
        User edituser = tableView.getSelectionModel().getSelectedItem();
        if (edituser == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "編集するレコードを選択してください。", ButtonType.OK);
            alert.showAndWait();
            return;
        }
        jobComboBoxEdit.setValue(edituser.getJob());
        nameTextFieldEdit.setText(edituser.getName());
        scoreTextFieldEdit.setText(String.valueOf(edituser.getScore()));
        editId = edituser.getId();
    }
    @FXML
    private void onUpdate(ActionEvent actionEvent) {
        String job = jobComboBoxEdit.getValue();
        String name = nameTextFieldEdit.getText();
        String scoreText = scoreTextFieldEdit.getText();

        if (editId == -1) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "編集するレコードを選択してください。", ButtonType.OK);
            alert.showAndWait();
            return;
        }

        if (job == null || name.trim().isEmpty() || scoreText.trim().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "すべての項目を入力してください。", ButtonType.OK);
            alert.showAndWait();
            return;
        }

        int score;
        try {
            score = Integer.parseInt(scoreText);
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "スコアには0～100の整数を入力してください。", ButtonType.OK);
            alert.showAndWait();
            return;
        }
        if (score < 0 || score > 100) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "スコアには0～100の整数を入力してください。", ButtonType.OK);
            alert.showAndWait();
            return;
        }

        User selectedIndex = tableView.getSelectionModel().getSelectedItem();
        tableView.getItems().remove(selectedIndex);

        User user = new User(job, name, score);
        user.setId(editId);
        tableView.getItems().add(user);

        jobComboBoxEdit.setValue(null);
        nameTextFieldEdit.setText(null);
        scoreTextFieldEdit.setText(null);
        tableView.getSortOrder().add(idColumn);
        editId = -1;
    }


}
