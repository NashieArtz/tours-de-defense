// Nom: WU
// Prénom: Ange
// Code: WUXA90340201

package logiciel.ecran;

import java.awt.*;
import java.awt.geom.AffineTransform;

/**
 * Ennemi qui se déplace le long d’un chemin et leurs caractéristiques.
 */
public class Ennemi {
  // Chemin à suivre et progression
  private Chemin chemin;
  private int noSegment;
  private double distance;

  private double vitesse;
  private int pointVie;
  private int pointVieMax;

  // Rendu
  private Tuile image;
  private int valeurArgent;

  // Couleurs/ressources d’origine pour restaurer l’apparence
  private Color couleurCourante;
  private Color couleurOriginale;
  private BitMap bitmapOriginal;
  private Tuile imageOriginale;

  private boolean ralenti = false;
  private double vitesseOriginale;

  private boolean enFlamme = false;
  // Ennemi perd 1 PV par tous les 5 ticks
  private int dureeFeuRestante = 0;
  private int compteurFeu = 0;

  /**
   * Crée un ennemi avec chemin, vitesse, PV, récompense et apparence.
   *
   *  @param chemin chemin à parcourir
   *  @param vitesse vitesse de déplacement par tick
   *  @param pointVie points de vie initiaux
   *  @param valeurArgent argent donné au joueur à la mort
   *  @param image tuile affichée pour cet ennemi
   *  @param bitmapOriginal bitmap source pour recoloriser la tuile selon les états
   */
  public Ennemi(Chemin chemin, double vitesse, int pointVie, int valeurArgent, Tuile image,
                BitMap bitmapOriginal) {
    this.chemin = chemin;
    this.vitesse = vitesse;
    this.vitesseOriginale = vitesse;
    this.pointVie = pointVie;
    this.pointVieMax = pointVie;
    this.valeurArgent = valeurArgent;
    this.image = image;
    this.imageOriginale = image;
    // Stocke le bitmap pour pouvoir recréer la tuile selon l’état (couleur)
    this.bitmapOriginal = bitmapOriginal;
    this.distance = 0.0;
    this.noSegment = 0;
  }

  /**
   * Copie d'un ennemi.
   *
   * @param original ennemi à copier.
   */
  public Ennemi(Ennemi original) {
    chemin = original.chemin;
    noSegment = original.noSegment;
    distance = original.distance;
    vitesse = original.vitesse;
    pointVie = original.pointVie;
    pointVieMax = original.pointVieMax;
    this.valeurArgent = original.valeurArgent;
    image = original.image;

    // Copie des attributs additionnels
    this.bitmapOriginal = original.bitmapOriginal;
    this.imageOriginale = original.imageOriginale;
    this.ralenti = original.ralenti;
    this.vitesseOriginale = original.vitesseOriginale;
  }

  /**
   * Compare deux ennemis selon leur distance restante au château.
   *
   * @param e1 premier ennemi
   * @param e2 deuxième ennemi
   */
  public static int comparer(Ennemi e1, Ennemi e2) {
    return Integer.compare(e1.distanceChateau(), e2.distanceChateau());
  }

  /**
   * Inflige des dégâts et indique si l’ennemi est mort.
   *
   * @param dommage nombre de PV à retirer
   * @return true si l'ennemi est mort après l'attaque, sinon false
   */
  public boolean reduireVie(int dommage) {
    pointVie -= dommage;
    return pointVie <= 0;
  }

  /**
   * Indique si l’ennemi a atteint la fin du chemin.
   */
  public boolean aAtteintChateau() {
    return chemin.nombreSegment() <= noSegment;
  }

  /**
   * Fait avancer l’ennemi selon sa vitesse (en pixels par tick).
   * Passe au segment suivant quand la distance dépasse la longueur du segment.
   */
  public void avancer() {
    if (noSegment < chemin.nombreSegment()) {
      distance += vitesse;
      int longueur = chemin.getSegment(noSegment).longueur();
      if (longueur < distance) {
        distance -= longueur;
        ++noSegment;
      }
    }
  }

  /**
   * Distance restante (approx.) jusqu’au château.
   */
  public int distanceChateau() {
    return chemin.getLongueur() - ((int) distance);
  }

