/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/*
 Copyright 2013 Clement Levallois
 Authors : Clement Levallois <clement.levallois@gephi.org>
 Website : http://www.clementlevallois.net


 DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.

 Copyright 2013 Clement Levallois. All rights reserved.

 The contents of this file are subject to the terms of either the GNU
 General Public License Version 3 only ("GPL") or the Common
 Development and Distribution License("CDDL") (collectively, the
 "License"). You may not use this file except in compliance with the
 License. You can obtain a copy of the License at
 http://gephi.org/about/legal/license-notice/
 or /cddl-1.0.txt and /gpl-3.0.txt. See the License for the
 specific language governing permissions and limitations under the
 License.  When distributing the software, include this License Header
 Notice in each file and include the License files at
 /cddl-1.0.txt and /gpl-3.0.txt. If applicable, add the following below the
 License Header, with the fields enclosed by brackets [] replaced by
 your own identifying information:
 "Portions Copyrighted [year] [name of copyright owner]"

 If you wish your version of this file to be governed by only the CDDL
 or only the GPL Version 3, indicate your decision by adding
 "[Contributor] elects to include this software in this distribution
 under the [CDDL or GPL Version 3] license." If you do not indicate a
 single choice of license, a recipient has the option to distribute
 your version of this file under either the CDDL, the GPL Version 3 or
 to extend the choice of license to its licensees as provided above.
 However, if you add GPL Version 3 code and therefore, elected the GPL
 Version 3 license, then the option applies only if the new code is
 made subject to such option by the copyright holder.

 Contributor(s):

 Portions Copyrighted 2011 Gephi Consortium.
 */
package WordsLists;

import TextCleaning.StatusCleaner;
import Utils.Clock;
import Utils.MultisetMostFrequentFiltering;
import Utils.NGramFinder;
import com.csvreader.CsvReader;
import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import com.google.common.collect.Multiset.Entry;
import com.google.common.collect.Multisets;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;

public class WordListBuilder {

