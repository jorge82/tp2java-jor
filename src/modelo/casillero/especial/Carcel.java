package modelo.casillero.especial;

import modelo.casillero.Casillero;
import modelo.excepciones.CapitalInsuficienteError;
import modelo.jugador.Jugador;

public class Carcel extends Casillero {

	private int precioFianza;

	
	public Carcel() {
		this.precioFianza = 45000;

	}
	
	@Override
	public void afectarJugador(Jugador unJugador) {
		unJugador.encarcelar();
	}

	public void cobrarFianza(Jugador jugador) throws CapitalInsuficienteError {
		jugador.extraerDinero(this.precioFianza);
		jugador.liberar();		
	}

	
}