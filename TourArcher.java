package logiciel.ecran;

import java.awt.*;
import java.util.List;

/**
 * Tour de type archer.
 * Tire périodiquement sur les ennemis les plus proches dans la limite de portée.
 */
public class TourArcher extends Tour {
  // Période de recharge
  public static final int PERIODE_RECHARGE_TIC = 15;
  // Dessin de la tour
  public static final Tuile TOUR_BAS = new Tuile(Color.darkGray, Color.lightGray, BitMap.MUR);
  public static final Tuile TOUR_HAUT =
      new Tuile(Color.darkGray, Color.lightGray, BitMap.TOUR_DESSUS);

 // Caractéristiques
  private static final int NB_CARACTERISTIQUE = 3;
  private static final int DOMMAGE = 0;
  private static final int NB_TIR = 1;
  private static final int DISTANCE = 2;

  // Tables d'évolutions et prix par niveau
  private static final int[] PRIX_AUGMENTATION_DOMMAGE = {20, 30, 50};
  private static final int[] DOMMAGE_PV = {2, 3, 4, 5};

  private static final int[] PRIX_AUGMENTATION_NB_TIR = {40, 80};
  private static final int[] NOMBRE_PROJECTILE_PAR_TIR = {1, 2, 3};

  private static final int[] PRIX_AUGMENTATION_DISTANCE = {40, 80, 120};
  private static final int[] DISTANCE_MAX_TIR_PIXEL = {24, 36, 48};

  // Instanciation des caractéristiques et leurs positions UI
  {
    caracteristiques = new Caracteristique[NB_CARACTERISTIQUE];

    caracteristiques[DOMMAGE] =
        new Caracteristique("dom", DOMMAGE_PV, PRIX_AUGMENTATION_DOMMAGE,
            Constantes.POSITION_CARACTERISTIQUE_Y);
    caracteristiques[NB_TIR] =
        new Caracteristique("nb", NOMBRE_PROJECTILE_PAR_TIR, PRIX_AUGMENTATION_NB_TIR,
            Constantes.POSITION_CARACTERISTIQUE_Y + 2);
    caracteristiques[DISTANCE] =
        new Caracteristique("dis", DISTANCE_MAX_TIR_PIXEL, PRIX_AUGMENTATION_DISTANCE,
            Constantes.POSITION_CARACTERISTIQUE_Y + 4);
  }

  // Compteur de temps de recharge depuis le dernier tir
  private int recharge_tic = 0;

  /**
   * Construit une tour archer à la position donnée.
   *
   * @param position position en coordonnée tuile
   */
  public TourArcher(PositionTuile position) {
    super(position);
  }

  /**
   * Animation et logique de tir de la tour.
   * Cible l'ennemi à un index j le plus proche et on continue à tirer dessus jusqu'à ce qu'il soit
   * hors de portée.
   *
   * @param ennemis liste d'ennemis présent sur la carte
   * @return argent gagnée à la mort d'un ennemi
   */
  private int tirer(List<Ennemi> ennemis) {
    int argentGagne = 0;
    // Scanner le premier ennemi
    int j = 0;

    // Pour chaque nombre de tir possible en un coup
    for (int k = 0; k < caracteristiques[NB_TIR].getValeur(); ++k) {
      try {
        // Avance jusqu'au premier ennemi à portée
        while (j < ennemis.size() && caracteristiques[DISTANCE].getValeur() <
            ennemis.get(j).getPositionPixel().distance(position.positionPixel())) {
          ++j;
        }
        // Ennemi à porté et valide, on inflige des dégâts
        if (0 <= j) {
          Ennemi ennemi = ennemis.get(j);
          boolean estMort = ennemi.reduireVie(caracteristiques[DOMMAGE].getValeur());
          if (estMort) {
            // Récompense à la mort de l'ennemi
            argentGagne += ennemi.getValeurArgent();
            ennemis.remove(j);
          }
        }
      } catch (IndexOutOfBoundsException e) {
        // Si la liste d'ennemis a rétréci ailleurs, on ignore et on continue les projectiles
        // suivants
      }
    }
    return argentGagne;
  }

  @Override
  public int animer(List<Ennemi> ennemis) {
    int argentGagne = 0;
    boolean pretTirer = false;

    // Avance le temps de recharge
    ++recharge_tic;

    // Temps de recharge atteint, on charge le tir et on réinitialise le temps de recharge
    if (PERIODE_RECHARGE_TIC <= recharge_tic) {
      pretTirer = true;
      recharge_tic = 0;
    }

    // Déclenche le tir
    if (pretTirer) {
      argentGagne = tirer(ennemis);
    }

    return argentGagne;
  }
}