  /**
   * Position pixel courante de l’ennemi sur le segment actuel.
   */
  public PositionPixel getPositionPixel() {
    return chemin.calculerPosition(noSegment, distance);
  }

  /**
   * Affiche l’ennemi et sa barre de PV au-dessus.
   *
   * @param affineTransform transform de base à partir de laquelle on se positionne
   * @param g2 contexte graphique 2D
   */
  public void afficher(Graphics2D g2, AffineTransform affineTransform) {
    if (noSegment < chemin.nombreSegment()) {
      AffineTransform pCurseur = (AffineTransform) affineTransform.clone();
      PositionPixel position = getPositionPixel();
      pCurseur.translate(position.x(), position.y());
      g2.drawImage(image, pCurseur, null);

      // Affiche la barre de PV au-dessus de l’ennemi
      pCurseur.translate(0, -2);
      int rPV = (pointVie * Constantes.FACTEUR_PV) / pointVieMax;

      // Clamp des PV affichés
      if (rPV < 0) {
        rPV = 0;
      } else if (rPV > Constantes.FACTEUR_PV) {
        rPV = Constantes.FACTEUR_PV;
      }

      g2.drawImage(Constantes.PV_ENNEMI[rPV], pCurseur, null);
    }
  }

  /**
   * Valeur d’argent gagnée à la mort de l’ennemi.
   */
  public int getValeurArgent() {
    return valeurArgent;
  }

  /**
   * Points de vie courants.
   */
  public int getPointVie() {
    return pointVie;
  }

  /**
   * Applique un ralentissement (réduction de vitesse en centièmes).
   * Met à jour la couleur pour refléter l’état.
   *
   * @param reductionCentiemes réduction en pourcentage
   */
  public void ralentir(int reductionCentiemes) {
    if (!ralenti) {
      ralenti = true;
      vitesse = vitesseOriginale * (1 - (reductionCentiemes / 100.0));
      mettreAJourCouleur();
    }
  }

  /**
   * Retire le ralentissement et restaure la vitesse.
   * Met à jour la couleur.
   */
  public void enleverRalentissement() {
    if (ralenti) {
      ralenti = false;
      vitesse = vitesseOriginale;
      mettreAJourCouleur();
    }
  }

  /**
   * Vrai si l’ennemi est enflammé ou encore en train de brûler.
   */
  public boolean estEnflammeOuEnfeu() {
    return enFlamme || dureeFeuRestante > 0;
  }

  /**
   * Met l’ennemi en feu pour une durée (perte de 1 PV tous les 5 ticks).
   * Perte immédiate gérée ailleurs au tir initial.
   *
   * @param duree durée pour le nombre total de PV à retirer
   */
  public void enflammer(int duree) {
    enFlamme = true;
    dureeFeuRestante = duree - 1; // -1 car 1 PV déjà perdu au tir
    compteurFeu = 0;
    mettreAJourCouleur();
  }

  /**
   * Met à jour l’effet de feu à chaque tick.
   * Toutes les 5 itérations, retire 1 PV jusqu’à extinction.
   */
  public void gererFeu() {
    if (dureeFeuRestante > 0) {
      compteurFeu++;
      if (compteurFeu >= 5) {
        compteurFeu = 0;
        // L’ennemi perd 1 PV
        if (reduireVie(1)) {
          // Mort gérée par le système appelant si nécessaire
        }
        dureeFeuRestante--;
      }
      if (dureeFeuRestante == 0) {
        enFlamme = false;
        mettreAJourCouleur();
      }
    }
  }

  /**
   * Met à jour l’apparence selon l’état : feu (rouge), glace (bleu), les deux (violet), sinon original.
   */
  private void mettreAJourCouleur() {
    if (enFlamme && ralenti) {
      this.image = new Tuile(new Color(128, 0, 128), bitmapOriginal); // violet
    } else if (enFlamme) {
      this.image = new Tuile(Color.red, bitmapOriginal);
    } else if (ralenti) {
      this.image = new Tuile(Color.blue, bitmapOriginal);
    } else {
      this.image = imageOriginale;
    }
  }

}