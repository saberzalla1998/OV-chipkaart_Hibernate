import dao.AdresDAO;
import dao.OVChipkaartDAO;
import dao.ProductDAO;
import dao.ReizigerDAO;
import dao.hibernateDAO.AdresDAOHibernate;
import dao.hibernateDAO.OVChipkaartDAOHibernate;
import dao.hibernateDAO.ProductDAOHibernate;
import dao.hibernateDAO.ReizigerDAOHibernate;
import domain.Adres;
import domain.OVChipkaart;
import domain.Product;
import domain.Reiziger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

public class Main {

    private static final SessionFactory factory;

    static {
        try {
            factory = new Configuration().configure().buildSessionFactory();
        } catch (Throwable ex) {
            throw new ExceptionInInitializerError(ex);
        }
    }

    private static Session getSession() throws HibernateException {
        return factory.openSession();
    }

    public static void main(String[] args) throws SQLException {

        ReizigerDAO rdao = new ReizigerDAOHibernate(getSession());
        AdresDAO adao = new AdresDAOHibernate(getSession());
        OVChipkaartDAO odao = new OVChipkaartDAOHibernate(getSession());
        ProductDAO pdao = new ProductDAOHibernate(getSession());

        testReizigerDAOHibernate(rdao);
        testAdresDAOHibernate(adao, rdao);
        testOVChipkaartDAOHibernate(odao, rdao);
        testProductDAOHibernate(pdao, rdao, odao);
    }

    private static void testReizigerDAOHibernate(ReizigerDAO rdao) throws SQLException {
        System.out.println("\n---------- Test ReizigerDAOHibernate -------------");

        // ReizigerDAOHibernate.findall()
        List<Reiziger> reizigers = rdao.findAll();
        System.out.println("[Test] ReizigerDAOHibernate.findAll() geeft de volgende reizigers:");
        for (Reiziger reiziger : reizigers) {
            System.out.println(reiziger);
        }
        System.out.println();

        // ReizigerDAOHibernate.save()
        System.out.print("[Test] Eerst " + rdao.findAll().size() + " reizigers, na ReizigerDAOHibernate.save() ");
        String gbdatum = "2001-05-20";
        Reiziger John = new Reiziger(12, "J", "van", "Doe", java.sql.Date.valueOf(gbdatum));
        rdao.save(John);
        reizigers = rdao.findAll();
        System.out.println(reizigers.size() + "\n");

        // Updaten naam
        System.out.println("[Test] Eerst " + "ReizigerDAOHibernate.update();");
        John.setVoorletters("J.");
        John.setAchternaam("Doe Jr.");
        rdao.update(John);
        System.out.println(John + "\n");

        // Verwijderen reiziger
        System.out.println("[TEST] Reizigers: " + reizigers.size() + " ReizigerDAOHibernate.delete();");
        rdao.delete(John);
        reizigers = rdao.findAll();
        System.out.println(reizigers.size() + "\n");

        // Zoek reiziger op ID
        System.out.println("[TEST] ReizigerDAOHibernate.findById");
        System.out.println(rdao.findById(5));
        System.out.println();

        // Zoek reiziger op geboortedatum
        System.out.println("[Test] ReizigerDAOHibernate.findByGbdatum() geeft de volgende reizigers: ");
        System.out.println(rdao.findByGbdatum("1998-08-11"));
        System.out.println();
    }

    private static void testAdresDAOHibernate(AdresDAO adao, ReizigerDAO rdao) throws SQLException {
        System.out.println("\n---------- Test AdresDAOHibernate -------------");

        // AdresDAOHibernate.findall()
        List<Adres> adressen = adao.findAll();
        System.out.println("[Test] AdresDAOHibernate.findAll() geeft de volgende adressen:");
        for (Adres adres : adressen) {
            System.out.println(adres);
        }
        System.out.println();

        // AdresDAOHibernate.save()
        System.out.print("[Test] Eerst " + adao.findAll().size() + " adressen, na AdresDAOHibernate.save() ");

        String gbdatum = "1997-09-15";
        Reiziger Jane = new Reiziger(13, "J", "de", "Vries", java.sql.Date.valueOf(gbdatum));
        rdao.save(Jane);

        Adres nieuwAdres = new Adres(7, "1234AB", "1", "Teststraat", "Teststad", Jane);
        adao.save(nieuwAdres);
        adressen = adao.findAll();
        System.out.println(adressen.size() + "\n");

        // Updaten huisnummer
        System.out.println("[Test] Eerst " + "AdresDAOHibernate.update();");
        nieuwAdres.setHuisnummer("99");
        adao.update(nieuwAdres);
        System.out.println(nieuwAdres + "\n");

        // Zoeken op reiziger
        System.out.println("[Test] AdresDAOHibernate.findByReiziger: ");
        System.out.println(adao.findByReiziger(Jane));
        System.out.println();

        // Zoeken op adres ID
        System.out.println("[TEST] AdresDAOHibernate.findById");
        System.out.println(adao.findById(7));
        System.out.println();

        // Verwijderen van een reiziger
        System.out.println("[TEST] Adressen: " + adressen.size() + " AdresDAOHibernate.delete();");
        adao.delete(nieuwAdres);
        adressen = adao.findAll();
        System.out.println(adressen.size() + "\n");
    }

