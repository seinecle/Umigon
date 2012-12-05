/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Utils;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import com.google.common.collect.Multisets;
import java.util.Iterator;

/**
 *
 * @author C. Levallois
 */
public class MultisetMostFrequentFiltering<L> {

    Multiset<L> multiset;
    Multiset<L> multisetToReturn;

    public MultisetMostFrequentFiltering(Multiset<L> multiset) {
        this.multiset = multiset;

    }

    public Multiset<L> keepMostfrequent(int n) {
        multiset = Multisets.copyHighestCountFirst(multiset);
        Iterator<L> multisetIterator;
        multisetIterator = multiset.elementSet().iterator();
        multisetToReturn = HashMultiset.create();
        int count = 0;
        L object;
        while (multisetIterator.hasNext()) {
            count++;
            object = multisetIterator.next();
            multisetToReturn.add(object, multiset.count(object));
            if (count == n) {
                break;
            }
        }
        return multisetToReturn;
    }
}
