package patinaud.lexiquevisuel;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import patinaud.lexiquevisuel.View.Game1ImageView;
import patinaud.lexiquevisuel.View.MenuView;

public class Game1ImageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Affiche l'IHM
        super.onCreate(savedInstanceState);
        Game1ImageView IHM = new Game1ImageView(this );
        setContentView(IHM);
    }

}
