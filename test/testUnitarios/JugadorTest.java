package testUnitarios;

import org.junit.Assert;
import org.junit.Test;

import modelo.casillero.compania.Aysa;
import modelo.casillero.compania.Subte;
import modelo.casillero.compania.Tren;
import modelo.casillero.especial.AvanceDinamico;
import modelo.casillero.especial.Carcel;
import modelo.casillero.especial.ImpuestoDeLujo;
import modelo.casillero.especial.Policia;
import modelo.casillero.especial.Quini6;
import modelo.casillero.especial.RetrocesoDinamico;
import modelo.casillero.terrenos.BuenosAiresSur;
import modelo.casillero.terrenos.CordobaNorte;
import modelo.casillero.terrenos.Neuquen;
import modelo.casillero.terrenos.SaltaSur;
import modelo.casillero.terrenos.SantaFe;
import modelo.casillero.terrenos.Tucuman;
import modelo.excepciones.CapitalInsuficienteError;
import modelo.excepciones.NoEstasEncarceladoException;
import modelo.excepciones.NoPuedePagarFianzaException;
import modelo.excepciones.NoSePuedeDesplazarJugadorEncarceladoException;
import modelo.jugador.Jugador;

public class JugadorTest {

	@Test
	public void testUnJugadorSeCreaConUnCiertoNombrePasadoComoParametro() {
		Jugador jugador = new Jugador("Ariel");
		Assert.assertEquals(jugador.tuNombreEs(), "Ariel");
	}

	@Test
	public void testUnJugadorSeCreaConCienMilPesosDeDineroDisponible() {
		Jugador jugador = new Jugador("Ariel");
		Assert.assertEquals(jugador.capitalTotal(), 100000);
	}

	@Test
	public void testUnJugadorCaeEnQuini6PorPrimeraVezYSuCapitalAumentaEn50000() {
		Jugador jugador = new Jugador("Ariel");
		Quini6 quini6 = Quini6.getQuini6();
		jugador.caerEnCasillero(quini6);
		Assert.assertEquals(jugador.capitalTotal(), 150000);
	}

	@Test
	public void testUnJugadorCaeEnQuini6PorSegundaVezYSuCapitalAumentaEn30000() {
		Jugador jugador = new Jugador("Gonzalo");
		Quini6 quini6 = Quini6.getQuini6();
		jugador.caerEnCasillero(quini6);
		jugador.caerEnCasillero(quini6);
		Assert.assertEquals(180000, jugador.capitalTotal());
	}

	@Test
	public void testUnJugadorCaeEnQuini6PorTerceraVezYSuCapitalNoAumenta() {
		Jugador jugador = new Jugador("Ariel");
		Quini6 quini6 = Quini6.getQuini6();
		jugador.caerEnCasillero(quini6);
		jugador.caerEnCasillero(quini6);
		jugador.caerEnCasillero(quini6);
		Assert.assertEquals(180000, jugador.capitalTotal());
	}

	@Test
	public void testUnJugadorCaeEnUnTerrenoYAlComprarloSeVuelveElPropietario() {
		Jugador jugador = new Jugador("Gonzalo");
		BuenosAiresSur bsasSur = BuenosAiresSur.getBuenosAiresSur();
		jugador.caerEnCasillero(bsasSur);
		jugador.comprar(bsasSur);
		Assert.assertEquals(jugador, bsasSur.propietario());
	}

	@Test (expected = NoSePuedeDesplazarJugadorEncarceladoException.class)
	public void testUnJugadorCaeEnLaCarcelIntentaDesplazarse2LugaresSaltaNoSePuedeDesplazarJugadorEnCarcelException() {
		Jugador jugador = new Jugador("Ariel");
		Carcel carcel = Carcel.getCarcel();
		jugador.caerEnCasillero(carcel);
		jugador.avanzar(2);
	}
	
	
	@Test 
	public void testUnJugadorCaeEnLaCarcelYPuedePagarLaFianzaSiElTurnoEsDosOTres()  {
		Jugador jugador = new Jugador("Gonzalo");
		Carcel carcel = Carcel.getCarcel();
		jugador.caerEnCasillero(carcel);
		jugador.esTuTurno(); // En este turno no puede realizar ninguna accion.
		jugador.esTuTurno();
		try {
			jugador.pagarFianza();
		}
		catch(NoPuedePagarFianzaException e) {
			Assert.assertTrue(false);
		}
	}
	
