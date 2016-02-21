package main.java.app;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import main.java.api.ParserService;
import main.java.api.ScraperService;
import main.java.api.StorageService;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.time.LocalDateTime;

public class StartupHandler extends Application{
    private static final Logger logger = LogManager.getLogger(StartupHandler.class);

    public static void main(String[] args) throws IOException, InterruptedException {
        Injector injector = Guice.createInjector(new Module());
        ScraperService scraperService = injector.getInstance(ScraperService.class);
        ParserService parserService = injector.getInstance(ParserService.class);
        StorageService storageService = injector.getInstance(StorageService.class);

        logger.info("Starting at: " + LocalDateTime.now());
        Module app = injector.getInstance(Module.class);
        launch(args);
//        app.theThing();
        logger.info("Finishing at: " + LocalDateTime.now());

//        storageService.testConnection();


    }


    @Override
    public void start(Stage primaryStage) throws Exception {
        Button button = new Button("Hello World");
        button.setOnAction(e -> {
                System.out.println("Hello world.");
        });

        StackPane myPane = new StackPane();
        myPane.getChildren().add(button);

        Scene myScene = new Scene(myPane);

        primaryStage.setScene(myScene);
        primaryStage.setWidth(400);
        primaryStage.setHeight(300);
        primaryStage.show();

    }
}
