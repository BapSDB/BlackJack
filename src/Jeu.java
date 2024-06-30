import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class Jeu extends Application implements Initializable {

    @FXML
    private AnchorPane root;

    @FXML
    private FlowPane flowPane;

    private static final Paquet paquet;

    static {
        paquet = Paquet.nouveauPaquetNeuf();
        paquet.melangerCartes();
    }

    private VBox vueCarte(Carte carte) {
        VBox vBox = new VBox(10.0,
                new Text(carte.valeur().toString()),
                new Text("de"),
                new Text(carte.couleur().toString())
        );
        vBox.setPrefSize(80.0, 120.0);
        vBox.setStyle("-fx-border-width:1px; -fx-border-style:solid; -fx-border-color:black;");
        vBox.setAlignment(Pos.CENTER);
        vBox.setOnMouseClicked(event -> {
            final Carte c = paquet.piocheCarte().get(0);
            System.out.println(c);
            flowPane.getChildren().add(vueCarte(c));
            flowPane.getChildren().set(0, vueCarte(paquet.iterator().next()));
        });
        vBox.setOnDragDetected(event -> {
            /* drag was detected, start a drag-and-drop gesture*/
            /* allow any transfer mode */
            Dragboard db = vBox.startDragAndDrop(TransferMode.ANY);

            /* Put a string on a dragboard */
            ClipboardContent content = new ClipboardContent();
//            content.put(new DataFormat("carte"), new Pioche.Paquet.Carte())
            content.putString(
                    vBox.getChildren().stream()
                            .map(Text.class::cast)
                            .map(Text::getText)
                            .collect(Collectors.joining(" "))
            );
            db.setContent(content);
            event.consume();
        });
        vBox.setOnDragOver(event -> {
            /* data is dragged over the target */
            /* accept it only if it is not dragged from the same node
             * and if it has a string data */
            if (event.getGestureSource() != vBox &&
                    event.getDragboard().hasString()) {
                /* allow for both copying and moving, whatever user chooses */
                event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
            }

            event.consume();
        });

        vBox.setOnDragDropped(event -> {
            /* data dropped */
            /* if there is a string data on dragboard, read it and use it */
            Dragboard db = event.getDragboard();
            boolean success = false;
            if (db.hasString()) {
//                    vBox.setText(db.getString());
                success = true;
            }
            /* let the source know whether the string was successfully
             * transferred and used */
            event.setDropCompleted(success);

            event.consume();
        });
        return vBox;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        flowPane.setPrefWrapLength(6 * (80.0 + 10.0));
        flowPane.getChildren().clear();
        VBox vCarte = vueCarte(paquet.iterator().next());
        flowPane.getChildren().add(vCarte);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        final FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Reussite.fxml"));
        final AnchorPane root = fxmlLoader.load();
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Réussite");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}