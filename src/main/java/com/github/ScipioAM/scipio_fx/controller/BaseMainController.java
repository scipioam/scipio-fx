package com.github.ScipioAM.scipio_fx.controller;

import com.github.ScipioAM.scipio_fx.app.config.ApplicationConfig;
import com.github.ScipioAM.scipio_fx.constant.AppView;
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

    protected final Map<AppView, FXMLView> children = new LinkedHashMap<>();

    protected ApplicationConfig appConfig;

    @Override
    public void onStop() throws Exception {
        super.onStop();
        //调用所有子界面的onStop
        for (Map.Entry<AppView, FXMLView> entry : children.entrySet()) {
            BaseController childController = entry.getValue().getController();
            childController.onStop();
        }
    }

    /**
     * 存放子view入children
     *
     * @param appView   子view的标识key，建议用枚举类去实现此接口
     * @param childView 子view实例
     */
    public void putChildView(AppView appView, FXMLView childView) {
        children.put(appView, childView);
    }

    /**
     * 获取子view
     *
     * @param appView           子view的标识key
     * @param exceptionWhenNull 如果获取为null则抛异常并弹出报错弹窗. true:要这样做
     * @return 子view
     * @throws IllegalStateException 子view为null（children中没有此子view）
     */
    public FXMLView getChildView(AppView appView, boolean exceptionWhenNull) throws IllegalStateException {
        FXMLView view = children.get(appView);
        if (view == null && exceptionWhenNull) {
            IllegalStateException e = new IllegalStateException("get child view failed, view[id=" + appView.id() + ", title=" + appView.title() + "] may not be loaded");
            DialogHelper.showExceptionDialog(e);
            throw e;
        }
        return view;
    }

    public FXMLView getChildView(AppView appView) {
        return getChildView(appView, false);
    }

    /**
     * 获取子view的controller实例
     */
    public BaseController getChildController(AppView appView) {
        return getChildView(appView).getController();
    }

    /**
     * 根据标识key加载子view
     *
     * @param viewInfo 子view的标识key
     */
    public abstract void loadChildViews(AppView viewInfo);

}
