package a22.sim203;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import org.mariuszgromada.math.mxparser.Expression;
import org.mariuszgromada.math.mxparser.Function;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class controlFX {

    private String tempExpression;
    private String tempCalcul;
    private String memoire;
    private ContextMenu contextMenu;
    private final DialoguesUtils d = new DialoguesUtils();
    private final List validKeys = new ArrayList(Arrays.asList("0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "+", "-", "*", "/", "=", ",", "(", ")", "BACK_SPACE", "DELETE"));
    private List<Button> buttons;

    @FXML
    private Label affichage;
    @FXML
    private ListView historique;
    @FXML
    private ListView fonctions;
    @FXML
    private CheckMenuItem modeChangementFonction;
    @FXML
    private BorderPane borderPane;
    @FXML
    private GridPane gridPane;


    public void initialize() {
        buttons = new ArrayList(Arrays.asList(gridPane.getChildren().toArray()));
        buttons.remove(buttons.size() - 1);

        fonctions.getItems().addAll(new Function("f(x)=x").getFunctionExpressionString(), new Function("f(x)=sqrt(x)").getFunctionExpressionString(), new Function("f(x)=sin(x)").getFunctionExpressionString(), new Function("f(x)=cos(x)").getFunctionExpressionString(), new Function("f(x)=x^2").getFunctionExpressionString());

        historique.setOnMouseClicked(e -> {
            int index = historique.getSelectionModel().getSelectedIndex();
            if (checkDoubleClick(e)) {
                String texte = historique.getItems().get(index).toString();
                int indexOfEqual = texte.indexOf('=');

                afficheTexte(texte.substring(indexOfEqual + 2));
            }
        });

        fonctions.setOnMouseClicked(e -> {
            int index = fonctions.getSelectionModel().getSelectedIndex();
            if (checkDoubleClick(e)) {
                Function function = new Function("f(x)=" + fonctions.getItems().get(index).toString());
                calculateAndShow(function);
            }
        });
        borderPane.setOnKeyPressed(this::keyPressedEvent);
        borderPane.setOnKeyReleased(this::keyReleasedEvent);

        addButtonPressReleaseListener();
    }

    private void afficheTexte(String texte) {
        if (affichage.getText().equals("Affichage"))
            affichage.setText(texte);
        else {
            String temp = affichage.getText();
            affichage.setText(temp + texte);
        }
    }

    @FXML
    private void changeSign(ActionEvent event) {
        if (!Objects.equals(affichage.getText(), "Affichage")) {
            try {
                double doubleAffichage = Double.parseDouble(affichage.getText());
                affichage.setText("-" + doubleAffichage);
            } catch (NumberFormatException e) {
                affichage.setText(affichage.getText() + "-");
            }
        }
    }

    private void keyPressedEvent(KeyEvent e) {
        String keyName = Objects.equals(e.getText(), "") ? e.getCode().toString() : e.getText();
        if (checkValidKey(keyName)) {
            changeButtonScale(findButton(keyName), 0.80);

            switch (keyName) {
                case "BACK_SPACE" -> erase((new ActionEvent()));
                case "DELETE" -> delete(new ActionEvent());
                case "=" -> calcul(new ActionEvent());
                default -> afficheTexte(e.getText());
            }
        }
    }

    private void keyReleasedEvent(KeyEvent e) {
        String keyName = Objects.equals(e.getText(), "") ? e.getCode().toString() : e.getText();

        if (checkValidKey(keyName)) {
            changeButtonScale(findButton(keyName), 1.25);
        }
    }

    private void addButtonPressReleaseListener() {
        for (Button button : buttons) {
            if (!button.getText().equals("no FX"))
                button.pressedProperty().addListener((observable, wasPressed, pressed) -> {
                    if (pressed) {
                        changeButtonScale(button, 0.80);
                    } else changeButtonScale(button, 1.25);
                });
        }
    }

    private void changeButtonScale(Button button, Double scale) {
        button.setScaleX(button.getScaleX() * scale);
        button.setScaleY(button.getScaleY() * scale);
    }

    private Button findButton(String text) {
        Button buttonReturn = null;
        for (Button button : buttons) {
            if (text.equals(button.getId())) {
                buttonReturn = button;
                break;
            }
        }
        return buttonReturn;
    }

    private boolean checkValidKey(String key) {
        return validKeys.contains(key);
    }

    private boolean checkDoubleClick(MouseEvent event) {
        return event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2;
    }

    @FXML
    void sqrt(ActionEvent event) {
        Function function = new Function("f(x)=sqrt(x)");
        try {
            double value = Double.parseDouble(affichage.getText());
            double calcul = function.calculate(value);
            if (Double.isNaN(calcul)) d.getCalculInvalide();
            else {
                affichage.setText(String.valueOf(calcul));
                historique.getItems().add("sqrt(" + (int) value + ")=" + calcul);
            }
        } catch (NumberFormatException e) {
            d.getCalculInvalide();
        }
    }

    @FXML
    void exp(ActionEvent event) {
        Function function = new Function("f(x)=exp(x)");
        try {
            double value = Double.parseDouble(affichage.getText());
            double calcul = function.calculate(value);
            if (Double.isNaN(calcul)) d.getCalculInvalide();
            else {
                affichage.setText(String.valueOf(calcul));
                historique.getItems().add("exp(" + (int) value + ")=" + calcul);
            }
        } catch (NumberFormatException e) {
            d.getCalculInvalide();
        }
    }

    @FXML
    void calculerBoutonFx(ActionEvent event) {
        if (modeChangementFonction.isSelected()) {
            String newText = (String) fonctions.getSelectionModel().getSelectedItem();
            ((Button) (event.getSource())).setText(newText);
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
        if (affichage.getText().equals("Affichage")) affichage.setText(memoire);
        else {
            String temp = affichage.getText();
            affichage.setText(temp + memoire);
        }
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
