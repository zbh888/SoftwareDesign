package ca.uwaterloo.cs349;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

public class LibraryFragment extends Fragment {

    private SharedViewModel mViewModel;
    LinearLayout lib;
    View root;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        mViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        mViewModel.LOAD(requireActivity());
        // locate the button
        root = inflater.inflate(R.layout.fragment_library, container, false);
        Button button = (Button) root.findViewById(R.id.button10);
        lib = (LinearLayout) root.findViewById(R.id.LibOfGes);

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AdditionActivity.class);
                intent.putStringArrayListExtra("names", mViewModel.getData().getValue().getDataNames());
                intent.putExtra("version", 1); // adding
                startActivityForResult(intent, 1);
            }
        });
        mViewModel.getData().observe(getViewLifecycleOwner(), new Observer<Data>() {
            @Override
            public void onChanged(@Nullable Data d) {
                ;
            }
        });

        // Loading
        Data d = mViewModel.getData().getValue();
        for(int i = 0; i< d.ids.size(); i++) {
            Addline(d.ids.get(i), d.names.get(i), d.gestures.get(i));
        }

        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                float[] x = data.getFloatArrayExtra("x");
                float[] y = data.getFloatArrayExtra("y");
                String n = data.getStringExtra("returnname");
                //receive the result and update model
                Gesture g = new Gesture(x, y);
                int id = mViewModel.getID();
                mViewModel.Addgesture(g, n);
                mViewModel.SAVE(requireActivity());
                Addline(id,n, g);
            }
        } else if (requestCode == 2) { //modify
            if (resultCode == Activity.RESULT_OK) {
                float[] x = data.getFloatArrayExtra("x");
                float[] y = data.getFloatArrayExtra("y");
                String n = data.getStringExtra("returnname");
                //receive the result and update model
                Gesture g = new Gesture(x, y);
                int id = data.getIntExtra("returnversion", -1);
                if (id < 0) {
                    Log.d("Unexpected", "Wrong");
                }
                TextView t = (TextView) root.findViewById(id + 1200000);
                Show s = (Show) root.findViewById(id + 1600000);
                t.setText(n);
                mViewModel.getData().getValue().modify(id, g, n);
                mViewModel.SAVE(requireActivity());
                s.process(g);
            }
        }
    }
    public void Addline(int id, String name, Gesture g) {
        LinearLayout line = new LinearLayout(getContext());
        line.setOrientation(LinearLayout.HORIZONTAL);
        line.setId(id);

        // Button
        Button deletee = new Button(getContext());
        deletee.setLayoutParams(new ViewGroup.LayoutParams(235, ViewGroup.LayoutParams.FILL_PARENT));
        deletee.setText("Delete");
        deletee.setId(400000 + id);
        deletee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button b = (Button) v;
                int buttonID = b.getId();
                int LineID = buttonID - 400000;
                LinearLayout l = (LinearLayout) root.findViewById(LineID);
                lib.removeView(l);
                mViewModel.Remove(LineID);
                mViewModel.SAVE(requireActivity());
            }
        });

        Button modify = new Button(getContext());
        modify.setLayoutParams(new ViewGroup.LayoutParams(235, ViewGroup.LayoutParams.FILL_PARENT));
        modify.setText("modify");
        modify.setId(800000 + id);
        modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button b = (Button) v;
                int buttonID = b.getId();
                int LineID = buttonID - 800000;
                Intent intent = new Intent(getActivity(), AdditionActivity.class);
                intent.putStringArrayListExtra("names", mViewModel.getData().getValue().getDataNames());
                intent.putExtra("originalname", mViewModel.getData().getValue().getIDName(LineID));
                intent.putExtra("version", LineID); // modify
                startActivityForResult(intent, 2); //modifying
            }
        });

        TextView NameView = new TextView(getContext());
        NameView.setId(1200000 + id);
        NameView.setText(name);
        NameView.setLayoutParams(new ViewGroup.LayoutParams(250, ViewGroup.LayoutParams.FILL_PARENT));
        NameView.setGravity(Gravity.CENTER);

        Show img = new Show(getContext());
        img.setId(1600000 + id);

        Drawable transparentDrawable = new ColorDrawable(Color.WHITE);
        img.setBackground(transparentDrawable);
        img.setLayoutParams(new LinearLayout.LayoutParams(300, 300));

        img.process(g);

        line.addView(img);

        line.setGravity(Gravity.LEFT);

        line.addView(NameView);
        line.addView(deletee);
        line.addView(modify);
        lib.addView(line);
    }
}