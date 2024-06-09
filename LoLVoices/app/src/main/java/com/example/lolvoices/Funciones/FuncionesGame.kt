package com.example.lolvoices.Funciones

/**
 * Comprueba si se ha acabado la partida
 * @param erroresJugadores Lista con los errores de los jugadores
 * @param numJugadores Número de jugadores
 * @return Booleano que indica si se ha acabado la partida
 */
fun comprobarFin(erroresJugadores: MutableList<Int>, numJugadores: Int) : Boolean{
    var showDialog = false
    // Si todos los jugadores han fallado 3 veces, se muestra el dialogo de fin de partida
    if (erroresJugadores.count { it >= 3 } == numJugadores ) {
        showDialog = true
    }
    return showDialog
}

/**
 * Maneja el turno de los jugadores
 * @param searchText Texto introducido por el jugador
 * @param campeon Campeón actual
 * @param puntuacionesJugadores Lista con las puntuaciones de los jugadores
 * @param erroresJugadores Lista con los errores de los jugadores
 * @param jugadorActual Jugador actual
 * @param numJugadores Número de jugadores
 * @param sigTurno Booleano que indica si es el turno del siguiente jugador
 * @return Triple con el booleano que indica si es el turno del siguiente jugador, el jugador actual
 * y si se debe mostrar el dialogo de fin de partida
 *
 */
fun manejarTurno(
    searchText: String,
    campeon: String,
    puntuacionesJugadores: MutableList<Int>,
    erroresJugadores: MutableList<Int>,
    jugadorActual: Int,
    numJugadores: Int,
    sigTurno: Boolean
): Triple<Boolean, Int, Boolean>{

    var jugadorActualLocal = jugadorActual
    var sigTurnoLocal = sigTurno
    var showDialog = false

    // Si es el turno del jugador local
    if (!sigTurnoLocal) {
        // Si el texto introducido es igual al campeón actual, se suman 100 puntos
        if (searchText.equals(campeon, ignoreCase = true)) {
            puntuacionesJugadores[jugadorActualLocal] += 100
        } else {
            // Si no, se restan 50 puntos
            puntuacionesJugadores[jugadorActualLocal] -= 50
            erroresJugadores[jugadorActualLocal] += 1
        }
        // Se pasa el turno al siguiente jugador
        sigTurnoLocal = true
    } else {
        // Si es el turno del siguiente jugador
        sigTurnoLocal = false
        // Si es el último jugador, se vuelve al primero
        if (jugadorActualLocal == numJugadores - 1) {
            jugadorActualLocal = 0
            showDialog = comprobarFin(erroresJugadores, numJugadores)
        } else {
            // Si no, se pasa al siguiente jugador
            jugadorActualLocal++
        }
    }
    return Triple(sigTurnoLocal, jugadorActualLocal, showDialog)
}