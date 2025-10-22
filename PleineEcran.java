package logiciel.ecran;

import javax.swing.*;
import java.awt.*;

/**
 * Fenêtre plein écran du jeu.
 */
public class PleineEcran extends JFrame {
    /**
     * Construit une fenêtre en plein écran. Ajoute le panneau du jeu.
     *
     * @throws HeadlessException si l'environnement ne dispose pas d'affichage
     */
    public PleineEcran() throws HeadlessException {
        setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );

        GraphicsDevice gDevice = GraphicsEnvironment
                .getLocalGraphicsEnvironment().getDefaultScreenDevice();

        if( gDevice.isFullScreenSupported() ) {
            gDevice.setFullScreenWindow( this );
        } else {
            System.err.println( "Mode plein écran non supporte." );
            System.exit( -1 );
        }
        // setUndecorated( true );
        add( new Jeu( getSize() ) );

    }
}
