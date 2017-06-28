package com.zc.express.bean;

/**
 * Created by ZC on 2017/6/28.
 */

public class QueryOrder {


    /**
     * id : 0
     * start_date : 2017-06-28T02:13:53.362Z
     * end_date : 2017-06-28T02:13:53.362Z
     * fullinfo : true
     * sort_desc : true
     */

    private int id;
    private String start_date;
    private String end_date;
    private boolean fullinfo;
    private boolean sort_desc;

    public QueryOrder(int id,String stime,String etime){
        this.id=id;
        this.start_date="2017-03-28T02:13:53.362Z";
        this.end_date="2017-06-28T02:13:53.362Z";
        this.fullinfo=true;
        this.sort_desc=true;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public boolean isFullinfo() {
        return fullinfo;
    }

    public void setFullinfo(boolean fullinfo) {
        this.fullinfo = fullinfo;
    }

    public boolean isSort_desc() {
        return sort_desc;
    }

    public void setSort_desc(boolean sort_desc) {
        this.sort_desc = sort_desc;
    }
}
