package main.java.app;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
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
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class StartupHandler extends Application{
    private static final Logger logger = LogManager.getLogger(StartupHandler.class);

    public static void main(String[] args) throws IOException, InterruptedException {
        Injector injector = Guice.createInjector(new Module());
        ScraperService scraperService = injector.getInstance(ScraperService.class);
        ParserService parserService = injector.getInstance(ParserService.class);
        StorageService storageService = injector.getInstance(StorageService.class);
        MySharedQueue queue = injector.getInstance(MySharedQueue.class);

        final int NTHREADS = 25;
        final ExecutorService exec =
                Executors.newFixedThreadPool(NTHREADS);
        exec.submit((Callable<Object>) parserService);
        exec.submit((Callable<Object>) scraperService);

        LocalDateTime startTime = LocalDateTime.now();
        logger.info("Starting at: " + startTime);
        Module app = injector.getInstance(Module.class);
//        launch(args);
//        app.theThing();

//        storageService.testConnection();


    }


    @Override
    public void start(Stage primaryStage) throws Exception {
        Label label1 = new Label("Name:");
        TextField textField = new TextField ();
        HBox hb = new HBox();
        hb.getChildren().addAll(label1, textField);
        hb.setSpacing(10);

        Button button = new Button("Fetch");
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
