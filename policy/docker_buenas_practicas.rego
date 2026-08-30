package main
import rego.v1

# No usar tag latest
deny contains msg if {
    some i
    lower(input.dockerfile[0][i].Cmd) == "from"
    endswith(lower(input.dockerfile[0][i].Value), ":latest")
    msg := "No se debe usar el tag 'latest' en la instruccion FROM"
}

# Dockerfile deberia exponer al menos un puerto
deny contains msg if {
    not algun_expose
    msg := "El dockerfile no expone ningun puerto (EXPOSE)"
}

algun_expose if {
    some i
    lower(input.dockerfile[0][i].Cmd) == "expose"
}