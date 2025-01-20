package com.github.scipioam.scipiofx.view;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.*;

/**
 * @since 2022/6/27
 */
@Data
@Accessors(chain = true)
public class Options {

    protected Double preWidth;

    protected Double preHeight;

    protected Collection<String> styleClass;

    public Options setStyleClass(String... styleClass) {
        if(this.styleClass == null) {
            this.styleClass = new HashSet<>();
        }
        this.styleClass.addAll(Arrays.asList(styleClass));
        return this;
    }

    public Options setStyleClass(Collection<String> styleClass) {
        this.styleClass = styleClass;
        return this;
    }

}
