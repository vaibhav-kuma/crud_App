package com.example.crud;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class PersonController {

    @FXML private TextField nameField, emailField;
    @FXML private TableView<Person> table;
    @FXML private TableColumn<Person, Number> idCol;
    @FXML private TableColumn<Person, String> nameCol, emailCol;

    private final PersonDAO dao = new PersonDAO();
    private final ObservableList<Person> data = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        idCol.setCellValueFactory(c -> c.getValue().idProperty());
        nameCol.setCellValueFactory(c -> c.getValue().nameProperty());
        emailCol.setCellValueFactory(c -> c.getValue().emailProperty());
        table.setItems(data);
        table.getSelectionModel().selectedItemProperty()
                .addListener((obs, oldVal, p) -> {
                    if (p != null) {
                        nameField.setText(p.getName());
                        emailField.setText(p.getEmail());
                    }
                });
        refresh();
    }

    private void refresh() {
        data.setAll(dao.findAll());
    }

    @FXML
    private void handleAdd() {
        if (validate()) {
            dao.insert(new Person(0, nameField.getText().trim(),
                    emailField.getText().trim()));
            refresh();
            handleClear();
        }
    }

    @FXML
    private void handleUpdate() {
        Person selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) { showAlert("Select a row first."); return; }
        if (validate()) {
            selected.setName(nameField.getText().trim());
            selected.setEmail(emailField.getText().trim());
            dao.update(selected);
            refresh();
        }
    }

    @FXML
    private void handleDelete() {
        Person selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) { showAlert("Select a row first."); return; }
        dao.delete(selected.getId());
        refresh();
        handleClear();
    }

    @FXML
    private void handleClear() {
        nameField.clear();
        emailField.clear();
        table.getSelectionModel().clearSelection();
    }

    private boolean validate() {
        if (nameField.getText().isBlank() || emailField.getText().isBlank()) {
            showAlert("Name and email are required.");
            return false;
        }
        return true;
    }

    private void showAlert(String msg) {
        new Alert(Alert.AlertType.WARNING, msg, ButtonType.OK).showAndWait();
    }
}
