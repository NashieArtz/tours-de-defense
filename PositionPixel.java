package logiciel.ecran;

/**
 * Position en coordonnées "pixels logiques"
 *
 * @param x position X
 * @param y position Y
 */
public record PositionPixel( int x, int y ) {
    /**
     * Distance entre une position et une autre
     *
     * @param p2 position cible
     * @return distance tronquée en int
     */
    public int distance( PositionPixel p2 ) {
        int deltaX = x - p2.x;
        int deltaY = y - p2.y;
        double d = Math.sqrt( deltaX * deltaX + deltaY * deltaY );
        return (int) d;
    }
}
