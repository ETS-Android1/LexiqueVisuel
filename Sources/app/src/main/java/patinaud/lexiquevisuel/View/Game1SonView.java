package patinaud.lexiquevisuel.View;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import patinaud.lexiquevisuel.R;
import patinaud.lexiquevisuel.Utils.Properties;
import patinaud.lexiquevisuel.Utils.Tools;
import patinaud.lexiquevisuel.ViewElement.ButtonImage;
import patinaud.lexiquevisuel.ViewElement.ButtonSound;
import patinaud.lexiquevisuel.ViewElement.Header;
import patinaud.lexiquevisuel.ViewElement.ImageSwitchMelt;
import patinaud.lexiquevisuel.ViewElement.WinLooseAnimation;

public class Game1SonView extends View implements View.OnTouchListener  {

    private Header header;

    private String motADeviner = "";
    private ButtonSound btsonADeviner = null;
    private int idBonBouton;
    private ArrayList<String> lstBtnStr = new ArrayList<String> ();
    private ArrayList<ImageSwitchMelt> lstBtnImage = new ArrayList<ImageSwitchMelt>();
    private Integer btnImgSelected;
    private int marge = 10;

    private ButtonImage btnValidate = null;
    private WinLooseAnimation winLoose = null;

    public Game1SonView(Context context) {
        super(context);
        init();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if ( event.getAction() == MotionEvent.ACTION_DOWN) {
            if (header.isClicked(event.getX(), event.getY())) {
                //Do nothing
            }


            if ( winLoose != null)
            {
                winLoose.isClicked( event.getX() , event.getY());
            }

            // Test les boutons normaux uniquement quand l'animation n'est pas affichée
            if ( winLoose == null ) {
                if (btsonADeviner.isClicked(event.getX(), event.getY())) {
                    // Do nothing
                }
                //test si l'une des réponses est séléctionnée
                for (int i = 0; i < lstBtnImage.size(); i++) {
                    if (lstBtnImage.get(i).isClicked(event.getX(), event.getY())) {
                        btnImgSelected = i;
                    }
                }

                //appuis sur le bouton valider
                if (btnValidate.isClicked(event.getX(), event.getY())) {
                    if (btnImgSelected != null) {
                        if (btnImgSelected == idBonBouton) {
                            Log.e("Game1ImageView", "Bonne réponse");
                            winLoose = new WinLooseAnimation(true, this.getContext(), motADeviner);
                        } else {
                            Log.e("Game1ImageView", "Mauvaise réponse");
                            winLoose = new WinLooseAnimation(false, this.getContext(), motADeviner);
                        }

                        btnImgSelected = null; //desactive le boutton de validation
                    }
                }
            }
        }
        return true;
    }

    public void clickAction(MotionEvent event) {   }



