package com.github.scipioam.scipiofx.materialfx;

import com.github.scipioam.scipiofx.framework.LogHelper;
import io.github.palexdev.materialfx.theming.JavaFXThemes;
import io.github.palexdev.materialfx.theming.MaterialFXStylesheets;
import io.github.palexdev.materialfx.theming.UserAgentBuilder;

/**
 * @author Alan Scipio
 * created on 2023/12/22
 */
public class DefaultMaterialFXInitializer implements MaterialFXInitializer{

    private final LogHelper log = new LogHelper(null);

    @Override
    public void init(boolean materialFXOnly, boolean includeLegacy) {
        UserAgentBuilder builder = UserAgentBuilder.builder();
        if (materialFXOnly) {
            builder.themes(MaterialFXStylesheets.forAssemble(includeLegacy));
        } else {
            builder.themes(JavaFXThemes.MODENA)
                    .themes(MaterialFXStylesheets.forAssemble(includeLegacy));
        }
        builder.setDeploy(true)
                .setResolveAssets(true)
                .build()
                .setGlobal();
        log.info("MaterialFX initialized");
    }

}
