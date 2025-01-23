package scipiofx.test;

import com.github.scipioam.scipiofx.framework.AppViewId;

/**
 * 弹出子画面
 *
 * @author Alan Scipio
 * created on 2025-01-22
 */
public enum JumpView implements AppViewId {

    /**
     * 输入项目及对话框测试界面
     */
    INPUTS_DIALOG(1, "输入项和弹框测试", "/test-views/inputs-dialog.fxml"),

    /**
     * ControlsFX表格测试界面
     */
    TABLE_CFX(2, "表格-ControlsFX", "/test-views/table-cfx.fxml"),

    /**
     * MaterialFX表格测试界面
     */
    TABLE_MFX(3, "表格-MaterialFX", "/test-views/table-mfx.fxml"),
    ;

    private final int id;
    private final String title;
    private final String fxmlPath;

    JumpView(int id, String title, String fxmlPath) {
        this.id = id;
        this.title = title;
        this.fxmlPath = fxmlPath;
    }

    @Override
    public int id() {
        return id;
    }

    @Override
    public String title() {
        return title;
    }

    @Override
    public String fxmlPath() {
        return fxmlPath;
    }
}
