package logiciel.ecran;

/**
 * Position en coordonnées des tuiles.
 *
 * @param x position x
 * @param y position Y
 */
public record PositionTuile( int x, int y ) {
    /**
     * Conversion des coordonnées tuiles en coordonnées pixels
     *
     * @return position en pixel
     */
    public PositionPixel positionPixel() {
        return new PositionPixel( x * Constantes.TAILLE_TUILE, y * Constantes.TAILLE_TUILE );
    }
}
