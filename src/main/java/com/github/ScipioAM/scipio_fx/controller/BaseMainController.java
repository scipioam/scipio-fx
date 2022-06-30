package com.github.ScipioAM.scipio_fx.controller;

import com.github.ScipioAM.scipio_fx.constant.AppViewId;
import com.github.ScipioAM.scipio_fx.dialog.DialogHelper;
import com.github.ScipioAM.scipio_fx.view.FXMLView;
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
     * @param appViewId         子view的标识key
     * @param exceptionWhenNull 如果获取为null则抛异常并弹出报错弹窗. true:要这样做
     * @return 子view
     * @throws IllegalStateException 子view为null（children中没有此子view）
     */
    public FXMLView getChildView(AppViewId appViewId, boolean exceptionWhenNull) throws IllegalStateException {
        FXMLView view = children.get(appViewId);
        if (view == null && exceptionWhenNull) {
            IllegalStateException e = new IllegalStateException("get child view failed, view[id=" + appViewId.id() + ", title=" + appViewId.title() + "] may not be loaded");
            DialogHelper.showExceptionDialog(e);
            throw e;
        }
        return view;
    }

    public FXMLView getChildView(AppViewId appViewId) {
        return getChildView(appViewId, false);
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
    public abstract void loadChildViews(AppViewId viewInfo, Object initArgs);

}
