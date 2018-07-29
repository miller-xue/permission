package com.miller.param;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * Created by miller on 2018/7/29
 * @author Miller
 */
@Getter
@Setter
@ToString
public class RoleParam {

    private Integer id;

    @NotBlank(message = "角色名称不可为空")
    @Length(min = 2, max = 20, message = "角色长度需要在2~64个字之间")
    private String name;

    /**
     * 1: 默认是管理员
     */
    @Min(value = 1, message = "角色类型不合法")
    @Max(value = 2, message = "角色类型不合法")
    private Integer type = 1;


    @NotNull(message = "必须指定角色的状态")
    @Min(value = 0, message = "角色状态不合法")
    @Max(value = 1, message = "角色状态不合法")
    private Integer status;


    @Length(min = 0, max = 200, message = "角色备注长度需要在200个字以内")
    private String remark;
}