    //Permet de gérer les scroll vers le bas
    public void moveAction(MotionEvent event)
    {
    }



    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }


    public void init()
    {
        //Recupère un mot aléatoirement
        motADeviner = Tools.getRandomWord(this.getContext());

        //Effecte un id de bouton aléatoire
        idBonBouton = (int)(Math.random() * 4);

        //reinitialise l'animation de gain
        winLoose = null;

        btsonADeviner = null;
        btnValidate = null;


        //Récupère aléatoirement trois autres mots
        lstBtnStr = new ArrayList<String> ();
        lstBtnImage = new ArrayList<ImageSwitchMelt>();
        for (int i = 0 ; i < 4; i++)
        {
            if ( i == idBonBouton)
            {
                lstBtnStr.add(motADeviner);
                Log.e("Game1ImageView", "Mot juste : " + motADeviner);
            }
            else
            {
                //Récupère un autre mot aléatoirement sauf ceux déjà renseignés
                ArrayList<String> listMotDejaRecuperés = new ArrayList<String>(lstBtnStr);
                listMotDejaRecuperés.add(motADeviner);
                String motFaux = Tools.getRandomWordExcept(listMotDejaRecuperés, false, this.getContext());
                lstBtnStr.add(motFaux);
                Log.e("Game1ImageView", "Mot faux : " + motFaux);
            }
        }
    }


    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //dessine le fond en blanc
        Paint p = new Paint();
        p.setColor(Color.WHITE);
        canvas.drawPaint(p);

        //dessine le header
        if ( header == null)
        {
            header  = new Header(canvas, this, "Je devine le mot", true);
        }
        header.drawOn(canvas);

        // Affichage de l'animation win loose
        if ( winLoose != null)
        {
            winLoose.drawOn(canvas, this);
            if ( winLoose.finished()) {
                winLoose = null;
                init(); // initialise un nouveau mot
            }
            return ;
        }


        //Gère le sens d'affiche
        if ( this.getHeight() > this.getWidth())
        {
            displayVertical(canvas);
        }
        else
        {
            displayHorizontal( canvas);
        }
    }



    private void displayHorizontal(Canvas canvas)
    {
        // Recupère la hauteur du bouton valider
        int hautBtnValidate = canvas.getHeight() / 5;
        int hautZoneImages = this.getHeight() - Properties.getHautHeader() - hautBtnValidate - 20;
        int largZoneImage = this.getWidth() / 2;

        //____ Chargement en mémoire des images ____

        if ( btsonADeviner == null) {
            int magreBtnSound = 50;
            btsonADeviner = new ButtonSound(motADeviner, magreBtnSound, Properties.getHautHeader(), this.getWidth() / 2 - magreBtnSound * 2, this.getHeight() - Properties.getHautHeader(), canvas, this);
            btsonADeviner.play(); //prononce le mot à l'ouverture de la page
        }


        if ( lstBtnImage.isEmpty() ) {

            for (int i = 0 ; i < 4 ; i++) {
                lstBtnImage.add(new ImageSwitchMelt(this));
                String[] listImages = (new File(Properties.getImageMotDirrectory(lstBtnStr.get(i),this.getContext()))).list();
                Arrays.sort(listImages);//trie par ordre alphabétique

                int posX = ( i == 0 || i == 2) ? this.getWidth() / 2 + marge : this.getWidth() / 4 * 3 + marge ;
                int posY = ( i == 0 || i == 1) ? Properties.getHautHeader() + marge : Properties.getHautHeader() + (this.getHeight() - Properties.getHautHeader() - hautBtnValidate ) / 2 + marge;

                lstBtnImage.get(i).load(lstBtnStr.get(i), listImages, posX, posY , this.getWidth() / 4 - 2 * marge , hautZoneImages / 2 - 3 * marge , canvas, this.getContext());
            }
        }


        if ( btnValidate == null)
        {
            btnValidate = new ButtonImage(R.drawable.btn_envoi, canvas.getWidth() / 2, canvas.getHeight() -  hautBtnValidate, canvas.getWidth() / 2 , hautBtnValidate, canvas, this );
            btnValidate.setAlpha(125);
        }



        //____ Affichage des images ____
        btsonADeviner.drawOn(canvas);


        if ( btnImgSelected != null)
        {
            Paint strokePaint = new Paint();
            strokePaint.setColor(Color.rgb(125,125,125));
            strokePaint.setStyle(Paint.Style.STROKE);
            strokePaint.setStrokeWidth(10);

            int posX = ( btnImgSelected == 0 || btnImgSelected == 2) ? this.getWidth() / 2 : this.getWidth() / 4 * 3;
            int posY = ( btnImgSelected == 0 || btnImgSelected == 1) ?  Properties.getHautHeader() :  Properties.getHautHeader() + hautZoneImages / 2 ;

            canvas.drawRect(posX, posY, posX + this.getWidth() / 4  , posY + hautZoneImages / 2, strokePaint);
        }



        for (int i = 0 ; i < 4 ; i++) {
            lstBtnImage.get(i).draw(canvas);
        }


        if ( btnImgSelected != null)
        {
            btnValidate.setAlpha(255);;
        }

        btnValidate.drawOn(canvas);

        //termine par dessiner le header
        if ( header == null)
        {
            header  = new Header(canvas, this, "Je devine le mot", true);
        }

        header.drawOn(canvas);
    }






    private void displayVertical(Canvas canvas)
    {

        // Recupère la hauteur du bouton valider
        int hautBtnValidate = canvas.getHeight() / 10;
        int hautZoneImages = (this.getHeight()/ 2) - hautBtnValidate - 20;




        //____ Chargement en mémoire des images ____

        if ( btsonADeviner == null) {
            btsonADeviner = new ButtonSound(motADeviner, 50, Properties.getHautHeader(), this.getWidth() - 100 , this.getHeight() / 2 - Properties.getHautHeader(), canvas, this);
            btsonADeviner.play(); //prononce le mot à l'ouverture de la page
        }

        if ( lstBtnImage.isEmpty() ) {

            for (int i = 0 ; i < 4 ; i++) {
                lstBtnImage.add(new ImageSwitchMelt(this));
                String[] listImages = (new File(Properties.getImageMotDirrectory(lstBtnStr.get(i) , this.getContext()))).list();
                Arrays.sort(listImages);//trie par ordre alphabétique

                int posX = ( i == 0 || i == 2) ? marge : this.getWidth() / 2 + marge ;
                int posY = ( i == 0 || i == 1) ? (this.getHeight()/ 2) + marge : (this.getHeight()/ 2) + hautZoneImages / 2 + marge ;

                lstBtnImage.get(i).load(lstBtnStr.get(i), listImages, posX, posY , this.getWidth() / 2 - 2 * marge , hautZoneImages / 2 - 2 * marge , canvas , this.getContext());
            }
        }


        if ( btnValidate == null)
        {
            btnValidate = new ButtonImage(R.drawable.btn_envoi, 0, canvas.getHeight() -  hautBtnValidate, canvas.getWidth() , hautBtnValidate, canvas, this );
            btnValidate.setAlpha(125);
        }




        //____ Affichage des images ____
        btsonADeviner.drawOn(canvas);

        if ( btnImgSelected != null)
        {
            Paint strokePaint = new Paint();
            strokePaint.setColor(Color.rgb(125,125,125));
            strokePaint.setStyle(Paint.Style.STROKE);
            strokePaint.setStrokeWidth(10);

            int posX = ( btnImgSelected == 0 || btnImgSelected == 2) ? 0 : this.getWidth() / 2;
            int posY = ( btnImgSelected == 0 || btnImgSelected == 1) ? this.getHeight() / 2 : this.getHeight() / 2 + hautZoneImages / 2 ;

            canvas.drawRect(posX, posY, posX + this.getWidth() / 2  , posY + + hautZoneImages / 2, strokePaint);
        }


        for (int i = 0 ; i < 4 ; i++) {
            lstBtnImage.get(i).draw(canvas);
        }


        if ( btnImgSelected != null)
        {
            btnValidate.setAlpha(255);;
        }

        //dessine le bouton valider
        btnValidate.drawOn(canvas);



        //termine par dessiner le header
        if ( header == null)
        {
            header  = new Header(canvas, this, "Jeu devine le mot", true);
        }

        header.drawOn(canvas);
    }


}
