package hu.alkfejl.dao;

import hu.alkfejl.model.Zene;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ZeneMemoryImpl implements ZeneDAO{
    private ArrayList<Zene> db = new ArrayList<>();

    public ZeneMemoryImpl(String s) {}

    @Override
    public boolean add(Zene zene) {
        var res = db.stream().filter( z ->
                zene.getCim().equals(z.getCim())
        ).toList();
        if ( ! res.isEmpty() ) { return false; }

        db.add(zene);

        return true;
    }

    @Override
    public List<Zene> find(Zene filter) {
        return db.stream().filter( z ->
                (filter.getCim() == null || filter.getCim().equals(z.getCim())) &&
                (filter.getEloado() == null || filter.getEloado().equals(z.getEloado())) &&
                (filter.getHossz() < 0 || filter.getHossz() == z.getHossz() )&&
                (filter.getStilus() == null || filter.getStilus().equals(z.getStilus()))
        ).toList();
    }
}
