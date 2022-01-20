package patinaud.lexiquevisuel.View;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;

import java.util.ArrayList;

import patinaud.lexiquevisuel.MotActivity;
import patinaud.lexiquevisuel.Utils.Properties;
import patinaud.lexiquevisuel.Utils.Tools;
import patinaud.lexiquevisuel.ViewElement.ButtonImageFloatHeight;
import patinaud.lexiquevisuel.ViewElement.Header;

public class MotsCategView extends ParentScrollableView {

    protected Header header = null;
    private String [] listeMots = null;
    private ArrayList<ButtonImageFloatHeight> listBtnMot = new ArrayList<ButtonImageFloatHeight>(); //pour accélérer la vitesse d'affichage on initialise qu'une seulle fois la liste des BitMap (images / lecture des fichiers)
    protected String categorie = "";

    private ArrayList<String> motsAafficher;


    public MotsCategView(Activity activity, String categorie) {
        super(activity);
        this.categorie = categorie;

        //récupère la liste des mots de la catégorie dans le constructeur pour ne pas reefffectuer le calcul à chaque fois
        motsAafficher = Tools.getListeMotCategorie(categorie, this.getContext());

        //Initialise la liste des boutons à vide
        listBtnMot = new ArrayList<ButtonImageFloatHeight>();
    }




    @Override
    public void clickAction(MotionEvent event)
    {
        if ( ! header.isClicked(event.getX() , event.getY())) {
            //regarde si il s'agit d'un clique sur un boutton
            for (int iBtn = 0; iBtn < listBtnMot.size(); iBtn++) {
                if (listBtnMot.get(iBtn).isClicked(event.getX(), event.getY())) {
                    Log.e("MenuView", "Ouverture du mot : " + listBtnMot.get(iBtn).getId());

                    Intent intent = new Intent(this.activity, MotActivity.class);
                    intent.putExtra("MOT", listBtnMot.get(iBtn).getId());
                    intent.putExtra("CATEGORIE", categorie);
                    this.activity.startActivity(intent);
                }
            }
        }
    }





    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //________________________________ PARAMETRES _____________________________________
        //dessine le fond en blanc
        Paint p = new Paint();
        p.setColor(Color.WHITE);
        canvas.drawPaint(p);


        int nmbColonnes = (this.getWidth() > this.getHeight()) ? 5 : 3;
        int largeurColonne = this.getWidth() / nmbColonnes;

        //____________________________________ LECTURE FICHIERS _____________________________________________

        //Gènere la liste des boutons pour la premierre fois
        if ( listBtnMot.isEmpty())
        {
            //Affiche les mots
            for (int itMot = 0 ; itMot < motsAafficher.size() ; itMot++) {
                String mot = motsAafficher.get(itMot);

                Log.e("MotsCategView", mot);



                //vérifie que le mot soit bien formatés, que des images soient bien présentes dans le repertoire et que le son aussi
                if (Tools.motPeutEtreAffiche(mot, this.getContext())) {

                    ButtonImageFloatHeight motBtn = new ButtonImageFloatHeight(Tools.getFirstImageMot(mot, this.getContext()), 0,0, largeurColonne, canvas, mot);

                    listBtnMot.add(motBtn);

                }
            }
        }



//______________________________  DESSINE LES BOUTONS ___________________________________________________

        ArrayList<Integer> taillesColonnes = new ArrayList<Integer>();

        //initialise les tailles des colonnes à 0
        for (int itInitCol = 0; itInitCol < nmbColonnes; itInitCol++)
            taillesColonnes.add(Properties.getHautHeader());


        for (int itBtn = 0 ; itBtn < listBtnMot.size(); itBtn++)
        {
            //trouve la colonne dans la quelle ajouter l'image (la plus petite)
            int numColonne = 0;
            for (int itC = 0; itC < nmbColonnes; itC++) {
                if (taillesColonnes.get(itC) < taillesColonnes.get(numColonne)) {
                    numColonne = itC;
                }
            }


            int x = numColonne * (this.getWidth() / nmbColonnes);
            int y = taillesColonnes.get(numColonne);

            listBtnMot.get(itBtn).drawOn(canvas, x, y - offSetY);
            taillesColonnes.set(numColonne, taillesColonnes.get(numColonne) + listBtnMot.get(itBtn).getHaut()); // ajoute la taille de chaque image à la colonne assoiciée
        }



        //retrouve la taille de la colonne la plus grande
        maxHeightContent = 0;

        for (int itC = 0 ; itC < taillesColonnes.size() ; itC++)
        {
            if ( taillesColonnes.get(itC) > maxHeightContent)
            {
                maxHeightContent = taillesColonnes.get(itC);
            }
        }

        //_______________________________________ FIN  ____________________________________________________________________

        //termine par dessiner le header
        if ( header == null)
        {
            header  = new Header(canvas, this, categorie, true);
        }
        header.drawOn(canvas);
    }

}
