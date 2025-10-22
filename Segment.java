package logiciel.ecran;

/**
 * Segment du chemin définit par une position de départ à une position d'arrivée.
 * Longueur en pixel.
 */
public abstract class Segment {
    protected PositionPixel depart;
    protected PositionPixel arrive;

    /**
     * Construit un segment avec deux extrémités.
     *
     * @param depart position de départ
     * @param arrive position de fin
     */
    public Segment( PositionPixel depart, PositionPixel arrive ) {
        this.depart = depart;
        this.arrive = arrive;
    }

    /**
     * Construction d'un segment horizontal ou vertical en dépend des positions pixels des
     * coordonnées de départ et d'arrivée.
     *
     * @param depart position de départ
     * @param arrive position d'¡arrivée
     * @return une instance d'un segment horizontal ou vertical
     */
    public static Segment construire( PositionPixel depart, PositionPixel arrive ) {
        return ( depart.y() == arrive.y() )
                ? new SegmentHorizontal( depart, arrive )
                : new SegmentVertical( depart, arrive );
    }

    /**
     * Longueur du segment en pixel.
     *
     * @return longueur en pixel
     */
    public abstract int longueur();

    /**
     * Interpolation le long du segment.
     *
     * @param t distance en pixel depuis le point de départ
     * @return position pixel correspondante sur le segment
     */
    public abstract PositionPixel tween( double t );
}
