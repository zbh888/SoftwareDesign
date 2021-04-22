import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.CubicCurve;
import javafx.scene.shape.Line;

public class Circle_Class {
    Circle start;
    Circle end;
    Circle control1;
    Circle control2;
    View v;

    Circle pre_control;
    boolean is_pre = false;
    Circle post_control;
    boolean is_post = false;
    Circle pre;
    Circle post;

    Line sc;
    Line ec;
    int s_type = 0; //0 is smooth -1 is start smooth, -2 start sharp
    int e_type = 0; //0 is smooth -2 is start smooth -1 is start sharp
    Circle_Class(CubicCurve cubic, int s_t, int e_t, View v) {
        this.v = v;
        this.s_type = s_t;
        this.e_type = e_t;
        start = new Circle(cubic.getStartX(), cubic.getStartY(),8, Color.WHITE);
        start.setStrokeWidth(5);
        end = new Circle(cubic.getEndX(), cubic.getEndY(),8,Color.WHITE);
        end.setStrokeWidth(5);
        control1 = new Circle(cubic.getControlX1(), cubic.getControlY1(),5, Color.BLACK);
        control1.setStrokeWidth(3);
        setDragListeners(control1, 1);
        control2 = new Circle(cubic.getControlX2(), cubic.getControlY2(),5, Color.BLACK);
        control2.setStrokeWidth(3);
        setDragListeners(control2, 2);

        setDragListeners(start, 3);
        setDragListeners(end, 4);

        sc = new Line(cubic.getControlX1(),cubic.getControlY1(),cubic.getStartX(),cubic.getStartY());
        ec = new Line(cubic.getControlX2(),cubic.getControlY2(),cubic.getEndX(),cubic.getEndY());
        sc.setStroke(Color.GREY);
        sc.setStrokeWidth(0.5);
        ec.setStroke(Color.GREY);
        ec.setStrokeWidth(0.5);
        control1.setStroke(Color.GREEN);
        control2.setStroke(Color.GREEN);

        if(s_t == 0 ) {
            start.setStroke(Color.BLUE);
        } else if(s_t == 1) {
            start.setStroke(Color.PURPLE);
            control1.setStroke(null);
            control1.setFill(null);
        } else if(s_t == -1) {
            start.setStroke(Color.BLUE);
        } else if(s_t == -2) {
            control1.setStroke(null);
            control1.setFill(null);
            start.setStroke(Color.PURPLE);
        }

        if(e_t == 0 ) {
            end.setStroke(Color.BLUE);
        } else if(e_t == 1) {
            control2.setStroke(null);
            control2.setFill(null);
            end.setStroke(Color.PURPLE);
        } else if(e_t == -1) {
            control2.setStroke(null);
            control2.setFill(null);
            end.setStroke(Color.PURPLE);
        } else if(e_t == -2) {
            end.setStroke(Color.BLUE);
        }
        start.centerXProperty().bindBidirectional(cubic.startXProperty());
        start.centerXProperty().bindBidirectional(sc.endXProperty());
        start.centerYProperty().bindBidirectional(sc.endYProperty());
        start.centerYProperty().bindBidirectional(cubic.startYProperty());

        end.centerXProperty().bindBidirectional(cubic.endXProperty());
        end.centerYProperty().bindBidirectional(cubic.endYProperty());
        end.centerXProperty().bindBidirectional(ec.endXProperty());
        end.centerYProperty().bindBidirectional(ec.endYProperty());

        control1.centerXProperty().bindBidirectional(sc.startXProperty());
        control1.centerXProperty().bindBidirectional(cubic.controlX1Property());
        control1.centerYProperty().bindBidirectional(sc.startYProperty());
        control1.centerYProperty().bindBidirectional(cubic.controlY1Property());
        control2.centerXProperty().bindBidirectional(ec.startXProperty());
        control2.centerXProperty().bindBidirectional(cubic.controlX2Property());
        control2.centerYProperty().bindBidirectional(ec.startYProperty());
        control2.centerYProperty().bindBidirectional(cubic.controlY2Property());

    }

    void push_to(Pane canvas) {
        canvas.getChildren().add(sc);
        canvas.getChildren().add(ec);
        canvas.getChildren().add(start);
        canvas.getChildren().add(end);
        canvas.getChildren().add(control1);
        canvas.getChildren().add(control2);
    }

