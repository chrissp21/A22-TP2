package a22.sim203;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseButton;
import org.mariuszgromada.math.mxparser.Expression;
import org.mariuszgromada.math.mxparser.Function;

public class controlFX {

    private String tempExpression;
    private String tempCalcul;
    private String memoire;
    private ContextMenu contextMenu;
    private final DialoguesUtils d = new DialoguesUtils();

    @FXML
    private Label affichage;
    @FXML
    private ListView historique;
    @FXML
    private ListView fonctions;
    @FXML
    private CheckMenuItem modeChangementFonction;

    public void initialize() {
        fonctions.getItems().addAll(new Function("f(x)=x").getFunctionExpressionString(), new Function("f(x)=sqrt(x)").getFunctionExpressionString(), new Function("f(x)=sin(x)").getFunctionExpressionString(), new Function("f(x)=cos(x)").getFunctionExpressionString(), new Function("f(x)=x^2").getFunctionExpressionString());

        historique.setOnMouseClicked(e -> {
            int index = historique.getSelectionModel().getSelectedIndex();
            if (e.getButton().equals(MouseButton.PRIMARY)) {
                if (e.getClickCount() == 2) {
                    if (affichage.getText().equals("Affichage"))
                        affichage.setText(historique.getItems().get(index).toString());
                    else {
                        String temp = affichage.getText();
                        affichage.setText(temp + historique.getItems().get(index).toString());
                    }
                }
            }
        });

        fonctions.setOnMouseClicked(e -> {
            int index = fonctions.getSelectionModel().getSelectedIndex();
            if (e.getButton().equals(MouseButton.PRIMARY)) {
                if (e.getClickCount() == 2) {
                    Function function = new Function("f(x)=" + fonctions.getItems().get(index).toString());
                    calculateAndShow(function);
                }
            }
        });

    }

    @FXML
    void calculerBoutonFx(ActionEvent event) {
        if (modeChangementFonction.isSelected()) {
            String newText = (String) fonctions.getSelectionModel().getSelectedItem();
            ((Button)(event.getSource())).setText(newText);
        } else {
            String buttonText = ((Button) (event.getSource())).getText();
            if (!buttonText.equals("no FX")) {
                calculateAndShow(new Function("f(x)=" + buttonText));
            }
        }
    }

    @FXML
    void addTextAffichage(ActionEvent event) {
        if (affichage.getText().equals("Affichage")) affichage.setText(((Button) event.getSource()).getText());
        else {
            String temp = affichage.getText();
            affichage.setText(temp + ((Button) event.getSource()).getText());
        }
    }

    @FXML
    void ajouteMemoire(ActionEvent event) {
        memoire = affichage.getText();
    }

    @FXML
    void retourneMemoire(ActionEvent event) {
        String temp = affichage.getText();
        affichage.setText(temp + memoire);
    }

    @FXML
    void calcul(ActionEvent event) {
        if (!affichage.getText().equals("Affichage")) {
            Expression expression = new Expression();
            expression.setExpressionString(affichage.getText());
            if (Double.isNaN(expression.calculate())) d.getCalculInvalide();
            else {
                tempExpression = affichage.getText();
                tempCalcul = Double.toString(expression.calculate());
                addHistorique(tempExpression, tempCalcul);
                affichage.setText(Double.toString(expression.calculate()));
            }
        }
    }

    @FXML
    void erase(ActionEvent event) {
        assert affichage.getText() != null;
        String s = affichage.getText();
        if (s.length() == 1) affichage.setText("Affichage");
        else if (!s.equals("Affichage")) affichage.setText(s.substring(0, s.length() - 1));
    }

    @FXML
    void delete(ActionEvent event) {
        affichage.setText("Affichage");
    }

    private void addHistorique(String expression, String calcul) {
        historique.getItems().add(expression + " = " + calcul);
    }

    @FXML
    void fonctionMenu(ContextMenuEvent event) {
        int index = fonctions.getSelectionModel().getSelectedIndex();
        contextMenu = new ContextMenu();
        MenuItem ajouter = new MenuItem("ajouter");
        MenuItem effacer = new MenuItem("effacer");
        MenuItem modifier = new MenuItem("modifier");

        ajouter.setOnAction(e -> {
            String input = d.getAjouterFonction();

            if (Double.isNaN(stringToFunction(input).calculate(10))) d.getFonctionInvalide();
            else fonctions.getItems().add(index, stringToFunction(input).getFunctionExpressionString());
        });

        effacer.setOnAction(e ->
                fonctions.getItems().remove(index)
        );

        modifier.setOnAction(e -> {
            String input = d.getModifierFonction(fonctions.getItems().get(index).toString());

            if (Double.isNaN(stringToFunction(input).calculate(10))) d.getFonctionInvalide();
            else fonctions.getItems().set(index, stringToFunction(input).getFunctionExpressionString());
        });

        contextMenu.getItems().addAll(ajouter, effacer, modifier);
        contextMenu.show(fonctions, event.getScreenX(), event.getScreenY());
    }

    private Function stringToFunction(String s) {
        return new Function("f(x)=" + s);
    }


    @FXML
    void menuAlert(ActionEvent event) {
        d.getAPropos();
    }

    public double calculateAndShow(Function function) {
        double value;
        double calcul = Double.NaN;
        try {
            value = Double.parseDouble(affichage.getText());
            calcul = (function.calculate(value));
            if (!Double.isNaN(calcul)) {
                affichage.setText(String.valueOf(calcul));
                CharSequence old = "x";
                CharSequence replacement = String.valueOf((int) value);
                historique.getItems().add(function.getFunctionExpressionString().replace(old, replacement) + "=" + calcul);
            }
        } catch (NumberFormatException exception) {
            d.getCalculInvalide();
        }
        return calcul;
    }

}
