//*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
package Export;

import Admin.ControllerBean;
import Admin.Parameters;
import Twitter.Tweet;
import com.csvreader.CsvWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

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
@ManagedBean
@ViewScoped
public class ExportToCsv {

    @ManagedProperty(value = "#{controllerBean}")
    ControllerBean controllerBean;

    public void setControllerBean(ControllerBean controllerBean) {
        this.controllerBean = controllerBean;
    }
    
//    @Inject ControllerBean controllerBean;

    private String pathFile;
    private String fileName;

    public ExportToCsv() {
    }

    public void export() throws IOException {
        init();
        createTweetList(controllerBean.getTweets());
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        externalContext.redirect("http://www.clementlevallois.net/Umigon/export/" + fileName);
    }

    private void init() throws IOException {
        if (Parameters.local) {
            pathFile = "H:\\Docs Pro Clement\\NetBeansProjects\\Umigon\\export\\";
        } else {
            pathFile = "/home/clementl/public_html/clementlevallois.net/Umigon/export/";
        }
        fileName = UUID.randomUUID().toString().substring(0, 9) + ".csv";
        pathFile = pathFile + fileName;

    }

    private void createTweetList(List<Tweet> tweets) throws IOException {

        CsvWriter csvOutput = new CsvWriter(new FileWriter(pathFile, true), ',');

        csvOutput.write("user");
        csvOutput.write("tweet");
        csvOutput.write("sentiment");
        csvOutput.write("promoted");
        csvOutput.endRecord();

        for (Tweet t : tweets) {

            csvOutput.write("@"+t.getUser().getScreenName());
            csvOutput.write(t.getText());
            csvOutput.write(t.getSentiment());
            if (t.getListCategories().contains("061")) {
                csvOutput.write("yes");
            } else {
                csvOutput.write("no");
            }
            csvOutput.endRecord();
        }
        csvOutput.close();
    }

}
