package ca.uwaterloo.cs349;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class SharedViewModel extends ViewModel {

    private MutableLiveData<Data> data;

    public SharedViewModel() {
        data = new MutableLiveData<>();
        data.setValue(new Data());
    }

    public LiveData<Data> getData() {
        return data;
    }

    public void Addgesture(Gesture g, String name) {
        Data d = data.getValue();
        d.gestures.add(g);
        d.names.add(name);
        d.ids.add(d.id);
        d.id += 1;
        data.setValue(d);
    }

    public void Remove(int id) {
        Data d = data.getValue();
        d.remove(id);
        data.setValue(d);
    }

    public int getID() {
        return data.getValue().id;
    }

    public void SAVE(Context context) {
        try {
            File dir = context.getFilesDir();
            File file = new File(dir, "config.txt");
            file.delete();
            FileOutputStream fos = context.openFileOutput("config.txt", Context.MODE_PRIVATE);
            ObjectOutputStream os = new ObjectOutputStream(fos);
            os.writeObject(data.getValue());
            os.close();
            fos.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    public void LOAD(Context context) {
        File dir = context.getFilesDir();
        File file = new File(dir, "config.txt");
        if(file.exists()) {
            try {
                FileInputStream fis = context.openFileInput("config.txt");
                ObjectInputStream is = new ObjectInputStream(fis);
                Data d = (Data) is.readObject();
                data.setValue(d);
                is.close();
                fis.close();
            } catch (Exception e) {
                Log.e("login activity", "Can not read file: " + e.toString());
            }
        }
    }
}