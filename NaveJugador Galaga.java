package entidades;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;
import excepciones.FueraDeLimitesException;
import excepciones.SinMunicionException;
import interfaces.Disparable;

public class NaveJugador extends Nave implements Disparable {
    private int puntaje;
    private int municion;
    private boolean escopetaDesbloqueada = false;
    private int acumuladorPuntosVida = 0;

    public NaveJugador(int x, int y) {
        super(x, y, 46, 34, 5, 6);
        this.municion = 60;
    }

    public int getPuntaje() { return puntaje; }
    public int getMunicion() { return municion; }

    public boolean sumarPuntos(int p) {
        puntaje += p;
        acumuladorPuntosVida += p;
        boolean excedeMaxVidas = false;
        
        while (acumuladorPuntosVida >= 1500) {
            if (curar(1)) {
                excedeMaxVidas = true;
            }
            acumuladorPuntosVida -= 1500;
        }
        return excedeMaxVidas;
    }

    public void recargarMunicion(int c) { municion += c; }
    public void setEscopetaDesbloqueada(boolean b) { this.escopetaDesbloqueada = b; }
    public boolean isEscopetaDesbloqueada() { return escopetaDesbloqueada; }

    public void moverIzquierda() throws FueraDeLimitesException { mover(-getVelocidad(), 0); }
    public void moverDerecha() throws FueraDeLimitesException { mover(getVelocidad(), 0); }

    @Override
    public List<Proyectil> disparar() throws SinMunicionException {
        if (municion <= 0) {
            throw new SinMunicionException("No tienes munición");
        }
        
        List<Proyectil> ráfaga = new ArrayList<>();
        municion--;

        if (escopetaDesbloqueada) {
            int centroX = getX() + getAncho() / 2 - 3;
            int posY = getY() - 5;
            // Ráfaga en abanico: Izquierda (-3 velX), Centro (0 velX), Derecha (+3 velX)
            ráfaga.add(new Proyectil(centroX, posY, -3, -1, true, 1));
            ráfaga.add(new Proyectil(centroX, posY, 0, -1, true, 1));
            ráfaga.add(new Proyectil(centroX, posY, 3, -1, true, 1));
        } else {
            ráfaga.add(new Proyectil(getX() + getAncho() / 2 - 3, getY(), 0, -1, true, 1));
        }
        return ráfaga;
    }

    @Override
    public void actualizar() { /* El jugador se mueve con el teclado */ }

    @Override
    public void dibujar(Graphics g) {
        int[] xPoints = { getX() + getAncho() / 2, getX(), getX() + getAncho() };
        int[] yPoints = { getY(), getY() + getAlto(), getY() + getAlto() };
        g.setColor(escopetaDesbloqueada ? Color.ORANGE : Color.GREEN);
        g.fillPolygon(xPoints, yPoints, 3);

        g.setColor(Color.WHITE);
        g.fillOval(getX() + getAncho() / 2 - 5, getY() + 10, 10, 12);

        g.setColor(Color.ORANGE);
        g.fillRect(getX() + 8, getY() + getAlto(), 7, 6);
        g.fillRect(getX() + getAncho() - 15, getY() + getAlto(), 7, 6);
    }
}