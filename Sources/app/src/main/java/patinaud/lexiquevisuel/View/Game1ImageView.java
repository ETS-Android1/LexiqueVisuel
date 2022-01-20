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

public class Game1ImageView extends View implements View.OnTouchListener   {

    // Le raraichissange est assuré par la classe ImageSwitchMelt

    private Header header;
    private ImageSwitchMelt imgSwitch;
    private String motADeviner = null;
    private int idBonBouton;
    private Integer btnSonSelected;
    private ArrayList<ButtonSound> lstBtn;
    private ArrayList<String> lstBtnStr;
    private int borderbtnSound = 50;
    private ButtonImage btnValidate = null;
    private WinLooseAnimation winLoose;


    public Game1ImageView(Context context) {
        super(context);
        init();
    }

    private void init()
    {
        imgSwitch = new ImageSwitchMelt(this);

        //Recupère un mot aléatoirement
        motADeviner = Tools.getRandomWord(this.getContext());

        //Effecte un id de bouton aléatoire
        idBonBouton = (int)(Math.random() * 4);

        //Alimente les bouton de son
        lstBtn = new ArrayList<ButtonSound>();
        lstBtnStr = new ArrayList<String>();

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
                String motFaux = Tools.getRandomWordExcept(listMotDejaRecuperés, false,this.getContext());
                lstBtnStr.add(motFaux);
                Log.e("Game1ImageView", "Mot faux : " + motFaux);
            }
        }

        Log.e("Game1ImageView", "Id bon bouton : " + idBonBouton);
        Log.e("Game1ImageView", "lstBtnStr : " + lstBtnStr.size());
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

                //si le bouton valider est cliqué et qu'un son est selectionné on test la validité de la réponse
                if (btnValidate.isClicked(event.getX(), event.getY()) && btnSonSelected != null) {
                    if (btnSonSelected == idBonBouton) {
                        Log.e("Game1ImageView", "Bonne réponse");
                        winLoose = new WinLooseAnimation(true, this.getContext() , motADeviner);
                    } else {
                        Log.e("Game1ImageView", "Mauvaise réponse");
                        winLoose = new WinLooseAnimation(false, this.getContext(), motADeviner);
                    }
                }


                btnSonSelected = null;

                // joue les sons si les cliques étaient sur les boutons de son
                for (int itSon = 0; itSon < lstBtn.size(); itSon++) {
                    if (lstBtn.get(itSon).isClicked(event.getX(), event.getY())) {
                        lstBtn.get(itSon).play();
                        lstBtn.get(itSon).setAlpha(255);
                        btnValidate.setAlpha(255);
                        btnSonSelected = itSon;
                    } else {
                        lstBtn.get(itSon).setAlpha(125);
                    }
                }

            }
        }
        return true;
    }

    public void clickAction(MotionEvent event) {   }




    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }



    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //dessine le fond en blanc
        Paint p = new Paint();
        p.setColor(Color.WHITE);
        canvas.drawPaint(p);

        if ( canvas.getHeight() > canvas.getWidth() )
        {
            drawVertical(canvas);
        }
        else {
            drawHorizontal(canvas);
        }

        //termine par dessiner le header
        if ( header == null)
        {
            header  = new Header(canvas, this, "Je devine le mot", true);
        }

        header.drawOn(canvas);
    }





    public void drawVertical(Canvas canvas)
    {
        int hautBtnValidate = 0;

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

        // Recupère la hauteur du bouton valider
        if ( hautBtnValidate == 0)
        {
            hautBtnValidate = canvas.getHeight() / 10;
        }

        if ( btnValidate == null)
        {
            btnValidate = new ButtonImage(R.drawable.btn_envoi, 0, canvas.getHeight() -  hautBtnValidate, canvas.getWidth() , hautBtnValidate, canvas, this );
            btnValidate.setAlpha(125);
        }

            //charge les fichiers d'image en mémoire
        if ( imgSwitch.getNmbImg() <= 0) {
            String [] listImages = (new File(Properties.getImageMotDirrectory(motADeviner ,this.getContext()))).list();
            Arrays.sort(listImages);//trie par ordre alphabétique

            imgSwitch.load(motADeviner, listImages,0, Properties.getHautHeader(), canvas.getWidth(), canvas.getHeight() / 2 - Properties.getHautHeader(), canvas, this.getContext());
        }

        //Charge les boutons de son
        if ( lstBtn == null || lstBtn.size() == 0)
        {
            lstBtn = new ArrayList<ButtonSound>();

            for (int itBtn = 0 ; itBtn < lstBtnStr.size(); itBtn++)
            {
                int hautZone = canvas.getHeight() / 2 - hautBtnValidate;
                int largZone = canvas.getWidth();

                int x = (largZone / 2 ) * (itBtn % 2) + borderbtnSound;
                int y = (canvas.getHeight() / 2) + (int)(itBtn / 2) * (hautZone / 2 ) + borderbtnSound;
                int larg = (largZone / 2 ) - (borderbtnSound * 2);
                int haut = (hautZone / 2)- (borderbtnSound * 2);

                ButtonSound bts = new ButtonSound(lstBtnStr.get(itBtn), x,y,larg,haut, canvas, this);
                lstBtn.add(bts);
                lstBtn.get( itBtn).setAlpha(125);
            }
        }

        //Dessine l'image
        imgSwitch.draw(canvas);

        //dessine le bouton valider
        btnValidate.drawOn(canvas);

        //Dessine les boutons de son
        for (int itBtn = 0 ; itBtn < lstBtn.size(); itBtn++)
        {
            lstBtn.get(itBtn).drawOn(canvas);
        }
    }







    public void drawHorizontal(Canvas canvas)
    {
        int hautBtnValidate =0;
        int largBtnValidate = 0;

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


        // Recupère la hauteur du bouton valider
        if ( hautBtnValidate == 0)
        {
            hautBtnValidate = canvas.getHeight() / 5;
            largBtnValidate = canvas.getWidth() / 2;
        }

        if ( btnValidate == null)
        {
            btnValidate = new ButtonImage(R.drawable.btn_envoi, largBtnValidate, canvas.getHeight() - hautBtnValidate, largBtnValidate, hautBtnValidate, canvas, this );
            btnValidate.setAlpha(125);
        }

        //charge les fichiers d'image en mémoire
        if ( imgSwitch.getNmbImg() <= 0) {
            String [] listImages = (new File(Properties.getImageMotDirrectory(motADeviner,this.getContext()))).list();
            Arrays.sort(listImages);//trie par ordre alphabétique

            imgSwitch.load(motADeviner, listImages,0, Properties.getHautHeader(), canvas.getWidth() / 2, canvas.getHeight() - Properties.getHautHeader() , canvas, this.getContext());
        }





        //Charge les boutons de son
        if ( lstBtn == null || lstBtn.size() == 0)
        {
            lstBtn = new ArrayList<ButtonSound>();

            for (int itBtn = 0 ; itBtn < lstBtnStr.size(); itBtn++)
            {
                int hautZone = canvas.getHeight() - Properties.getHautHeader() - hautBtnValidate;
                int largZone = canvas.getWidth() / 2;

                int x = (canvas.getWidth() / 2) + (largZone / 2 ) * (itBtn % 2) + borderbtnSound;
                int y = Properties.getHautHeader() + (int)(itBtn / 2) * (hautZone / 2 ) + borderbtnSound;
                int larg = (largZone / 2 ) - (borderbtnSound * 2);
                int haut = (hautZone / 2)- (borderbtnSound * 2);

                ButtonSound bts = new ButtonSound(lstBtnStr.get(itBtn), x,y,larg,haut, canvas, this);
                lstBtn.add(bts);
                lstBtn.get( itBtn).setAlpha(125);
            }
        }

        //Dessine l'image
        imgSwitch.draw(canvas);

        //dessine le bouton valider
        btnValidate.drawOn(canvas);

        //Dessine les boutons de son
        for (int itBtn = 0 ; itBtn < lstBtn.size(); itBtn++)
        {
            lstBtn.get(itBtn).drawOn(canvas);
        }

        //Dessine l'image
        imgSwitch.draw(canvas);
    }



}
