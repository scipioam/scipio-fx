package com.github.ScipioAM.scipio_fx.persistence;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @since 2022/6/30
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class EntityField {

    private String fieldName;

    private Object value;

}
