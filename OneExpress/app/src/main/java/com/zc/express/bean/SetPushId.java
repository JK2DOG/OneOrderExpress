package com.zc.express.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JK2DOG on 2017/7/6.
 */

public class SetPushId {


    /**
     * registration_id : string
     * alias : string
     * tags : ["string"]
     */

    private String registration_id;
    private String alias;
    private List<String> tags;

    public SetPushId(String registration_id){
        this.registration_id=registration_id;
        this.tags=new ArrayList<>();
        this.tags.add("eshipshippertag");
        this.alias="eshipv2";
    }


    public String getRegistration_id() {
        return registration_id;
    }

    public void setRegistration_id(String registration_id) {
        this.registration_id = registration_id;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }
}
