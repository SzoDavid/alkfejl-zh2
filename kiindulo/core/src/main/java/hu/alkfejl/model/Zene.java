package hu.alkfejl.model;

import java.util.Objects;

/**
 * Modell osztály default konstruktorral és setter/getter fgv-ekel.
 * */
public class Zene {
    private String cim;
    private String stilus;
    private String eloado;
    private int hossz = -1;

    /**
     * A cim a kulcs!
     * */
    public String getCim() {
        return cim;
    }

    /**
     * A cim a kulcs!
     * */
    public Zene setCim(String cim) {
        this.cim = cim;
        return this;
    }

    public String getStilus() {
        return stilus;
    }

    public Zene setStilus(String stilus) {
        this.stilus = stilus;
        return this;
    }

    public String getEloado() {
        return eloado;
    }

    public Zene setEloado(String eloado) {
        this.eloado = eloado;
        return this;
    }

    public int getHossz() {
        return hossz;
    }

    public Zene setHossz(int hossz) {
        this.hossz = hossz;
        return this;
    }

    @Override
    public String toString() {
        return "Zene [cim=" + cim + ", eloado=" + eloado + ", hossz=" + hossz + ", stilus=" + stilus + "]";
    }
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Zene zene) {
            return hossz == zene.hossz &&
                    Objects.equals(cim, zene.cim) &&
                    Objects.equals(eloado, zene.eloado) &&
                    Objects.equals(stilus, zene.stilus);
        }
        return false;
    }
    @Override
    public int hashCode() {
        return Objects.hash(cim, eloado, hossz, stilus);
    }
}
