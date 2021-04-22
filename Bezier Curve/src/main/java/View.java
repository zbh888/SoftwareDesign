import javafx.event.Event;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.CubicCurve;
import javafx.scene.shape.Line;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.*;
import java.util.ArrayList;

import static javafx.scene.input.KeyCode.*;


public class View extends VBox {
    ArrayList<Curve> curves = new ArrayList<>();
    ArrayList<Circle_Class> circles = new ArrayList<>();

    int cell_size = 35;
    Pane canvas = new Pane();

    String mode = "Pen"; //
    String thick = "Medium"; // S M L
    int width = 5;
    String style = "Solid"; // 1 2 3
    String Drawing = "No"; //or no


    Label mod = new Label("1");
    Label thic = new Label("2");
    Label styl = new Label("3");
    Label Draw = new Label("4");
    Label cop = new Label("5");
    int start_point_x = -1;
    int start_point_y = -1;
    boolean hold_delete = false;

    Curve temp = new Curve();
    boolean selected = false;
    Curve under_selection = new Curve();

    Button thick_button1 = new Button();
    Button thick_button2 = new Button();
    Button thick_button3 = new Button();
    Button style_button1 = new Button();
    Button style_button2 = new Button();
    Button style_button3 = new Button();
    Button button1 = new Button();
    Button button2 = new Button();
    Button button3 = new Button();
    Button button4 = new Button();
    ColorPicker colorPicker = new ColorPicker(Color.BLACK);
    FileChooser fileChooser = new FileChooser();
    MenuItem Copy = new MenuItem("Copy");
    MenuItem Cut = new MenuItem("Cut");
    MenuItem Paste = new MenuItem("Paste");
    MenuItem stop_Paste = new MenuItem("Stop Paste");

    boolean saved = true;
    boolean promoted = false;
    boolean promoted_load = false;
    boolean promoted_new = false;
    boolean ever_saved = false;

    String copying = "No";
    Curve under_copying = new Curve();

