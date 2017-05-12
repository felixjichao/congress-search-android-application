package edu.usc.jichaozh.ownnavigation.other;

/**
 * Created by Jichao on 2016/11/25 0025.
 */
public class LegislatorInfo {
    public String party;
    public String name;
    public String district;
    public String state;
    public String bioguide_id;

    public LegislatorInfo(String[] info) {
        bioguide_id = info[0];
        party = info[1];
        name = info[2];
        district = info[3];
        state = info[4];
    }

    @Override
    public String toString() {
        return bioguide_id + ";" + party + ";" + name + ";" + district
                + ";" + state;
    }
}
