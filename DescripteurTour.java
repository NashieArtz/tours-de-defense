package logiciel.ecran;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.function.BiConsumer;
import java.util.function.Function;

/*
* UI pour acheter les tours.
*/
public class DescripteurTour {
    private String nom;
    private int prix;

    // À partir d'une position tuile, retourne un type de tour.
    private Function< PositionTuile, Tour > fonctionConstructionTour;

    private int posY;

    // Positions d'affichage (grille en tuiles).
    private PositionTuile positionNom_tuile;
    private PositionTuile positionBouton_tuile;
    private PositionPixel positionBouton_pixel;
    private PositionTuile positionCout_tuile;


    /**
     * Instancie un nouveau {@code DescripteurTour}.
     *
     * @param nom le nom affiché de la tour
     * @param prix le prix d'achat/remplacement
     * @param fonctionConstructionTour la fabrique de construction de tour
     * @param posY la position en Y
     */
    public DescripteurTour( String nom, int prix, Function< PositionTuile, Tour > fonctionConstructionTour, int posY ) {
        this.nom = nom;
        this.prix = prix;
        this.fonctionConstructionTour = fonctionConstructionTour;
        this.posY = posY;

        this.positionNom_tuile = new PositionTuile( Constantes.POSITION_CARACTERISTIQUE_X_NOM, posY );
        this.positionBouton_tuile = new PositionTuile( Constantes.POSITION_CARACTERISTIQUE_X_BOUTON, posY );
        this.positionBouton_pixel = this.positionBouton_tuile.positionPixel();
        this.positionCout_tuile = new PositionTuile( Constantes.POSITION_CARACTERISTIQUE_X_NOMBRE, posY + 1 );
    }

    /**
     * Action d'achat/remplacement de la tour, déclenchée au clic sur la tuile du bouton.
     */
    public BiConsumer<Jeu, PositionTuile> acheterTour = ( jeu, position ) ->
    {
        if( prix <= jeu.argent ) {
            jeu.argent -= prix;

            // On enlève la tour actuellement choisie
            jeu.tourChoisie.desinscrireEvenements( jeu.gestionSouris );
            jeu.toursConstruites.remove( jeu.tourChoisie );
            // On conserve la même PositionTuile pour replacer la nouvelle tour au même endroit
            PositionTuile p = jeu.tourChoisie.getPositionTuile();

            // Construit la nouvelle tour à la PositionTuile p
            jeu.tourChoisie = fonctionConstructionTour.apply( p );
            jeu.toursConstruites.add( jeu.tourChoisie );
            jeu.tourChoisie.inscrireEvenements( jeu.gestionSouris );
            jeu.repaint();
        }
    };

    /**
     * Inscrit les événements d'ahcats.
     *
     * @param gestionSouris le gestionnaire des événements souris
     */
    public void inscrireEvenements( GestionSouris gestionSouris ) {
        gestionSouris.inscrireEvenement( positionBouton_tuile, acheterTour );
    }

    /**
     * Désinscrit les événements d'achats.
     *
     * @param gestionSouris le gestionnaire des événements souris
     */
    public void desinscrireEvenements( GestionSouris gestionSouris ) {
        gestionSouris.desinscrireEvenement( positionBouton_tuile );
    }

    /**
     * Affiche les contrôles d'UI.
     *
     * @param g2  le contexte graphique 2D
     * @param jeu le contexte du jeu
     */
    public void afficherControl( Graphics2D g2, Jeu jeu ) {
        jeu.afficherMessage( g2, nom, positionNom_tuile );

        AffineTransform pC = (AffineTransform) jeu.origine_pixel.clone();
        pC.translate( positionBouton_pixel.x(), positionBouton_pixel.y() );

        g2.drawImage( Constantes.BOUTON_AUGMENTER, pC, null );
        jeu.afficherNombre( g2, positionCout_tuile, prix );

        pC.translate( 0, Constantes.TAILLE_TUILE );
        g2.drawImage(Constantes.DOLLAR, pC, null);
    }

}