    ArrayList<Curve> new_curves = new ArrayList<>();
    View(Stage stage) {
        label_update();
        /// layout ==========================================
        canvas.setPrefSize(1055,720);
        canvas.setStyle("-fx-background-color: white;");
        // Top menu bar
        MenuBar menuBar = new MenuBar();
        Menu File_menu = new Menu("File");
        MenuItem F1 = new MenuItem("New");
        MenuItem F2 = new MenuItem("Load");
        MenuItem F3 = new MenuItem("Save");
        MenuItem F4 = new MenuItem("Quit");
        File_menu.getItems().addAll(F1,F2,F3,F4);
        Menu Help_menu = new Menu("Help");
        MenuItem H1 = new MenuItem("About");
        Help_menu.getItems().add(H1);
        Menu Edit_menu = new Menu("Edit");
        Edit_menu.getItems().addAll(Copy,Cut,Paste,stop_Paste);
        stop_Paste.setDisable(true);
        menuBar.getMenus().addAll(File_menu, Help_menu, Edit_menu);
        Copy.setDisable(true);
        Cut.setDisable(true);
        Paste.setDisable(true);
        // Bottom
        HBox Body = new HBox();
        // Bottom Left
        ScrollPane scroll = new ScrollPane();
        ScrollPane scroll2 = new ScrollPane();
        VBox Body_left = new VBox();

        scroll.setContent(Body_left);

        GridPane Four_button = new GridPane();
        Four_button.setAlignment(Pos.CENTER);
        GridPane Thick_Three_button = new GridPane();
        Thick_Three_button.setAlignment(Pos.CENTER);
        GridPane Style_three_button = new GridPane();
        Style_three_button.setAlignment(Pos.CENTER);

        ImageView view1 = new ImageView(new Image("1.png"));
        ImageView view2 = new ImageView(new Image("2.png"));
        ImageView view3 = new ImageView(new Image("3.png"));
        ImageView view4 = new ImageView(new Image("4.png"));
        view1.setFitHeight(cell_size*1.3);
        view1.setFitWidth(cell_size*1.3);
        view2.setFitHeight(cell_size*1.3);
        view2.setFitWidth(cell_size*1.3);
        view3.setFitHeight(cell_size*1.3);
        view3.setFitWidth(cell_size*1.3);
        view4.setFitHeight(cell_size*1.3);
        view4.setFitWidth(cell_size*1.3);
        button1.setGraphic(view1);
        button2.setGraphic(view2);
        button3.setGraphic(view3);
        button4.setGraphic(view4);

        ImageView thick_view1 = new ImageView(new Image("5.png"));
        ImageView thick_view2 = new ImageView(new Image("6.png"));
        ImageView thick_view3 = new ImageView(new Image("7.png"));
        thick_view1.setFitHeight(cell_size*2);
        thick_view1.setFitWidth((cell_size*2/3));
        thick_view2.setFitHeight(cell_size*2);
        thick_view2.setFitWidth((cell_size*2/3));
        thick_view3.setFitHeight(cell_size*2);
        thick_view3.setFitWidth((cell_size*2/3));
        thick_button1.setGraphic(thick_view1);
        thick_button2.setGraphic(thick_view2);
        thick_button3.setGraphic(thick_view3);

        ImageView style_view1 = new ImageView(new Image("8.png"));
        ImageView style_view2 = new ImageView(new Image("9.png"));
        ImageView style_view3 = new ImageView(new Image("10.png"));
        style_view1.setFitHeight(cell_size);
        style_view1.setFitWidth(cell_size*3.2);
        style_view2.setFitHeight(cell_size);
        style_view2.setFitWidth(cell_size*3.2);
        style_view3.setFitHeight(cell_size);
        style_view3.setFitWidth(cell_size*3.2);
        style_button1.setGraphic(style_view1);
        style_button2.setGraphic(style_view2);
        style_button3.setGraphic(style_view3);

        Four_button.add(button1, 0, 0);
        Four_button.add(button2, 1, 0);
        Four_button.add(button3, 0, 1);
        Four_button.add(button4, 1, 1);
        Thick_Three_button.add(thick_button1, 0, 0);
        Thick_Three_button.add(thick_button2, 1, 0);
        Thick_Three_button.add(thick_button3, 2, 0);
        Style_three_button.add(style_button1, 0, 0);
        Style_three_button.add(style_button2, 0, 1);
        Style_three_button.add(style_button3, 0, 2);

        colorPicker.setPrefSize(cell_size*4, cell_size);
        scroll.setMinWidth(cell_size*4+2);
        Body_left.getChildren().addAll(Four_button, colorPicker, Thick_Three_button, Style_three_button, mod, thic, styl, Draw, cop);

        // Bottom Right
        scroll2.setContent(canvas);
        scroll2.getContent().setOnMousePressed(Event::consume);

        Body.getChildren().addAll(scroll, scroll2);
     //   Body.getChildren().addAll(Body_left, scroll2);
        this.getChildren().addAll(menuBar,Body);
        /// layout ==========================================

        /// Logic ===========================================
        button1.setOnAction(event -> { mode = "Pen";button_disable();finish_drawing(); finish_selecting(); label_update();});
        button2.setOnAction(event -> { mode = "Select";finish_drawing(); if(!selected) {button_disable();} label_update(); });
        button3.setOnAction(event -> { mode = "Point"; finish_drawing(); button_disable(); label_update();});
        button4.setOnAction(event -> { mode = "Delete";finish_drawing(); finish_selecting(); button_disable(); label_update();});
        H1.setOnAction(event -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Personal Information");
            alert.setHeaderText(null);
            alert.setContentText("Program: CS \nName: Bohan Zhang \nWatID: b327zhan ");
            alert.showAndWait();
        });

        thick_button1.setOnAction(event -> {
            thick = "Small"; width = 1; label_update();
            if(selected) {
                under_selection.width = 1;
                change_save_to_false();
                update();
            }
        }); //change the selected curve
        thick_button2.setOnAction(event -> {
            thick = "Medium"; width = 5; label_update();
            if(selected) {
                under_selection.width = 5;
                change_save_to_false();
                update();
            }
        });
        thick_button3.setOnAction(event -> {
            thick = "Large"; width = 10; label_update();
            if(selected) {
                under_selection.width = 10;
                change_save_to_false();
                update();
            }
        });
        style_button1.setOnAction(event -> {
            style = "Dotted"; label_update();
            if(selected) {
                under_selection.style = "Dotted";
                change_save_to_false();
                update();
            }
        });
        style_button2.setOnAction(event -> {
            style = "Dashed"; label_update();
            if(selected) {
                under_selection.style = "Dashed";
                change_save_to_false();
                update();
            }
        });
        style_button3.setOnAction(event -> {
            style = "Solid"; label_update();
            if(selected) {
                under_selection.style = "Solid";
                change_save_to_false();
                update();
            }
        });
        colorPicker.setOnAction(event -> {
            if(selected) {
                under_selection.color = colorPicker.getValue();
                change_save_to_false();
                update();
            }
        });

