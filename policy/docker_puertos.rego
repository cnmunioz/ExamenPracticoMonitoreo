package main
import rego.v1

# Validacion de los puertos expuestos, que sean correctos
deny contains msg if {
    some i
    lower(input.dockerfile[0][i].Cmd) == "expose"
    not puerto_valido(input.dockerfile[0][i].Value)
    msg := sprintf("Puerto %v no es valido, solo se permiten valores de 8083 y 8084", [input.dockerfile[0][i].Value])
}

puerto_valido(p) if {
    p == "8083"
}

puerto_valido(p) if {
    p == "8084"
}