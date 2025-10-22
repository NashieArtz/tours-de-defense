// Nom: WU
// Prénom: Ange
// Code: WUXA90340201

package logiciel.ecran;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.util.List;

/**
 * Représente une tour de glace ralentissant les ennemis
 * ELle tire à chaque tir sur l'ennemi le plus proche dans sa zone de tir
 * Les ennemis sont ralentis pendant 1 tic et reprennent leurs vitesses normales
 */
public class TourGlace extends Tour {

  private static final int RAYON = 0;
  private static final int REDUCTION = 1;

  // Rayon d'attaque
  private static final int[] RAYONS = {24, 32, 40};
  // Prix amélioration du rayon
  private static final int[] PRIX_RAYON = {40, 60};

  // Réduction de la vitesse
  private static final int[] REDUCTIONS = {20, 40};
  // Prix amélioration réduction de vitesse
  private static final int[] PRIX_REDUCTION = {60};

  private static final int NB_CARACTERISTIQUE = 2;

  public static final Tuile TOUR_BAS = new Tuile(Color.blue, Color.darkGray, BitMap.MUR);
  public static final Tuile TOUR_HAUT = new Tuile(Color.blue, Color.darkGray, BitMap.TOUR_DESSUS);


  // Instanciation des caractéristiques
  {
    caracteristiques = new Caracteristique[NB_CARACTERISTIQUE];

    caracteristiques[RAYON] =
        new Caracteristique("ray", RAYONS, PRIX_RAYON, Constantes.POSITION_CARACTERISTIQUE_Y);
    caracteristiques[REDUCTION] =
        new Caracteristique("red", REDUCTIONS, PRIX_REDUCTION,
            Constantes.POSITION_CARACTERISTIQUE_Y + 2);
  }

  /**
   * Constructeur de la tour de glace.
   *
   * @param position position de la tour sur le terrain.
   */
  public TourGlace(PositionTuile position) {
    super(position);
  }


  /**
   * Gestion d'animation de la tour
   *
   * @param ennemis liste des ennemis sur la carte
   * @return ne retourne rien, car ne tue aucun ennemi
   */
  @Override
  public int animer(List<Ennemi> ennemis) {
    int rayon = caracteristiques[RAYON].getValeur();
    int reduction = caracteristiques[REDUCTION].getValeur();

    // Ralentir tous les ennemis dans le rayon
    PositionPixel tourPosition = getPositionPixel();

    // Enlever le ralentissement de tous
    for (Ennemi e : ennemis) {
      e.enleverRalentissement();
    }
    // Appliquer ralentissement
    for (Ennemi e : ennemis) {
      if (!e.aAtteintChateau()) {
        int distance = tourPosition.distance(e.getPositionPixel());
        if (distance <= rayon) {
          e.ralentir(reduction);
        }
      }
    }
    return 0;
  }

  /**
   * Afficher la tour en bleu
   *
   * @param g2            contexte graphique
   * @param origine_pixel transformation d'origine pixel
   */
  @Override
  public void afficherTour(Graphics2D g2, AffineTransform origine_pixel) {
    AffineTransform pCurseur = (AffineTransform) origine_pixel.clone();
    PositionPixel pos = getPositionTuile().positionPixel();
    pCurseur.translate(pos.x(), pos.y());

    g2.drawImage(TOUR_BAS, pCurseur, null);
    pCurseur.translate(0, -Constantes.TAILLE_TUILE);
    g2.drawImage(TOUR_HAUT, pCurseur, null);
  }
}
