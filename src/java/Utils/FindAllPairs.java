/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Utils;

import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

/**
 *
 * C. Levallois
 */
public class FindAllPairs<T extends Comparable<? super T>> {

    private T t;

    public Set<DirectedPair<T, T>> getAllDirectedPairs(Set<T> setObjects) {
        Set<T> setObjectsProcessed = new TreeSet<T>();
        Set<DirectedPair<T, T>> setPairs = new TreeSet<DirectedPair<T, T>>();
        Iterator<T> setObjectsIteratorA = setObjects.iterator();
        Iterator<T> setObjectsIteratorB;
        T currTA;
        T currTB;
        while (setObjectsIteratorA.hasNext()) {
            currTA = setObjectsIteratorA.next();
            setObjectsIteratorB = setObjects.iterator();
            while (setObjectsIteratorB.hasNext()) {
                currTB = setObjectsIteratorB.next();
                if (!setObjectsProcessed.contains(currTB) && currTA != currTB) {
                    setPairs.add(new DirectedPair(currTA, currTB));
                }
            }
            setObjectsProcessed.add(currTA);
        }
        return setPairs;

    }

    public Set<DirectedPair<T, T>> getAllDirectedPairsFromTwoSets(Set<T> setSources, Set<T> setTargets) {
        Set<DirectedPair<T, T>> setPairs = new TreeSet();
        Iterator<T> setSourcesIterator = setSources.iterator();
        Iterator<T> setTargetsIterator;
        T source;
        T target;
        while (setSourcesIterator.hasNext()) {
            source = setSourcesIterator.next();
            setTargetsIterator = setTargets.iterator();
            while (setTargetsIterator.hasNext()) {
                target = setTargetsIterator.next();
                if (!source.equals(target)) {
                    setPairs.add(new DirectedPair(source, target));
                }
            }
        }
        return setPairs;

    }

    public Set<UnDirectedPair<T>> getAllUndirectedPairs(Set<T> setObjects) {
        Set<T> setObjectsProcessed = new TreeSet();
        Set<UnDirectedPair<T>> setPairs;
        setPairs = new TreeSet();
        Iterator<T> setObjectsIteratorA = setObjects.iterator();
        Iterator<T> setObjectsIteratorB;
        T currTA;
        T currTB;
        while (setObjectsIteratorA.hasNext()) {
            currTA = setObjectsIteratorA.next();
            setObjectsProcessed.add(currTA);
            setObjectsIteratorB = setObjects.iterator();
            while (setObjectsIteratorB.hasNext()) {
                currTB = setObjectsIteratorB.next();
                if (!setObjectsProcessed.contains(currTB) && !currTA.equals(currTB)) {
                    setPairs.add(new UnDirectedPair(currTA, currTB));
                }
            }
            setObjectsProcessed.add(currTA);
        }
        return setPairs;

    }
}
