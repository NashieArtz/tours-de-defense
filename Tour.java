package logiciel.ecran;


import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.List;

/**
 * Tour abstraite placée sur la grille.
 * Gère la position, l'affichage des caractéristiques et le rendu.
 */
public abstract class Tour {

    /**
     * Position sur la grille de tuiles
     */
    protected PositionTuile position;
    /**
     * Caractéristiques paramétables de la tour
     */
    protected Caracteristique [] caracteristiques;

    /**
     * Construit une tour à une position donnée.
     *
     * @param position position de la tour
     */
    public Tour( PositionTuile position ) {
        this.position = position;
    }

    /**
     * Animation d'attaque de la tour.
     *
     * @param ennemis liste d'ennemis présents sur la carte
     * @return argent gagné
     */
    public abstract int animer( List< Ennemi > ennemis );

    /**
     * Enregistres les actions des caractéristiques
     *
     * @param gestionSouris événement de souris
     */
    public void inscrireEvenements( GestionSouris gestionSouris ) {
        for( int i = 0; i < caracteristiques.length; ++ i ) {
            caracteristiques[ i ].inscrireEvenement( gestionSouris );
        }
    }

    /**
     * Désinscrit les actions.
     *
     * @param gestionSouris événement de souris
     */
    public void desinscrireEvenements( GestionSouris gestionSouris ) {
        for( int i = 0; i < caracteristiques.length; ++ i ) {
            caracteristiques[ i ].desinscrireEvenement( gestionSouris );
        }
    }

    /**
     * Affichage des options de la tour.
     *
     * @param g2 contexte graphique 2D
     * @param jeu contexte du jeu, accès à l'échelle du rendu
     */
    public void afficherControl( Graphics2D g2, Jeu jeu ) {
        // Soulignement visuel de la tour séléctionnée
        AffineTransform pCug = (AffineTransform) jeu.origine_pixel.clone();
        pCug.translate( getPositionPixel().x(), getPositionPixel().y() );
        g2.drawImage( Constantes.SOUSLIGNE_TOUR, pCug, null );

        // Affichage de l'UI pour chaque caractéristique
        for( int i = 0; i < caracteristiques.length; ++ i ) {
            caracteristiques[ i ].afficherControl( g2, jeu );
        }
    }

    /**
     * Dessine la tour sur la carte.
     *
     * @param g2 contexte grapghique 2D
     * @param origine_pixel transform global du jeu
     */
    public void afficherTour( Graphics2D g2, AffineTransform origine_pixel ) {
        AffineTransform pCurseur = (AffineTransform) origine_pixel.clone();
        PositionPixel pos = getPositionTuile().positionPixel();
        pCurseur.translate( pos.x(), pos.y() );

        // Dessin de la partie basse de la tour
        g2.drawImage( TourArcher.TOUR_BAS, pCurseur, null );
        pCurseur.translate( 0, - Constantes.TAILLE_TUILE );
        // Dessin de la partie haute de la tour une tuile au dessus
        g2.drawImage( TourArcher.TOUR_HAUT, pCurseur, null );
    }

    /**
     * Donne la position en tuile de la tour.
     *
     * @return position de la tuile de la tour
     */
    public PositionTuile getPositionTuile() {
        return position;
    }

    /**
     * Donne la position en pixels de la tour
     *
     * @return position pixel de la tour
     */
    public PositionPixel getPositionPixel() {
        return position.positionPixel();
    }
}
