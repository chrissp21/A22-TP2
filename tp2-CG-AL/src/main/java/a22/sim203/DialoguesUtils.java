package a22.sim203;

import javafx.scene.control.Alert;

public class DialoguesUtils{

    public DialoguesUtils(String dialogType) {
        switch (dialogType){
            case "calculInvalide" -> getCalculInvalide();
            default -> System.out.println("Type de dialogue invalide");
        }
    }

    private void getCalculInvalide() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setHeaderText("Expression invalide");
        alert.setTitle("Calculateur avancé");
        alert.setContentText("L'expression saisie ne peut être calculée");
        alert.show();
    }
}