    public void setDragListeners(final Circle block, int i) {
        final Delta dragDelta = new Delta();
        final Delta dragDelta2 = new Delta();
        final Delta dragDelta3 = new Delta();

        block.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent mouseEvent) {
                // record a delta distance for the drag and drop operation.
                if(v.mode != "Point") {
                    dragDelta.x = block.getCenterX() - mouseEvent.getSceneX();
                    dragDelta.y = block.getCenterY() - mouseEvent.getSceneY();
                    block.setCursor(Cursor.NONE);
                    if(i == 3) {
                        dragDelta2.x = control1.getCenterX() - mouseEvent.getSceneX();
                        dragDelta2.y = control1.getCenterY() - mouseEvent.getSceneY();
                        if(is_pre) {
                            dragDelta3.x = pre_control.getCenterX() - mouseEvent.getSceneX();
                            dragDelta3.y = pre_control.getCenterY() - mouseEvent.getSceneY();
                        }
                    } else if (i ==4) {
                        dragDelta2.x = control2.getCenterX() - mouseEvent.getSceneX();
                        dragDelta2.y = control2.getCenterY() - mouseEvent.getSceneY();
                        if(is_post) {
                            dragDelta3.x = post_control.getCenterX() - mouseEvent.getSceneX();
                            dragDelta3.y = post_control.getCenterY() - mouseEvent.getSceneY();
                        }
                    }
                }
            }
        });
        block.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent mouseEvent) {
                if(v.mode != "Point") {
                    block.setCursor(Cursor.HAND);
                }
            }
        });
        block.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent mouseEvent) {
                if(v.mode != "Point") {
                    v.saved = false;
                    v.promoted_new = false;
                    v.promoted_load = false;
                    v.promoted = false;
                    block.setCenterX(mouseEvent.getSceneX() + dragDelta.x);
                    block.setCenterY(mouseEvent.getSceneY() + dragDelta.y);
                    if ((i == 1) && is_pre && s_type == 0) {
                        double a = start.getCenterX();
                        double b = start.getCenterY();
                        double x = block.getCenterX();
                        double y = block.getCenterY();
                        double k = Math.sqrt((x - a) * (x - a) + (y - b) * (y - b));
                        double xp = pre_control.getCenterX();
                        double yp = pre_control.getCenterY();
                        double r = Math.sqrt((xp - a) * (xp - a) + (yp - b) * (yp - b));
                        pre_control.setCenterX((k * a + r * a - r * x) / k);
                        pre_control.setCenterY((k * b + r * b - r * y) / k);
                    } else if (i == 2 && is_post && e_type == 0) {
                        double a = end.getCenterX();
                        double b = end.getCenterY();
                        double x = block.getCenterX();
                        double y = block.getCenterY();
                        double k = Math.sqrt((x - a) * (x - a) + (y - b) * (y - b));
                        double xp = post_control.getCenterX();
                        double yp = post_control.getCenterY();
                        double r = Math.sqrt((xp - a) * (xp - a) + (yp - b) * (yp - b));
                        post_control.setCenterX((k * a + r * a - r * x) / k);
                        post_control.setCenterY((k * b + r * b - r * y) / k);
                    } else if (i == 3) {
                        control1.setCenterX(mouseEvent.getSceneX() + dragDelta2.x);
                        control1.setCenterY(mouseEvent.getSceneY() + dragDelta2.y);
                        if(is_pre) {
                            pre_control.setCenterY(mouseEvent.getSceneY() + dragDelta3.y);
                            pre_control.setCenterX(mouseEvent.getSceneX() + dragDelta3.x);
                        }
                    } else if (i == 4) {
                        control2.setCenterX(mouseEvent.getSceneX() + dragDelta2.x);
                        control2.setCenterY(mouseEvent.getSceneY() + dragDelta2.y);
                        if(is_post) {
                            post_control.setCenterY(mouseEvent.getSceneY() + dragDelta3.y);
                            post_control.setCenterX(mouseEvent.getSceneX() + dragDelta3.x);
                        }
                    }
                    v.update();
                }
            }
        });
    }

    class Delta { double x, y; }

    void add_pre(Circle_Class c) {
        if(s_type < 0) {
        } else {
            pre_control = c.control2;
            is_pre = true;
        }
    }
    void add_post(Circle_Class c) {
        if(e_type < 0) {
        } else {
            post_control = c.control1;
            is_post = true;
        }
    }
    void change_etype() {
        if(e_type == 1) {
            e_type = 0;
            control2.setCenterX(end.getCenterX()-20);
            control2.setCenterY(end.getCenterY()-50);
            control2.setFill(Color.BLACK);
            control2.setStroke(Color.GREEN);
            end.setStroke(Color.BLUE);
        } else if(e_type == 0) {
            control2.setCenterX(end.getCenterX());
            control2.setFill(null);
            control2.setStroke(null);
            control2.setCenterY(end.getCenterY());
            end.setStroke(Color.PURPLE);
            e_type = 1;
        } else if(e_type == -2) { //smooth
            control2.setCenterX(end.getCenterX());
            control2.setFill(null);
            control2.setStroke(null);
            control2.setCenterY(end.getCenterY());
            end.setStroke(Color.PURPLE);
            e_type = -1;
        } else if(e_type == -1) {
            e_type = -2;
            control2.setCenterX(end.getCenterX()-20);
            control2.setCenterY(end.getCenterY()-50);
            control2.setFill(Color.BLACK);
            control2.setStroke(Color.GREEN);
            end.setStroke(Color.BLUE);
        }
    }
    void change_stype() {
        if(s_type == 1) {
            s_type = 0;
            control1.setCenterX(start.getCenterX()+20);
            control1.setCenterY(start.getCenterY()+50);
            control1.setFill(Color.BLACK);
            control1.setStroke(Color.GREEN);
            start.setStroke(Color.BLUE);
        } else if(s_type == 0) {
            control1.setCenterX(start.getCenterX());
            control1.setFill(null);
            control1.setStroke(null);
            control1.setCenterY(start.getCenterY());
            start.setStroke(Color.PURPLE);
            s_type = 1;
        } else if(s_type == -1) { //smooth
            control1.setCenterX(start.getCenterX());
            control1.setFill(null);
            control1.setStroke(null);
            control1.setCenterY(start.getCenterY());
            start.setStroke(Color.PURPLE);
            s_type = -2;
        } else if(s_type == -2) {
            control1.setCenterX(start.getCenterX()+20);
            control1.setCenterY(start.getCenterY()+50);
            control1.setFill(Color.BLACK);
            control1.setStroke(Color.GREEN);
            start.setStroke(Color.BLUE);
            s_type = -1;
        }
    }
}
