package logiciel.ecran;

import java.util.ArrayList;
import java.util.List;

/**
 * Représentation d'une suite de segments contenants deux extrémités. Chaque tuile d'un ssegment,
 * exprimées en coordonnées a une paire consécutive convertie en pixel
 */
public class Chemin {
    // Liste ordonnée des segments qui composent le chemin
    private List<Segment> segments;
    // Longueur totale du chemin = somme des longueurs des segments (en pixels)
    private int longueur;

    /**
     * Construit un chemin à partir d'une suite d'extrémités en tuiles.
     *
     * @param extremites extrémités du chemin en coordonnées tuile (au moins 2 pour créer un segment)
     */
    public Chemin(PositionTuile... extremites) {
        longueur = 0;
        segments = new ArrayList<>();

        // Conversion de chaque tuile en pixel pour chaque extrémité, et construction du segment
        for (int i = 1; i < extremites.length; ++i) {
            Segment s = Segment.construire(
                extremites[i - 1].positionPixel(),
                extremites[i].positionPixel()
            );
            segments.add(s);
            longueur += s.longueur();
        }
    }

    /**
     * Retourne le nombre de segments composant le chemin.
     *
     * @return le nombre de segments
     */
    public int nombreSegment() {
        return segments.size();
    }

    /**
     * Retourne le segment à l'index donné.
     *
     * @param pos index du segment (0..nombreSegment()-1)
     * @return le segment à cet index
     */
    public Segment getSegment(int pos) {
        return segments.get(pos);
    }

    /**
     * Calcule une position interpolée sur un segment donné.
     *
     * @param noSegment index du segment
     * @param t progression normalisée sur le segment
     * @return la position en pixels correspondante
     */
    public PositionPixel calculerPosition(int noSegment, double t) {
        return segments.get(noSegment).tween(t);
    }

    /**
     * Retourne la longueur totale du chemin en pixels.
     *
     * @return la longueur totale
     */
    public int getLongueur() {
        return longueur;
    }
}
