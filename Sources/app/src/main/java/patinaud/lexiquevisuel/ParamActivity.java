package patinaud.lexiquevisuel;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import patinaud.lexiquevisuel.View.MenuJeuxView;
import patinaud.lexiquevisuel.View.MotView;
import patinaud.lexiquevisuel.View.ParamView;

public class ParamActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ParamView IHM = new ParamView(this);
        setContentView(IHM);
    }
}