        // pen mode, circle show up is enough
        canvas.setOnMouseClicked(mouseEvent -> {
            int point_x = (int) (mouseEvent.getX());
            int point_y = (int) (mouseEvent.getY());
            if(mode.equals("Pen")) {
                if(start_point_y + start_point_x < 0) {
                    start_point_x = point_x;
                    start_point_y = point_y;
                    Drawing = "Yes";
                    temp.setPara(style, width, colorPicker.getValue());
                    button_disable();
                    label_update();
                } else {
                    temp.addCurve(start_point_x, start_point_y, point_x, point_y);
                    start_point_x = point_x;
                    start_point_y = point_y;
                    label_update();
                    update();
                }
            } else if(mode.equals("Delete") || (mode.equals("Select") && (hold_delete))) {
                boolean found = false;
                for(Curve delete_curve : curves) {
                    for(CubicCurve delete_curve_cubic: delete_curve.list_curves) {
                        delete_curve_cubic.setFill(null);
                        delete_curve_cubic.setStroke(delete_curve.color);
                        delete_curve_cubic.setStrokeWidth(delete_curve.width);
                        if(delete_curve_cubic.contains(point_x, point_y)) {
                            if(selected) {
                                if(under_selection == delete_curve) {
                                    finish_selecting();
                                }
                            }
                            curves.remove(delete_curve);
                            change_save_to_false();
                            found = true;
                            update();
                            break;
                        }
                    }
                    if(found) {saved=false; break;}
                }
            } else if(mode.equals("Select")) {
                boolean found_select = false;
                for(Curve select_curve : curves) {
                    for(CubicCurve select_curve_cubic: select_curve.list_curves) {
                        select_curve_cubic.setFill(null);
                        select_curve_cubic.setStroke(select_curve.color);
                        select_curve_cubic.setStrokeWidth(select_curve.width);
                        if(select_curve_cubic.contains(point_x, point_y)) {
                            if(selected) {
                                finish_selecting();
                            }
                            under_selection = select_curve;
                            colorPicker.setValue(under_selection.color);
                            setThick(under_selection.width);
                            style = under_selection.style;
                            label_update();
                            //do something with our database
                            // maybe circles_array helps
                            selection();
                            colorPicker.requestFocus();
                            found_select = true;
                            update();
                            break;
                        }
                    }
                    if(found_select) {break;}
                }
            } else if(mode.equals("Point")) {
                if(selected) {
                    for(int i = 0; i< circles.size(); i++) {
                        if(i==0 && circles.get(i).start.contains(point_x,point_y)) {
                            circles.get(0).change_stype();
                            under_selection.change_stype();
                            change_save_to_false();
                            update();
                        }
                        else if(i != circles.size() - 1) {
                            if(circles.get(i).end.contains(point_x,point_y)) {
                                circles.get(i).change_etype();
                                circles.get(i + 1).change_stype();
                                under_selection.change_type(i);
                                change_save_to_false();
                                update();
                            }
                        } else if(i == circles.size()-1 && circles.get(i).end.contains(point_x,point_y)) {
                            circles.get(i).change_etype();
                            under_selection.change_etype();
                            change_save_to_false();
                            update();
                        }
                    }
                }
            } else if(mode.equals("Paste") && copying.equals("Yes")) {
                double diffX = under_copying.list_curves.get(0).getStartX() - point_x;
                double diffY = under_copying.list_curves.get(0).getStartY() - point_y;
                Curve c = under_copying.copy();
                for(CubicCurve cubic: c.list_curves) {
                    cubic.setStartX(cubic.getStartX() - diffX);
                    cubic.setStartY(cubic.getStartY() - diffY);
                    cubic.setEndX(cubic.getEndX() - diffX);
                    cubic.setEndY(cubic.getEndY() - diffY);
                    cubic.setControlX1(cubic.getControlX1() - diffX);
                    cubic.setControlX2(cubic.getControlX2() - diffX);
                    cubic.setControlY1(cubic.getControlY1() - diffY);
                    cubic.setControlY2(cubic.getControlY2() - diffY);
                }
                curves.add(c);
                change_save_to_false();
                update();
            }
        });

