package com.github.ScipioAM.scipio_fx.controller;

import com.github.ScipioAM.scipio_fx.constant.AppView;
import com.github.ScipioAM.scipio_fx.view.FXMLView;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @since 2022/6/21
 */
public abstract class BaseMainController extends BaseController {

    protected final Map<AppView, FXMLView> children = new LinkedHashMap<>();

    @Override
    public void onStop() throws Exception {
        super.onStop();
        //调用所有子界面的onStop
        for (Map.Entry<AppView, FXMLView> entry : children.entrySet()) {
            BaseController childController = entry.getValue().getController();
            childController.onStop();
        }
    }

    public void putChildView(AppView appView, FXMLView childView) {
        children.put(appView, childView);
    }

    public FXMLView getChildView(AppView appView) {
        FXMLView view = children.get(appView);
        if (view == null) {
            throw new IllegalStateException("get child view failed, view[id=" + appView.id() + ", title=" + appView.title() + "] may not be loaded");
        }
        return view;
    }

    public BaseController getChildController(AppView appView) {
        return getChildView(appView).getController();
    }

    public abstract void loadChildViews(AppView viewInfo);

}
