import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static javafx.scene.input.KeyCode.*;

class Grid {
    int black = 0; //white
    int back_up = 0;

    void clean() {
        this.black = 0;
        this.back_up = 0;
    }
    void live() {
        this.black = 1;
    }
    void ready_live() {
        this.back_up = 1;
    }
    void ready_sleep() {
        this.back_up = 0;
    }
    void update() {
        black = back_up;
        back_up = 0;
    }
}

class Board {
    List<Grid> board = new ArrayList<>(50 * 75);

    Board() {
        for(int i=0;i<50*75;i++){
            board.add(new Grid());
        }
    }
    void clear() {
        for(int i=0;i<50*75;i++){
            board.get(i).clean();
        }
    }
    boolean valid_point(int x, int y) {
        if(x >= 0 & x<75 & y>=0 & y<50) {
            return true;
        } else {
            return false;
        }
    }
    int convert(int x, int y)
    {
        return 75*y+x;
    }

    void step_update(int x, int y) {
        int count = 0;
        if(valid_point(x,y-1) && board.get(convert(x,y-1)).black == 1) {count += 1;}
        if(valid_point(x-1,y) && board.get(convert(x-1,y)).black == 1) {count += 1;}
        if(valid_point(x+1,y) && board.get(convert(x+1,y)).black == 1) {count += 1;}
        if(valid_point(x,y+1) && board.get(convert(x,y+1)).black == 1) {count += 1;}
        if(valid_point(x-1,y+1) && board.get(convert(x-1,y+1)).black == 1) {count += 1;}
        if(valid_point(x+1,y+1) && board.get(convert(x+1,y+1)).black == 1) {count += 1;}
        if(valid_point(x-1,y-1) && board.get(convert(x-1,y-1)).black == 1) {count += 1;}
        if(valid_point(x+1,y-1) && board.get(convert(x+1,y-1)).black == 1) {count += 1;}

        if(board.get(convert(x,y)).black == 1) {
            if(count < 2) {
                board.get(convert(x,y)).ready_sleep();
            } else if (count >3) {
                board.get(convert(x,y)).ready_sleep();
            } else {
                board.get(convert(x,y)).ready_live();
            }
        } else {
            if(count == 3) {
                board.get(convert(x,y)).ready_live();
            } else {
                board.get(convert(x,y)).ready_sleep();
            }
        }
    }

    void ready_update() {
        for(int x = 0; x<75 ; x++) {
            for (int y = 0; y < 50; y++) {
                step_update(x,y);
            }
        }
    }

    void update() {
        for(int x = 0; x<75 ; x++) {
            for (int y = 0; y < 50; y++) {
                board.get(convert(x,y)).update();
            }
        }
    }

    void create(String type, int x, int y) {
        if(type.equals("Block")) {
            if(valid_point(x,y)) {board.get(convert(x,y)).live();}
            if(valid_point(x+1,y)) {board.get(convert(x+1,y)).live();}
            if(valid_point(x,y+1)) {board.get(convert(x,y+1)).live();}
            if(valid_point(x+1,y+1)) {board.get(convert(x+1,y+1)).live();}
        }
        else if(type.equals("Beehive")) {
            if(valid_point(x+1,y)) {board.get(convert(x+1,y)).live();}
            if(valid_point(x+2,y)) {board.get(convert(x+2,y)).live();}
            if(valid_point(x,y+1)) {board.get(convert(x,y+1)).live();}
            if(valid_point(x+3,y+1)) {board.get(convert(x+3,y+1)).live();}
            if(valid_point(x+1,y+2)) {board.get(convert(x+1,y+2)).live();}
            if(valid_point(x+2,y+2)) {board.get(convert(x+2,y+2)).live();}
        }
        else if(type.equals("Blinker")) {
            if(valid_point(x,y+1)) {board.get(convert(x,y+1)).live();}
            if(valid_point(x+1,y+1)) {board.get(convert(x+1,y+1)).live();}
            if(valid_point(x+2,y+1)) {board.get(convert(x+2,y+1)).live();}
        }
        else if(type.equals("Toad")) {
            if(valid_point(x,y+1)) {board.get(convert(x,y+1)).live();}
            if(valid_point(x+1,y+1)) {board.get(convert(x+1,y+1)).live();}
            if(valid_point(x+2,y+1)) {board.get(convert(x+2,y+1)).live();}
            if(valid_point(x+1,y)) {board.get(convert(x+1,y)).live();}
            if(valid_point(x+2,y)) {board.get(convert(x+2,y)).live();}
            if(valid_point(x+3,y)) {board.get(convert(x+3,y)).live();}
        }
        else if(type.equals("Glider")) {
            if(valid_point(x+2,y)) {board.get(convert(x+2,y)).live();}
            if(valid_point(x+2,y+1)) {board.get(convert(x+2,y+1)).live();}
            if(valid_point(x,y+1)) {board.get(convert(x,y+1)).live();}
            if(valid_point(x+1,y+2)) {board.get(convert(x+1,y+2)).live();}
            if(valid_point(x+2,y+2)) {board.get(convert(x+2,y+2)).live();}
        }
        else {System.out.println("Something is wrong");}
    }
}

