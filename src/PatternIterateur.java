import java.util.Iterator;
import java.util.List;

class Cellule<T>
{
    T element;
    Cellule<T> next;

    public Cellule(T element, Cellule<T> next)
    {
        this.element = element;
        this.next = next;
    }
}

class Liste<T> implements Iterable<T>
{
    Cellule<T> tete;

    public Liste(T element)
    {
        this.tete = new Cellule<>(element, null);
    }

    public void add(T element)
    {
        tete = new Cellule<>(element, tete);
    }

    public T last()
    {
        return tete.element;
    }

    public T removeLast()
    {
        Cellule<T> last = tete;
        tete = tete.next;
        T element = last.element;
        last = null;
        return element;
    }

    @Override
    public Iterator<T> iterator()
    {
        return new Iterateur<>(tete);
    }

    static class Iterateur<T> implements Iterator<T>
    {

        private Cellule<T> previous;

        private Iterateur(Cellule<T> next)
        {
            this.previous = new Cellule<>(null, next);
        }

        @Override
        public boolean hasNext()
        {
            return previous.next != null;
        }

        @Override
        public T next()
        {
            T element = previous.next.element;
            previous = previous.next;
            return element;
        }
    }
}

class Arbre<T> implements Iterable<T>
{
    Liste<Liste<T>> racine;

    public Arbre(T element)
    {
        this.racine = new Liste<>(new Liste<>(element));
    }

    public Arbre(Liste<Liste<T>> elements)
    {
        this.racine = elements;
    }

    @Override
    public Iterator<T> iterator()
    {
        return new Iterateur<>(racine);
    }

    static class Iterateur<T> implements Iterator<T>
    {

        Cellule<Liste<T>> previous;
        Liste<Iterator<T>> remain;

        public Iterateur(Liste<Liste<T>> element)
        {
            this.previous = new Cellule<>(null, new Cellule<>(element.last(), null));
            Iterator<T> it = element.last().iterator();
            it.next();
            this.remain = new Liste<>(null);
            this.remain.add(it);
        }

        @Override
        public boolean hasNext()
        {
            return previous.next != null;
        }

        @Override
        public T next()
        {

            T element = previous.next.element.last();
            previous = previous.next;

            if (previous == null)
            {
                if (remain.last() != null)
                {
                    Iterator<T> last = remain.removeLast();
                    previous = new Cellule<>(null, new Cellule<>((Liste<T>)last.next(),null));
                }
            }
            return element;
        }
    }

}


public class PatternIterateur
{
    public static void main(String[] args)
    {
        Liste<Integer> liste = new Liste<>(0);
        for (int i = 1; i < 10; i++)
        {
            liste.add(i);
        }
        for (Integer elt : liste)
        {
            System.out.println(elt);
        }

        Iterator<Integer> it = liste.iterator();
        while (it.hasNext())
        {
            System.out.println(it.next());
        }

        liste.forEach(System.out::println);

        Arbre<Integer> arbre = new Arbre<>(new Liste<>(new Liste<>(1)));

    }
}
