package main
import rego.v1

# Asegurarse de que se use multistage build
deny contains msg if {
    not es_multistage
    msg := "Dockerfile no usa multistage build (es necesario mas de un FROM)"
}

es_multistage if {
    count([1 |
      some i
      lower(input.dockerfile[0][i].Cmd) == "from"
    ]) > 1
}