        this.setOnKeyPressed(keyEvent -> {
            if(keyEvent.getCode() == ESCAPE ) {
                if(mode.equals("Pen") && Drawing.equals("Yes")) {
                    finish_drawing();
                    label_update();
                } else if ((mode.equals("Select") || mode.equals("Point")) && selected){
                    finish_selecting();
                    button_disable();
                    if(mode.equals("Select")) {button2.requestFocus();}
                    if(mode.equals("Point")) {button3.requestFocus();}
                }
            }
            if(keyEvent.getCode() == BACK_SPACE) {
                hold_delete = true;
            }
        });

        this.setOnKeyReleased(keyEvent -> {
            if(keyEvent.getCode() == BACK_SPACE) {
                hold_delete = false;
            }
        });


        F1.setOnAction(e -> {
            if(saved || promoted_new || curves.size() == 0) {
                button_enable();
                stop_Paste.setDisable(true);
                Paste.setDisable(true);
                start_point_x = -1;
                start_point_y = -1;
                mode = "Pen";
                thick = "Medium";
                style = "Solid";
                Drawing = "No";
                copying = "No";
                temp = new Curve();
                label_update();
                canvas.getChildren().clear();
                curves = new ArrayList<>();
                button1.requestFocus();
                saved = true;
            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Suggest Saving");
                alert.setHeaderText(null);
                if(!ever_saved) {
                    alert.setContentText("Maybe you want to save this work \n (This message won't show up if you Load again without further changing your work)");
                } else {
                    alert.setContentText("You have made some action after you saved last time\n Maybe you want to save this work again \n (This message won't show up if you New again without further changing your work)");
                }
                alert.showAndWait();
                promoted_new = true;
            }
        });

        F2.setOnAction(e -> {
            finish_drawing();
            finish_selecting();
            if(saved || promoted_load || curves.size() == 0) {
                fileChooser.setTitle("Load");
                File load = fileChooser.showOpenDialog(stage);
                try {
                    if (load != null) {
                        new_curves = new ArrayList<>();
                        BufferedReader in = new BufferedReader(new FileReader(load));
                        String line;
                        while ((line = in.readLine()) != null) {
                            Curve c = create_Curve(line);
                            new_curves.add(c);
                        }
                        curves = new_curves;
                        saved = true;
                        promoted_load = false;
                        update();
                    }
                } catch (Exception exp) {
                    Alert err = new Alert(Alert.AlertType.ERROR);
                    err.setTitle("Error");
                    err.setHeaderText("Wrong File format");
                    err.setContentText("You should load a file which was saved using this program");
                    err.showAndWait();
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Suggest Saving");
                alert.setHeaderText(null);
                if(!ever_saved) {
                    alert.setContentText("Maybe you want to save this work \n (This message won't show up if you Load again without further changing your work)");
                } else {
                    alert.setContentText("You have made some action after you saved last time\n Maybe you want to save this work again \n (This message won't show up if you Load again without further changing your work)");
                }
                alert.showAndWait();
                promoted_load = true;
            }

        });

        F3.setOnAction(e -> {
            finish_drawing();
            finish_selecting();
            fileChooser.setTitle("Save");
            File save = fileChooser.showSaveDialog(stage);
            try {
                if(save != null) {
                    BufferedWriter out = new BufferedWriter(new FileWriter(save));
                    for (Curve cur : curves) {
                        out.write(cur.save());
                    }
                    out.close();
                    saved = true;
                    ever_saved = true;
                }
            } catch (IOException exception) {
            }
        });

        F4.setOnAction(e -> {
            if(saved || promoted || curves.size() == 0) {
                System.exit(0);
            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Suggest Saving");
                alert.setHeaderText(null);
                if(!ever_saved) {
                    alert.setContentText("Maybe you want to save this work \n (This message won't show up if you Quit again without further changing your work)");
                } else {
                    alert.setContentText("You have made some action after you saved last time\n Maybe you want to save this work again \n (This message won't show up if you Quit again without further changing your work)");
                }
                alert.showAndWait();
                promoted = true;
            }
        });

        Copy.setOnAction(e -> {
            if(selected) {
                under_copying = under_selection.copy();
                copying = "Yes";
                Paste.setDisable(false);
                label_update();
            }
        });

        Cut.setOnAction(e -> {
            if(selected) {
                under_copying = under_selection.copy();
                curves.remove(under_selection);
                copying = "Yes";
                Paste.setDisable(false);
                label_update();
                finish_selecting();
            }

        });

        Paste.setOnAction(e -> {
            if(Drawing.equals("Yes")) {
                finish_drawing();
            }
            if(selected) {
                finish_selecting();
            }
            button_disable();
            all_button_disable();
            mode = "Paste";
            stop_Paste.setDisable(false);
            label_update();
        });
        stop_Paste.setOnAction(e -> {
            button_enable();
            mode = "Pen";
            stop_Paste.setDisable(true);
            label_update();
        });


        // select mode, processing curve and binding circle
        /// Logic ===========================================
    }
    void label_update() {
        mod.setText("  Mode:                  "+mode);
        cop.setText("  Copy:                   "+copying);
        styl.setText("  Style:                    "+ style);
        thic.setText("  Thickness:         "+ thick);
        Draw.setText("  Drawing:             "+Drawing);
    }

