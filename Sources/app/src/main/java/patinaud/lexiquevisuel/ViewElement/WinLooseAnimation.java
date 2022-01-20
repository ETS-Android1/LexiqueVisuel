package patinaud.lexiquevisuel.ViewElement;

import android.content.Context;
import android.graphics.Canvas;
import android.util.Log;
import android.view.View;

import java.io.File;
import java.util.Arrays;

import patinaud.lexiquevisuel.R;
import patinaud.lexiquevisuel.Utils.Properties;
import patinaud.lexiquevisuel.Utils.Tools;

public class WinLooseAnimation {

    private boolean win;
    private ButtonImage imgBonhomme;
    private long tmpExpire;
    private int duree = 3 *1000;
    private int idImg = 0;
    private Context context;
    private String bonneReponse;
    private ButtonSound btnSound = null;
    private ButtonImage btnClose = null;
    private ImageSwitchMelt imgReponse = null;
    private Boolean finished = false;




    public WinLooseAnimation(boolean win, Context context, String bonneReponse){
        this.win = win;
        this.context = context;
        this.bonneReponse = bonneReponse;
        tmpExpire = System.currentTimeMillis() + duree;
        finished = false;

        //Par défaut ont prend le premier son du repertoire
        /*
        File repSon = new File(Properties.getSonMotDirrectory(bonneReponse));
        String sourceSon = Properties.getSonMotDirrectory(bonneReponse) + repSon.list()[0];
        Tools.playSound(sourceSon);
        */

        if (win) {
            idImg = R.drawable.content1;

            int rand = (int)(Math.random() * 4);
            if (rand == 0) idImg = R.drawable.content1;
            if (rand == 1) idImg = R.drawable.content2;
            if (rand == 2) idImg = R.drawable.content3;
            if (rand == 3) idImg = R.drawable.content4;

            Tools.playSoundWin(context);

        }
        else {
            idImg = R.drawable.triste1;

            int rand = (int)(Math.random() * 5);
            if (rand == 0) idImg = R.drawable.triste1;
            if (rand == 1) idImg = R.drawable.triste2;
            if (rand == 2) idImg = R.drawable.triste3;
            if (rand == 3) idImg = R.drawable.triste4;
            if (rand == 4) idImg = R.drawable.triste5;

            Tools.playSoundLose(context);

        }
    }


    public void isClicked(float x , float y)
    {
        if ( btnSound.isClicked(x,y))
        {
            btnSound.play();
        }
        if ( btnClose.isClicked(x,y))
        {
            finished = true;
        }
    }

    public void drawOn(Canvas canvas, View v)
    {
        // affichage dans la largeur
        if ( canvas.getWidth() > canvas.getHeight()) {

            //charge les images
            if ( imgReponse == null ) {
                int marge = 20;

                imgBonhomme = new ButtonImage(idImg, marge, Properties.getHautHeader() + marge, (canvas.getWidth() / 6 * 2)-(marge * 2), canvas.getHeight() - Properties.getHautHeader() - ( marge * 2), canvas, v);
                btnSound = new ButtonSound(bonneReponse, canvas.getWidth() / 6 * 5 ,Properties.getHautHeader(),canvas.getWidth() /6 - marge,canvas.getHeight() / 2 - Properties.getHautHeader(), canvas, v);
                btnClose = new ButtonImage(R.drawable.next , canvas.getWidth() / 6 * 5, canvas.getHeight() / 2,canvas.getWidth() / 6,canvas.getHeight() / 2 - Properties.getHautHeader(), canvas, v);

                imgReponse = new ImageSwitchMelt(v);
                String[] listImages = (new File(Properties.getImageMotDirrectory(bonneReponse,v.getContext()))).list();
                Log.e("winloose", "" + listImages.length);
                Arrays.sort(listImages); //trie par ordre alphabétique
                imgReponse.load(bonneReponse, listImages, canvas.getWidth() / 6 * 2 + marge, Properties.getHautHeader() + marge, canvas.getWidth() / 6 * 3 - 2 * marge, canvas.getHeight() - Properties.getHautHeader() - 2 * marge, canvas, v.getContext());
            }


            imgBonhomme.drawOn(canvas);
            btnSound.drawOn(canvas);
            btnClose.drawOn(canvas);
            imgReponse.draw(canvas);
        }

        //affichage dans la hauteur
        if ( canvas.getHeight() > canvas.getWidth() ) {

            //charge les images
            if ( imgReponse == null ) {
                int marge = 20;

                imgBonhomme = new ButtonImage(idImg, marge, Properties.getHautHeader() +marge, canvas.getWidth()-(marge * 2), (canvas.getHeight()  / 10 * 3) - Properties.getHautHeader() - ( marge * 2), canvas, v);
                btnSound = new ButtonSound(bonneReponse, 0,canvas.getHeight() / 10 * 9,canvas.getWidth() / 2,canvas.getHeight() / 10, canvas, v);
                btnClose = new ButtonImage(R.drawable.next , canvas.getWidth() / 2, canvas.getHeight() / 10 * 9,canvas.getWidth() / 2,canvas.getHeight() / 10, canvas, v);

                imgReponse = new ImageSwitchMelt(v);
                String[] listImages = (new File(Properties.getImageMotDirrectory(bonneReponse , v.getContext()))).list();
                Log.e("winloose", "" + listImages.length);
                Arrays.sort(listImages); //trie par ordre alphabétique
                imgReponse.load(bonneReponse, listImages, marge, (canvas.getHeight() / 10 * 3) + marge, canvas.getWidth() - (2 * marge), (canvas.getHeight() / 10 * 6) - ( 2 * marge), canvas , v.getContext());
            }


            imgBonhomme.drawOn(canvas);
            btnSound.drawOn(canvas);
            btnClose.drawOn(canvas);
            imgReponse.draw(canvas);
        }

    }

    public boolean finished()
    {
        /*
        if ( System.currentTimeMillis() > tmpExpire)
        {
            return true;
        }
        */

        return finished;
    }

}
