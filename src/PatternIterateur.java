import java.util.*;

class Liste<T> implements Deque<T> {

    private static final class Cellule<T> {
        T element;
        Cellule<T> prev, next;

        Cellule() {
            this(null);
        }

        Cellule(T element) {
            this(element, null, null);
        }

        Cellule(T element, Cellule<T> prev, Cellule<T> next) {
            this.element = element;
            this.prev = prev;
            this.next = next;
        }

    }

    private final Cellule<T> fictif;
    private int size;

    public Liste() {
        fictif = new Cellule<>();
        fictif.next = fictif.prev = fictif;
        size = 0;
    }

    public Liste(Collection<? extends T> elements) {
        this();
        this.addAll(elements);
    }

    @SafeVarargs
    public Liste(T... elements) {
        this(Arrays.asList(elements));
    }

    public void reverse() {
        var tmp = new Liste<>(this);
        this.clear();
        tmp.forEach(this::push);
    }

    @Override
    public void addFirst(T t) {
        final Cellule<T> cellule = new Cellule<>(t, fictif, fictif.next);
        fictif.next.prev = cellule;
        fictif.next = cellule;
        size++;
    }

    @Override
    public void addLast(T t) {
        final Cellule<T> cellule = new Cellule<>(t, fictif.prev, fictif);
        fictif.prev.next = cellule;
        fictif.prev = cellule;
        size++;
    }

    @Override
    public boolean offerFirst(T t) {
        addFirst(t);
        return true;
    }

    @Override
    public boolean offerLast(T t) {
        addLast(t);
        return true;
    }

    @Override
    public T removeFirst() {
        if (isEmpty())
            throw new NoSuchElementException("La Liste est vide");
        final T element = fictif.next.element;
        fictif.next = fictif.next.next;
        fictif.next.prev = fictif;
        size--;
        return element;
    }

    @Override
    public T removeLast() {
        if (isEmpty())
            throw new NoSuchElementException("La Liste est vide");
        final T element = fictif.prev.element;
        fictif.prev.prev.next = fictif;
        fictif.prev = fictif.prev.prev;
        size--;
        return element;
    }

    @Override
    public T pollFirst() {
        if (isEmpty())
            return null;
        final T element = fictif.next.element;
        fictif.next = fictif.next.next;
        fictif.next.prev = fictif;
        size--;
        return element;
    }

    @Override
    public T pollLast() {
        if (isEmpty())
            return null;
        final T element = fictif.prev.element;
        fictif.prev.prev.next = fictif;
        fictif.prev = fictif.prev.prev;
        size--;
        return element;
    }

    @Override
    public T getFirst() {
        if (isEmpty())
            throw new NoSuchElementException("La Liste est vide");
        return fictif.next.element;
    }

    @Override
    public T getLast() {
        if (isEmpty())
            throw new NoSuchElementException("La Liste est vide");
        return fictif.prev.element;
    }

    @Override
    public T peekFirst() {
        if (isEmpty())
            return null;
        return fictif.next.element;
    }

    @Override
    public T peekLast() {
        if (isEmpty())
            return null;
        return fictif.prev.element;
    }

    @Override
    public boolean removeFirstOccurrence(Object o) {
        return remove(o);
    }

