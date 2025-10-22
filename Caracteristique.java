package logiciel.ecran;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.function.BiConsumer;

/**
 * Représente une caractéristique upgradable affichée dans l'interface du jeu.
 */
public class Caracteristique {
    private String nom;
    private int valeur;
    private int niveau;

    private int [] valeursParNiveau;
    // Convention habituelle : valeursParNiveau.length == coutsProchainNiveau.length + 1
    private int [] coutsProchainNiveau;

    private PositionTuile positionNom_tuile;
    private PositionTuile positionValeur_tuile;
    private PositionTuile positionBouton_tuile;
    private PositionPixel positionBouton_pixel;
    private PositionTuile positionCout_tuile;

    /**
     * Callback déclenché au clic sur la tuile du bouton d'upgrade.
     * Vérifie la possibilité d'augmenter, débite l'argent, applique l'upgrade et déclenche un
     * repaint si nécessaire.
     */
    public BiConsumer<Jeu, PositionTuile> augmenterFct = ( jeu, position) ->
    {
        if ( peutAugmenter() ) {
            int prix = coutProchainNiveau();
            if( prix <= jeu.argent ) {
                jeu.argent -= prix;
                augmenter();

                if( ! jeu.action ) {
                    jeu.repaint();
                }
            }
        }
    };

    /**
     * Crée une caractéristique à partir de ses valeurs et coûts par niveau, et positionne
     * les éléments d'UI.
     *
     * @param nom nom affiché de la caractéristique
     * @param valeursParNiveau valeurs par niveau, indexées de 0 à N
     * @param coutsProchainNiveau coûts pour passer au niveau suivant
     *                            (taille = valeursParNiveau.length - 1)
     * @param posY ligne (en tuiles) où afficher cette caractéristique
     */
    public Caracteristique( String nom, int[] valeursParNiveau, int[] coutsProchainNiveau, int posY ) {
        this.nom = nom;
        this.niveau = 0;
        this.valeursParNiveau = valeursParNiveau;
        this.coutsProchainNiveau = coutsProchainNiveau;
        this.valeur = valeursParNiveau[ niveau ];
        this.positionNom_tuile = new PositionTuile( Constantes.POSITION_CARACTERISTIQUE_X_NOM, posY );
        this.positionValeur_tuile = new PositionTuile( Constantes.POSITION_CARACTERISTIQUE_X_NOMBRE, posY );
        this.positionBouton_tuile = new PositionTuile( Constantes.POSITION_CARACTERISTIQUE_X_BOUTON, posY );
        this.positionBouton_pixel = this.positionBouton_tuile.positionPixel();
        // Coût affichñe une tuile plus bas que la valeur
        this.positionCout_tuile = new PositionTuile( Constantes.POSITION_CARACTERISTIQUE_X_NOMBRE, posY + 1 );
    }

    /**
     * Dessine le contrôle (nom, valeur, bouton, coût et symbole dollar si un upgrade est possible).
     *
     * @param g2 contexte graphique 2D
     * @param jeu contexte du jeu (images, helpers d'affichage, transform d'origine)
     */
    public void afficherControl( Graphics2D g2, Jeu jeu ) {
        // Affiche le libellé et la valeur
        jeu.afficherMessage( g2, nom, positionNom_tuile );
        jeu.afficherNombre( g2, positionValeur_tuile, valeur );

        // On n'affiche le bouton et le coût que s'il reste un niveau à acheter
        if( peutAugmenter() ) {
            // On clone la transform d'origine et on translate jusqu'au coin du bouton
            AffineTransform pC = (AffineTransform) jeu.origine_pixel.clone();
            pC.translate( positionBouton_pixel.x(), positionBouton_pixel.y() );

            // Dessine l'icône du bouton "+"
            g2.drawImage( Constantes.BOUTON_AUGMENTER, pC, null );

            // Affiche le coût du prochain niveau à la position prévue
            jeu.afficherNombre( g2, positionCout_tuile, coutProchainNiveau() );

            // Décale d'une tuile vers le bas et dessine le symbole "$"
            pC.translate( 0, Constantes.TAILLE_TUILE );
            g2.drawImage( Constantes.DOLLAR, pC, null );
        }
    }

    /**
     * Inscrit l'action d'augmentation sur la tuile du bouton.
     *
     * @param gestionSouris gestionnaire des événements souris
     */
    public void inscrireEvenement( GestionSouris gestionSouris ) {
        gestionSouris.inscrireEvenement( positionBouton_tuile, augmenterFct );
    }

    /**
     * Désinscrit l'action d'augmentation de la tuile du bouton.
     *
     * @param gestionSouris gestionnaire des événements souris
     */
    public void desinscrireEvenement( GestionSouris gestionSouris ) {
        gestionSouris.desinscrireEvenement( positionBouton_tuile );
    }

    /**
     * Retourne la valeur courante.
     *
     * @return valeur actuelle de la caractéristique
     */
    public int getValeur() {
        return valeur;
    }

    /**
     * Indique si un niveau supérieur est disponible pour le niveau courant.
     *
     * @return true si un upgrade est possible, sinon false
     */
    public boolean peutAugmenter() {
        return niveau < coutsProchainNiveau.length;
    }

    /**
     * Passe au niveau suivant et synchronise la valeur avec la table.
     * Pré-condition : {@link #peutAugmenter()} == true
     */
    // Passe au niveau suivant et synchronise la valeur avec la table
    // (Pré-condition : peutAugmenter() == true)
    public void augmenter() {
        ++ niveau;
        valeur = valeursParNiveau[ niveau ];
    }

    /**
     * Retourne le coût pour passer du niveau courant au niveau suivant.
     *
     * @return coût du prochain niveau
     */
    public int coutProchainNiveau() {
        return coutsProchainNiveau[ niveau ];
    }
}
