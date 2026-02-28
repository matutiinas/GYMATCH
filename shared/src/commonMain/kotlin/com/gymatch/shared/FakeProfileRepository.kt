package com.gymatch.shared

class FakeProfileRepository {
  fun getDiscoverProfiles(): List<Profile> = listOf(
    Profile("1","Laura",27,"Running","Intermedio","Busco compañero para series y café después ☕"),
    Profile("2","Dani",29,"Gym","Avanzado","Objetivo: 100kg press banca. Spotter bienvenido."),
    Profile("3","Marta",25,"CrossFit","Intermedio","WODs y luego brunch."),
    Profile("4","Sergio",31,"Ciclismo","Avanzado","Z2, puertos y música."),
    Profile("5","Paula",26,"Yoga","Intermedio","Flow suave + movilidad."),
    Profile("6","Álex",28,"Pádel","Intermedio","Partidito y cañas."),
    Profile("7","Nerea",24,"Natación","Intermedio","Técnica y resistencia."),
    Profile("8","Javi",30,"Trail","Avanzado","Domingos de montaña."),
    Profile("9","Claudia",27,"Pilates","Principiante","Empiezo y quiero constancia."),
    Profile("10","Hugo",29,"Boxeo","Intermedio","Sombra, saco y buen rollo.")
  )
}
