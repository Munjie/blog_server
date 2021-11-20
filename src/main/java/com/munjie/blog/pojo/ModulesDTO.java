package com.munjie.blog.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

/**
 * @author 86158
 * @Auther: munjie
 * @Date: 2/19/2021 22:11
 * @Description:
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ModulesDTO {
    private List<ModuleDO> rows;
    private Long total;
}
