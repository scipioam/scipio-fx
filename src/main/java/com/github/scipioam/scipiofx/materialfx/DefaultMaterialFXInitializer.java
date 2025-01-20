package com.github.scipioam.scipiofx.materialfx;

import io.github.palexdev.materialfx.theming.JavaFXThemes;
import io.github.palexdev.materialfx.theming.MaterialFXStylesheets;
import io.github.palexdev.materialfx.theming.UserAgentBuilder;

/**
 * @author Alan Scipio
 * created on 2023/12/22
 */
public class DefaultMaterialFXInitializer implements MaterialFXInitializer{

    @Override
    public void init(UserAgentBuilder builder, boolean materialFXOnly) {
        if (materialFXOnly) {
            builder.themes(MaterialFXStylesheets.forAssemble(true));
        } else {
            builder.themes(JavaFXThemes.MODENA)
                    .themes(MaterialFXStylesheets.forAssemble(true));
        }
        builder.setDeploy(true)
                .setResolveAssets(true)
                .build()
                .setGlobal();
    }

}
