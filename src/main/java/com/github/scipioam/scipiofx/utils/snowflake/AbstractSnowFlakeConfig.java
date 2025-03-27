package com.github.scipioam.scipiofx.utils.snowflake;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Alan Scipio
 * created on 2024/4/10
 */
public abstract class AbstractSnowFlakeConfig {

    protected final Logger log = LoggerFactory.getLogger(AbstractSnowFlakeConfig.class);

    /**
     * 定义工作机器ID和数据中心ID
     */
    @PostConstruct
    public void defineWorkerAndDataCenterId() {
        Long workerId = getWorkerId();
        Long dataCenterId = getDataCenterId();
        if (workerId != null && workerId > -1L && dataCenterId != null && dataCenterId > -1L) {
            SnowFlakeIdGenerator.setWorkerId(workerId);
            SnowFlakeIdGenerator.setDataCenterId(dataCenterId);
            log.info("SnowFlakeIdGenerator set workerId: {} and dataCenterId: {}", workerId, dataCenterId);
        } else {
            log.warn("SnowFlakeIdGenerator not set workerId and dataCenterId, use default setting");
        }

        SequenceBuilder sequenceBuilder = getSequenceBuilder();
        if (sequenceBuilder != null) {
            SnowFlakeIdGenerator.setSequenceBuilder(sequenceBuilder);
        }
    }

    public abstract Long getWorkerId();

    public abstract Long getDataCenterId();

    public SequenceBuilder getSequenceBuilder() {
        return null;
    }

}
