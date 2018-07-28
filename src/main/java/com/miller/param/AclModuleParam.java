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
 * Created by miller on 2018/7/28
 * @author Miller
 */
@Getter
@Setter
@ToString
public class AclModuleParam {

    private Integer id;

    @NotBlank(message = "权限模块名称不能为空")
    @Length(min = 2,max = 20,message = "权限模块名称长度需要在2~64之间")
    private String name;


    private Integer parentId = 0;

    @NotNull(message = "权限模块展示顺序不能为空")
    private Integer seq;

    @NotNull(message = "权限模块状态不能为空")
    @Max(value = 1 , message = "权限模块状态不合法")
    @Min(value = 0 , message = "权限模块状态不合法")
    private Integer status;

    @Length(max = 200,message = "权限模块名称备注需要在200之间")
    private String remark;
}
