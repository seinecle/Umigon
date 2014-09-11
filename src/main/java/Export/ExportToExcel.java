//*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
package Export;

import Admin.ControllerBean;
import Admin.Parameters;
import Twitter.Tweet;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

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
public class ExportToExcel {

    @ManagedProperty(value = "#{controllerBean}")
    ControllerBean controllerBean;

    public void setControllerBean(ControllerBean controllerBean) {
        this.controllerBean = controllerBean;
    }

    private XSSFWorkbook wb = null;
    private String pathFile;
    private String fileName;

    public ExportToExcel() {
    }

    public void export() throws IOException {
        init();
        createTweetPage(controllerBean.getTweets());
        closeWorkBook();
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        externalContext.redirect("http://www.clementlevallois.net/Umigon/export/" + fileName);
    }

    private void init() {
        wb = new XSSFWorkbook();
        if (Parameters.local) {
            pathFile = "H:\\Docs Pro Clement\\NetBeansProjects\\Umigon\\export\\";
        } else {
            pathFile = "/home/clementl/public_html/clementlevallois.net/Umigon/export/";
        }
        fileName = UUID.randomUUID().toString().substring(0, 9) + ".xlsx";
        pathFile = pathFile + fileName;

    }

    private void closeWorkBook() {
        FileOutputStream fileOut = null;
        try {
            fileOut = new FileOutputStream(pathFile);
        } catch (FileNotFoundException ex) {
        }
        try {
            wb.write(fileOut);
        } catch (IOException ex) {
        }
        try {
            fileOut.close();
        } catch (IOException ex) {
        }

    }

    private void createTweetPage(List<Tweet> tweets) {

        Row row;
        Cell cell;

        Sheet sheet = wb.createSheet("tweets");

        CreationHelper createHelper = wb.getCreationHelper();

        //Cell style for dates
        CellStyle cellStyleDate = wb.createCellStyle();
        cellStyleDate.setDataFormat(createHelper.createDataFormat().getFormat("m/d/yy h:mm"));

        //Cell style for bold
        CellStyle cellStyleBold = wb.createCellStyle();
        Font my_font = wb.createFont();
        /* set the weight of the font */
        my_font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
        cellStyleBold.setFont(my_font);

        //COLUMNS HEADER
        row = sheet.createRow((short) 0);
        row.setRowStyle(cellStyleBold);

        cell = row.createCell(0);
        cell.setCellValue("name");
        cell.setCellStyle(cellStyleBold);

        cell = row.createCell(1);
        cell.setCellValue("tweet");
        cell.setCellStyle(cellStyleBold);

        cell = row.createCell(2);
        cell.setCellValue("sentiment");
        cell.setCellStyle(cellStyleBold);

        cell = row.createCell(3);
        cell.setCellValue("promoted tweet");
        cell.setCellStyle(cellStyleBold);

        sheet = wb.getSheetAt(0);

        int index = 1;

        for (Tweet t : tweets) {

            row = sheet.createRow((short) index);
            cell = row.createCell(0);
            cell.setCellValue("@" + t.getUser().getScreenName());

            cell = row.createCell(1);
            cell.setCellValue(t.getText());

            cell = row.createCell(2);
            cell.setCellValue(t.getSentiment());

            cell = row.createCell(3);
            if (t.getListCategories().contains("061")) {
                cell.setCellValue("yes");
            } else {
                cell.setCellValue("no");

            }

            index++;

        }
        //auto-resize columns
        for (int i = 0; i < 18; i++) {
            sheet.autoSizeColumn(i, false);
        }

    }

}
