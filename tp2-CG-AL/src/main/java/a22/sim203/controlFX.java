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
import javafx.scene.paint.Color;
import org.mariuszgromada.math.mxparser.Expression;
import org.mariuszgromada.math.mxparser.Function;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class controlFX {

    /**
     * La string garder en memoire du label affichage
     */
    private String memoire;
    /**
     * Creation de la class créatrice de dialogue
     */
    private final DialoguesUtils d = new DialoguesUtils();
    /**
     * List des boutons du clavier utilisable
     */
    private final List validKeys = new ArrayList(Arrays.asList("0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "+", "-", "*", "/", "=", ",", "(", ")", "BACK_SPACE", "DELETE"));
    /**
     * List des boutons dans l'affichage gridpane
     */
    private List<Button> buttons;
    /**
     * Label de l'affichage géré par le dossier fxml
     */
    @FXML
    private Label affichage;
    /**
     * List view de droit gardant en memoire les calculs effectués
     */
    @FXML
    private ListView historique;
    /**
     * List view de gauche ayant les fonctions de bases et celles ajouté en memoire
     */
    @FXML
    private ListView fonctions;
    /**
     * CheckMenu activant l'ajout de fonction
     */
    @FXML
    private CheckMenuItem modeChangementFonction;
    /**
     * borderPane principale
     */
    @FXML
    private BorderPane borderPane;
    /**
     * gridPane au centre de la borderPane
     */
    @FXML
    private GridPane gridPane;

    /**
     * initialise toute la propriété de la scène
     */
    public void initialize() {

        //ajout des boutons de la gridpane dans le list de boutons
        buttons = new ArrayList(Arrays.asList(gridPane.getChildren().toArray()));
        buttons.remove(buttons.size() - 1);

        //ajout des fonctions de base dans la listview de gauche
        fonctions.getItems().addAll(new Function("f(x)=x").getFunctionExpressionString(), new Function("f(x)=sqrt(x)").getFunctionExpressionString(), new Function("f(x)=sin(x)").getFunctionExpressionString(), new Function("f(x)=cos(x)").getFunctionExpressionString(), new Function("f(x)=x^2").getFunctionExpressionString());

        //ajout d'un handler lors du mouseclick sur la list view historique
        historique.setOnMouseClicked(e -> {
            int index = historique.getSelectionModel().getSelectedIndex();
            if (checkDoubleClick(e)) {
                String texte = historique.getItems().get(index).toString();
                int indexOfEqual = texte.indexOf('=');

                afficheTexte(texte.substring(indexOfEqual + 2));
            }
        });

        //ajout d'un handler lors d'un mouseClick sur la listview des fonctions
        fonctions.setOnMouseClicked(e -> {
            int index = fonctions.getSelectionModel().getSelectedIndex();
            if (checkDoubleClick(e)) {
                Function function = new Function("f(x)=" + fonctions.getItems().get(index).toString());
                calculateAndShow(function);
            }
        });

        //ajout de handler sur la borderpane lors d'une touche du clavier et pressed et released
        borderPane.setOnKeyPressed(this::keyPressedEvent);
        borderPane.setOnKeyReleased(this::keyReleasedEvent);

        addButtonPressReleaseListener();
    }

    /**
     * Ajoute le texte au label affichage
     *
     * @param texte le texte a ajouté
     */
    private void afficheTexte(String texte) {
        affichage.setTextFill(Color.DARKGREEN);
        if (affichage.getText().equals("Affichage")) {
            affichage.setText(texte);
        } else {
            String temp = affichage.getText();
            affichage.setText(temp + texte);
        }
    }

    /**
     * Handler de la touche qui change le signe
     */
    @FXML
    private void changeSign(ActionEvent event) {
        if (!Objects.equals(affichage.getText(), "Affichage")) {
            affichage.setTextFill(Color.DARKGREEN);
            try {
                double doubleAffichage = Double.parseDouble(affichage.getText());
                affichage.setText("-" + doubleAffichage);
            } catch (NumberFormatException e) {
                affichage.setText(affichage.getText() + "-");
            }
        }
    }

    /**
     * Handle une touche appuyée
     *
     * @param e l'évènement de la touche appuyée
     */


    private void keyPressedEvent(KeyEvent e) {
        String keyName = Objects.equals(e.getText(), "") ? e.getCode().toString() : e.getText();
        if (checkValidKey(keyName)) {

            if (findButton(keyName).getScaleX() != 0.80)
                changeButtonScale(findButton(keyName), 0.80);

                switch (keyName) {
                    case "BACK_SPACE" -> erase((new ActionEvent()));
                    case "DELETE" -> delete(new ActionEvent());
                    case "=" -> calcul(new ActionEvent());
                    default -> afficheTexte(e.getText());

            }
        }
    }

    /**
     * Handle une touche relâchée
     *
     * @param e l'événement de la touche relâchée
     */
    private void keyReleasedEvent(KeyEvent e) {
        String keyName = Objects.equals(e.getText(), "") ? e.getCode().toString() : e.getText();

        if (checkValidKey(keyName)) {
            changeButtonScale(findButton(keyName), 1.25);
        }
    }


    /**
     * Ajoute le handle pour changement de taille sur l'appuie et le relâchement des boutons du a un click
     */
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

    /**
     * effectue le changement de taille sur un bouton
     *
     * @param button le bouton à modifier
     * @param scale  le multiplicateur de taille
     */
    private void changeButtonScale(Button button, Double scale) {
        button.setScaleX(button.getScaleX() * scale);
        button.setScaleY(button.getScaleY() * scale);
    }

    /**
     * trouve le bouton recherché à partir de ton texte
     *
     * @param text le texte du bouton rechercher
     */
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

    /**
     * verified si texte du bouton est valide
     *
     * @param key le texte de la touche vérifié
     */
    private boolean checkValidKey(String key) {
        return validKeys.contains(key);
    }

    /**
     * Vérifie si le click a été effectué 2 fois
     *
     * @param event l'évènement de la souris vérifiée
     */
    private boolean checkDoubleClick(MouseEvent event) {
        return event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2;
    }

    /**
     * Handler de la touche sqrt permettant de faire la racine carré du nombre dans l'affichage
     */
    @FXML
    void sqrt(ActionEvent event) {
        Function function = new Function("f(x)=sqrt(x)");
        try {
            double value = Double.parseDouble(affichage.getText());
            double calcul = function.calculate(value);
            if (Double.isNaN(calcul)) d.getCalculInvalide();
            else {
                affichage.setText(String.valueOf(calcul));
                affichage.setTextFill(Color.DARKGREEN);
                historique.getItems().add("sqrt(" + (int) value + ")=" + calcul);
            }
        } catch (NumberFormatException e) {
            d.getCalculInvalide();
        }
    }

    /**
     * Handler de la touche exp permettant de faire l'exposant du nombre dans l'affichage
     */
    @FXML
    void exp(ActionEvent event) {
        Function function = new Function("f(x)=exp(x)");
        try {
            double value = Double.parseDouble(affichage.getText());
            double calcul = function.calculate(value);
            if (Double.isNaN(calcul)) d.getCalculInvalide();
            else {
                affichage.setText(String.valueOf(calcul));
                affichage.setTextFill(Color.DARKGREEN);
                historique.getItems().add("exp(" + (int) value + ")=" + calcul);
            }
        } catch (NumberFormatException e) {
            d.getCalculInvalide();
        }
    }

    /**
     * Effectue la methode personalise sur le nombre dans l'affichage ou personalise une touche depuis la methode sélectionné dans la listview
     *
     * @param event l'évènement analysé
     */
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

    /**
     * Ajoute la methode d'un bouton à l'affichage
     *
     * @param event L'évènement analysé
     */
    @FXML
    void addTextAffichage(ActionEvent event) {
        affichage.setTextFill(Color.DARKGREEN);
        if (affichage.getText().equals("Affichage")) {
            affichage.setText(((Button) event.getSource()).getText());
        } else {
            String temp = affichage.getText();
            affichage.setText(temp + ((Button) event.getSource()).getText());
        }
    }

    /**
     * Mémorise l'affichage
     */
    @FXML
    void ajouteMemoire(ActionEvent event) {
        memoire = affichage.getText();
    }

    /**
     * Ajout l'élément de la memoire de la mémoire dans l'affichage
     */
    @FXML
    void retourneMemoire(ActionEvent event) {
        if (affichage.getText().equals("Affichage")) {
            affichage.setTextFill(Color.DARKGREEN);
            affichage.setText(memoire);
        } else {
            affichage.setTextFill(Color.DARKGREEN);
            String temp = affichage.getText();
            affichage.setText(temp + memoire);
        }
    }

    /**
     *
     */
    @FXML
    void calcul(ActionEvent event) {
        if (!affichage.getText().equals("Affichage")) {
            Expression expression = new Expression();
            expression.setExpressionString(affichage.getText());
            if (Double.isNaN(expression.calculate())) d.getCalculInvalide();
            else {
                String tempExpression = affichage.getText();
                String tempCalcul = Double.toString(expression.calculate());
                addHistorique(tempExpression, tempCalcul);
                affichage.setText(Double.toString(expression.calculate()));
                affichage.setTextFill(Color.DARKGREEN);
            }
        }
    }

    /**
     *
     */
    @FXML
    void erase(ActionEvent event) {
        assert affichage.getText() != null;
        String s = affichage.getText();
        if (s.length() == 1) {
            affichage.setText("Affichage");
            affichage.setTextFill(Color.WHITE);
        } else if (!s.equals("Affichage")) {
            affichage.setText(s.substring(0, s.length() - 1));
            affichage.setTextFill(Color.DARKGREEN);
        }
    }

    /**
     *
     */
    @FXML
    void delete(ActionEvent event) {
        affichage.setText("Affichage");
        affichage.setTextFill(Color.WHITE);
    }

    /**
     *
     */
    private void addHistorique(String expression, String calcul) {
        historique.getItems().add(expression + " = " + calcul);
    }

    /**
     *
     */
    @FXML
    void fonctionMenu(ContextMenuEvent event) {
        int index = fonctions.getSelectionModel().getSelectedIndex();

        ContextMenu contextMenu = new ContextMenu();
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

    /**
     *
     */
    private Function stringToFunction(String s) {
        return new Function("f(x)=" + s);
    }

    /**
     *
     */
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
                affichage.setTextFill(Color.DARKGREEN);
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
