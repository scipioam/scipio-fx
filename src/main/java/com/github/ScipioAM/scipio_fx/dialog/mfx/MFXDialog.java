package com.github.ScipioAM.scipio_fx.dialog.mfx;

import com.github.ScipioAM.scipio_fx.constant.Language;
import com.github.ScipioAM.scipio_fx.dialog.IDialog;
import io.github.palexdev.materialfx.dialogs.MFXDialogs;
import io.github.palexdev.materialfx.dialogs.MFXGenericDialog;
import io.github.palexdev.materialfx.dialogs.MFXGenericDialogBuilder;
import io.github.palexdev.materialfx.dialogs.MFXStageDialog;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import lombok.AccessLevel;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * MaterialFX的dialog易用封装
 *
 * @author Alan Scipio
 * @since 2022/2/23
 */
@Getter
public class MFXDialog implements IDialog {

    //TODO MFXFilterDialog待扩充

    private final MFXGenericDialog dialogContent;
    private final MFXStageDialog dialogContainer;

    /** 对话框类型 */
    private final MFXDialogType dialogType;
    /** 标题文本 */
    private String headerText;
    /** 内容文本 */
    private String contentText;
    /** 是否显示右上角关闭图标 */
    private boolean showClose = true;
    /** 是否显示右上角最小化图标 */
    private boolean showMinimize = true;
    /** 是否显示右上角置顶图标 */
    private boolean showAlwaysOnTop = false;
    /** 是否可被拖拽 */
    private boolean draggable = true;
    /** 是否可点击对话框外的区域后，关闭对话框 */
    private boolean overlayClose = false;
    /** 是否开启遮罩特效 */
    private boolean shadowMask = false;
    /**  */
    private Pane parentPane;
    /**  */
    private Node headerIcon;

    @Getter(AccessLevel.NONE)
    private final List<MFXDialogButton> buttons = new ArrayList<>();

    /**  */
    @Getter(AccessLevel.NONE)
    private EventHandler<MouseEvent> onCloseAction;
    /**  */
    @Getter(AccessLevel.NONE)
    private EventHandler<MouseEvent> onMinimizeAction;
    /**  */
    @Getter(AccessLevel.NONE)
    private EventHandler<MouseEvent> onAlwaysOnTopAction;

    //==================================== ↓↓↓↓↓↓ 初始化 ↓↓↓↓↓↓ ====================================

    public MFXDialog(MFXDialogType dialogType) {
        if (dialogType == null) {
            throw new IllegalArgumentException("dialogType can not be null !");
        }
        this.dialogType = dialogType;
        switch (dialogType) {
            case NONE:
                dialogContent = MFXGenericDialogBuilder.build().get();
                break;
            case INFO:
                dialogContent = MFXDialogs.info().get();
                break;
            case WARN:
                dialogContent = MFXDialogs.warn().get();
                break;
            case ERROR:
                dialogContent = MFXDialogs.error().get();
                break;
//            case FILTER:
//                dialogContent = MFXDialogs.filter().get();
//                break;
            default:
                throw new IllegalArgumentException("unknown dialogType:" + dialogType);
        }
        dialogContainer = new MFXStageDialog();
        dialogContainer.setContent(dialogContent);
        init();
    }

    public static MFXDialog info() {
        return new MFXDialog(MFXDialogType.INFO);
    }

    public static MFXDialog warn() {
        return new MFXDialog(MFXDialogType.WARN);
    }

    public static MFXDialog error() {
        return new MFXDialog(MFXDialogType.ERROR);
    }

//    public static MFXDialog filter() {
//        return new MFXDialog(MFXDialogType.FILTER);
//    }

    //==================================== ↓↓↓↓↓↓ 调用API ↓↓↓↓↓↓ ====================================

    @Override
    public void show() {
        setButtons();
        dialogContainer.showDialog();
    }

    @Override
    public void close() {
        //视觉上消失
        dialogContainer.close();
        //不可被再使用
        dialogContainer.dispose();
    }

    public void showAndWait() {
        dialogContainer.showAndWait();
    }

    public void minimize() {
        dialogContainer.setIconified(true);
    }

    public void alwaysOnTop() {
        dialogContainer.requestFocus();
        dialogContainer.toFront();
    }

    //==================================== ↓↓↓↓↓↓ 设置 ↓↓↓↓↓↓ ====================================

    /**
     * 初始化默认设置
     */
    private void init() {
        dialogContent.setShowClose(showClose);
        dialogContent.setShowMinimize(showMinimize);
        dialogContent.setShowAlwaysOnTop(showAlwaysOnTop);
        dialogContainer.setDraggable(draggable);
        dialogContainer.setOverlayClose(overlayClose);
        dialogContainer.setScrimOwner(shadowMask);
        if (showClose) {
            setOnCloseAction();
        }
        if (showMinimize) {
            setOnMinimizeAction();
        }
        if (showAlwaysOnTop) {
            setOnAlwaysOnTopAction();
        }
    }

    /**
     * 落实所有按钮对象
     */
    private void setButtons() {
        if (buttons.size() == 0) {
            return;
        }
        this.buttons.sort((o1, o2) -> o2.getSortToRight() - o1.getSortToRight());
        dialogContent.clearActions();
        for (MFXDialogButton btn : this.buttons) {
            Map.Entry<Node, EventHandler<MouseEvent>> btnEntry = btn.get();
            dialogContent.addActions(btnEntry);
        }
    }

    /**
     * 清除所有动作回调
     */
    public void clearActions() {
        setOnCloseAction(null);
        setOnMinimizeAction(null);
        setOnAlwaysOnTopAction(null);
        dialogContent.clearActions();
    }

