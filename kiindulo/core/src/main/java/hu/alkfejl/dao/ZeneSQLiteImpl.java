package hu.alkfejl.dao;

import hu.alkfejl.model.Zene;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ZeneSQLiteImpl implements ZeneDAO {
    enum Query {
        INSERT("INSERT INTO Zene (cim, eloado, stilus, hossz) VALUES (?,?,?,?)"),
        FILTER_MULTI("SELECT * FROM Zene WHERE 1 = 1");

        private final String queryString;
        Query(String q) { queryString = q; }
        @Override
        public String toString() {
            return queryString;
        }
    }
    private final String dbURL;

    public ZeneSQLiteImpl(String dbPath) {
        try {
            Class.forName("org.sqlite.JDBC");
            dbURL = dbPath;
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean add(Zene zene) {
        boolean result = false;
        try (Connection conn = DriverManager.getConnection(dbURL);
             PreparedStatement ps = conn.prepareStatement(Query.INSERT.queryString)
        ) {
            int index = 1;
            ps.setString(index++, zene.getCim());
            ps.setString(index++, zene.getEloado());
            ps.setString(index++, zene.getStilus());
            ps.setInt(index++, zene.getHossz());

            int rows = ps.executeUpdate();
            if ( rows == 1 ) {
                result = true;
            }
        } catch (SQLException e) {
            result = false;
            System.err.println("SQLError in Zene add: " + e.getMessage());
        }

        return result;
    }

    @Override
    public List<Zene> find(Zene zene) {
        var results = new ArrayList<Zene>();
        StringBuilder multiQuery = new StringBuilder(Query.FILTER_MULTI.queryString);
        if ( zene.getCim() != null ) {
            multiQuery.append(" AND cim = ?");
        }
        if ( zene.getHossz() > 0 ) {
            multiQuery.append(" AND hossz = ?");
        }
        if ( zene.getEloado() != null ) {
            multiQuery.append(" AND eloado = ?");
        }
        if ( zene.getStilus() != null ) {
            multiQuery.append(" AND stilus = ?");
        }

        try (Connection conn = DriverManager.getConnection(dbURL);
            PreparedStatement ps = conn.prepareStatement(multiQuery.toString())
        ) {
            int index = 1;
            if ( zene.getCim() != null ) {
                ps.setString(index++, zene.getCim());
            }
            if ( zene.getHossz() > 0 ) {
                ps.setInt(index++, zene.getHossz());
            }
            if ( zene.getEloado() != null ) {
                ps.setString(index++, zene.getEloado());
            }
            if ( zene.getStilus() != null ) {
                ps.setString(index++, zene.getStilus());
            }

            ResultSet rs = ps.executeQuery();
            while( rs.next() ) {
                results.add( retriveUtazasFromRS( rs ) );
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return results;
    }

    private Zene retriveUtazasFromRS(ResultSet rs) throws SQLException {
        return new Zene()
                .setCim(rs.getString("cim"))
                .setEloado(rs.getString("eloado"))
                .setStilus("stilus")
                .setHossz(rs.getInt("hossz"));
    }
}
