/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Singletons;

import Admin.Parameters;
import Classifier.Categories;
import Heuristics.Heuristic;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import org.apache.commons.lang3.StringUtils;

/*
 Copyright 2008-2013 Clement Levallois
 Authors : Clement Levallois <clementlevallois@gmail.com>
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

 Contributor(s): Clement Levallois

 */
@Singleton
@Startup

// initialize at deployment time instead of first invocation
public class HeuristicsLoader {

    BufferedReader br;
    FileReader fr;
    String folderPath = "heuristics//";
    String string;
    Heuristic heuristic;
    Map<String, Heuristic> mapHeuristics;
    Map<String, Heuristic> mapH1;
    Map<String, Heuristic> mapH2;
    Map<String, Heuristic> mapH3;
    Map<String, Heuristic> mapH4;
    Map<String, Heuristic> mapH5;
    Map<String, Heuristic> mapH6;
    Map<String, Heuristic> mapH7;
    Map<String, Heuristic> mapH8;
    Map<String, Heuristic> mapH9;
    Map<String, Heuristic> mapH10;
    Map<String, Heuristic> mapH11;
    Map<String, Heuristic> mapH12;
    Map<String, Heuristic> mapH13;
    Set<String> setNegations;
    Set<String> setTimeTokens;
    Set<String> setHashTags;
    Set<String> setModerators;
    Set<String> setFalsePositiveOpinions;
    Set<String> setIronicallyPositive;

