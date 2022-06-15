package com.github.ScipioAM.scipio_fx.table;

import com.github.ScipioAM.scipio_fx.table.annotations.TableColumnBind;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.lang.reflect.Field;
import java.util.Collection;

/**
 * @since 2022/6/13
 */
@Data
@Accessors(chain = true)
public abstract class AbstractTableBuilder<T> {

    @Setter(AccessLevel.NONE)
    protected ObservableList<T> dataSource;

    protected Class<T> dataType;

    @Setter(AccessLevel.NONE)
    protected boolean initEmptyData = false; //当数据源是空的时候，为其添加一行空数据后再删除，否则ui会出问题不更新数据源的变化

    /**
     * 构建tableView
     *
     * @return 绑定的数据源
     */
    public ObservableList<T> build() throws Exception {
        //检查构建参数
        checkAndInit();
        //列构建
        int bindCount = 0;
        Field[] fields = dataType.getDeclaredFields();
        for (Field field : fields) {
            //字段信息
            TableColumnBind bindInfo = field.getAnnotation(TableColumnBind.class);
            if (bindInfo == null) {
                continue;
            } else {
                bindCount++;
            }
            fieldColumnBuild(field, bindInfo);
        }
        //列构建的统计总结
        if (bindCount == 0) {
            throw new IllegalStateException("type[" + dataType.getName() + "] must declared at least one annotation of [" + TableColumnBind.class.getName() + "]");
        } else {
            System.out.println(bindCount + " columns of TableView have been build, based on type[" + dataType.getName() + "]");
        }
        //其他构建
        otherBuild();
        return dataSource;
    }

    /**
     * 每列的构建
     *
     * @param field    该列对应的字段
     * @param bindInfo 该字段上设定的基础信息
     */
    protected abstract void fieldColumnBuild(Field field, TableColumnBind bindInfo) throws Exception;

    /**
     * 其他构建
     */
    protected abstract void otherBuild() throws Exception;

    //===============================================================================================================================================

    protected void checkAndInit() throws IllegalArgumentException {
        if (dataType == null) {
            throw new IllegalArgumentException("dataType can not be null");
        }
        if (dataSource == null) {
            dataSource = FXCollections.observableArrayList();
        }
    }

    public AbstractTableBuilder<T> initDataSource(boolean initEmptyData) {
        this.initEmptyData = initEmptyData;
        return this;
    }

    public AbstractTableBuilder<T> initDataSource(Collection<T> initData) {
        if (initData != null) {
            if (initData instanceof ObservableList) {
                dataSource = (ObservableList<T>) initData;
            } else {
                dataSource.addAll(initData);
            }
            initEmptyData = (initData.size() == 0);
        }
        return this;
    }

}