    void finish_drawing() {
        Drawing = "No";
        start_point_x = -1;
        start_point_y = -1;
        button_enable();
        if(temp.size !=0) {
            curves.add(temp);
            change_save_to_false();
            temp = new Curve();
            update();
        }
    }
    void setThick(int a) {
        width = a;
        if(a==1) {
            thick = "Small";
        } else if(a == 5) {
            thick = "Medium";
        } else if(a==10) {
            thick = "Large";
        }
    }
    void update() {
        if(selected) {
            canvas.getChildren().removeIf(i -> i.getClass() == CubicCurve.class);
        } else {
            canvas.getChildren().clear();
        }
        for(Curve c : curves) {
            ArrayList<CubicCurve> new_curve_segs = c.getCuve_segs();
            for (CubicCurve cubic : new_curve_segs) {
                cubic.setFill(null);
                if(c.style.equals("Dotted")) {
                    cubic.getStrokeDashArray().addAll(2d, 20d);
                }
                else if(c.style.equals("Dashed")) {
                    cubic.getStrokeDashArray().addAll(50d, 40d);
                }
                cubic.setStrokeWidth(c.width);
                cubic.setStroke(c.color);
                canvas.getChildren().add(cubic);
                cubic.toBack();
            }
        }
        if(temp.size != 0) {
            ArrayList<CubicCurve> new_curve_segs2 = temp.getCuve_segs();
            for (int i = 0; i < new_curve_segs2.size(); i++) {
                CubicCurve cubic = new_curve_segs2.get(i);
                if(style.equals("Dotted")) {
                    cubic.getStrokeDashArray().addAll(2d, 20d);
                }
                else if(style.equals("Dashed")) {
                    cubic.getStrokeDashArray().addAll(50d, 40d);
                }
                cubic.setStrokeWidth(width);
                cubic.setFill(null);
                cubic.setStroke(colorPicker.getValue());
                canvas.getChildren().add(cubic);
                //circles and lines
                Line l = new Line(cubic.getControlX1(),cubic.getControlY1(),cubic.getStartX(),cubic.getStartY());
                canvas.getChildren().add(l);
                l.setStrokeWidth(0.5);
                l.setStroke(Color.GREY);
                l = new Line(cubic.getControlX2(),cubic.getControlY2(),cubic.getEndX(),cubic.getEndY());
                canvas.getChildren().add(l);
                l.setStroke(Color.GREY);
                l.setStrokeWidth(0.5);
                Circle circle = new Circle(cubic.getStartX(), cubic.getStartY(), 7, Color.WHITE);
                canvas.getChildren().add(circle);
                circle.setStroke(Color.BLUE);
                circle.setStrokeWidth(2.0);
                circle = new Circle(cubic.getEndX(), cubic.getEndY(), 7, Color.WHITE);
                canvas.getChildren().add(circle);
                circle.setStroke(Color.BLUE);
                circle.setStrokeWidth(2.0);
                circle = new Circle(cubic.getControlX1(), cubic.getControlY1(), 4, Color.WHITE);
                canvas.getChildren().add(circle);
                circle.setStroke(Color.GREEN);
                circle.setStrokeWidth(2.0);
                circle = new Circle(cubic.getControlX2(), cubic.getControlY2(), 4, Color.WHITE);
                canvas.getChildren().add(circle);
                circle.setStroke(Color.GREEN);
                circle.setStrokeWidth(2.0);
            }
        }
    }
    void finish_selecting() {
        Copy.setDisable(true);
        Cut.setDisable(true);
        selected = false;
        circles = new ArrayList<>();
        update();
    }
    void selection() {
        selected = true;
        Copy.setDisable(false);
        Cut.setDisable(false);
        button_enable();
        for(int i = 0; i<under_selection.size; i++) {
            CubicCurve cubic = under_selection.list_curves.get(i);
            int s_type=0;
            int e_type=0;
            if(i==0) {
                s_type = under_selection.s_type;
                if(i == under_selection.size - 1) {
                    e_type = under_selection.e_type;
                } else {
                    e_type = under_selection.list_point.get(i);
                }
            } else {
                s_type = under_selection.list_point.get(i-1);
                if(i == under_selection.size - 1) {
                    e_type = under_selection.e_type;
                } else {
                    e_type = under_selection.list_point.get(i);
                }
            }
            // node stuff
            Circle_Class circle_class = new Circle_Class(cubic, s_type, e_type, this);
            circles.add(circle_class);
            circle_class.push_to(canvas);
        }
        for(int i = 0; i < circles.size(); i++) {
            if(i != 0) {
                circles.get(i).add_pre(circles.get(i - 1));
            }
            if(i != circles.size() -1) {
                circles.get(i).add_post(circles.get(i + 1));
                circles.get(i).end.centerXProperty().bindBidirectional(circles.get(i+1).start.centerXProperty());
                circles.get(i).end.centerYProperty().bindBidirectional(circles.get(i+1).start.centerYProperty());
            }
        }
    }
    void button_disable() {
        thick_button1.setDisable(true);
        thick_button2.setDisable(true);
        thick_button3.setDisable(true);
        style_button1.setDisable(true);
        style_button2.setDisable(true);
        style_button3.setDisable(true);
        colorPicker.setDisable(true);
    }
    void all_button_disable() {
        button1.setDisable(true);
        button2.setDisable(true);
        button3.setDisable(true);
        button4.setDisable(true);
    }
    void button_enable() {
        thick_button1.setDisable(false);
        thick_button2.setDisable(false);
        thick_button3.setDisable(false);
        style_button1.setDisable(false);
        style_button2.setDisable(false);
        style_button3.setDisable(false);
        colorPicker.setDisable(false);
        button1.setDisable(false);
        button2.setDisable(false);
        button3.setDisable(false);
        button4.setDisable(false);
    }

