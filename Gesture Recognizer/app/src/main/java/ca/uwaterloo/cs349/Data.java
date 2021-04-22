package ca.uwaterloo.cs349;

import java.util.ArrayList;

public class Data implements java.io.Serializable {
    ArrayList<Gesture> gestures = new ArrayList<>();
    ArrayList<String> names = new ArrayList<>();
    ArrayList<Integer> ids = new ArrayList<>();
    int id = 1000;

    public ArrayList<String> getDataNames() {
        return names;
    }

    public void remove(int id) {
        boolean f = false;
        int i = 0;
        while (i < ids.size()) {
            if (ids.get(i) == id) {
                f = true;
                break;
            }
            i++;
        }
        if (f) {
            gestures.remove(i);
            names.remove(i);
            ids.remove(i);
        }
    }

    public void modify(int id, Gesture g, String n) {
        boolean f = false;
        int i = 0;
        while (i < ids.size()) {
            if (ids.get(i) == id) {
                f = true;
                break;
            }
            i++;
        }
        if (f) {
            gestures.set(i, g);
            names.set(i, n);
        }
    }

    public String getIDName(int id) {
        int i = 0;
        while (i < ids.size()) {
            if (ids.get(i) == id) {
                break;
            }
            i++;
        }
        return names.get(i);
    }
}


class Gesture implements java.io.Serializable {
    float[] x;
    float[] y;

    Gesture(float[] xx, float[] yy) {
        x = xx;
        y = yy;
    }
}