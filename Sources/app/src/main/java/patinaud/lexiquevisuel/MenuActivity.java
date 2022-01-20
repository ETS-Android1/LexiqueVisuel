package patinaud.lexiquevisuel;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import patinaud.lexiquevisuel.Utils.Tools;
import patinaud.lexiquevisuel.View.MenuView;


public class MenuActivity extends AppCompatActivity {

    /*

    Le fichier zip doit contenir directement les deux repertoire MOTS et CATEGORIES à sa racine
    il doit également etre généré avec Alzip pour garder les accents

     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Affiche l'IHM
        super.onCreate(savedInstanceState);
        MenuView IHM = new MenuView(this );
        setContentView(IHM);

        // pour dezipper il faut avoir les habilitations necessaires !!!
        Tools.verifyPermissions(this);

    }

}
