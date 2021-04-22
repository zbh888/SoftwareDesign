package ca.uwaterloo.cs349;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HomeFragment extends Fragment {

    private SharedViewModel mViewModel;
    View root;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_home, container, false);
        Button button = (Button) root.findViewById(R.id.button11);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                CalculateThree();
            }
        });
        mViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        mViewModel.LOAD(requireActivity());
        mViewModel.getData().observe(getViewLifecycleOwner(), new Observer<Data>() {
            @Override
            public void onChanged(@Nullable Data d) {
                ;
            }
        });
        return root;
    }

    public void CalculateThree() {
        float[] res = {0, 0, 0, -Float.MAX_VALUE, -Float.MAX_VALUE, -Float.MAX_VALUE}; // id, score
        Draw pageView = (Draw) root.findViewById(R.id.drawinMain);
        if (pageView.canSave) {
            ArrayList<Float> x_process = pageView.x_process;
            ArrayList<Float> y_process = pageView.y_process;

            float[] x_array = new float[x_process.size()];
            float[] y_array = new float[y_process.size()];
            int index = 0;
            for (final Float value : x_process) {
                x_array[index++] = value;
            }
            index = 0;
            for (final Float value : y_process) {
                y_array[index++] = value;
            }
            Gesture g_draw = process(new Gesture(x_array,y_array));

            Data d = mViewModel.getData().getValue();

            Map<Integer, Float> map=new HashMap<Integer, Float>(); // id -> score
            for(int i =0; i < d.ids.size(); i++) { //fill in the map
                Gesture g = d.gestures.get(i);
                float[] x_arr = new float[g.x.length];
                float[] y_arr = new float[g.y.length];
                int ind = 0;
                for (final Float value : g.x) {
                    x_arr[ind++] = value;
                }
                ind = 0;
                for (final Float value : g.y) {
                    y_arr[ind++] = value;
                }
                float result=ComputeScore(g_draw,process(new Gesture(x_arr,y_arr)));
                map.put(d.ids.get(i), result);
            }

            int id = 0;
            float score = -Float.MAX_VALUE;
            for(Integer num : map.keySet()) {
                float tempScore = map.get(num);
                if(tempScore >= score) {
                    score = tempScore;
                    id = num;
                }
            }
            res[0] = id;
            res[3] = score;
            id = 0;
            score = -Float.MAX_VALUE;
            for(Integer num : map.keySet()) {
                if(num != (int) res[0]) {
                    float tempScore = map.get(num);
                    if (tempScore >= score) {
                        score = tempScore;
                        id = num;
                    }
                }
            }
            res[1] = id;
            res[4] = score;
            id = 0;
            score = -Float.MAX_VALUE;
            for(Integer num : map.keySet()) {
                if(num != (int) res[0] && num!= (int) res[1]) {
                    float tempScore = map.get(num);
                    if (tempScore >= score) {
                        score = tempScore;
                        id = num;
                    }
                }
            }
            res[2] = id;
            res[5] = score;

            Show show1 = (Show) root.findViewById(R.id.imageView);
            Show show2 = (Show) root.findViewById(R.id.imageView2);
            Show show3 = (Show) root.findViewById(R.id.imageView3);

            TextView a = (TextView) root.findViewById(R.id.textView);
            TextView b = (TextView) root.findViewById(R.id.textView2);
            TextView c = (TextView) root.findViewById(R.id.textView3);

            String s = "";
            if(res[3] != -Float.MAX_VALUE) {
                for(int i =0; i < d.ids.size(); i++) {
                    if(d.ids.get(i) == (int) res[0]) {
                        s = "1st \""+ d.names.get(i) + "\" :  " + String.valueOf((int)-res[3]);
                        show1.process(d.gestures.get(i));
                        break;
                    }
                }
                a.setText(s);
            }
            if(res[4] != -Float.MAX_VALUE) {
                for(int i =0; i < d.ids.size(); i++) {
                    if(d.ids.get(i) == (int) res[1]) {
                        s = "2nd \""+d.names.get(i) + "\" :  " + String.valueOf((int)-res[4]);
                        show2.process(d.gestures.get(i));
                    }
                }
                b.setText(s);
            }
            if(res[5] != -Float.MAX_VALUE) {
                for(int i =0; i < d.ids.size(); i++) {
                    if(d.ids.get(i) == (int) res[2]) {
                        s = "3nd \""+d.names.get(i) + "\" :  " + String.valueOf((int)-res[5]);
                        show3.process(d.gestures.get(i));
                    }
                }
                c.setText(s);
            }
        } else {
            new AlertDialog.Builder(requireActivity())
                    .setTitle("Can't Analyze")
                    .setMessage("Hi, just to remind you we don't analyze empty drawing!")
                    .setCancelable(false)
                    .setPositiveButton("Fine", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    }).show();
        }
    }

    public float ComputeScore(Gesture fit, Gesture g_from_lib) {
        // Both should be well done
        int length = Math.min(fit.x.length, g_from_lib.x.length) - 1;
        float score = 0f;
        for(int i = 0; i<length; i++) {
            score += distance(fit.x[i],fit.y[i], g_from_lib.x[i], g_from_lib.y[i]);
        }
        return -score / length;
    }

    public Gesture process(Gesture g) { //rotation and scale
        float cen_x = g.x[g.x.length-1];
        float cen_y = g.y[g.y.length-1];
        for(int i = 0; i < g.x.length; i++) {
            g.x[i] = g.x[i] - cen_x;
            g.y[i] = g.y[i] - cen_y;
        }
        if (!(g.x[0] == 0 && g.y[0]==0)) {
            float cos = g.x[0] / distance(g.x[0], g.y[0], 0, 0);
            float sin = -g.y[0] / distance(g.x[0], g.y[0], 0, 0);
            for (int i = 0; i < g.x.length - 1; i++) {
                float t1 = g.x[i];
                float t2 = g.y[i];
                g.x[i] = t1 * cos - t2 * sin;
                g.y[i] = t1 * sin + t2 * cos;
            }
        }
        float up = g.y[0];
        float down = g.y[0];
        float left = g.x[0];
        float right = g.x[0];
        for(int i = 0; i< g.x.length-1; i++) {
            up = Math.max(up, g.y[i]);
            down = Math.min(down, g.y[i]);
            left = Math.min(left, g.x[i]);
            right = Math.max(right, g.x[i]);
        }
        float max = Math.max(Math.abs(up), Math.max(Math.abs(down), Math.max( Math.abs(left), Math.abs(right))));
        float factor = 50/max;
        for(int i = 0; i < g.x.length; i++) {
            g.x[i] = g.x[i]*factor + 50;
            g.y[i] = g.y[i]*factor + 50;
        }
        return g;
    }

    float distance(float x1, float y1, float x2, float y2) {
        return (float) Math.sqrt((y2 - y1) * (y2 - y1) + (x2 - x1) * (x2 - x1));
    }


}