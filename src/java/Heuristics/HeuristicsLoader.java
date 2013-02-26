/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Heuristics;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
import javax.faces.context.FacesContext;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author C. Levallois
 */
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
    Set<String> setFalsePositiveOpinions;
    Set<String> setIronicallyPositive;

    public void load() throws FileNotFoundException, IOException {


        File folder = new File("D:\\Docs Pro Clement\\NetBeansProjects\\Umigon\\private\\heuristics\\");
        Set<String> setPathResources = new TreeSet();
        setPathResources.addAll(FacesContext.getCurrentInstance().getExternalContext().getResourcePaths("/resources/private/"));
//        System.out.println("folder is: " + folder.getCanonicalPath());
        mapHeuristics = new HashMap();
        setNegations = new HashSet();
        setTimeTokens = new HashSet();
        setFalsePositiveOpinions = new HashSet();
        setIronicallyPositive = new HashSet();
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
        for (String pathFile : setPathResources) {
            InputStream inp = FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream(pathFile);
            br = new BufferedReader(new InputStreamReader(inp));
            if (!pathFile.contains("_")) {
                continue;
            }
            String fileName = StringUtils.substring(pathFile, StringUtils.ordinalIndexOf(pathFile, "/", 3)+1);
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

                heuristic = new Heuristic(term, mapFeatures, rule);
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

            }
        }

//        System.out.println(
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
}
