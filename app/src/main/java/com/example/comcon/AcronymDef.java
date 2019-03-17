package com.example.comcon;

import java.util.List;

public class AcronymDef {

    public String sf;
    public List<LongForm> lfs;

    public String printPretty(){
        StringBuilder pretty = new StringBuilder();
        pretty.append(sf);
        pretty.append(":");
        for (LongForm item: lfs) {
            pretty.append(item.lf);
            pretty.append("(since)" + item.since);
            pretty.append(System.lineSeparator());
            for (Variation varItem:item.vars) {
                pretty.append(varItem.lf);
                pretty.append("since" + varItem.since);
                pretty.append(System.lineSeparator());
            }
            pretty.append(System.lineSeparator());
        }
        return pretty.toString();
    }


    public static class LongForm{
        public String lf;
        public int freq;
        public int since;
        public List<Variation> vars;
    }

    public static class Variation{
        public String lf;
        public int freq;
        public int since;
    }


}
