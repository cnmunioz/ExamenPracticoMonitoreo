package main
import rego.v1

deny contains msg if {
    some i
    lower(input.dockerfile[0][i].Cmd) == "user"
    input.dockerfile[0][i].Value == "root"
    msg := "No se permite ejecutar el contenedor como root (USER root)"
}