    @Override
    public boolean removeLastOccurrence(Object o) {
        Iterator<T> it = descendingIterator();
        while (it.hasNext()) {
            T element = it.next();
            if (element.equals(o)) {
                it.remove();
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean offer(T t) {
        addFirst(t);
        return true;
    }

    @Override
    public T remove() {
        if (isEmpty())
            throw new NoSuchElementException("La Liste est vide");
        final T element = fictif.next.element;
        fictif.next = fictif.next.next;
        fictif.next.prev = fictif;
        size--;
        return element;
    }

    @Override
    public T poll() {
        if (isEmpty())
            return null;
        final T element = fictif.next.element;
        fictif.next = fictif.next.next;
        fictif.next.prev = fictif;
        size--;
        return element;
    }

    @Override
    public T element() {
        if (isEmpty())
            throw new NoSuchElementException("La Liste est vide");
        return fictif.next.element;
    }

    @Override
    public T peek() {
        if (isEmpty())
            return null;
        return fictif.next.element;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean contains(Object o) {
        for (T elt : this)
            if (elt.equals(o))
                return true;
        return false;
    }

    @Override
    public void push(T t) {
        final Cellule<T> cellule = new Cellule<>(t, fictif, fictif.next);
        fictif.next.prev = cellule;
        fictif.next = cellule;
        size++;
    }

    public void pushAll(Collection<? extends T> elements) {
        Liste<T> tmp = new Liste<>(elements);
        tmp.fictif.prev.next = fictif.next;
        fictif.next.prev = tmp.fictif.prev;
        fictif.next = tmp.fictif.next;
        size += tmp.size;
    }

    @SafeVarargs
    public final void pushAll(T... elements) {
        pushAll(Arrays.asList(elements));
    }

    @Override
    public T pop() {
        if (isEmpty())
            throw new NoSuchElementException("La Liste est vide");
        final T element = fictif.next.element;
        fictif.next = fictif.next.next;
        fictif.next.prev = fictif;
        size--;
        return element;
    }

    @Override
    public Iterator<T> iterator() {
        return new IteratorImpl();
    }

    @Override
    public Iterator<T> descendingIterator() {
        return new DescendingIteratorImpl();
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
        final Cellule<T> cellule = new Cellule<>(t, fictif.prev, fictif);
        fictif.prev.next = cellule;
        fictif.prev = cellule;
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
        fictif.next = fictif.prev = fictif;
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

    private class IteratorImpl implements Iterator<T> {

        private Cellule<T> previous;

        private IteratorImpl() {
            this.previous = fictif;
        }

        @Override
        public boolean hasNext() {
            return previous.next != fictif;
        }

        @Override
        public T next() {
            T element = previous.next.element;
            previous = previous.next;
            return element;
        }

        @Override
        public void remove() {
            previous.prev.next = previous.next;
            previous.next.prev = previous.prev;
            size--;
        }
    }

    private class DescendingIteratorImpl implements Iterator<T> {

        private Cellule<T> successor;

        private DescendingIteratorImpl() {
            this.successor = fictif;
        }

        @Override
        public boolean hasNext() {
            return successor.prev != fictif;
        }

        @Override
        public T next() {
            T element = successor.prev.element;
            successor = successor.prev;
            return element;
        }

        @Override
        public void remove() {
            successor.next.prev = successor.prev;
            successor.prev.next = successor.next;
            size--;
        }
    }
}

abstract class AbstractArbre<T> implements Iterable<T> {
    enum Parcours {PROFONDEUR, LARGEUR}

    public static Parcours parcours = Parcours.PROFONDEUR;
}

class ArbreVide<T> extends AbstractArbre<T> {

    @Override
    public Iterator<T> iterator() {
        return new Iterator<>() {
            @Override
            public boolean hasNext() {
                return false;
            }

            @Override
            public T next() {
                return null;
            }
        };
    }
}

class Arbre<T> extends AbstractArbre<T> {

    private final Noeud<T> racine;

    private Arbre(Noeud<T> racine) {
        this.racine = racine;
    }

    private Arbre(T noeud, Collection<? extends Arbre<T>> fils) {
        this(new Noeud<>(noeud));
        fils.stream().map(Noeud::getNoeud).forEach(racine.fils::add);
    }

    static <T> Arbre<T> creerFeuille(T racine) {
        return new Arbre<>(new Noeud<>(racine));
    }

    static <T> Arbre<T> creerArbre(T noeud, Collection<? extends Arbre<T>> fils) {
        return new Arbre<>(noeud, fils);
    }

    @SafeVarargs
    static <T> Arbre<T> creerArbre(T noeud, Arbre<T>... fils) {
        return new Arbre<>(noeud, Arrays.asList(fils));
    }

    @Override
    public Iterator<T> iterator() {
        return racine.iterator();
    }

    @Override
    public String toString() {
        return racine.toString();
    }

    private static class Noeud<T> implements Iterable<T> {

        private final T noeud;
        private final Liste<Noeud<T>> fils;

        static <T> Noeud<T> getNoeud(Arbre<T> arbre) {
            return arbre.racine;
        }

        public Noeud(T noeud) {
            this.noeud = noeud;
            this.fils = new Liste<>();
        }

        public Noeud(T noeud, Collection<? extends Noeud<T>> fils) {
            this(noeud);
            this.fils.addAll(fils);
        }

        @SafeVarargs
        public Noeud(T noeud, Noeud<T>... fils) {
            this(noeud, Arrays.asList(fils));
        }

        @Override
        public Iterator<T> iterator() {
            return new IteratorImpl();
        }

        @Override
        public String toString() {
            Iterator<T> it = iterator();
            StringBuilder stringBuilder = new StringBuilder("[");
            if (it.hasNext())
                stringBuilder.append(it.next());
            while (it.hasNext()) {
                stringBuilder.append(", ");
                stringBuilder.append(it.next());
            }
            return stringBuilder.append("]").toString();
        }

        class IteratorImpl implements Iterator<T> {

            Liste<Noeud<T>> noeuds;
            StartParcours<T> startParcours;

            interface StartParcours<T> {
                Noeud<T> execute();
            }

            class ParcoursEnProfondeur implements StartParcours<T> {

                @Override
                public Noeud<T> execute() {
                    Noeud<T> next = noeuds.pop();
                    noeuds.pushAll(next.fils);
                    return next;
                }
            }

            class ParcoursEnLargeur implements StartParcours<T> {

                @Override
                public Noeud<T> execute() {
                    Noeud<T> next = noeuds.pop();
                    noeuds.addAll(next.fils);
                    return next;
                }
            }

            private StartParcours<T> creerStratParours() {
                return switch (AbstractArbre.parcours) {
                    case PROFONDEUR -> new ParcoursEnProfondeur();
                    case LARGEUR -> new ParcoursEnLargeur();
                };
            }

            public IteratorImpl() {
                this.noeuds = new Liste<>(new Noeud<>(noeud, fils));
                this.startParcours = creerStratParours();
            }

            @Override
            public boolean hasNext() {
                return !noeuds.isEmpty();
            }

            @Override
            public T next() {
                return startParcours.execute().noeud;
            }
        }

    }
}


public class PatternIterateur {
    public static void main(String[] args) {
        Liste<Integer> liste = new Liste<>(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

        System.out.println();
        for (Integer elt : liste) {
            System.out.println(elt);
        }

        liste.removeIf(integer -> integer % 2 == 1);

        System.out.println();
        liste.forEach(System.out::println);

        AbstractArbre<Integer> arbre1 = Arbre.creerArbre(1,
                Arbre.creerFeuille(2),
                Arbre.creerFeuille(3),
                Arbre.creerFeuille(4),
                Arbre.creerFeuille(5),
                Arbre.creerFeuille(6),
                Arbre.creerFeuille(7)
        );
        AbstractArbre<Integer> arbre2 = Arbre.creerArbre(
                1,
                Arbre.creerArbre(
                        2,
                        Arbre.creerFeuille(5),
                        Arbre.creerFeuille(6)
                ),
                Arbre.creerArbre(
                        3,
                        Arbre.creerFeuille(7),
                        Arbre.creerFeuille(8)
                ),
                Arbre.creerArbre(
                        4,
                        Arbre.creerFeuille(9),
                        Arbre.creerFeuille(10)
                )
        );

        System.out.println();
        System.out.println(arbre1);
        System.out.println(arbre2);
        AbstractArbre.parcours = AbstractArbre.Parcours.LARGEUR;
        System.out.println(arbre2);
        System.out.println(arbre1);
        var tmp = new Liste<>();
        arbre1.iterator().forEachRemaining(tmp::push);
        System.out.println(tmp);
        liste.reverse();
        liste.addAll(List.of(11, 12, 13));
        List.of(11, 12, 13).forEach(liste::push);
        for (int i = 0; i < 6; i++) {
            System.out.println(liste.poll());
        }
        for (int i = 0; i < 6; i++) {
            System.out.println(liste.pollLast());
        }
        System.out.println(liste);

    }
}
