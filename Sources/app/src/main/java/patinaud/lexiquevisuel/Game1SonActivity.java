package patinaud.lexiquevisuel;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import patinaud.lexiquevisuel.View.Game1ImageView;
import patinaud.lexiquevisuel.View.Game1SonView;

public class Game1SonActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Affiche l'IHM
        super.onCreate(savedInstanceState);
        Game1SonView IHM = new Game1SonView(this );
        setContentView(IHM);
    }

}
