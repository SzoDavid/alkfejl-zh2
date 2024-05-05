package hu.alkfejl.dao;

import hu.alkfejl.model.Zene;

import java.util.List;

public interface ZeneDAO {

    /**
     * Hozzáad az adatbázishoz.
     * @param zene a kapott zene objektum amit beszúr az adatbázisba
     *             A zenének a címe egyedi, így ha olyat adunk meg ami már létezik, akár más stílusban is, akkor hibát kapunk.
     * */
    boolean add(Zene zene);
    /**
     * Listázza a zenéket.
     * @param filter filter objektum aminek a mintájára mindent kilistáz ami illeszkedik a filterre.
     *               Amire szűrni akarunk, azt állítsuk be a filter objektumnak.
     *               A zenének a címe egyedi, így ha azt beállítjuk csak egy zenét kapunk
     * */
    List<Zene> find(Zene filter);
}
