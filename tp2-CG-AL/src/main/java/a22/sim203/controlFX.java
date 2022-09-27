package a22.sim203;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import org.mariuszgromada.math.mxparser.Expression;

public class controlFX {

    private String tempExpression;
    private String tempCalcul;

    @FXML
    private Label affichage;
    @FXML
    private ListView historique;

    @FXML
    void addTextAffichage(ActionEvent event) {
        if (affichage.getText().equals("Affichage")) affichage.setText(((Button) event.getSource()).getText());
        else {
            String temp = affichage.getText();
            affichage.setText(temp + ((Button) event.getSource()).getText());
        }
    }

    @FXML
    void calcul(ActionEvent event) {
        if (!affichage.getText().equals("Affichage")) {
            Expression expression = new Expression();
            expression.setExpressionString(affichage.getText());
            if (Double.isNaN(expression.calculate())) new DialoguesUtils("calculInvalide");
            else {
                tempExpression = affichage.getText();
                tempCalcul = Double.toString(expression.calculate());
                addHistorique(tempExpression, tempCalcul);
                affichage.setText(Double.toString(expression.calculate()));}
        }
    }

    @FXML
    void erase(ActionEvent event){
        String s = affichage.getText();
        affichage.setText(s.substring(0, s.length() - 1) );
    }

    @FXML
    void delete(ActionEvent event){
        affichage.setText("Affichage");
    }

    private void addHistorique(String expression, String calcul) {
        historique.getItems().add(expression + " = " + calcul);
    }

    @FXML
    void fonctionMenu(MouseEvent event) {

    }

    @FXML
    void menuAlert(ActionEvent event) {

    }

}
