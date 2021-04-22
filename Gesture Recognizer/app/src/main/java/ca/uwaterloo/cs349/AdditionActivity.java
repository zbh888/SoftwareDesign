package ca.uwaterloo.cs349;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;


public class AdditionActivity extends AppCompatActivity {
    Draw pageView;
    ArrayList<String> names = new ArrayList<>();
    int version = -1;
    String modifyName = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_draw);
        Intent intent = getIntent();
        names = intent.getExtras().getStringArrayList("names");
        version = intent.getIntExtra("version", -1);
        Button b = (Button) findViewById(R.id.button);
        b.setText("Create New");
        if (version > 1) {
            modifyName = intent.getStringExtra("originalname");
            b.setText("Modify");
        }
    }

    public void Save(View view) {
        pageView = (Draw) findViewById(R.id.drawing);
        EditText nameView = findViewById(R.id.nameenter);
        String name = nameView.getText().toString();
        boolean flag = true;
        if (version > 1) {
            if (names.contains(name)) {
                if (!name.equals(modifyName)) {
                    flag = false;
                }
            }
        } else {
            flag = !names.contains(name);
        }
        if (pageView.canSave && flag && !name.equals("")) {
            Intent returnIntent = new Intent();
            float[] x = new float[pageView.x_process.size()];
            float[] y = new float[pageView.x_process.size()];
            int index = 0;
            for (final Float value : pageView.x_process) {
                x[index++] = value;
            }
            index = 0;
            for (final Float value : pageView.y_process) {
                y[index++] = value;
            }
            returnIntent.putExtra("x", x);
            returnIntent.putExtra("y", y);
            returnIntent.putExtra("returnname", name);
            returnIntent.putExtra("returnversion", version);
            setResult(Activity.RESULT_OK, returnIntent);
            finish();
        } else {
            String m = "";
            if (name.equals("")) {
                m = "name can't be empty";
            } else if (names.contains(name)) {
                m = "Duplicate name provided";
            } else if (!pageView.canSave) {
                m = "No drawing is there";
            }
            new AlertDialog.Builder(AdditionActivity.this)
                    .setTitle("Can't save")
                    .setMessage(m)
                    .setCancelable(false)
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Whatever...
                        }
                    }).show();

        }
    }

    public void Cancel(View view) {
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_CANCELED, returnIntent);
        finish();
    }


}