    /**
     * 重新设置所有按钮
     *
     * @param newButtons 新按钮们
     */
    public void resetAllButtons(MFXDialogButton... newButtons) {
        this.buttons.clear();
        this.buttons.addAll(List.of(newButtons));
        setButtons();
    }

    public MFXDialog setHeaderText(String headerText) {
        this.headerText = headerText;
        dialogContent.setHeaderText(headerText);
        return this;
    }

    public MFXDialog setContentText(String contentText) {
        this.contentText = contentText;
        dialogContent.setContentText(contentText);
        return this;
    }

    public MFXDialog setShowClose(boolean showClose) {
        this.showClose = showClose;
        dialogContent.setShowClose(showClose);
        return this;
    }

    public MFXDialog setShowMinimize(boolean showMinimize) {
        this.showMinimize = showMinimize;
        dialogContent.setShowClose(showClose);
        return this;
    }

    public MFXDialog setShowAlwaysOnTop(boolean showAlwaysOnTop) {
        this.showAlwaysOnTop = showAlwaysOnTop;
        dialogContent.setShowClose(showClose);
        return this;
    }

    public MFXDialog setDraggable(boolean draggable) {
        this.draggable = draggable;
        dialogContainer.setDraggable(draggable);
        return this;
    }

    public MFXDialog setOverlayClose(boolean overlayClose) {
        this.overlayClose = overlayClose;
        dialogContainer.setOverlayClose(overlayClose);
        return this;
    }

    public MFXDialog setShadowMask(boolean shadowMask) {
        this.shadowMask = shadowMask;
        dialogContainer.setScrimOwner(shadowMask);
        return this;
    }

    public MFXDialog setParentPane(Pane parentPane) {
        this.parentPane = parentPane;
        dialogContainer.setOwnerNode(parentPane);
        return this;
    }

    public MFXDialog setHeaderIcon(Node headerIcon) {
        this.headerIcon = headerIcon;
        dialogContent.setHeaderIcon(headerIcon);
        return this;
    }

    public MFXDialog addButton(MFXDialogButton btn) {
        buttons.add(btn);
        return this;
    }

    public MFXDialog addButton(String btnText, EventHandler<MouseEvent> btnAction, Integer sortToRight) {
        MFXDialogButton btn = MFXDialogButton.create()
                .setText(btnText)
                .setAction(btnAction);
        if (sortToRight == null) {
            //自动确定值
            //从0开始，因为是先确定sort值再加到list里去的
            sortToRight = buttons.size();
        }
        btn.setSortToRight(sortToRight);
        buttons.add(btn);
        return this;
    }

    public MFXDialog addButton(String btnText, EventHandler<MouseEvent> btnAction) {
        return addButton(btnText, btnAction, null);
    }

    public MFXDialog addOkButton(String btnText, EventHandler<MouseEvent> btnAction) {
        return addButton(btnText, btnAction, 0);
    }

    public MFXDialog addOkButton(EventHandler<MouseEvent> btnAction, Language language) {
        String btnText ;
        switch (language) {
            case CN :
                btnText = "确定";
                break;
            case EN:
            default:
                btnText = "O K";
                break;
        };
        return addOkButton(btnText, btnAction);
    }

    public MFXDialog addOkButton(Language language) {
        return addOkButton(getCloseAction(), language);
    }

    public MFXDialog addOkButton(String btnText) {
        return addOkButton(btnText, getCloseAction());
    }

    public MFXDialog addCancelButton(String btnText, EventHandler<MouseEvent> btnAction) {
        return addButton(btnText, btnAction, 1);
    }

    public MFXDialog addCancelButton(EventHandler<MouseEvent> btnAction, Language language) {
        String btnText;
        switch (language) {
            case CN:
                btnText = "取消";
                break;
            case EN:
            default:
                btnText = "Cancel";
                break;
        }
        return addCancelButton(btnText, btnAction);
    }

    public MFXDialog addCancelButton(Language language) {
        return addCancelButton(getCloseAction(), language);
    }

    public MFXDialog addCancelButton(String btnText) {
        return addCancelButton(btnText, getCloseAction());
    }

    public MFXDialog setOnCloseAction(EventHandler<MouseEvent> onCloseAction) {
        this.onCloseAction = onCloseAction;
        dialogContent.setOnClose(onCloseAction);
        return this;
    }

    public MFXDialog setOnCloseAction() {
        onCloseAction = getCloseAction();
        dialogContent.setOnClose(onCloseAction);
        return this;
    }

    public MFXDialog setOnMinimizeAction(EventHandler<MouseEvent> onMinimizeAction) {
        this.onMinimizeAction = onMinimizeAction;
        dialogContent.setOnMinimize(onMinimizeAction);
        return this;
    }

    public MFXDialog setOnMinimizeAction() {
        onMinimizeAction = (event) -> minimize();
        dialogContent.setOnMinimize(onMinimizeAction);
        return this;
    }

    public MFXDialog setOnAlwaysOnTopAction(EventHandler<MouseEvent> onAlwaysOnTopAction) {
        this.onAlwaysOnTopAction = onAlwaysOnTopAction;
        dialogContent.setOnAlwaysOnTop(onAlwaysOnTopAction);
        return this;
    }

    public MFXDialog setOnAlwaysOnTopAction() {
        onAlwaysOnTopAction = (event) -> alwaysOnTop();
        dialogContent.setOnAlwaysOnTop(onAlwaysOnTopAction);
        return this;
    }

    public EventHandler<MouseEvent> getCloseAction() {
        return (event) -> close();
    }

}