	@Test
	public void testUnJugadorNoPuedePagarLaFianzaSiElTurnoEsUno()  {
		Jugador jugador = new Jugador("Ariel");
		Carcel carcel = Carcel.getCarcel();
		jugador.caerEnCasillero(carcel);
		jugador.esTuTurno();
		try {
			jugador.pagarFianza();
		}
		catch(NoPuedePagarFianzaException e) {
			Assert.assertTrue(true);
		}
	}
	
	@Test 
	public void testUnJugadorPuedePagarLaFianzaSiEsElTercerTurnoQueEstaEnLaCarcel() {
		Jugador jugador = new Jugador("Ariel");
		Carcel carcel = Carcel.getCarcel();
		jugador.caerEnCasillero(carcel);
		jugador.esTuTurno();
		jugador.esTuTurno();
		jugador.esTuTurno();
		try {
			jugador.pagarFianza();
		}
		catch(NoPuedePagarFianzaException e) {
			Assert.assertTrue(false);
		}
	}
	
	@Test 
	public void testUnJugadorEnLibertadNoPuedePagarLaFianza() {
		Jugador jugador = new Jugador("Ariel");
		try {
			jugador.pagarFianza();
		}
		catch(NoEstasEncarceladoException e) {
			Assert.assertTrue(true);
		}
	}
	
	@Test
	public void testUnJugadorEncarceladoIntentaDesplazarse5CasillerosYSeLanzaNoSePuedeDesplazarJugadorEnCarcelException() {
		Jugador jugador = new Jugador("Ariel");
		jugador.encarcelar();
		try {
			jugador.avanzar(5);
		}
		catch(NoSePuedeDesplazarJugadorEncarceladoException e) {
			Assert.assertTrue(true);
		}
	}
	
	@Test
	public void testUnJugadorQueCaeEnLaCarcelQuedaLibreAlCuartoTurnoEntoncesPuedeDesplazarse() {
		Jugador jugador = new Jugador("Ariel");
		Carcel carcel = Carcel.getCarcel();
		jugador.caerEnCasillero(carcel);
		jugador.esTuTurno();
		jugador.esTuTurno();
		jugador.esTuTurno();
		jugador.esTuTurno();
		try {
			jugador.avanzar(5);	
		}
		catch(NoSePuedeDesplazarJugadorEncarceladoException e) {
			Assert.assertTrue(false);
		}
	}
	
	@Test
	public void testUnJugadorNoPuedePagarLaFianzaPorFaltaDeFondosSiendoElTurnoDosOTres () {
		Jugador jugador = new Jugador("Ariel", 40000); // Este constructor es solo para testear casos de saldo insuficiente
		Carcel carcel = Carcel.getCarcel();
		jugador.caerEnCasillero(carcel);
		jugador.esTuTurno();
		jugador.esTuTurno();
		try {
			jugador.pagarFianza();
		}
		catch(CapitalInsuficienteError e) {
			Assert.assertTrue(true);
		}
	}
	
	@Test
	public void testUnJugadorCaeEnAvanceDinamicoHabiendoSumadoDosEntoncesAvanzaCeroLugares () {
		Jugador jugador = new Jugador("Ariel");
		jugador.obtuvo(2); // Este mensaje recibira una tirada de dados aleatoria como paramtero
		jugador.caerEnCasillero(AvanceDinamico.getAvanceDinamico());
		Assert.assertEquals(AvanceDinamico.getAvanceDinamico(), jugador.casilleroActual());		
	}
	
