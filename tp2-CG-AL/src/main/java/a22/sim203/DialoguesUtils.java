package a22.sim203;

import javafx.scene.control.Alert;
import javafx.scene.control.TextInputDialog;

public class DialoguesUtils {

    public DialoguesUtils() {
    }

    public String getAjouterFonction() {
        TextInputDialog textInputDialog = new TextInputDialog();
        textInputDialog.setTitle("Calculateur avancé");
        textInputDialog.setHeaderText("Saisie de fonction");
        textInputDialog.setContentText("Saissiez la fonction: f(x)= ");
        textInputDialog.showAndWait();

        return textInputDialog.getEditor().getText();
    }

    public String getModifierFonction(String string) {
        TextInputDialog textInputDialog = new TextInputDialog(string);
        textInputDialog.setTitle("Calculateur avancé");
        textInputDialog.setHeaderText("Saisie de fonction");
        textInputDialog.setContentText("Saissiez la fonction: f(x)= ");
        textInputDialog.showAndWait();

        return textInputDialog.getEditor().getText();
    }

    public void getCalculInvalide() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setHeaderText("Expression invalide");
        alert.setTitle("Calculateur avancé");
        alert.setContentText("L'expression saisie ne peut être calculée");
        alert.show();
    }

    public void getFonctionInvalide(){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Calculateur avancé");
        alert.setHeaderText("Fonction invalide");
        alert.setContentText("La fonction utilisée ne peut être calculée");
        alert.show();
    }

    public void getAPropos(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Calculateur avancé");
        alert.setHeaderText("Sim 203");
        alert.setContentText("Cours de programmation 203 en sim\nCégep Limoilou A22\npar:Christophe Guérin et Vincent Fortier");
        alert.show();
    }
}
