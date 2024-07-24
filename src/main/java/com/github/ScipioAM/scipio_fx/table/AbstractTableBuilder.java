package com.github.ScipioAM.scipio_fx.table;

import com.github.ScipioAM.scipio_fx.table.annotations.TableColumnBind;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.SelectionMode;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.lang.reflect.Field;
import java.util.*;

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

    protected boolean readSuperClassFields = false; //是否读取父类的@TableColumnBind注解

    protected final Map<String, String> excludeFieldsCache = new HashMap<>(); //通过代码排除的列（字段名）

    protected SelectionMode selectionMode; // 单选还是多选

    /**
     * 构建tableView
     *
     * @return 绑定的数据源
     */
    public ObservableList<T> build() throws Exception {
        //检查构建参数
        checkAndInit();
        //读取所有要构建的字段
        List<TableColumnBindEntry> entries = new ArrayList<>();
        getBindEntries(dataType, entries, readSuperClassFields);
        //排序
        entries.sort(Comparator.comparing(TableColumnBindEntry::getSort));
        //列构建
        for (TableColumnBindEntry entry : entries) {
            //字段信息
            fieldColumnBuild(entry.getField(), entry.getBindInfo());
        }
        //列构建的统计总结
        if (entries.isEmpty()) {
            throw new IllegalStateException("type[" + dataType.getName() + "] must declared at least one annotation of [" + TableColumnBind.class.getName() + "]");
        } else {
            System.out.println(entries.size() + " columns of TableView have been build, based on type[" + dataType.getName() + "]");
        }
        //其他构建
        otherBuild();
        return dataSource;
    }

    private void getBindEntries(Class<?> type, List<TableColumnBindEntry> entries, boolean readSuperClassFields) {
        if (type == Object.class) {
            return;
        }
        Field[] fields = type.getDeclaredFields();
        for (Field field : fields) {
            String fieldName = field.getName();
            //排除指定的字段
            if (!excludeFieldsCache.isEmpty() && excludeFieldsCache.containsKey(fieldName)) {
                continue;
            }
            //字段信息
            TableColumnBind bindInfo = field.getAnnotation(TableColumnBind.class);
            if (bindInfo != null) {
                TableColumnBindEntry entry = new TableColumnBindEntry(field, bindInfo, bindInfo.order());
                entries.add(entry);
            }
        }
        if (readSuperClassFields) {
            Class<?> superClass = type.getSuperclass();
            getBindEntries(superClass, entries, true);
        }
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
            initEmptyData = (initData.isEmpty());
        }
        return this;
    }

    public AbstractTableBuilder<T> setExcludeFields(Collection<String> excludeFields) {
        if(excludeFields == null || excludeFields.isEmpty()) {
            excludeFieldsCache.clear();
        } else {
            for(String excludeField : excludeFields) {
                excludeFieldsCache.put(excludeField, null);
            }
        }
        return this;
    }

    public AbstractTableBuilder<T> setExcludeFields(String... excludeFields) {
        List<String> list = Arrays.asList(excludeFields);
        return setExcludeFields(list);
    }

    public AbstractTableBuilder<T> setSelectionMode(SelectionMode selectionMode) {
        this.selectionMode = selectionMode;
        return this;
    }

}
