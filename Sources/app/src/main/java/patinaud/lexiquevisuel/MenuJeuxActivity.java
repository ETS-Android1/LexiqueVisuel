package patinaud.lexiquevisuel;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import patinaud.lexiquevisuel.View.MenuJeuxView;
import patinaud.lexiquevisuel.View.MotView;

public class MenuJeuxActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MenuJeuxView IHM = new MenuJeuxView(this);
        setContentView(IHM);
    }
}
