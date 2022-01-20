package patinaud.lexiquevisuel.View;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import patinaud.lexiquevisuel.MotActivity;
import patinaud.lexiquevisuel.Utils.Properties;
import patinaud.lexiquevisuel.Utils.Tools;
import patinaud.lexiquevisuel.ViewElement.ButtonSound;
import patinaud.lexiquevisuel.ViewElement.Header;
import patinaud.lexiquevisuel.ViewElement.ImageSwitchMelt;
import patinaud.lexiquevisuel.ViewElement.ListeImgButtonHoriz;

public class MotView extends View implements View.OnTouchListener  {

    private String mot = "";
    private String categorie = "";
    private Header header;
    private ButtonSound btnSon;
    private ListeImgButtonHoriz lstMotsLies = new ListeImgButtonHoriz();
    private Context context;
    private ImageSwitchMelt imgSwitch;




    public MotView(Context context, String categorie, String mot) {
        super(context);
        this.context =  context;
        this.mot = mot;
        this.categorie = categorie;
        imgSwitch = new ImageSwitchMelt(this);

    }




    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if ( header.isClicked(event.getX() , event.getY())) {
            //Do nothing
        }
        else  if ( btnSon.isClicked(event.getX() , event.getY()) )
        {
            //Do nothing
        }
        else if (lstMotsLies.isClicked((int)event.getX() , (int)event.getY()) != null)
        {
            String motClique = lstMotsLies.isClicked((int)event.getX() , (int)event.getY());
            Log.v("MotView" , "Mot cliqué : " + motClique);

            Intent intent = new Intent(this.context, MotActivity.class);
            intent.putExtra("MOT", motClique);
            intent.putExtra("CATEGORIE", categorie);
            this.context.startActivity(intent);
        }
        return false;
    }



    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }




    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Log.v("MotView" , "dessine l'IHM" );

        //dessine le fond en blanc
        Paint p = new Paint();
        p.setColor(Color.WHITE);
        canvas.drawPaint(p);

        //charge les fichiers d'image en mémoire
        if ( imgSwitch.getNmbImg() <= 0) {
            String [] listImages = (new File(Properties.getImageMotDirrectory(mot, this.getContext()))).list();
            Arrays.sort(listImages);//trie par ordre alphabétique

            imgSwitch.load(mot , listImages, 0, Properties.getHautHeader(), canvas.getWidth(), canvas.getHeight() / 2, canvas, this.getContext());

            //charge également les boutons en mémoire, !!!!!!!!!!!!!!! on enlève la hauteur du header au bouton du son pour gagner de la place sur les images  !!!!!!!!!!!!!!!!!!!!
            btnSon = new ButtonSound(mot,0 ,this.getHeight() / 2 + Properties.getHautHeader(), this.getWidth(), this.getHeight() / 4 - Properties.getHautHeader(), canvas, this);
            btnSon.play();
        }


        //Dessine l'image
        imgSwitch.draw(canvas);


        //Desine l'icone du son
        if (Tools.motHasASound(mot, this.getContext())) {
            btnSon.drawOn(canvas);
        }


        //Dessine les mots liés
        lstMotsLies = new ListeImgButtonHoriz();
        ArrayList<String> motsLies = Tools.getMotsLies(mot, this.getContext());

        for (int i = 0 ; i < motsLies.size(); i++)
        {
            String motLie = motsLies.get(i);
            Log.v("MotView", "Mots liés : " + motLie + " : " + Tools.getFirstImageMot(motLie, this.getContext()) + " : " + Tools.motPeutEtreAffiche(motLie, this.getContext()));
            lstMotsLies.addImg(motLie);
        }

        lstMotsLies.draw(0, this.getHeight() / 4 * 3 + this.getHeight() / 20, this.getWidth(), this.getHeight() / 10, canvas, context);

        //termine par dessiner le header
        if ( header == null)
        {
            header  = new Header(canvas, this, mot, true);
        }
        header.drawOn(canvas);

    }


}
