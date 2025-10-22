// Nom: WU
// Prénom: Ange
// Code: WUXA90340201

package logiciel.ecran;

import java.util.List;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

/**
 * Représente une tour de feu enflammant les ennemis
 * ELle tire tous les 15 tics sur l'ennemi le plus proche non enflammé présent dans sa zone de tir
 * Les ennemis enflammés perdent 1 point de vie tous les 5 tics jusqu'à ce qu'ils ne soient plus en feu
 */
public class TourFeu extends Tour {
  // Nombre de tics entre chaque tir
  public static final int PERIODE_RECHARGE_TIC = 15;

  // Compteur de recharge de tir, à 0, la tour peut tirer
  private int compteurRecharge = 0;

  private static final int DISTANCE = 0;
  private static final int DUREE = 1;

  //Distance de tir et prix d'améliorations
  private static final int[] DISTANCES = {20, 30, 50};
  private static final int[] PRIX_DISTANCE = {40, 80};
  // Durees du feu et prix d'améliorations
  private static final int[] DUREES = {3, 5, 7};
  private static final int[] PRIX_DUREE = {20, 30};

  public static final Tuile TOUR_BAS = new Tuile(Color.red, Color.darkGray, BitMap.MUR);
  public static final Tuile TOUR_HAUT = new Tuile(Color.red, Color.darkGray, BitMap.TOUR_DESSUS);

  /**
   * @return la distance de tir actuelle de la tour
   */
  private int getDistanceTir() {
    return caracteristiques[DISTANCE].getValeur();
  }

  /**
   * @return la durée de feu de la tour
   */
  private int getDureeEnflamme() {
    return caracteristiques[DUREE].getValeur();
  }

  // Instanciation des caractéristiques
  {
    caracteristiques = new Caracteristique[2];
    caracteristiques[DISTANCE] =
        new Caracteristique("dis", DISTANCES, PRIX_DISTANCE, Constantes.POSITION_CARACTERISTIQUE_Y);
    caracteristiques[DUREE] =
        new Caracteristique("dur", DUREES, PRIX_DUREE, Constantes.POSITION_CARACTERISTIQUE_Y + 2);
  }

  /**
   * Constructeur de la tour de feu.
   *
   * @param position La position de la tour sur le terrain.
   */
  public TourFeu(PositionTuile position) {
    super(position);
  }


  /**
   * Gestion d'animation de la tour appelé 25 fois par seconde
   *
   * @param ennemis liste des ennemis sur la carte
   * @return argent gagné après la mort de l'ennemi
   */
  @Override
  public int animer(List<Ennemi> ennemis) {
    int argentGagne = 0;

    // Tour encore en recharge, on décrémente
    if (compteurRecharge > 0) {
      compteurRecharge--;
      return 0;
    }

    // Trouver une cible non-enflammée
    PositionPixel tourPos = getPositionPixel();
    Ennemi cible = trouverCible(ennemis, tourPos, getDistanceTir());

    if (cible != null) {
      // Tir sur cible
      argentGagne += tirer(cible);
      //Recharge
      compteurRecharge = PERIODE_RECHARGE_TIC;
    }

    return argentGagne;
  }

  /**
   * Trouve l'ennemi cible en fonction de la distance de tir et de son état
   * D'abord, cherche le plus proche ennemi non enflammé dans la portée.
   * Si aucun trouvé, cherche le plus proche ennemi (même enflammé) dans la portée.
   *
   * @param ennemis liste des ennemis
   * @param tourPos position de la tour
   * @param distanceTir distance de tir
   * @return la cible ou null
   */
  private Ennemi trouverCible(List<Ennemi> ennemis, PositionPixel tourPos, int distanceTir) {
    Ennemi cible = null;
    // Distance de la meilleure cible
    int distanceMin = Integer.MAX_VALUE;

    // Cherche l'ennemi non enflammé
    for (Ennemi e : ennemis) {
      if (!e.aAtteintChateau()) {
        int distance = tourPos.distance(e.getPositionPixel());
        // Cible à porté et strictement plus proche que la meilleure cible actuelle
        if (distance <= distanceTir && distance < distanceMin) {
          if (!e.estEnflammeOuEnfeu()) {
            // Nouveau record de distance à battre
            distanceMin = distance;
            // Nouvelle meilleure cible
            cible = e;
          }
        }
      }
    }

    //Aucun non enflammé trouvé, cible reste null, on ne tire pas
    return cible;
  }


  /**
   * Tire sur un ennemi lui infligeant 1 point de dégât
   * Ennemi meurt, on retourne l'argent, sinon on l'enflamme
   *
   * @param e cible
   * @return argent gagné s'il meurt, sinon 0
   */
  private int tirer(Ennemi e) {
    int argentGagner = 0;
    // Inflige 1 PV immédiatement
    boolean mort = e.reduireVie(1);

    if (mort) {
      // augmenter l'argent du joueur
      argentGagner = e.getValeurArgent();
    }

    // Enflammer si pas déjà enflammé
    if (!e.estEnflammeOuEnfeu()) {
      e.enflammer(getDureeEnflamme());
    }
    return argentGagner;
  }

  /**
   * Affiche la tour de feu sur la carte
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
