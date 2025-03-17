package com.github.scipioam.scipiofx.materialfx.table;

import com.github.scipioam.scipiofx.view.table.AbstractTableBuilder;
import com.github.scipioam.scipiofx.view.table.TableColumnBindEntry;
import com.github.scipioam.scipiofx.view.table.annotations.TableColumnBind;
import com.github.scipioam.scipiofx.view.table.annotations.TableColumnComparator;
import com.github.scipioam.scipiofx.view.table.annotations.TableColumnFilter;
import io.github.palexdev.materialfx.builders.control.TableBuilder;
import io.github.palexdev.materialfx.controls.MFXPaginatedTableView;
import io.github.palexdev.materialfx.controls.MFXTableColumn;
import io.github.palexdev.materialfx.controls.MFXTableView;
import io.github.palexdev.materialfx.filter.base.AbstractFilter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.SelectionMode;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

/**
 * {@link MFXTableView}的构建器
 */
public class MFXTableBuilder<T> extends AbstractTableBuilder<T> {

    private final TableBuilder<T, MFXTableView<T>> innerBuilder;
    private final ObservableList<AbstractFilter<T, ?>> filters = FXCollections.observableArrayList();
    private final MFXTableView<T> tableView;

    private MFXRowCellBuilder<T> rowCellBuilder;
    private MFXTableColumnBuildListener<T> columnBuildListener;

    //===============================================================================================================================================

    public MFXTableBuilder() {
        this(null);
    }

    public MFXTableBuilder(MFXTableView<T> tableView) {
        if (tableView == null) {
            innerBuilder = TableBuilder.table();
            this.tableView = innerBuilder.getNode();
        } else {
            this.tableView = tableView;
            innerBuilder = TableBuilder.table(tableView);
        }
    }

    //===============================================================================================================================================

    /**
     * 每列的构建
     *
     * @param field    该列对应的字段
     * @param bindInfo 该字段上设定的基础信息
     */
    @SuppressWarnings("unchecked")
    protected void fieldColumnBuild(Field field, TableColumnBind bindInfo) throws Exception {
        MFXTableColumn<T> column = new MFXTableColumn<>(bindInfo.title(), bindInfo.resizable());
        //行构建监听器，可自定义构建逻辑，以取代后续所有默认构建逻辑
        if (columnBuildListener != null) {
            if (!columnBuildListener.onBuildColumn(tableView, column, dataType, field, bindInfo)) {
                return;
            }
        }
        Pos alignment = bindInfo.alignment();
        column.setAlignment(alignment);
        //列排序
        if (bindInfo.compared()) {
            TableColumnComparator comparatorInfo = field.getAnnotation(TableColumnComparator.class);
            if (comparatorInfo == null) {
                column.setComparator(new MFXColumnComparator<>(field));
            } else {
                Class<? extends Comparator<?>> comparatorType = comparatorInfo.value();
                Comparator<?> comparator;
                if (comparatorInfo.bindField()) {
                    comparator = comparatorType.getDeclaredConstructor(Field.class).newInstance(field);
                } else {
                    comparator = comparatorType.getDeclaredConstructor().newInstance();
                }
                column.setComparator((Comparator<T>) comparator);
            }
        }
        //列数据绑定
        MFXRowCellBuilder<T> rcBuilder = rowCellBuilder != null ? rowCellBuilder : new MFXDefaultRowCellBuilder<>();
        column.setRowCellFactory(t -> rcBuilder.build(t, dataType, field, bindInfo));
        //过滤器信息
        if (bindInfo.filtered()) {
            //过滤器名称，如果不设置就默认采用列标题
            String filterName = bindInfo.filterName();
            if (filterName == null || filterName.isEmpty()) {
                filterName = bindInfo.title();
            }
            TableColumnFilter filterInfo = field.getAnnotation(TableColumnFilter.class);
            AbstractFilter<T, ?> filter;
            if (filterInfo == null) {
                //默认过滤器
                filter = MFXTableFilterUtil.getDefaultFilter(filterName, field, dataType);
            } else {
                //自定义过滤器（也可以手动添加）
                Class<? extends AbstractFilter<?, ?>> rawFilterType = filterInfo.value();
                Class<AbstractFilter<T, ?>> filterType = (Class<AbstractFilter<T, ?>>) rawFilterType;
                filter = filterType.getDeclaredConstructor().newInstance();
            }
            filters.add(filter);
        }
        //百分比列宽
        double widthPercent = bindInfo.widthPercent();
        if (widthPercent > 0.0 && widthPercent <= 1.0) {
            column.prefWidthProperty().bind(tableView.widthProperty().multiply(widthPercent));
        }

        tableView.getTableColumns().add(column);
    }

    /**
     * 后处理
     */
    @Override
    protected void postProcessing(List<TableColumnBindEntry> entries) throws Exception {
        //过滤器
        if (!filters.isEmpty()) {
            tableView.getFilters().clear();
            tableView.getFilters().addAll(filters);
        }
        //数据源初始化
        if (this.dataSource == null) {
            this.dataSource = FXCollections.observableArrayList();
        }
        //先添加空数据（否则ui会无法更新数据源的变化）
        if (initEmptyData && dataSource.isEmpty()) {
            T emptyData = dataType.getDeclaredConstructor().newInstance();
            this.dataSource.add(emptyData);
        }
        //再绑定数据源
        tableView.setItems(dataSource);
        //选择模式
        if (selectionMode != null) {
            tableView.getSelectionModel().setAllowsMultipleSelection(selectionMode == SelectionMode.MULTIPLE);
        }
        //最后清除此空数据
        if (initEmptyData) {
            dataSource.clear();
        }
    }

