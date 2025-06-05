package model;

import com.fasterxml.jackson.annotation.JsonProperty; // Importa la anotación para mapeo de propiedades JSON

public class Cuenta {
    // Usamos @JsonProperty para mapear los nombres del JSON a los atributos de la clase
    @JsonProperty("estado")
    private boolean estado;
    @JsonProperty("nro_cuenta")
    private long nroCuenta; // Usamos long para el número de cuenta si es grande
    @JsonProperty("saldo")
    private double saldo;
    @JsonProperty("banco")
    private String banco;

    // Constructor por defecto (necesario para Jackson)
    public Cuenta() {
    }

    // Constructor con todos los campos (opcional, pero útil)
    public Cuenta(boolean estado, long nroCuenta, double saldo, String banco) {
        this.estado = estado;
        this.nroCuenta = nroCuenta;
        this.saldo = saldo;
        this.banco = banco;
    }

    // --- Getters y Setters (Jackson los necesita para leer y escribir) ---
    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public long getNroCuenta() {
        return nroCuenta;
    }

    public void setNroCuenta(long nroCuenta) {
        this.nroCuenta = nroCuenta;
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    public String getBanco() {
        return banco;
    }

    public void setBanco(String banco) {
        this.banco = banco;
    }

    @Override
    public String toString() {
        return "Cuenta{" +
                "estado=" + estado +
                ", nro_cuenta=" + nroCuenta +
                ", saldo=" + saldo +
                ", banco='" + banco + '\'' +
                '}';
    }
}