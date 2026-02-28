package com.gymatch.shared

interface ProfileRepository {
    suspend fun getDiscoverProfiles(): List<Profile>
}

class FakeProfileRepository : ProfileRepository {
    override suspend fun getDiscoverProfiles(): List<Profile> = listOf(
        Profile("1", "Álvaro", 27, "gym", "intermedio", "Busco partner para fuerza 4 días/semana."),
        Profile("2", "Lucía", 24, "running", "avanzado", "Me preparo para mi próxima media maratón."),
        Profile("3", "Marcos", 30, "crossfit", "intermedio", "Entreno WOD por las mañanas."),
        Profile("4", "Paula", 26, "padel", "principiante", "Quiero mejorar técnica y constancia."),
        Profile("5", "Sergio", 33, "tenis", "intermedio", "Partidos entre semana y cardio extra."),
        Profile("6", "Nerea", 29, "ciclismo", "avanzado", "Salidas largas los domingos."),
        Profile("7", "Iván", 25, "natación", "intermedio", "Objetivo: bajar tiempos en 1.000m."),
        Profile("8", "Marta", 31, "gym", "avanzado", "Entreno hipertrofia y movilidad."),
        Profile("9", "Diego", 28, "running", "principiante", "Empecé hace 2 meses, motivado."),
        Profile("10", "Clara", 27, "crossfit", "avanzado", "Compito en throwdowns locales."),
        Profile("11", "Rubén", 35, "padel", "intermedio", "Busco compañero para ligas amateurs."),
        Profile("12", "Elena", 23, "tenis", "principiante", "Aprendiendo saque y consistencia."),
        Profile("13", "Héctor", 32, "ciclismo", "intermedio", "Rodillos entre semana, puerto el sábado."),
        Profile("14", "Noelia", 30, "natación", "avanzado", "Serie técnica + fondo en piscina."),
        Profile("15", "Javier", 26, "gym", "principiante", "Quiero ganar músculo con buena rutina."),
        Profile("16", "Sara", 34, "running", "intermedio", "Me encantan los intervalos y el trail suave."),
        Profile("17", "Adrián", 29, "crossfit", "principiante", "Busco grupo para mejorar halterofilia."),
        Profile("18", "Ainhoa", 28, "padel", "avanzado", "Juego torneos de fin de semana."),
        Profile("19", "Pablo", 31, "tenis", "avanzado", "Entreno físico + pista 5 días."),
        Profile("20", "Carla", 25, "ciclismo", "principiante", "Ruta corta al salir del trabajo."),
    )
}
