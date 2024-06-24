import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

record Adresse(String adresse)
{
}

record Prenom(String prenom)
{
}

class Exemple
{
    final String nom;
    final int num;
    final Adresse adresse;
    final Prenom prenom;

    private static final Prenom defaultPrenom = new Prenom("Pas de Prénomn");
    private static final Adresse defaultAdresse = new Adresse("Pas d'Adresse");


    Exemple(String nom, int num)
    {
        this.nom = nom;
        this.num = num;
        this.adresse = defaultAdresse;
        this.prenom = defaultPrenom;
    }

    Exemple(String nom, int num, Adresse adresse)
    {
        this.nom = nom;
        this.num = num;
        this.adresse = adresse;
        this.prenom = defaultPrenom;
    }

    Exemple(String nom, int num, Prenom prenom)
    {
        this.nom = nom;
        this.num = num;
        this.adresse = defaultAdresse;
        this.prenom = prenom;
    }

    Exemple(String nom, int num, Adresse adresse, Prenom prenom)
    {
        this.nom = nom;
        this.num = num;
        this.adresse = adresse;
        this.prenom = prenom;
    }

    @Override
    public String toString()
    {
        return "Exemple{" +
                "nom='" + nom + '\'' +
                ", num=" + num +
                ", adresse=" + adresse +
                ", prenom=" + prenom +
                '}';
    }
}


class Exemple2
{
    final String nom;
    final int num;
    final Adresse adresse;
    final Prenom prenom;

    private static final Prenom defaultPrenom = new Prenom("Pas de Prénomn");
    private static final Adresse defaultAdresse = new Adresse("Pas d'Adresse");

    private Exemple2(Builder builder)
    {
        this.nom = builder.nom;
        this.num = builder.num;
        this.adresse = builder.adresse;
        this.prenom = builder.prenom;
    }

    public static Builder builder(String nom, int num)
    {
        return new Builder(nom, num);
    }

    public static class Builder
    {

        private final String nom;
        private final int num;
        private Adresse adresse;
        private Prenom prenom;

        private Builder(String nom, int num)
        {
            this.nom = nom;
            this.num = num;
            this.adresse = defaultAdresse;
            this.prenom = defaultPrenom;
        }

        public Builder clear()
        {
            this.adresse = defaultAdresse;
            this.prenom = defaultPrenom;
            return this;
        }

        public Builder setAdresse(Adresse adresse)
        {
            this.adresse = adresse;
            return this;
        }

        public Builder setPrenom(Prenom prenom)
        {
            this.prenom = prenom;
            return this;
        }

        public Exemple2 build()
        {
            return new Exemple2(this);
        }

    }

    @Override
    public String toString()
    {
        return "Exemple2{" +
                "nom='" + nom + '\'' +
                ", num=" + num +
                ", adresse=" + adresse +
                ", prenom=" + prenom +
                '}';
    }
}


final class Module
{

    public static int monNum = 42;

    public static int add(int par1, int par2)
    {
        return par1 + par2;
    }

    public static int mul(int par1, int par2)
    {
        return par1 * par2;
    }

}

class Singleton
{

    public final int monNum;

    public int add(int par1, int par2)
    {
        return par1 + par2;
    }

    public int mul(int par1, int par2)
    {
        return par1 * par2;
    }

    private Singleton(int monNum)
    {
        this.monNum = monNum;
    }

    private static Singleton singleton;

    public static void initInstance(int monNum)
    {
        if (singleton == null)
            singleton = new Singleton(monNum);
    }

    public static Singleton getInstance()
    {
        if (singleton == null)
            singleton = new Singleton(42);
        return singleton;
    }
}

abstract class Pizzeria
{

    List<String> typesPizzas = new ArrayList<>(2);

    Pizza creerPizza1()
    {
        System.out.println("Votre pizza " + typesPizzas.get(0) + "est prête !");
        return new PizzaNull();
    }

    Pizza creerPizza2()
    {
        System.out.println("Votre pizza " + typesPizzas.get(1) + "est prête !");
        return new PizzaNull();
    }
}

class PizzeriaFrançaise extends Pizzeria
{

    public PizzeriaFrançaise()
    {
        typesPizzas.add("Savoyarde");
        typesPizzas.add("Paysanne");
    }

    @Override
    Pizza creerPizza1()
    {
        super.creerPizza1();
        return new PizzaSavoyarde();
    }

    @Override
    Pizza creerPizza2()
    {
        super.creerPizza1();
        return new PizzaPaysanne();
    }
}

class PizzeriaItalienne extends Pizzeria
{
    public PizzeriaItalienne()
    {
        typesPizzas.add("Margherita");
        typesPizzas.add("Bolognaise");
    }

    @Override
    Pizza creerPizza1()
    {
        super.creerPizza1();
        return new PizzaMargherita();
    }

    @Override
    Pizza creerPizza2()
    {
        super.creerPizza2();
        return new PizzaBolognaise();
    }
}

