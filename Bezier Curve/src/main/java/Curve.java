import javafx.scene.paint.Color;
import javafx.scene.shape.CubicCurve;
import java.util.ArrayList;

public class Curve {
    ArrayList<CubicCurve> list_curves = new ArrayList<>();
    ArrayList<Integer> list_point = new ArrayList<>();
    int size = 0;
    String style;
    int width;
    Color color;

    int s_type = -1;
    int e_type = -2;

    public void change_stype() {
        if(s_type == -1) {
            s_type = -2;
        } else if(s_type == -2) {
            s_type = -1;
        }
    }

    public void change_etype() {
        if(e_type == -1) {
            e_type = -2;
        } else if(e_type == -2) {
            e_type = -1;
        }
    }
    public void setPara(String s, int w, Color c) {
        this.style = s;
        this.width = w;
        this.color = c;
    }
    public Curve copy() {
        Curve c = new Curve();
        c.list_curves = this.getCuve_segs();
        c.size = this.size;
        c.width = this.width;
        c.color = this.color;
        c.style = this.style;
        c.s_type = this.s_type;
        c.e_type = this.e_type;
        for(int i = 0; i< this.size -1; i++) {
            c.list_point.add(this.list_point.get(i));
        }
        return  c;
    }
    public ArrayList<CubicCurve> getCuve_segs() {
        ArrayList<CubicCurve> new_cuves = new ArrayList<>();
        for(CubicCurve i : list_curves) {
            CubicCurve cubic = new CubicCurve();
            cubic.setStartX(i.getStartX());
            cubic.setStartY(i.getStartY());
            cubic.setControlX1(i.getControlX1());
            cubic.setControlY1(i.getControlY1());
            cubic.setControlX2(i.getControlX2());
            cubic.setControlY2(i.getControlY2());
            cubic.setEndX(i.getEndX());
            cubic.setEndY(i.getEndY());
            new_cuves.add(cubic);
        }
        return  new_cuves;
    }

    public void addCurve(int a, int b, int c, int d) {
        CubicCurve cubic = new CubicCurve();
        cubic.setStartX(a);
        cubic.setStartY(b);
        cubic.setControlX1(a+30);
        cubic.setControlY1(b+40);
        cubic.setControlX2(c-30);
        cubic.setControlY2(d-40);
        cubic.setEndX(c);
        cubic.setEndY(d);
        list_curves.add(cubic);
        size += 1;
        if(size > 1) {
        list_point.add(0);
        }
    }
    public void change_type(int i) {
        if(list_point.get(i)==1) {
            list_point.set(i, 0);
        } else {
            list_point.set(i, 1);
        }
    }
    public String save() {
        String s = String.valueOf(size);

        s = s + " " + width + " ";
        s = s + color.getBlue() + " " + color.getGreen()+ " " + color.getRed()+ " " +  color.getOpacity() + " ";
        s = s + style + " ";
        for(int i=0; i< size - 1; i++) {
            s = s + list_point.get(i) + " ";
        }
        for(int i=0; i< size ; i++) {
            s = s + list_curves.get(i).getStartX() + " ";
            s = s + list_curves.get(i).getStartY() + " ";
            s = s + list_curves.get(i).getEndX() + " ";
            s = s + list_curves.get(i).getEndY() + " ";
            s = s + list_curves.get(i).getControlX1() + " ";
            s = s + list_curves.get(i).getControlY1() + " ";
            s = s + list_curves.get(i).getControlX2() + " ";
            s = s + list_curves.get(i).getControlY2() + " ";
        }
        s = s + s_type + " " + e_type;
        s = s + "\n";
        return s;
    }
}
