package com.github.scipioam.scipiofx.framework;

import com.github.scipioam.scipiofx.framework.fxml.FXMLView;
import com.github.scipioam.scipiofx.framework.fxml.ViewArgs;
import com.github.scipioam.scipiofx.framework.fxml.ViewLoadOptions;
import com.github.scipioam.scipiofx.controlsfx.CFXDialogHelper;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.stage.Modality;
import javafx.stage.StageStyle;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @since 2022/6/21
 */
@SuppressWarnings({"LombokGetterMayBeUsed", "UnusedReturnValue"})
public abstract class BaseMainController extends BaseController {

    protected final Map<AppViewId, FXMLView> children = new LinkedHashMap<>();

    public abstract void onMainControllerInit(AppContext context, Parent rootNode, ViewArgs initArgs);

    public Region getContentPane() {
        return null;
    }

    protected void loadChildrenForContentPane(Region contentPane, FXMLView childView) {
        if (contentPane instanceof Pane cp) {
            cp.getChildren().clear();
            cp.getChildren().add(childView.getSelf());
        }
    }

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
    public void onLoadInit(AppContext context, Parent rootNode, ViewArgs initArgs) {
        onMainControllerInit(context, rootNode, initArgs);
    }

    /**
     * 加载内部子view
     *
     * @param appViewId 子view的标识key
     * @param initArgs  自定义初始化参数
     */
    public FXMLView loadInternalChildView(AppViewId appViewId, ViewArgs initArgs) {
        ViewLoadOptions options = ViewLoadOptions.build()
                .setInitArgs(initArgs);
        FXMLView childView = getOrBuildChildView(appViewId, options);
        if (childView == null) {
            throw new IllegalStateException("childView is null, appViewId:[" + appViewId.id() + "], title:[" + appViewId.title() + "], fxmlPath: [" + appViewId.fxmlPath() + "]");
        }
        Region contentPane = getContentPane();
        if (contentPane == null) {
            throw new IllegalStateException("contentPane cannot be null");
        }

        // 内部子view的window和stage跟随父view
        BaseController childController = childView.getController();
        childController.setMyWindow(myWindow);
        childView.setStage(JFXApplication.getContext().mainStage);

        loadChildrenForContentPane(contentPane, childView);
        return childView;
    }

    /**
     * 加载并显示外部子view
     *
     * @param appViewId  子view的标识key
     * @param container  父容器
     * @param stageStyle 窗口风格
     * @param modality   模态的模式
     * @param initArgs   初始化参数
     */
    public FXMLView showExternalChildView(AppViewId appViewId, Parent container, StageStyle stageStyle, Modality modality, ViewArgs initArgs) {
        if (container == null) {
            throw new IllegalArgumentException("parent container cannot be null");
        }
        ViewLoadOptions options = ViewLoadOptions.build()
                .setInitArgs(initArgs);
        if (stageStyle == null && modality == null) {
            options.defaultStageOptions(container);
        } else {
            options.setStageOptions(container, stageStyle, modality);
        }
        FXMLView childView = getOrBuildChildView(appViewId, options);
        if (childView == null) {
            throw new IllegalStateException("childView is null, appViewId:[" + appViewId.id() + "], title:[" + appViewId.title() + "], fxmlPath: [" + appViewId.fxmlPath() + "]");
        }
        childView.show(initArgs);
        return childView;
    }

    public FXMLView showExternalChildView(AppViewId appViewId, Parent container, ViewArgs initArgs) {
        return showExternalChildView(appViewId, container, null, null, initArgs);
    }

    public FXMLView showExternalChildView(AppViewId appViewId, Parent container, StageStyle stageStyle, Modality modality) {
        return showExternalChildView(appViewId, container, stageStyle, modality, null);
    }

    /**
     * 根据标识key加载子view
     *
     * @param appViewId 子view的标识key
     * @param options   加载参数
     */
    protected FXMLView getOrBuildChildView(AppViewId appViewId, ViewLoadOptions options) {
        if (appViewId == null) {
            throw new IllegalArgumentException("appViewId cannot be null");
        }
        return children.computeIfAbsent(appViewId, key -> {
            try {
                options.setFxml(key);
                FXMLView childView = FXMLView.load(options);
                if (childView == null) {
                    throw new IllegalStateException("childView load failed, appViewId:[" + appViewId.id() + "], title:[" + appViewId.title() + "], fxmlPath: [" + appViewId.fxmlPath() + "]");
                }
                BaseController childController = childView.getController();
                childController.setParentController(this);
                return childView;
            } catch (Exception e) {
                e.printStackTrace();
                CFXDialogHelper.showExceptionDialog(e);
                return null;
            }
        });
    }

    /**
     * 存放子view入children
     *
     * @param appViewId 子view的标识key，建议用枚举类去实现此接口
     * @param childView 子view实例
     */
    public void putChildView(AppViewId appViewId, FXMLView childView) {
        if (childView == null) {
            throw new IllegalArgumentException("childView cannot be null");
        }
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

    public Map<AppViewId, FXMLView> getChildren() {
        return children;
    }
}
