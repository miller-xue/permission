package com.miller.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * Created by miller on 2018/8/5
 */
@Getter
@Setter
@ToString
public class SearchLogDto {

    private Integer type;

    private String beforeSeg;

    private String afterSeg;

    private String operator;

    private Date formTime; // yyyy-MM-dd HH:mm:ss

    private Date toTime;
}
