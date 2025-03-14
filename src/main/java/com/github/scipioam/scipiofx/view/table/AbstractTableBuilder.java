package com.github.scipioam.scipiofx.view.table;

import com.github.scipioam.scipiofx.framework.LogHelper;
import com.github.scipioam.scipiofx.view.table.annotations.TableColumnBind;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.SelectionMode;

import java.lang.reflect.Field;
import java.util.*;

@SuppressWarnings("LombokGetterMayBeUsed")
public abstract class AbstractTableBuilder<T> {

    protected final LogHelper log = new LogHelper(null);

    protected ObservableList<T> dataSource;

    protected Class<T> dataType;

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
            log.info("{} columns of TableView have been build, based on type[{}]", entries.size(), dataType.getName());
        }
        //其他构建
        postProcessing(entries);
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
     * 后处理
     */
    protected abstract void postProcessing(List<TableColumnBindEntry> entries) throws Exception;

    //===============================================================================================================================================

    protected void checkAndInit() throws IllegalArgumentException {
        if (dataType == null) {
            throw new IllegalArgumentException("dataType can not be null");
        }
        if (dataSource == null) {
            dataSource = FXCollections.observableArrayList();
        }
    }

    protected void setDataSource(boolean initEmptyData) {
        this.initEmptyData = initEmptyData;
    }

    protected void setDataSource(Collection<T> initData) {
        if (initData != null) {
            if (initData instanceof ObservableList) {
                dataSource = (ObservableList<T>) initData;
            } else {
                dataSource.clear();
                dataSource.addAll(initData);
            }
        }
        initEmptyData = initData == null || initData.isEmpty();
    }

    protected void setExcludeFields(Collection<String> excludeFields) {
        if(excludeFields == null || excludeFields.isEmpty()) {
            excludeFieldsCache.clear();
        } else {
            for(String excludeField : excludeFields) {
                excludeFieldsCache.put(excludeField, null);
            }
        }
    }

    protected void setExcludeFields(String... excludeFields) {
        List<String> list = Arrays.asList(excludeFields);
        setExcludeFields(list);
    }

    public ObservableList<T> getDataSource() {
        return dataSource;
    }

    public Class<T> getDataType() {
        return dataType;
    }

    public boolean isInitEmptyData() {
        return initEmptyData;
    }

    public boolean isReadSuperClassFields() {
        return readSuperClassFields;
    }

    public Map<String, String> getExcludeFieldsCache() {
        return excludeFieldsCache;
    }

    public SelectionMode getSelectionMode() {
        return selectionMode;
    }
}
