package apontamento;

import org.apache.log4j.Logger;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {
	
	private final static Logger log = Logger.getLogger(Main.class);

	public static void main(String[] args) {
		log.info("Iniciando aplicação");
        launch(Main.class);
	}

	@Override
	public void start(Stage stage2) throws Exception {
        Parent parent = FXMLLoader.load(Main.class.getResource("/layouts/apontamento_listagem.fxml"));
        
        Stage stage1 = new Stage();
        Scene scene = new Scene(parent);
        stage1.setScene(scene);
        stage1.setTitle("Timesheet");
        stage1.getIcons().add(new Image("/icon.png"));
        stage1.show();
	}

}
