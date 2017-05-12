package edu.usc.jichaozh.ownnavigation.other;

/**
 * Created by Jichao on 2016/11/25 0025.
 */
public class CommInfo {
    public String committee_id;
    public String name;
    public String chamberImg;
    public String chamber;
    public String parent_comm;
    public String contact;
    public String office;

    public CommInfo(String[] info) {
        committee_id = info[0];
        name = info[1];
        chamberImg = info[2];
        chamber = info[3];
        parent_comm = info[4];
        contact = info[5];
        office = info[6];
    }

    @Override
    public String toString() {
        return committee_id + ";" + name + ";" + chamberImg + ";"
                + chamber + ";" + parent_comm + ";" + contact + ";" + office;
    }
}
