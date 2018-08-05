package com.miller.param;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by miller on 2018/8/5
 */
@Getter
@Setter
@ToString
public class SearchLogParam {

    private Integer type;

    private String beforeSeg;

    private String afterSeg;

    private String operator;

    private String formTime; // yyyy-MM-dd HH:mm:ss

    private String toTime;

}
