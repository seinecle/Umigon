/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Utils;

import java.io.Serializable;

/**
 *
 * @author C. Levallois
 */

public class MapLabels implements Comparable<MapLabels>, Serializable {

//    @Id
//    private ObjectId id;
    private Author author1;
    private Author author2;
    private String author1displayed;
    private String author2displayed;
    private String uuid;
    private boolean editable;
    private boolean deleted;

    public MapLabels() {
    }

    public MapLabels(Author author1, Author author2, String uuid) {
        this.author1 = author1;
        this.author2 = author2;
        this.uuid = uuid;
    }

    public MapLabels(Author author1, Author author2) {
        this.author1 = author1;
        this.author2 = author2;
    }

    public MapLabels(String author1displayed, String author2displayed) {
        this.author1displayed = author1displayed;
        this.author2displayed = author2displayed;
    }

    public Author getAuthor1() {
        return author1;
    }

    public void setAuthor1(Author author1) {
        this.author1 = author1;
    }

    public Author getAuthor2() {
        return author2;
    }

    public void setAuthor2(Author author2) {
        this.author2 = author2;
    }

    public void setUuid(String Uuid) {
        this.uuid = Uuid;
    }

    public String getUuid() {
        return uuid;
    }

    public boolean isEditable() {
        return editable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public String getAuthor1displayed() {
        return author1.getFullname();
    }

    public void setAuthor1displayed(String author1displayed) {
        this.author1.setFullname(author1displayed);
    }

    public String getAuthor2displayed() {
        return author2.getFullname();
    }

    public void setAuthor2displayed(String author2displayed) {
        this.author2.setFullname(author2displayed);
    }

    @Override
    public int compareTo(MapLabels o) {
//        return this.author2displayed.compareTo(o.getAuthor2displayed());
        return this.author2.getFullname().compareTo(o.getAuthor2().getFullname());
    }
}
