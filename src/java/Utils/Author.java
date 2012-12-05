/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Utils;

import java.util.UUID;


public class Author extends Quidam {

    private int yearFirstCollab;
    private int yearLastCollab;
    private int timesMentioned;

    public Author() {
    }

    public Author(String forename, String surname) {
        super(forename.trim(), surname.trim());
    }

    public Author(String forename, String surname, UUID uuid) {
        super(forename.trim(), surname.trim(), uuid);
    }

    public Author(String fullname, UUID uuid) {
        super(fullname.trim(), uuid);
    }

    public Author(String fullnameWithComma) {
        super(fullnameWithComma.trim());
    }

    public int getYearFirstCollab() {
        return yearFirstCollab;
    }

    public void setYearFirstCollab(int yearFirstCollab) {
        this.yearFirstCollab = yearFirstCollab;
    }

    public int getYearLastCollab() {
        return yearLastCollab;
    }

    public void setYearLastCollab(int yearLastCollab) {
        this.yearLastCollab = yearLastCollab;
    }

    public int getTimesMentioned() {
        return timesMentioned;
    }

    public void setTimesMentioned(int timesMentioned) {
        this.timesMentioned = timesMentioned;
    }
    
    
}
