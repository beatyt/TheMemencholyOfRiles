package app;

import api.Configuration;
import api.ParserService;
import api.ScraperService;
import api.StorageService;
import com.google.inject.Guice;
import com.google.inject.Injector;
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
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;


public class StartupHandler extends Application {
    private static final Logger logger = LoggerFactory.getLogger(StartupHandler.class);

    public static void main(String[] args) {
        Injector injector = Guice.createInjector(new Module());
        ScraperService scraperService = injector.getInstance(ScraperService.class);
        ParserService parserService = injector.getInstance(ParserService.class);
        StorageService storageService = injector.getInstance(StorageService.class);

        Module app = injector.getInstance(Module.class);

        LocalDateTime startTime = LocalDateTime.now();
        logger.info("Starting at: " + startTime);

        Configuration.OPTIONS.REPLACE_NAME_ONCE.setOption(true);
        Configuration.OPTIONS.USE_GENDER_MALE.setOption(true);

        app.start();

        String useGui = PropertyHandler.getInstance().getValue("useGui");
        if (useGui != null && useGui.equals("true")) {
            launch(args);
        }
    }


    @Override
    public void start(Stage primaryStage) throws Exception {
        File dataFile = new File(System.getProperty("user.dir"),
                PropertyHandler.getInstance().getValue("saveToFileName"));

        Label errMsg = new Label("Copy autocopies lines to your clipboard =)");
        errMsg.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        errMsg.setTextFill(Color.WHITE);

        TextArea textArea = new TextArea();
        textArea.setWrapText(true);
        textArea.setPrefRowCount(30);

        VBox vbox = new VBox();
        vbox.setPadding(new Insets(10));
        vbox.setSpacing(10);
        vbox.setStyle("-fx-background-color: #336699;");

        vbox.getChildren().addAll(errMsg, textArea);

        HBox hbox = new HBox();
        hbox.setPadding(new Insets(10));
        hbox.setSpacing(10);

        Button fetchBtn = new Button("Fetch");
        fetchBtn.setPrefSize(100, 20);
        fetchBtn.setOnAction(e -> {
//            app.start();
        });

        Button copyBtn = new Button("Copy Random");
        copyBtn.setPrefSize(100, 20);
        copyBtn.setOnAction(e -> {
            String contents;
            try {
                logger.info("Copying random line from: " + dataFile.getAbsoluteFile());
                contents = FileUtils.readFileToString(dataFile);
                if (!dataFile.exists()) {
                    logger.info("The dataFile was not found.");
                    errMsg.setText("The dataFile was not found.");
                } else {
                    String lines = "";
                    String[] linesInFile = contents.split("\r\n");
                    for (int i = 0; i < 9; i++) {
                        String copyString = linesInFile[(int)(Math.random()*linesInFile.length)];
                        lines += copyString + "\n\n";
                    }
                    String copyString = linesInFile[(int)(Math.random()*linesInFile.length)];
                    textArea.setText(lines);
                    copyToClipboard(copyString);

                }
            } catch (IOException e1) {
                e1.printStackTrace();
            }

        });


        hbox.getChildren().addAll(fetchBtn, copyBtn);

        BorderPane myPane = new BorderPane();
        myPane.setTop(vbox);
        myPane.setCenter(hbox);

        Scene myScene = new Scene(myPane);

        primaryStage.setScene(myScene);
        primaryStage.setWidth(400);
        primaryStage.setHeight(700);
        primaryStage.show();

    }

    public void copyToClipboard(String toCopy) {
        StringSelection stringSelection = new StringSelection(toCopy);
        Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
        clpbrd.setContents(stringSelection, null);
    }
}
