package com.miller.param;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

/**
 * Created by miller on 2018/7/21
 */
@Getter
@Setter
@Deprecated
public class TestVo {

    @NotBlank
    private String msg;

    @NotNull
    private Integer id;
}
