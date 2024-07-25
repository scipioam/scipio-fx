package com.github.ScipioAM.scipio_fx.controller;

import com.github.ScipioAM.scipio_fx.constant.AppViewId;
import com.github.ScipioAM.scipio_fx.dialog.DialogHelper;
import com.github.ScipioAM.scipio_fx.view.FXMLView;
import com.github.ScipioAM.scipio_fx.view.ViewLoadOptions;
import javafx.scene.Parent;
import javafx.stage.Modality;
import javafx.stage.StageStyle;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @since 2022/6/21
 */
@Data
@EqualsAndHashCode(callSuper = true)
public abstract class BaseMainController extends BaseController {

    protected final Map<AppViewId, FXMLView> children = new LinkedHashMap<>();

    @Override
    public void onStop() throws Exception {
        super.onStop();
        //调用所有子界面的onStop
        for (Map.Entry<AppViewId, FXMLView> entry : children.entrySet()) {
            BaseController childController = entry.getValue().getController();
            childController.onStop();
        }
    }

    @Override
    public void onInitThreadFinished() {
        for (Map.Entry<AppViewId, FXMLView> child : children.entrySet()) {
            FXMLView childView = child.getValue();
            if (childView == null || childView.getController() == null) {
                continue;
            }
            BaseController childController = childView.getController();
            childController.onInitThreadFinished();
        }
    }

    @Override
    public void onLoadInit(Parent rootNode, Object... initArgs) {
        onMainControllerInit(rootNode, initArgs);
    }

    /**
     * 存放子view入children
     *
     * @param appViewId 子view的标识key，建议用枚举类去实现此接口
     * @param childView 子view实例
     */
    public void putChildView(AppViewId appViewId, FXMLView childView) {
        children.put(appViewId, childView);
    }

    /**
     * 获取子view
     *
     * @param appViewId 子view的标识key
     * @return 子view，如果没有则返回null
     * @throws IllegalStateException 子view为null（children中没有此子view）
     */
    public FXMLView getChildView(AppViewId appViewId) throws IllegalStateException {
        return children.get(appViewId);
    }

    /**
     * 获取子view的controller实例
     */
    public BaseController getChildController(AppViewId appViewId) {
        return getChildView(appViewId).getController();
    }

    /**
     * 根据标识key加载子view
     *
     * @param viewInfo 子view的标识key
     * @param options  view构建参数
     */
    public FXMLView getOrBuildChildView(AppViewId viewInfo, ViewLoadOptions options) {
        if (options == null) {
            throw new IllegalArgumentException("options cannot be null");
        }
        return children.computeIfAbsent(viewInfo, key -> {
            try {
                options.setFxml(this.getClass(), viewInfo);
                options.setViewInfo(viewInfo);
                FXMLView childView = FXMLView.load(options);
                BaseController childController = childView.getController();
                childController.setParentController(this);
                return childView;
            } catch (Exception e) {
                e.printStackTrace();
                DialogHelper.showExceptionDialog(e);
                return null;
            }
        });
    }

    public FXMLView getOrBuildChildView(AppViewId viewInfo, Object initArgs) {
        ViewLoadOptions options = ViewLoadOptions.build()
                .setInitArg(initArgs);
        return getOrBuildChildView(viewInfo, options);
    }

    public <T extends AppViewId> void showJumpView(Parent rootPane, BaseController parentController, T viewId, Object initArgs) {
        if (rootPane == null) {
            throw new IllegalArgumentException("rootPane cannot be null");
        }
        if (parentController == null) {
            throw new IllegalArgumentException("parentController cannot be null");
        }
        if (viewId == null) {
            throw new IllegalArgumentException("viewId cannot be null");
        }
        ViewLoadOptions options = ViewLoadOptions.build()
                .setStageOptions(rootPane, StageStyle.UTILITY, Modality.APPLICATION_MODAL)
                .setInitArg(initArgs);
        FXMLView jumpView = getOrBuildChildView(viewId, options);
        BaseController jumpController = jumpView.getController();
        jumpController.setParentController(parentController);
        jumpView.show(initArgs);
    }

    public abstract void onMainControllerInit(Parent rootNode, Object... initArgs);

}