    private static void testOVChipkaartDAOHibernate(OVChipkaartDAO odao, ReizigerDAO rdao) throws SQLException {
        System.out.println("\n---------- Test OVChipkaartDAOHibernate -------------");

        // OVChipkaartDAOHibernate.findall()
        List<OVChipkaart> ovChipkaarten = odao.findAll();
        System.out.println("[Test] OVChipkaartDAOHibernate.findAll() geeft de volgende OVChipkaarten:");
        for (OVChipkaart ovChipkaart : ovChipkaarten) {
            System.out.println(ovChipkaart);
        }
        System.out.println();

        // Maak een nieuwe reiziger aan en persisteer deze in de database
        String gbdatum = "2002-03-10";
        Reiziger Lisa = new Reiziger(14, "L", "van der", "Hout", java.sql.Date.valueOf(gbdatum));

        rdao.save(Lisa);

        OVChipkaart ovChipkaart1 = new OVChipkaart(123457, Date.valueOf("2022-12-12"), 1, 100.00, Lisa);

        // Save
        System.out.print("[Test] Eerst " + ovChipkaarten.size() + " ov-chipkaarten, na OVChipkaartDAO.save() ");
        odao.save(ovChipkaart1);
        ovChipkaarten = odao.findAll();
        System.out.println(ovChipkaarten.size() + " ov-chipkaarten\n");

        // Update
        System.out.println("[Test] Eerst " + "OVChipkaart.update();");
        System.out.println("Voor wijziging: " + ovChipkaart1);
        ovChipkaart1.setSaldo(2000.00);
        odao.update(ovChipkaart1);
        System.out.println("Na wijziging: " + ovChipkaart1 + "\n");

        // Zoek reiziger
        System.out.println("[Test] OVChipkaartDAO.findByReiziger: ");
        System.out.println(odao.findByReiziger(Lisa));
        System.out.println();

        // Zoek OV-chipkaart op kaartnummer
        System.out.println("[TEST] OVChipkaartDAOHibernate.findByKaartNummer");
        System.out.println(odao.findByKaartNummer(123457));
        System.out.println();

        // Verwijder reiziger
        System.out.println("[TEST] OV-chipkaarten: " + ovChipkaarten.size() + " OVChipkaartDAOHibernate.delete();");
        odao.delete(ovChipkaart1);
        ovChipkaarten = odao.findAll();
        System.out.println(ovChipkaarten.size() + "\n");
    }

    private static void testProductDAOHibernate(ProductDAO pdao, ReizigerDAO rdao, OVChipkaartDAO odao) throws SQLException {
        System.out.println("\n---------- Test ProductDAOHibernate -------------");

        // AdresDAOHibernate.findall()
        List<Product> producten = pdao.findAll();
        System.out.println("[Test] ProductDAOHibernate.findAll() geeft de volgende producten:");
        for (Product product : producten) {
            System.out.println(product);
        }
        System.out.println();

        Product product = new Product(9999, "Testproduct", "Dit is een testproduct", 15.99);
        OVChipkaart ovChipkaart1 = new OVChipkaart(2001, Date.valueOf("2022-12-12"), 2, 20.00, rdao.findById(6));
        ovChipkaart1.addProduct(product);
        product.addOVKaart(ovChipkaart1);
        odao.update(ovChipkaart1);
        pdao.update(product);

        // Save
        System.out.print("[Test] Eerst " + producten.size() + " product(en), na ProductDAOHibernate.save() ");
        pdao.save(product);
        producten = pdao.findAll();
        System.out.println(producten.size() + " producten\n");

        // Update
        System.out.println("[Test] Eerst " + "ProductenDAOHibernate.update();");
        System.out.println("Voor wijziging: " + product);
        product.setPrijs(25.99);
        pdao.update(product);
        System.out.println("Na wijziging: " + product + "\n");

        // Zoeken op OV-chipkaart
        System.out.println("[Test] ProductDAOHibernate.findByOVChipkaart: ");
        System.out.println(pdao.findByOVChipkaart(ovChipkaart1));
        System.out.println();

        // Zoeken op product ID
        System.out.println("[TEST] ProductDAOHibernate.findById");
        System.out.println(pdao.findById(9999));
        System.out.println();

        // Verwijderen reiziger
        System.out.println("[TEST] Producten: " + producten.size() + " ProductDAOHibernate.delete();");
        pdao.delete(product);
        producten = pdao.findAll();
        System.out.println(producten.size() + "\n");
    }
}