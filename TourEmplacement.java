// Nom: WU
// Prénom: Ange
// Code: WUXA90340201

package logiciel.ecran;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.List;

/**
 * Emplacement de construction vide cliquable faisant apparaître un panneau pour acheter un type
 * de tour.
 */
public class TourEmplacement extends Tour {
    // Nombre de tours proposées
    private static final int NB_TOURS = 3;
    // Catalogue des tours disponibles, leurs prix, nom, etc.
    private static final DescripteurTour [] tours = new DescripteurTour[ NB_TOURS ];

    // Catalogue maniable pour ajouter ou enlever des tours
    static {
        tours[ 0 ] = new DescripteurTour( "archer", 40, TourArcher::new, Constantes.POSITION_ACHAT_TOUR_Y + 0 );
        tours[ 1 ] = new DescripteurTour( "glace", 50, TourGlace::new, Constantes.POSITION_ACHAT_TOUR_Y + 2 );
        tours[ 2 ] = new DescripteurTour( "feu", 45, TourFeu::new, Constantes.POSITION_ACHAT_TOUR_Y + 4);
    }

    /**
     * Construit un emplacement de construction à la positiion donnée.
     *
     * @param position position de l'emplacement vide
     */
    public TourEmplacement( PositionTuile position ) {
        super( position );
    }

    @Override
    public int animer( List< Ennemi > ennemis ) {
        return 0;
    }

    @Override
    public void inscrireEvenements( GestionSouris gestionSouris ) {
        for( int i = 0; i < NB_TOURS; ++ i ) {
            tours[ i ].inscrireEvenements( gestionSouris );
        }
    }

    @Override
    public void desinscrireEvenements( GestionSouris gestionSouris ) {
        for( int i = 0; i < NB_TOURS; ++ i ) {
            tours[ i ].desinscrireEvenements( gestionSouris );
        }
    }

    @Override
    public void afficherControl( Graphics2D g2, Jeu jeu ) {
        AffineTransform pCug = (AffineTransform) jeu.origine_pixel.clone();
        pCug.translate( getPositionPixel().x(), getPositionPixel().y() );
        g2.drawImage( Constantes.SOUSLIGNE_TOUR, pCug, null );

        // Dessin des boutons d'achats
        for( int i = 0; i < NB_TOURS; ++ i ) {
            tours[ i ].afficherControl( g2, jeu );
        }
    }

    @Override
    public void afficherTour( Graphics2D g2, AffineTransform origine_pixel ) {
        AffineTransform pCurseur = (AffineTransform) origine_pixel.clone();
        PositionPixel pos = getPositionTuile().positionPixel();
        pCurseur.translate( pos.x(), pos.y() );

        g2.drawImage( Constantes.EMPLACEMENT_TOUR, pCurseur, null );
    }
}
