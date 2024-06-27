import java.util.*;

final class Cellule<T> {
    T element;
    Cellule<T> next;

    Cellule(T element, Cellule<T> next) {
        this.element = element;
        this.next = next;
    }

    Cellule(Cellule<T> copy) {
        this(copy.element, copy.next);
    }
}

class Liste<T> implements Iterable<T>, Collection<T> {

    private Cellule<T> fictif, queue;
    private int size;

    public Liste() {
        this.fictif = new Cellule<>(null, fictif);
        this.queue = this.fictif;
        this.size = 0;
    }

    public Liste(Collection<? extends T> elements) {
        this();
        this.addAll(elements);
    }

    @SafeVarargs
    public Liste(T... elements) {
        this(Arrays.asList(elements));
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return fictif.next == fictif;
    }

    @Override
    public boolean contains(Object o) {
        for (T elt : this)
            if (elt.equals(o))
                return true;
        return false;
    }

    public void push(T element) {
        fictif.next = new Cellule<>(element, fictif.next);
        size++;
    }

    public void pushAll(Collection<? extends T> elements) {
        Liste<T> tmp = new Liste<>(elements);
        tmp.queue.next = fictif.next;
        fictif.next = tmp.fictif.next;
        size += tmp.size;
    }

    @SafeVarargs
    public final void pushAll(T... elements) {
        pushAll(Arrays.asList(elements));
    }


    public T top() {
        return fictif.next.element;
    }

    public T pop() {
        Cellule<T> top = fictif.next;
        fictif.next = fictif.next.next;
        size--;
        return top.element;
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterateur(this);
    }

    @Override
    public Object[] toArray() {
        return new ArrayList<>(this).toArray();
    }

    @Override
    public <T1> T1[] toArray(T1[] a) {
        throw new UnsupportedOperationException("Liste cannot be transformed to Array");
    }

    @Override
    public boolean add(T t) {
        queue.next = new Cellule<>(t, fictif);
        queue = queue.next;
        size++;
        return true;
    }

    @Override
    public boolean remove(Object o) {
        Iterator<T> it = iterator();
        while (it.hasNext())
            if (it.next().equals(o)) {
            it.remove();
            return true;
        }
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return c.stream().allMatch(this::contains);
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        c.forEach(this::add);
        return true;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean removed = false;
        for (Object elt : c) {
            if (remove(elt))
                removed = true;
        }
        return removed;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean removed = false;
        Iterator<T> it = iterator();
        while (it.hasNext()) {
            if (!c.contains(it.next())) {
                it.remove();
                removed = true;
            }
        }
        return removed;
    }

    @Override
    public void clear() {
        fictif.next = fictif;
        queue = fictif;
        size = 0;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("[");
        Iterator<T> it = iterator();
        if (it.hasNext())
            stringBuilder.append(it.next());
        while (it.hasNext()) stringBuilder.append(", ").append(it.next());
        return stringBuilder.append("]").toString();
    }

    class Iterateur implements Iterator<T> {

        private Cellule<T> previous, end;

        private Iterateur(Liste<T> liste) {
            this.previous = new Cellule<>(fictif);
            this.end = liste.queue.next;
        }

        @Override
        public boolean hasNext() {
            return previous.next != end;
        }

        @Override
        public T next() {
            T element = previous.next.element;
            previous = previous.next;
            return element;
        }

        @Override
        public void remove() {
            previous = previous.next;
            size--;
        }
    }
}

class Arbre<T> implements Iterable<T> {

    private final T racine;
    private final Liste<Arbre<T>> fils;

    public Arbre(T racine) {
        this.racine = racine;
        this.fils = new Liste<>();
    }

    public Arbre(T racine, Collection<? extends Arbre<T>> fils) {
        this(racine);
        this.fils.addAll(fils);
//        System.out.println(this.fils);
    }

    @SafeVarargs
    public Arbre(T racine, Arbre<T>... fils) {
        this(racine, Arrays.asList(fils));
    }

//    public Arbre(T racine, Collection<? extends T> fils) {
//        this(racine);
//        fils.stream().map(Arbre::new).forEach(this.fils::add);
//    }
//
//    @SafeVarargs
//    public Arbre(T racine, T... fils) {
//        this(racine);
//        Arrays.stream(fils).map(Arbre::new).forEach(this.fils::add);
//    }

    @Override
    public Iterator<T> iterator() {
        return new Iterateur(this);
    }

    @Override
    public String toString() {
        return "Arbre{" +
                "racine=" + racine +
                ", fils=" + fils +
                '}';
    }

    class Iterateur implements Iterator<T> {

        Arbre<T> previous;
        Liste<Arbre<T>> others;

        public Iterateur(Arbre<T> racine) {
            this.previous = new Arbre<>(null, new Arbre<T>(racine.racine, racine.fils));
            this.others = new Liste<>();
        }

        @Override
        public boolean hasNext() {
            return !previous.fils.isEmpty();
        }

        @Override
        public T next() {

            Arbre<T> next = previous.fils.pop();
            Liste<Arbre<T>> others = previous.fils;
            if (!others.isEmpty())
                this.others.pushAll(others);
            previous = next;

            if (previous.fils.isEmpty()) {
                if (!this.others.isEmpty()) {
                    previous = this.others.pop();
                }
            }
            return next.racine;
        }
    }

}


public class PatternIterateur {
    public static void main(String[] args) {
        Liste<Integer> liste = new Liste<>(0);
        for (int i = 1; i < 10; i++) {
            liste.push(i);
        }
        for (Integer elt : liste) {
            System.out.println(elt);
        }

        Iterator<Integer> it = liste.iterator();
        while (it.hasNext()) {
            System.out.println(it.next());
        }

        liste.forEach(System.out::println);

        Arbre<Integer> arbre1 = new Arbre<>(1,
                new Arbre<>(2),
                new Arbre<>(3),
                new Arbre<>(4),
                new Arbre<>(5),
                new Arbre<>(6),
                new Arbre<>(7)
        );
        Arbre<Integer> arbre2 = new Arbre<>(
                1,
                new Arbre<>(
                        2,
                        new Arbre<>(5),
                        new Arbre<>(6)
                ),
                new Arbre<>(
                        3,
                        new Arbre<>(7),
                        new Arbre<>(8)
                ),
                new Arbre<>(
                        4,
                        new Arbre<>(9),
                        new Arbre<>(10)
                )
        );

        System.out.println();

        arbre1.forEach(System.out::println);
        arbre2.forEach(System.out::println);

    }
}
