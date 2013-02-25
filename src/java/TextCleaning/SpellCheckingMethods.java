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
package TextCleaning;

import Twitter.ControllerBean;
import java.util.HashSet;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;

public class SpellCheckingMethods {

    public String repeatedCharacters(String currTerm) {
        String toReturn;
        Integer index = null;
        Set<RepeatedLetters> setRL = new HashSet();
        int count = 1;
        char[] chars = currTerm.toCharArray();
        char currChar;
        char previousChar = 0;
        for (int i = 0; i < chars.length; i++) {
            currChar = chars[i];
            if (i > 0) {
                previousChar = chars[i - 1];
            }
            if (previousChar == currChar && StringUtils.isAlpha(String.valueOf(previousChar))) {
                if (index == null) {
                    index = i - 1;
                }
                count++;

            } else {
                if (count > 1) {
                    setRL.add(new RepeatedLetters(previousChar, index, count));
                    count = 1;
                }
                index = null;

            }
            if (i == (chars.length - 1) && count > 1) {
                setRL.add(new RepeatedLetters(previousChar, index, count));

            }
        }


        for (RepeatedLetters rl : setRL) {
            String letter = String.valueOf(rl.getCurrChar());
            if (rl.getCount() > 2) {

                String toReplace = letter + letter;
                String toBeReplaced = currTerm.substring(rl.getIndex(), rl.getIndex() + rl.getCount());
                toReturn = StringUtils.replace(currTerm, toBeReplaced, toReplace);

                if (ControllerBean.Hloader.getMapHeuristics().containsKey(toReturn)) {
                    return toReturn;
                }

                toReplace = letter;
                toReturn = StringUtils.replace(currTerm, toBeReplaced, toReplace);

                if (ControllerBean.Hloader.getMapHeuristics().containsKey(toReturn)) {
                    return toReturn;
                }
            }
        }
        return currTerm;
    }

    private class RepeatedLetters {

        private char currChar;
        private int index;
        private int count;

        public RepeatedLetters(char currChar, int index, int count) {
            this.currChar = currChar;
            this.index = index;
            this.count = count;
        }

        public char getCurrChar() {
            return currChar;
        }

        public int getIndex() {
            return index;
        }

        public int getCount() {
            return count;
        }
    }
}
