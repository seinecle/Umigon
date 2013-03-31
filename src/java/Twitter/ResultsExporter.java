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
package Twitter;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class ResultsExporter {

    private ArrayList<Tweet> setTweets;
    private String fileName;

    public ResultsExporter(ArrayList<Tweet> setTweets, String fileName) {
        this.setTweets = setTweets;
        this.fileName = fileName;
    }

    public void writeSemeValOutput() throws IOException {

        BufferedWriter bw = new BufferedWriter(new FileWriter(fileName));
        Iterator<Tweet> iterator = setTweets.iterator();
        StringBuilder sb = new StringBuilder();
        String polarity;
        while (iterator.hasNext()) {
            Tweet tweet = iterator.next();
            if (tweet.isIsPositive()) {
                polarity = "positive";
            } else if (tweet.isIsNegative()) {
                polarity = "negative";
            } else {
                polarity = "neutral";
            }
            sb.append("NA").append("\t").append(tweet.getSemevalId()).append("\t").append(polarity).append("\t").append(tweet.getText()).append("\n");
        }
        bw.write(sb.toString());
        bw.close();



    }
}