	@Test
	public void testUnJugadorCaeEnAvanceDinamicoHabiendoSumadoTresEntoncesAvanzaUnLugar () {
		Jugador jugador = new Jugador("Ariel");
		Subte subte = Subte.getSubte();
		jugador.obtuvo(3);
		jugador.caerEnCasillero(AvanceDinamico.getAvanceDinamico());
		Assert.assertEquals(subte, jugador.casilleroActual());
	}
	
	@Test
	public void testUnJugadorCaeEnAvanceDinamicoHabiendoSumadoCuatroEntoncesAvanzaDosLugares () {
		Jugador jugador = new Jugador("Ariel");
		CordobaNorte cn = CordobaNorte.getCordobaNorte();
		jugador.obtuvo(4);
		jugador.caerEnCasillero(AvanceDinamico.getAvanceDinamico());
		Assert.assertEquals(cn, jugador.casilleroActual());
	}
	
	@Test
	public void testUnJugadorCaeEnAvanceDinamicoHabiendoSumadoCincoEntoncesAvanzaTresLugares () {
		Jugador jugador = new Jugador("Ariel");
		jugador.obtuvo(5);
		jugador.caerEnCasillero(AvanceDinamico.getAvanceDinamico());
		Assert.assertEquals(ImpuestoDeLujo.getImpuestoDeLujo(), jugador.casilleroActual());
	}
	
	@Test
	public void testUnJugadorCaeEnAvanceDinamicoHabiendoSumadoSeisEntoncesAvanzaCuatroLugares () {
		Jugador jugador = new Jugador("Ariel");
		jugador.obtuvo(6);
		jugador.caerEnCasillero(AvanceDinamico.getAvanceDinamico());
		Assert.assertEquals(SantaFe.getSantaFe(), jugador.casilleroActual());
	}
	
	@Test
	public void testUnJugadorCaeEnAvanceDinamicoHabiendoSumadoSieteEntoncesAvanza100000mod7 () {
		// 100000 % 7 = 5
		Jugador jugador = new Jugador("Ariel");
		jugador.obtuvo(7);
		jugador.caerEnCasillero(AvanceDinamico.getAvanceDinamico());
		Assert.assertEquals(Aysa.getAysa(), jugador.casilleroActual());
	}
	
	@Test
	public void testUnJugadorCaeEnAvanceDinamicoHabiendoSumadoOchoEntoncesAvanza100000mod8 () {
		// 100000 % 8 = 0
		Jugador jugador = new Jugador("Ariel");
		AvanceDinamico ad = AvanceDinamico.getAvanceDinamico();
		jugador.obtuvo(8);
		jugador.caerEnCasillero(AvanceDinamico.getAvanceDinamico());
		Assert.assertEquals(ad, jugador.casilleroActual());
	}
	
	@Test
	public void testUnJugadorCaeEnAvanceDinamicoHabiendoSumadoNueveEntoncesAvanza100000mod9 () {
		// 100000 % 9 = 1
		Jugador jugador = new Jugador("Ariel");
		Subte subte = Subte.getSubte();
		jugador.obtuvo(9);
		jugador.caerEnCasillero(AvanceDinamico.getAvanceDinamico());
		Assert.assertEquals(subte, jugador.casilleroActual());
	}
	
	@Test
	public void testUnJugadorCaeEnAvanceDinamicoHabiendoSumadoDiezEntoncesAvanza100000mod10 () {
		// 100000 % 10 = 0
		Jugador jugador = new Jugador("Ariel");
		AvanceDinamico ad = AvanceDinamico.getAvanceDinamico();
		jugador.obtuvo(10);
		jugador.caerEnCasillero(AvanceDinamico.getAvanceDinamico());
		Assert.assertEquals(ad, jugador.casilleroActual());
	}
	
	@Test
	public void testUnJugadorSinPropiedadesCaeEnAvanceDinamicoHabiendoSumadoOnceEntoncesAvanzaOnceCaeEnRetrocesoDinamicoYRetrocedeHastaSantaFe () {
		Jugador jugador = new Jugador("Ariel");
		jugador.obtuvo(11);
		jugador.caerEnCasillero(AvanceDinamico.getAvanceDinamico());
		Assert.assertEquals(CordobaNorte.getCordobaNorte(), jugador.casilleroActual());
	}
	