abstract class Pizza extends Prototype
{
    String sauce;

    String garniture;

    abstract public void setSauce();

    abstract public void setGarniture();
}

class PizzaNull extends Pizza
{
    @Override
    public void setSauce()
    {
        sauce = "Vide";
    }

    @Override
    public void setGarniture()
    {
        garniture = "Vide";
    }
}

class PizzaSavoyarde extends Pizza
{


    @Override
    public void setSauce()
    {
        sauce = "Creme";
    }

    @Override
    public void setGarniture()
    {
        sauce = "Pommes de Terre";
    }
}

class PizzaPaysanne extends Pizza
{
    @Override
    public void setSauce()
    {
        sauce = "Creme";
    }

    @Override
    public void setGarniture()
    {
        garniture = "Foin";
    }
}

class PizzaMargherita extends Pizza
{
    @Override
    public void setSauce()
    {
        sauce = "Tomate";
    }

    @Override
    public void setGarniture()
    {
        garniture = "Vide";
    }
}

class PizzaBolognaise extends Pizza
{
    @Override
    public void setSauce()
    {
        sauce = "Tomate";
    }

    @Override
    public void setGarniture()
    {
        garniture = "Viande Hachée";
    }
}


enum TypePizza
{
    Savoyarde, Paysanne, Margherita, Bolognaise
}

class PizzeriaConcrete
{

    void prenderCommande()
    {
    }

    void ServirClient()
    {
    }

    Pizza creerPizza(TypePizza typePizza)
    {
        Pizza pizza = new PizzaNull();
        switch (typePizza)
        {
            case Paysanne -> pizza = new PizzaPaysanne();
            case Savoyarde -> pizza = new PizzaSavoyarde();
            case Margherita -> pizza = new PizzaMargherita();
            case Bolognaise-> pizza = new PizzaBolognaise();

        }
        return pizza;
    }

}

abstract class Prototype implements Cloneable {

}

class PrototypeManager {

}


abstract class PizzaFrançaise extends Pizza {

    PizzaFrançaise() {
        super();
        sauce = "Creme";
    }

    @Override
    protected Object clone() throws CloneNotSupportedException
    {
        Object pizza = super.clone();
        ((Pizza) pizza).sauce = this.sauce;
        return pizza;
    }
}

abstract class PizzaItalienne extends Pizza {

    PizzaItalienne() {
        super();
        sauce = "Tomate";
    }

    @Override
    protected Object clone() throws CloneNotSupportedException
    {
        Object pizza = super.clone();
        ((Pizza) pizza).sauce = this.sauce;
        return pizza;
    }
}



// type Forme = Cercle of int | Carre of int | Triangle of int*int*int

abstract class Forme
{
    abstract void dessiner();

}

class Cercle extends Forme
{
    int rayon;

    public Cercle(int rayon)
    {
        this.rayon = rayon;
    }

    @Override
    void dessiner()
    {
        System.out.println("Je suis un cercle");
    }
}

class Carre extends Forme
{
    int cote;

    public Carre(int cote)
    {
        this.cote = cote;
    }

    @Override
    void dessiner()
    {
        System.out.println("Je suis un carre");
    }
}

class Triangle extends Forme
{
    int a, b, c;

    public Triangle(int a, int b, int c)
    {
        this.a = a;
        this.b = b;
        this.c = c;
    }

    @Override
    void dessiner()
    {
        System.out.println("Je suis un triangle");
    }
}



public class DesignPattern
{

    public static void main(String[] args)
    {
        List<Forme> formes = new ArrayList<>(Arrays.asList(new Carre(5), new Cercle(3), new Triangle(6, 6, 6)));
        for (Forme forme : formes)
        {
            forme.dessiner();
        }
    }


    /*
    public static void main(String[] args)
    {
        var ex1 = new Exemple("Bleuze", 40312253);
        var ex2 = new Exemple("Bleuze", 40312253, new Prenom("Bap"));
        var ex3 = new Exemple("Bleuze", 40312253, new Adresse("Mon Adresse"));

        var ex3bis = new Exemple(
                "Bleuze",
                40312253,
                new Adresse("Mon Adresse")
        );

        Exemple2.Builder builder = Exemple2.builder("Bleuze", 40312253);
        var ex4 = builder.build();
        var ex5 = builder.setPrenom(new Prenom("Bap")).build();
        var ex6 = builder.clear().setAdresse(new Adresse("Mon Adresse")).build();

        var ex7 = Exemple2.builder("Bleuze", 40312253)
                .setPrenom(new Prenom("Bap"))
                .setAdresse(new Adresse("Mon Adresse"))
                .build();

        System.out.println(ex1);
        System.out.println(ex4);
        System.out.println();
        System.out.println(ex2);
        System.out.println(ex5);
        System.out.println();
        System.out.println(ex3);
        System.out.println(ex6);
        System.out.println();

        System.out.println(Module.add(2, 3));
        System.out.println(Module.mul(2, 3));
        Module.monNum = 0;

    }
    */

}


