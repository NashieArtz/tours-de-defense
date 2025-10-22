package logiciel.ecran;

/**
 * Générateur d’ennemis : attend un délai initial puis crée des ennemis à intervalle régulier.
 * Stocke un modèle d’ennemi et en produit des copies jusqu’à épuisement.
 */
public class Generateur {
    // Compteur de ticks interne
    private int heure;
    // Délai initial (ticks) avant le tout premier spawn
    private int nbTicInitiale;
    // Cadence (ticks) entre deux spawns après le premier
    private int nbTicSpawn;
    // Nombre d’ennemis restants à générer
    private int nbEnnemi;
    // Modèle à cloner pour chaque apparition
    private Ennemi ennemi;
    // Référence au jeu pour pousser les nouveaux ennemis dans parent.ennemis
    private Jeu parent;
    // True tant qu’on n’a pas encore effectué le premier spawn
    private boolean phaseInitiale = true;

    /**
     * Initialise le générateur (délai initial, cadence, modèle, quantité).
     *
     * @param nbTicInitiale nombre de ticks à attendre avant le premier spawn
     * @param nbTicSpawn nombre de ticks entre deux génération après le premier
     * @param ennemi modèle d’ennemi à cloner à chaque apparition
     * @param nbEnnemi nombre total d’ennemis à générer
     */
    public Generateur(int nbTicInitiale, int nbTicSpawn, Ennemi ennemi, int nbEnnemi) {
        this.heure = 0;
        this.nbTicInitiale = nbTicInitiale;
        this.nbTicSpawn = nbTicSpawn;
        this.ennemi = ennemi;
        this.nbEnnemi = nbEnnemi;
    }

    /**
     * Associe le jeu recevant les ennemis générés.
     *
     * @param parent instance du jeu
     */
    public void setParent(Jeu parent) {
        this.parent = parent;
    }

    /**
     * Indique s’il reste des ennemis à générer.
     *
     * @return true s’il reste au moins un ennemi à produire, sinon false
     */
    public boolean estVivant() {
        return 0 < nbEnnemi;
    }

    /**
     * Avance d’un tick : déclenche un spawn selon la phase (initiale puis régulière).
     * Le compteur est remis à zéro (par soustraction) à chaque apparition.
     */
    public void avance() {
        if (!phaseInitiale) {
            // Phase régulière : une apparition toutes nbTicSpawn
            if (heure == nbTicSpawn && 0 < nbEnnemi) {
                heure -= nbTicSpawn;
                // Clone du modèle
                parent.ennemis.add(new Ennemi(ennemi));
                --nbEnnemi;
            }
        } else {
            // Phase initiale : première apparition après nbTicInitiale
            if (heure == nbTicInitiale) {
                heure -= nbTicInitiale;
                // Phase régulière
                phaseInitiale = false;
                parent.ennemis.add(new Ennemi(ennemi));
                --nbEnnemi;
            }
        }
        ++heure;
    }
}
