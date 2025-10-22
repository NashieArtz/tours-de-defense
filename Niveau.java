package logiciel.ecran;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.List;

/**
 * Description de niveau de jeu, les emplacements de tours disponibles, argent de départ,
 * générateurs d'ennemis, etc.
 */
public interface Niveau {
    /**
     * Dessine le niveau.
     *
     * @param g2 contexte graphique 2D
     * @param affineTransform transform de base
     */
    void dessiner(Graphics2D g2, AffineTransform affineTransform );

    /**
     * Argent de depart
     *
     * @return montant de départ
     */
    int argentDepart();

    /**
     * Emplacements de tours disponibles durant un niveau.
     *
     * @return un tableau de positions en tuiles pour placer les tours
     */
    PositionTuile [] positionTours();

    /**
     * Générateurs d'ennemis à activer pour le niveau.
     *
     * @return liste des générateurs à utiliser
     */
    List< Generateur > generateurs();

    /**
     * Position en tuiles où afficher les PVs.
     *
     * @return position des PVs
     */
    PositionTuile positionPV();
}