public class Main extends Application {
    int frame = 0;
    String hold = "Block";
    String create = "Empty";
    int create_x = 1;
    int create_y = 1;
    float width = 1200;
    float height = 780;
    float canvas_width = 1200;
    float canvas_height = 700;
    float grid_width = canvas_width / 75;
    float grid_height = canvas_height / 50;
    Board b;
    Timer timer;
    boolean pause = false;
    final Canvas canvas = new Canvas(canvas_width,canvas_height);
    GraphicsContext gc = canvas.getGraphicsContext2D();
    Label left = new Label("Choose a shape and click to start");
    Label right = new Label("Frame  1");
    boolean move = false;

    public int convert(int x, int y)
    {
        return 75*y+x;
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("Conway's Game of Life (b327zhan)");
        b = new Board();

    //Tool bar
        ToolBar toolBar = new ToolBar();
        Button button1 = new Button("Block");
        Button button2 = new Button("Beehive");
        Button button3 = new Button("Blinker");
        Button button4 = new Button("Toad");
        Button button5 = new Button("Glider");
        Button button6 = new Button("Clear");
        ImageView view1 = new ImageView(new Image("1.png"));
        ImageView view2 = new ImageView(new Image("2.png"));
        ImageView view3 = new ImageView(new Image("3.png"));
        ImageView view4 = new ImageView(new Image("4.png"));
        ImageView view5 = new ImageView(new Image("5.png"));
        ImageView view6 = new ImageView(new Image("6.png"));
        view1.setFitHeight(30);
        view1.setFitWidth(30);
        view2.setFitHeight(30);
        view2.setFitWidth(40);
        view3.setFitHeight(30);
        view3.setFitWidth(30);
        view4.setFitHeight(30);
        view4.setFitWidth(60);
        view5.setFitHeight(30);
        view5.setFitWidth(30);
        view6.setFitHeight(30);
        view6.setFitWidth(35);
        button1.setGraphic(view1);
        button1.setOnAction(event -> { hold = "Block"; });
        button2.setGraphic(view2);
        button2.setOnAction(event -> { hold = "Beehive"; });
        button3.setGraphic(view3);
        button3.setOnAction(event -> { hold = "Blinker"; });
        button4.setGraphic(view4);
        button4.setOnAction(event -> { hold = "Toad"; });
        button5.setGraphic(view5);
        button5.setOnAction(event -> { hold = "Glider"; });
        button6.setGraphic(view6);
        button6.setOnAction(event -> { b.clear(); create = "Empty"; hold = "Empty"; frame = 0; draw();});
        Pane emptyPane = new Pane();
        HBox.setHgrow(emptyPane, Priority.ALWAYS);
        toolBar.getItems().addAll(button1,button2,button3,button4,button5,emptyPane,button6);


    //Canvas
    //Status bar
        ToolBar status_bar = new ToolBar();
        Pane emptyPane2 = new Pane();
        HBox.setHgrow(emptyPane2, Priority.ALWAYS);
        status_bar.getItems().addAll(left,emptyPane2, right);
    //add to vbox
        VBox root = new VBox(toolBar, canvas, status_bar);
    //add to root
        Scene scene = new Scene(root, width, height);

        scene.setOnKeyPressed(keyEvent -> {
            if(keyEvent.getCode() == P ) {
                pause = !(pause);
            }
            else if(keyEvent.getCode() == M && pause) {
                frame += 1;
                b.ready_update();
                b.update();
                draw();
            }
        });
        
        stage.setScene(scene);
        stage.setResizable(false);


        //mouse
        canvas.setOnMouseClicked(mouseEvent -> {
            int point_x = (int) (mouseEvent.getX() / grid_width);
            int point_y = (int) (mouseEvent.getY() / grid_height);

            //clicking behaviour **=======================
            if(!hold.equals("Empty")) {
                create = hold;
                create_x = point_x+1;
                create_y = point_y+1;
                b.create(create, point_x,point_y);
            }
            if(pause) {
                draw();
            }

            //clicking behaviour **==========================
        });

        //timer
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if(!pause){

                frame += 1;
                b.ready_update();
                b.update();

                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        draw();
                    }
                });
            }
        }

        }, 0, 1000);
        stage.show();
    }
    public void draw() {
        right.setText("Frame: " + frame);
        if (!create.equals("Empty")) {
            left.setText("Created " + create + " at (" + create_x + ", " + create_y + ")");
        } else {
            left.setText("Choose a shape and click to start");
        }
        for (int x = 0; x < 75; x++) {
            for (int y = 0; y < 50; y++) {
                if (b.board.get(convert(x, y)).black == 1) {
                    gc.setFill(Color.BLACK);
                    gc.fillRect(x * grid_width, y * grid_height, grid_width, grid_height);
                } else {
                    gc.setFill(Color.WHITE);
                    gc.fillRect(x * grid_width, y * grid_height, grid_width, grid_height);
                }

            }
        }
        gc.setFill(Color.GREY);
        for (int i = 1; i < 75; i++) {
            gc.fillRect(i * grid_width, 0, 1, canvas_height);
        }
        for (int i = 1; i < 50; i++) {
            gc.fillRect(0, i * grid_height, canvas_width, 1);
        }
    }

    @Override
    public void stop() throws Exception {
        timer.cancel();
    }
}
