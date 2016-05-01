package main.java.app;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import main.java.api.ParserService;
import main.java.api.ScraperService;
import main.java.api.StorageService;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;


public class StartupHandler extends Application{
    private static final Logger logger = LogManager.getLogger(StartupHandler.class);

    public static void main(String[] args) throws IOException, InterruptedException {
        Injector injector = Guice.createInjector(new Module());
        ScraperService scraperService = injector.getInstance(ScraperService.class);
        ParserService parserService = injector.getInstance(ParserService.class);
        StorageService storageService = injector.getInstance(StorageService.class);
        MySharedQueue queue = injector.getInstance(MySharedQueue.class);
        String debug = PropertyHandler.getInstance().getValue("debug");

        LocalDateTime startTime = LocalDateTime.now();
        logger.info("Starting at: " + startTime);
        Module app = injector.getInstance(Module.class);

        if (debug.equals("true")) {
            launch(args);
        }
        else {
            app.start();
        }
    }


    @Override
    public void start(Stage primaryStage) throws Exception {
        File dataFile = new File(System.getProperty("user.dir") +
                "\\target\\classes\\data\\" +
                PropertyHandler.getInstance().getValue("saveToFileName"));
        Label errMsg = new Label("Copy autocopies lines to your clipboard =)");
        errMsg.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        errMsg.setTextFill(Color.WHITE);

        TextArea textField = new TextArea();
        textField.setWrapText(true);

        VBox vbox = new VBox();
        vbox.setPadding(new Insets(10));
        vbox.setSpacing(10);
        vbox.setStyle("-fx-background-color: #336699;");

        HBox hbox = new HBox();
        hbox.setSpacing(10);

        Button fetchBtn = new Button("Fetch");
        fetchBtn.setPrefSize(100, 20);
        fetchBtn.setOnAction(e -> {
//            app.start();
        });

        Button copyBtn = new Button("Copy Random");
        copyBtn.setPrefSize(100, 20);
        copyBtn.setOnAction(e -> {
            String contents = "";
            try {
                logger.info("Copying random line from: " + dataFile.getAbsoluteFile());
                contents = FileUtils.readFileToString(dataFile);
                if (!dataFile.exists()) {
                    logger.info("The dataFile was not found.");
                    errMsg.setText("The dataFile was not found.");
                } else {
                    String[] linesInFile = contents.split("\r\n");
                    String copyString = linesInFile[(int)(Math.random()*linesInFile.length)];
                    copyToClipboard(copyString);
                    textField.setText(copyString);
                }
            } catch (IOException e1) {
                e1.printStackTrace();
            }

        });
        vbox.getChildren().addAll(errMsg, textField);
        hbox.getChildren().addAll(fetchBtn, copyBtn);
        BorderPane myPane = new BorderPane();
        myPane.setTop(vbox);
        myPane.setCenter(hbox);

        Scene myScene = new Scene(myPane);

        primaryStage.setScene(myScene);
        primaryStage.setWidth(400);
        primaryStage.setHeight(300);
        primaryStage.show();

    }

    public void copyToClipboard(String toCopy) {
        StringSelection stringSelection = new StringSelection(toCopy);
        Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
        clpbrd.setContents(stringSelection, null);
    }
}