    //===============================================================================================================================================

    public MFXTableBuilder<T> dataType(Class<T> dataType) {
        super.dataType = dataType;
        return this;
    }

    public MFXTableBuilder<T> initDataSource(boolean initEmptyData) {
        super.setDataSource(initEmptyData);
        return this;
    }

    public MFXTableBuilder<T> initDataSource(Collection<T> initData) {
        super.setDataSource(initData);
        return this;
    }

    public MFXTableBuilder<T> readSuperClassFields(boolean readSuperClassFields) {
        super.readSuperClassFields = readSuperClassFields;
        return this;
    }

    public MFXTableBuilder<T> excludeFields(Collection<String> excludeFields) {
        super.setExcludeFields(excludeFields);
        return this;
    }

    public MFXTableBuilder<T> excludeFields(String... excludeFields) {
        super.setExcludeFields(excludeFields);
        return this;
    }

    public MFXTableBuilder<T> selectionMode(SelectionMode selectionMode) {
        super.selectionMode = selectionMode;
        return this;
    }

    public MFXTableBuilder<T> rowCellBuilder(MFXRowCellBuilder<T> rowCellBuilder) {
        this.rowCellBuilder = rowCellBuilder;
        return this;
    }

    public MFXTableBuilder<T> columnBuildListener(MFXTableColumnBuildListener<T> columnBuildListener) {
        this.columnBuildListener = columnBuildListener;
        return this;
    }

    /**
     * 手动添加过滤器
     */
    @SafeVarargs
    public final MFXTableBuilder<T> addFilters(AbstractFilter<T, ?>... f) {
        if (f == null) {
            throw new IllegalArgumentException("filter can no be null");
        }
        filters.addAll(f);
        return this;
    }

    /**
     * 清除所有过滤器
     */
    public void clearFilters() {
        filters.clear();
    }

    public MFXTableBuilder<T> autosizeColumnsOnInitialization() {
        innerBuilder.autosizeColumnsOnInitialization();
        return this;
    }

    public MFXTableBuilder<T> scrollBy(double pixels) {
        innerBuilder.scrollBy(pixels);
        return this;
    }

    public MFXTableBuilder<T> scrollTo(int index) {
        innerBuilder.scrollTo(index);
        return this;
    }

    public MFXTableBuilder<T> scrollToFirst() {
        innerBuilder.scrollToFirst();
        return this;
    }

    public MFXTableBuilder<T> scrollToLast() {
        innerBuilder.scrollToLast();
        return this;
    }

    public MFXTableBuilder<T> scrollToPixel(double pixel) {
        innerBuilder.scrollToPixel(pixel);
        return this;
    }

    public MFXTableBuilder<T> horizontalScrollbarSpeed(double unit, double block) {
        innerBuilder.setHSpeed(unit, block);
        return this;
    }

    public MFXTableBuilder<T> verticalScrollbarSpeed(double unit, double block) {
        innerBuilder.setVSpeed(unit, block);
        return this;
    }

    public MFXTableBuilder<T> enableSmoothScrolling(double speed) {
        innerBuilder.enableSmoothScrolling(speed);
        return this;
    }

    public MFXTableBuilder<T> enableSmoothScrolling(double speed, double trackPadAdjustment) {
        innerBuilder.enableSmoothScrolling(speed, trackPadAdjustment);
        return this;
    }

    public MFXTableBuilder<T> enableSmoothScrolling(double speed, double trackPadAdjustment, double scrollThreshold) {
        innerBuilder.enableSmoothScrolling(speed, trackPadAdjustment, scrollThreshold);
        return this;
    }

    public MFXTableBuilder<T> enableBounceEffect() {
        innerBuilder.enableBounceEffect();
        return this;
    }

    public MFXTableBuilder<T> enableBounceEffect(double strength, double maxOverscroll) {
        innerBuilder.enableBounceEffect(strength, maxOverscroll);
        return this;
    }

    public MFXTableBuilder<T> footerVisible(boolean footerVisible) {
        innerBuilder.setFooterVisible(footerVisible);
        return this;
    }

    /**
     * 初始化时对数据的排序
     */
    public MFXTableBuilder<T> initDataSort(Comparator<T> comparator) {
        innerBuilder.setComparator(comparator);
        return this;
    }

    /**
     * 初始化时对数据的排序
     *
     * @param isReverse 是否反转
     */
    public MFXTableBuilder<T> initDataSort(Comparator<T> comparator, boolean isReverse) {
        innerBuilder.setComparator(comparator, isReverse);
        return this;
    }

    /**
     * 启用{@link MFXPaginatedTableView}的前提下，设定每页数量
     */
    public MFXTableBuilder<T> rowsPerPage(int rowsPerPage) {
        if (rowsPerPage <= 0) {
            throw new IllegalArgumentException("rowsPerPage must be positive");
        }
        if (tableView instanceof MFXPaginatedTableView<T> paginatedTableView) {
            paginatedTableView.setRowsPerPage(rowsPerPage);
        } else {
            throw new UnsupportedOperationException("tableView is not instance of MFXPaginatedTableView");
        }
        return this;
    }


}
