package com.github.ScipioAM.scipio_fx.controller;

import com.github.ScipioAM.scipio_fx.constant.AppViewId;
import com.github.ScipioAM.scipio_fx.dialog.DialogHelper;
import com.github.ScipioAM.scipio_fx.view.FXMLView;
import com.github.ScipioAM.scipio_fx.view.ViewLoadOptions;
import javafx.scene.Parent;
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
     * @param initArgs 自定义参数
     */
    public FXMLView getOrBuildChildView(AppViewId viewInfo, Object initArgs) {
        return children.computeIfAbsent(viewInfo, key -> {
            try {
                ViewLoadOptions options = ViewLoadOptions.build()
                        .setFxml(this.getClass(), viewInfo)
                        .setInitArg(initArgs);
                FXMLView view = FXMLView.load(options);
                BaseController childController = view.getController();
                childController.setParentStage(parentStage);
                return view;
            } catch (Exception e) {
                e.printStackTrace();
                DialogHelper.showExceptionDialog(e);
                return null;
            }
        });
    }

    public abstract void onMainControllerInit(Parent rootNode, Object... initArgs);

}