    public static void main(String args[]) throws FileNotFoundException, IOException {
        String fieldDelimiter = ",";
        String textDelimiter = "\"";
        String fileName = "D:\\Docs Pro Clement\\E-humanities\\Sentiment Analysis\\datasets\\training.1600000.processed.noemoticon.csv";
        CsvReader csvReader = new CsvReader(new BufferedReader(new FileReader(fileName)), fieldDelimiter.charAt(0));
        csvReader.setTextQualifier(textDelimiter.charAt(0));
        csvReader.setUseTextQualifier(true);
        String[] values;
        String string;
        HashMultiset<String> nGrams;
        Iterator<String> nGramsIterator;
        Multiset<String> posunigrams = HashMultiset.create();
        Multiset<String> posbigrams = HashMultiset.create();
        Multiset<String> postrigrams = HashMultiset.create();
        Multiset<String> posquadrigrams = HashMultiset.create();
        Multiset<String> negunigrams = HashMultiset.create();
        Multiset<String> negbigrams = HashMultiset.create();
        Multiset<String> negtrigrams = HashMultiset.create();
        Multiset<String> negquadrigrams = HashMultiset.create();
        StatusCleaner statusCleaner = new StatusCleaner();
        Integer polarity;
        String ext;
        Integer polarityCurrentlyBuilt = 0;
        if (polarityCurrentlyBuilt == 0) {
            ext = "neg";
        } else {
            ext = "pos";
        }
        int countLines = 0;
        Clock readingRecordsClock = new Clock("reading tweets in file");
        while (csvReader.readRecord()) {
            if (countLines++ % 10000 == 0) {
                System.out.println("lines: " + countLines);
            }
            values = csvReader.getValues();
            string = statusCleaner.removePunctuationSigns(values[5]);
            polarity = Integer.valueOf(values[0]);
            nGrams = new NGramFinder(string).runIt(4, true);
            nGramsIterator = nGrams.iterator();
            String nGramOrig;
            String nGramLowerCaseStripped;
            int counter;

            while (nGramsIterator.hasNext()) {
                counter = 0;
                nGramOrig = nGramsIterator.next();
                nGramLowerCaseStripped = nGramOrig.toLowerCase();

                for (int i = 0; i < nGramLowerCaseStripped.length(); i++) {
                    if (nGramLowerCaseStripped.charAt(i) == ' ') {
                        counter++;
                    }
                }
                if (values[0].equals("0")) {
                    if (counter == 0) {
                        negunigrams.add(nGramLowerCaseStripped);
                    }
                    if (counter == 1) {
                        negbigrams.add(nGramLowerCaseStripped);
                    }
                    if (counter == 2) {
                        negtrigrams.add(nGramLowerCaseStripped);
                    }
                    if (counter == 3) {
                        negquadrigrams.add(nGramLowerCaseStripped);
                    }
                }
                if (values[0].equals("4")) {
                    if (counter == 0) {
                        posunigrams.add(nGramLowerCaseStripped);
                    }
                    if (counter == 1) {
                        posbigrams.add(nGramLowerCaseStripped);
                    }
                    if (counter == 2) {
                        postrigrams.add(nGramLowerCaseStripped);
                    }
                    if (counter == 3) {
                        posquadrigrams.add(nGramLowerCaseStripped);
                    }
                }

            }
        }
        csvReader.close();
        readingRecordsClock.closeAndPrintClock();


        Clock ngramsCheckClock = new Clock("checking n-grams in duplicate appearing both in pos and neg");
        Multiset<String> bisposunigrams = HashMultiset.create();
        for (Iterator<String> i = posunigrams.elementSet().iterator(); i.hasNext();) {
            String string1 = i.next();
            if (negunigrams.contains(string1)) {
                negunigrams.remove(string1, negunigrams.count(string1));
            } else {
                bisposunigrams.add(string1, posunigrams.count(string1));
            }
        }
        posunigrams = HashMultiset.create(bisposunigrams);

        Multiset<String> bisposbigrams = HashMultiset.create();
        for (Iterator<String> i = posbigrams.elementSet().iterator(); i.hasNext();) {
            String string1 = i.next();
            if (negbigrams.contains(string1)) {
                negbigrams.remove(string1, negbigrams.count(string1));
            } else {
                bisposbigrams.add(string1, posbigrams.count(string1));
            }
        }
        posbigrams = HashMultiset.create(bisposbigrams);

        Multiset<String> bispostrigrams = HashMultiset.create();
        for (Iterator<String> i = postrigrams.elementSet().iterator(); i.hasNext();) {
            String string1 = i.next();
            if (negtrigrams.contains(string1)) {
                negtrigrams.remove(string1, negtrigrams.count(string1));
            } else {
                bispostrigrams.add(string1, postrigrams.count(string1));
            }
        }
        postrigrams = HashMultiset.create(bispostrigrams);

        Multiset<String> bisposquadrigrams = HashMultiset.create();
        for (Iterator<String> i = posquadrigrams.elementSet().iterator(); i.hasNext();) {
            String string1 = i.next();
            if (negquadrigrams.contains(string1)) {
                negquadrigrams.remove(string1, negquadrigrams.count(string1));
            } else {
                bisposquadrigrams.add(string1, posquadrigrams.count(string1));
            }
        }
        posquadrigrams = HashMultiset.create(bisposquadrigrams);

        ngramsCheckClock.closeAndPrintClock();




        MultisetMostFrequentFiltering filter = new MultisetMostFrequentFiltering(posunigrams);
        posunigrams = filter.keepMostfrequent(1000);
        filter = new MultisetMostFrequentFiltering(posbigrams);
        posbigrams = filter.keepMostfrequent(1000);
        filter = new MultisetMostFrequentFiltering(postrigrams);
        postrigrams = filter.keepMostfrequent(1000);
        filter = new MultisetMostFrequentFiltering(posquadrigrams);
        posquadrigrams = filter.keepMostfrequent(1000);
        filter = new MultisetMostFrequentFiltering(negunigrams);
        negunigrams = filter.keepMostfrequent(1000);
        filter = new MultisetMostFrequentFiltering(negbigrams);
        negbigrams = filter.keepMostfrequent(1000);
        filter = new MultisetMostFrequentFiltering(negtrigrams);
        negtrigrams = filter.keepMostfrequent(1000);
        filter = new MultisetMostFrequentFiltering(negquadrigrams);
        negquadrigrams = filter.keepMostfrequent(1000);


        Clock writeunigrams = new Clock("write pos unigrams");
        BufferedWriter bw = new BufferedWriter(new FileWriter("D:\\Docs Pro Clement\\E-humanities\\Sentiment Analysis\\datasets\\pos_unigrams.txt"));
        Iterator<Entry<String>> nGramsEntriesIterator = Multisets.copyHighestCountFirst(posunigrams).entrySet().iterator();
        Entry entry;
        StringBuilder sb = new StringBuilder();
        while (nGramsEntriesIterator.hasNext()) {
            entry = nGramsEntriesIterator.next();
            sb.append(entry.getElement()).append(", ").append(entry.getCount()).append("\n");
        }
        bw.write(sb.toString());
        bw.close();
        writeunigrams.closeAndPrintClock();


        Clock writebigrams = new Clock("write pos bigrams");
        bw = new BufferedWriter(new FileWriter("D:\\Docs Pro Clement\\E-humanities\\Sentiment Analysis\\datasets\\pos_bigrams.txt"));
        nGramsEntriesIterator = Multisets.copyHighestCountFirst(posbigrams).entrySet().iterator();
        sb = new StringBuilder();
        while (nGramsEntriesIterator.hasNext()) {
            entry = nGramsEntriesIterator.next();
            sb.append(entry.getElement()).append(", ").append(entry.getCount()).append("\n");
        }
        bw.write(sb.toString());
        bw.close();
        writebigrams.closeAndPrintClock();


        Clock writetrigrams = new Clock("write pos trigrams");
        bw = new BufferedWriter(new FileWriter("D:\\Docs Pro Clement\\E-humanities\\Sentiment Analysis\\datasets\\pos_trigrams.txt"));
        nGramsEntriesIterator = Multisets.copyHighestCountFirst(postrigrams).entrySet().iterator();
        sb = new StringBuilder();
        while (nGramsEntriesIterator.hasNext()) {
            entry = nGramsEntriesIterator.next();
            sb.append(entry.getElement()).append(", ").append(entry.getCount()).append("\n");
        }
        bw.write(sb.toString());
        bw.close();
        writetrigrams.closeAndPrintClock();

        Clock writequadrigrams = new Clock("write pos quadrigrams");
        bw = new BufferedWriter(new FileWriter("D:\\Docs Pro Clement\\E-humanities\\Sentiment Analysis\\datasets\\pos_quadrigrams.txt"));
        nGramsEntriesIterator = Multisets.copyHighestCountFirst(posquadrigrams).entrySet().iterator();
        sb = new StringBuilder();
        while (nGramsEntriesIterator.hasNext()) {
            entry = nGramsEntriesIterator.next();
            sb.append(entry.getElement()).append(", ").append(entry.getCount()).append("\n");
        }
        bw.write(sb.toString());
        bw.close();
        writequadrigrams.closeAndPrintClock();

        writeunigrams = new Clock("write neg unigrams");
        bw = new BufferedWriter(new FileWriter("D:\\Docs Pro Clement\\E-humanities\\Sentiment Analysis\\datasets\\neg_unigrams.txt"));
        nGramsEntriesIterator = Multisets.copyHighestCountFirst(negunigrams).entrySet().iterator();
        sb = new StringBuilder();
        while (nGramsEntriesIterator.hasNext()) {
            entry = nGramsEntriesIterator.next();
            sb.append(entry.getElement()).append(", ").append(entry.getCount()).append("\n");
        }
        bw.write(sb.toString());
        bw.close();
        writeunigrams.closeAndPrintClock();


        writebigrams = new Clock("write neg bigrams");
        bw = new BufferedWriter(new FileWriter("D:\\Docs Pro Clement\\E-humanities\\Sentiment Analysis\\datasets\\neg_bigrams.txt"));
        nGramsEntriesIterator = Multisets.copyHighestCountFirst(negbigrams).entrySet().iterator();
        sb = new StringBuilder();
        while (nGramsEntriesIterator.hasNext()) {
            entry = nGramsEntriesIterator.next();
            sb.append(entry.getElement()).append(", ").append(entry.getCount()).append("\n");
        }
        bw.write(sb.toString());
        bw.close();
        writebigrams.closeAndPrintClock();


        writetrigrams = new Clock("write trigrams");
        bw = new BufferedWriter(new FileWriter("D:\\Docs Pro Clement\\E-humanities\\Sentiment Analysis\\datasets\\neg_trigrams.txt"));
        nGramsEntriesIterator = Multisets.copyHighestCountFirst(negtrigrams).entrySet().iterator();
        sb = new StringBuilder();
        while (nGramsEntriesIterator.hasNext()) {
            entry = nGramsEntriesIterator.next();
            sb.append(entry.getElement()).append(", ").append(entry.getCount()).append("\n");
        }
        bw.write(sb.toString());
        bw.close();
        writetrigrams.closeAndPrintClock();

        writequadrigrams = new Clock("write quadrigrams");
        bw = new BufferedWriter(new FileWriter("D:\\Docs Pro Clement\\E-humanities\\Sentiment Analysis\\datasets\\neg_quadrigrams.txt"));
        nGramsEntriesIterator = Multisets.copyHighestCountFirst(negquadrigrams).entrySet().iterator();
        sb = new StringBuilder();
        while (nGramsEntriesIterator.hasNext()) {
            entry = nGramsEntriesIterator.next();
            sb.append(entry.getElement()).append(", ").append(entry.getCount()).append("\n");
        }
        bw.write(sb.toString());
        bw.close();
        writequadrigrams.closeAndPrintClock();
    }
}
