package Utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.UUID;

//@Embedded
public class Quidam implements Comparable<Quidam>, Serializable {

    private String forename;
    private String surname;
    private String fullname;
    private ArrayList<Quidam> ld;
    private String uuid;

    public Quidam() {
    }

    public Quidam(String forename, String surname) {
        this.forename = forename.trim();
        this.surname = surname.trim();
    }

    public Quidam(String forename, String surname, UUID uuid) {
        this.forename = forename.trim();
        this.surname = surname.trim();
        this.uuid = uuid.toString();
    }

    public Quidam(String fullname, UUID uuid) {
        this.surname = fullname.trim();
        this.uuid = uuid.toString();
    }

    public Quidam(String fullname) {
        this.fullname = fullname.trim();
    }

    public void setForename(String forename) {
        this.forename = forename.trim();
    }

    public String getForename() {
        return forename;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname.trim();
    }

    public String getFullname() {

        String fullnameToReturn;
        if (fullname == null) {
            fullnameToReturn = this.toString().trim();
        } else {
            fullnameToReturn = this.fullname.trim();
        }
        return fullnameToReturn;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname.trim();
    }

    public String getLd() {
        return ld.toArray().toString();
    }

    public void setLd(ArrayList<Quidam> ld) {
        this.ld = ld;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 53 * hash + (this.getFullname() != null ? this.getFullname().hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Quidam other = (Quidam) obj;
        if ((this.getFullname() == null) ? (other.getFullname() != null) : !this.getFullname().equals(other.getFullname())) {
            return false;
        }
        return true;
    }

    public String[] toArray() {
        String[] args = new String[2];
        args[0] = forename;
        args[1] = surname;
        return args;
    }


    @Override
    public int compareTo(Quidam other) {
        int result;
        if (getFullname() == null || other.getFullname() == null) {
            result = (getForename() + getSurname()).compareTo(other.getForename() + other.getSurname());
        } else {
            result = getFullname().compareTo(other.getFullname());
        }
        return result;
    }

    @Override
    public String toString() {

        String fullnameToReturn;
        if (fullname == null) {
            fullnameToReturn = forename + " " + surname;
        } else {
            fullnameToReturn = this.fullname.trim();
        }
        return fullnameToReturn;
    }

}