    @PostConstruct
    public void load() {
        Categories.populate();

        
        Set<File> setPathResources = new TreeSet();
//        setPathResources.addAll(FacesContext.getCurrentInstance().getExternalContext().getResourcePaths("/resources/private/"));
//        File file = new File ("/usr/sharedfilesapps/lexicons");
        File file;
        if (Parameters.local) {
            file = new File("H:\\Docs Pro Clement\\NetBeansProjects\\Umigon\\src\\main\\webapp\\resources\\private\\");
        } else {
            file = new File("/usr/sharedfilesapps/lexicons");
        }
        File[] files = file.listFiles();
        setPathResources.addAll(Arrays.asList(files));
//        System.out.println("folder is: " + folder.getCanonicalPath());
        mapHeuristics = new HashMap();
        setNegations = new HashSet();
        setTimeTokens = new HashSet();
        setFalsePositiveOpinions = new HashSet();
        setIronicallyPositive = new HashSet();
        setModerators = new HashSet();
        mapH1 = new HashMap();
        mapH2 = new HashMap();
        mapH4 = new HashMap();
        mapH3 = new HashMap();
        mapH5 = new HashMap();
        mapH6 = new HashMap();
        mapH7 = new HashMap();
        mapH8 = new HashMap();
        mapH9 = new HashMap();
        mapH10 = new HashMap();
        mapH11 = new HashMap();
        mapH12 = new HashMap();
        mapH13 = new HashMap();

//        for (File file : arrayFiles) {
        for (File filezz : setPathResources) {
            try {
                InputStream inp = new FileInputStream(filezz.getPath());
                br = new BufferedReader(new InputStreamReader(inp));
                if (!filezz.getPath().contains("_")) {
                    continue;
                }
                String fileName;
                if (Parameters.local) {
                    fileName = StringUtils.substring(filezz.getPath(), StringUtils.lastIndexOf(filezz.getPath(), "\\") + 1);
                } else {
                    fileName = StringUtils.substring(filezz.getPath(), StringUtils.lastIndexOf(filezz.getPath(), "/") + 1);
                }

                int map = Integer.parseInt(StringUtils.left(fileName, fileName.indexOf("_")));
                if (map == 0) {
                    continue;
                }
//            System.out.println("map: " + map);
//            System.out.println("loading " + pathFile);
//            System.out.println("folder is: " + folder.getCanonicalPath());

                String term = null;
                String featureString;
                String feature;
                String rule = null;
                String fields[];
                String[] parametersArray;
                String[] featuresArray;
                Set<String> featuresSet;
                Iterator<String> featuresSetIterator;
                String field0;
                String field1;
                String field2;
                //mapFeatures:
                //key: a feature
                //value: a set of parameters for the given feature
                Multimap<String, Set<String>> mapFeatures;
                while ((string = br.readLine()) != null) {
                    fields = string.split("\t", -1);
                    mapFeatures = HashMultimap.create();

                    //sometimes the heuristics is just a term, not followed by a feature or a rule
                    //in this case put a null value to these fields
                    field0 = fields[0].trim();
                    if (field0.isEmpty()) {
                        continue;
                    }
                    field1 = (fields.length < 2) ? null : fields[1].trim();
                    field2 = (fields.length < 3) ? null : fields[2].trim();

                    term = field0;
                    featureString = field1;
                    rule = field2;

                    //parse the "feature" field to disentangle the feature from the parameters
                    //this parsing rule will be extended to allow for multiple features
//                if (featureString.contains("+++")) {
//                    System.out.println("featureString containing +++ " + featureString);
//                }
                    featuresArray = featureString.split("\\+\\+\\+");
                    featuresSet = new HashSet(Arrays.asList(featuresArray));
                    featuresSetIterator = featuresSet.iterator();
                    while (featuresSetIterator.hasNext()) {
                        featureString = featuresSetIterator.next();
//                    System.out.println("featureString: " + featureString);
//                    if (featureString.contains("///")) {
//                        System.out.println("featureString containing ||| " + featureString);
//                    }
                        if (featureString.contains("///")) {
                            parametersArray = StringUtils.substringAfter(featureString, "///").split("\\|");
                            feature = StringUtils.substringBefore(featureString, "///");
                            mapFeatures.put(feature, new HashSet(Arrays.asList(parametersArray)));
                        } else {
                            mapFeatures.put(featureString, null);
                        }
                    }

//                if (term.equals("I was wondering")){
//                    System.out.println("HERE!!!!");
//                }
//                System.out.println("feature: "+feature);
                    heuristic = new Heuristic();
                    heuristic.generateNewHeuristic(term, mapFeatures, rule);
                    mapHeuristics.put(term, heuristic);
                    //positive
                    if (map == 1) {
                        mapH1.put(term, heuristic);
                        continue;
                    }
                    //negative
                    if (map == 2) {
                        mapH2.put(term, heuristic);
                        continue;
                    }
                    //strong
                    if (map == 3) {
                        mapH3.put(term, heuristic);
                        continue;
                    }
                    //time
                    if (map == 4) {
                        mapH4.put(term, heuristic);
                        continue;
                    }
                    //question
                    if (map == 5) {
                        mapH5.put(term, heuristic);
                        continue;
                    }
                    //subjective
                    if (map == 6) {
                        mapH6.put(term, heuristic);
                        continue;
                    }
                    //address
                    if (map == 7) {
                        mapH7.put(term, heuristic);
                        continue;
                    }
                    //humor
                    if (map == 8) {
                        mapH8.put(term, heuristic);
                        continue;
                    }
                    //commercial offer
                    if (map == 9) {
                        mapH9.put(term, heuristic);
                        continue;
                    }
                    //negations
                    if (map == 10) {
                        setNegations.add(term);
                        continue;
                    }
                    //hints difficulty
                    if (map == 11) {
                        mapH11.put(term, heuristic);
                        continue;
                    }
                    //time indications
                    if (map == 12) {
                        setTimeTokens.add(term);
                        continue;
                    }
                    //time indications
                    if (map == 13) {
                        mapH13.put(term, heuristic);
                        continue;
                    }
                    //set of terms which look like opinions but are false postives
                    if (map == 12) {
                        setFalsePositiveOpinions.add(term);
                        continue;
                    }
                    //set of terms which look like opinions but are false postives
                    if (map == 15) {
                        setIronicallyPositive.add(term);
                        continue;
                    }

                    //set of moderators
                    if (map == 16) {
                        setModerators.add(term);
                        continue;
                    }

                }
                br.close();
            } //        System.out.println(
            //                "total number heuristics used: " + mapHeuristics.keySet().size());
            //        System.out.println(
            //                "--------------------------------------------");
            //
            //        System.out.println(
            //                "positive tone: " + mapH1.keySet().size());
            //        System.out.println(
            //                "negative tone: " + mapH2.keySet().size());
            //        System.out.println(
            //                "strength of opinion: " + mapH3.keySet().size());
            //        System.out.println(
            //                "time related: " + mapH4.keySet().size());
            //        System.out.println(
            //                "question: " + mapH5.keySet().size());
            //        System.out.println(
            //                "self turned: " + mapH6.keySet().size());
            //        System.out.println(
            //                "humor or light: " + mapH8.keySet().size());
            //        System.out.println(
            //                "direct address: " + mapH7.keySet().size());
            //        System.out.println(
            //                "commercial offer: " + mapH9.keySet().size());
            catch (IOException ex) {
                Logger.getLogger(HeuristicsLoader.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    public Map<String, Heuristic> getMapHeuristics() {
        return mapHeuristics;
    }

    public void setMapHeuristics(Map<String, Heuristic> mapHeuristics) {
        this.mapHeuristics = mapHeuristics;
    }

    public Map<String, Heuristic> getMapH1() {
        return mapH1;
    }

    public void setMapH1(Map<String, Heuristic> mapH1) {
        this.mapH1 = mapH1;
    }

    public Map<String, Heuristic> getMapH2() {
        return mapH2;
    }

    public void setMapH2(Map<String, Heuristic> mapH2) {
        this.mapH2 = mapH2;
    }

    public Map<String, Heuristic> getMapH3() {
        return mapH3;
    }

    public void setMapH3(Map<String, Heuristic> mapH3) {
        this.mapH3 = mapH3;
    }

    public Map<String, Heuristic> getMapH4() {
        return mapH4;
    }

    public void setMapH4(Map<String, Heuristic> mapH4) {
        this.mapH4 = mapH4;
    }

    public Map<String, Heuristic> getMapH5() {
        return mapH5;
    }

    public void setMapH5(Map<String, Heuristic> mapH5) {
        this.mapH5 = mapH5;
    }

    public Map<String, Heuristic> getMapH6() {
        return mapH6;
    }

    public void setMapH6(Map<String, Heuristic> mapH6) {
        this.mapH6 = mapH6;
    }

    public Map<String, Heuristic> getMapH7() {
        return mapH7;
    }

    public void setMapH7(Map<String, Heuristic> mapH7) {
        this.mapH7 = mapH7;
    }

    public Map<String, Heuristic> getMapH8() {
        return mapH8;
    }

    public void setMapH8(Map<String, Heuristic> mapH8) {
        this.mapH8 = mapH8;
    }

    public Map<String, Heuristic> getMapH9() {
        return mapH9;
    }

    public void setMapH9(Map<String, Heuristic> mapH9) {
        this.mapH9 = mapH9;
    }

    public Map<String, Heuristic> getMapH10() {
        return mapH10;
    }

    public void setMapH10(Map<String, Heuristic> mapH10) {
        this.mapH10 = mapH10;
    }

    public Map<String, Heuristic> getMapH11() {
        return mapH11;
    }

    public void setMapH11(Map<String, Heuristic> mapH11) {
        this.mapH11 = mapH11;
    }

    public Map<String, Heuristic> getMapH12() {
        return mapH12;
    }

    public Map<String, Heuristic> getMapH13() {
        return mapH13;
    }

    public void setMapH13(Map<String, Heuristic> mapH13) {
        this.mapH13 = mapH13;
    }

    public void setMapH12(Map<String, Heuristic> mapH12) {
        this.mapH12 = mapH12;
    }

    public Set<String> getSetNegations() {
        return setNegations;
    }

    public void setSetNegations(Set<String> setNegations) {
        this.setNegations = setNegations;
    }

    public Set<String> getSetTimeTokens() {
        return setTimeTokens;
    }

    public void setSetTimeTokens(Set<String> setTimeTokens) {
        this.setTimeTokens = setTimeTokens;
    }

    public Set<String> getSetFalsePositiveOpinions() {
        return setFalsePositiveOpinions;
    }

    public void setSetFalsePositiveOpinions(Set<String> setFalsePositiveOpinions) {
        this.setFalsePositiveOpinions = setFalsePositiveOpinions;
    }

    public Set<String> getSetIronicallyPositive() {
        return setIronicallyPositive;
    }

    public Set<String> getSetModerators() {
        return setModerators;
    }

}