	@Test
	public void testUnJugadorSinPropiedadesCaeEnAvanceDinamicoHabiendoSumadoDoceEntoncesAvanzaDoce () {
		Jugador jugador = new Jugador("Ariel");
		jugador.obtuvo(12);
		jugador.caerEnCasillero(AvanceDinamico.getAvanceDinamico());
		Assert.assertEquals(Tucuman.getTucuman(), jugador.casilleroActual());
	}
	
	@Test
	public void testUnJugadorConTresPropiedadesCaeEnAvanceDinamicoHabiendoSumadoDoceEntoncesAvanzaNueve () throws CapitalInsuficienteError {
		Jugador jugador = new Jugador("Ariel");
		jugador.comprar(Neuquen.getNeuquen());
		jugador.comprar(SantaFe.getSantaFe());
		jugador.comprar(SaltaSur.getSaltaSur());
		jugador.obtuvo(12);
		jugador.caerEnCasillero(AvanceDinamico.getAvanceDinamico());
		Assert.assertEquals(Tren.getTren(), jugador.casilleroActual());
	}
	
	@Test
	public void testUnJugadorCaeEnRetrocesoDinamicoHabiendoSumadoDoceEntoncesRetrocede10HastaSubte() {
		Jugador jugador = new Jugador("Lucas");
		jugador.obtuvo(12);
		jugador.caerEnCasillero(RetrocesoDinamico.getRetrocesoDinamico());
		Assert.assertEquals(Subte.getSubte(), jugador.casilleroActual());
	}
	
	@Test
	public void testUnJugadorCaEnRetrocesoDinamicoHabiendoSumado11EntoncesRetrocede9HastaCordobaNorte() {
		Jugador jugador = new Jugador("Uriel");
		jugador.obtuvo(11);
		jugador.caerEnCasillero(RetrocesoDinamico.getRetrocesoDinamico());
		Assert.assertEquals(CordobaNorte.getCordobaNorte(), jugador.casilleroActual());
	}
	
	@Test
	public void testUnJugadorCaeEnRetrocesoDinamicoHabiendoSumado10EntoncesRetrocede100000mod10() {
		Jugador jugador = new Jugador("Damian");
		jugador.obtuvo(10);
		jugador.caerEnCasillero(RetrocesoDinamico.getRetrocesoDinamico());
		Assert.assertEquals(RetrocesoDinamico.getRetrocesoDinamico(),jugador.casilleroActual());
	}
	
	@Test 
	public void testUnJugadorCaeEnRetrocesoDinamicoHabiendoSumado2EntoncesRetrocede2CasillerosAlTener0Propiedades() {
		Jugador jugador = new Jugador("Esteban");
		jugador.obtuvo(2);
		jugador.caerEnCasillero(RetrocesoDinamico.getRetrocesoDinamico());
		Assert.assertEquals(RetrocesoDinamico.getRetrocesoDinamico(), jugador.casilleroActual());
	}
	
	@Test
	public void testUnJugadorCaeEnLaPoliciaYPasaAEstarEncarcelado() {
		Jugador jugador = new Jugador("Amado Budou");
		jugador.caerEnCasillero(Policia.getPolicia());
		try {
			jugador.avanzar(5);	
		}
		catch(NoSePuedeDesplazarJugadorEncarceladoException e) {
			Assert.assertTrue(true);
		}
	}
	
	@Test
	public void testUnJugadorCaeEnLaPoliciaYSuUbicacionActualEsLaCarcel() {
		Jugador jugador = new Jugador("Amado Budou");
		jugador.caerEnCasillero(Policia.getPolicia());
		Assert.assertEquals(Carcel.getCarcel(), jugador.casilleroActual());
	}
	
	// HASTA ACA LOS TEST DE LA PRIMERA ENTREGA

}
