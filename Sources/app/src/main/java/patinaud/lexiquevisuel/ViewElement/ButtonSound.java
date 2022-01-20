package patinaud.lexiquevisuel.ViewElement;

import android.graphics.Canvas;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.View;

import java.io.File;

import patinaud.lexiquevisuel.R;
import patinaud.lexiquevisuel.Utils.Properties;
import patinaud.lexiquevisuel.Utils.Tools;

public class ButtonSound extends ButtonImage{
    private String mot;
    private String source;

    public ButtonSound(String mot, int x, int y , int larg , int haut , Canvas canvas, View view)
    {
        super(R.drawable.sound  , x , y , larg , haut , canvas , view);
        this.mot = mot;

        //Par défaut ont prend le premier son du repertoire
        File repSon = new File(Properties.getSonMotDirrectory(mot, view.getContext()));
        source = Properties.getSonMotDirrectory(mot, view.getContext()) + repSon.list()[0];
    }


    public boolean isClicked(float x , float y)
    {
        if ( super.isClicked(x, y) )
        {
            Log.e("ButtonSound" , "Play : " + source);
            Tools.playSound(source);
            return true;
        }
        return false;
    }

    //force la lecture du son du bouton
    public void play() {
        Tools.playSound(source);
    }
}