    public Curve create_Curve(String line) {
        String[] arrOfStr = line.split(" ");
        Curve new_curve = new Curve();
        int s = Integer.parseInt(arrOfStr[0]);
        int w = Integer.parseInt(arrOfStr[1]);
        new_curve.size = s;
        new_curve.width = w;
        new_curve.color = new Color(Double.parseDouble(arrOfStr[4]),
                Double.parseDouble(arrOfStr[3]),Double.parseDouble(arrOfStr[2]),Double.parseDouble(arrOfStr[5]));
        new_curve.style = arrOfStr[6];
        int k = 7;
        for(int i = 0; i< s-1;i++) {
            new_curve.list_point.add(Integer.parseInt(arrOfStr[k]));
            k = 7+i+1;
        }
        for(int i = 0; i< s;i++) {
            CubicCurve cubic = new CubicCurve();
            cubic.setStartX(Double.parseDouble(arrOfStr[k]));
            cubic.setStartY(Double.parseDouble(arrOfStr[k+1]));
            cubic.setEndX(Double.parseDouble(arrOfStr[k+2]));
            cubic.setEndY(Double.parseDouble(arrOfStr[k+3]));
            cubic.setControlX1(Double.parseDouble(arrOfStr[k+4]));
            cubic.setControlY1(Double.parseDouble(arrOfStr[k+5]));
            cubic.setControlX2(Double.parseDouble(arrOfStr[k+6]));
            cubic.setControlY2(Double.parseDouble(arrOfStr[k+7]));
            k = k+8;
            new_curve.list_curves.add(cubic);
        }
        int s_t = Integer.parseInt(arrOfStr[k]);
        int e_t = Integer.parseInt(arrOfStr[k+1]);
        new_curve.s_type = s_t;
        new_curve.e_type = e_t;
        return new_curve;
    }
    void change_save_to_false() {
        saved = false;
        promoted_load = false;
        promoted = false;
        promoted_new = false;
    }
}