package com.unquindisoft.colpensionex.model;

public class PersonaPrioritaria implements Comparable<PersonaPrioritaria> {
    private String id;
    private String nombre;
    private String apellido;
    private int edad;  // Ahora usamos edad para la comparación

    // Constructor
    public PersonaPrioritaria(String id, String nombre, String apellido, int edad) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.edad = edad;
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public int getEdad() {
        return edad;
    }

    // Método compareTo para ordenar por edad (personas menores de 35 años tendrán mayor prioridad)
    @Override
    public int compareTo(PersonaPrioritaria otraPersona) {
        // Primero comparo por edad, las personas menores de 35 tendrán prioridad
        if (this.edad < 35 && otraPersona.edad >= 35) {
            return -1;  // Esta persona debe tener mayor prioridad
        } else if (this.edad >= 35 && otraPersona.edad < 35) {
            return 1;  // Otra persona tiene mayor prioridad
        } else {
            // Si ambas son menores de 35 o ambas mayores de 35, comparamos por edad ascendente
            return Integer.compare(this.edad, otraPersona.edad);
        }
    }

    // Método toString para imprimir la información
    @Override
    public String toString() {
        return "ID: " + id + ", Nombre: " + nombre + " " + apellido + ", Edad: " + edad;
    }
}
