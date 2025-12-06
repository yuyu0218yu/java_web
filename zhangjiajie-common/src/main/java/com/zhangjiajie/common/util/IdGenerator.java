package com.zhangjiajie.common.util;

import java.net.InetAddress;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 分布式ID生成器
 * 基于雪花算法实现，支持分布式环境下的唯一ID生成
 *
 * 结构（64位）：
 * 1位符号位 + 41位时间戳 + 5位数据中心ID + 5位机器ID + 12位序列号
 *
 * @author yushuang
 * @since 2025-12-06
 */
public class IdGenerator {

    // ==================== 常量定义 ====================

    /**
     * 起始时间戳（2024-01-01 00:00:00）
     */
    private static final long START_TIMESTAMP = 1704067200000L;

    /**
     * 数据中心ID位数
     */
    private static final long DATA_CENTER_ID_BITS = 5L;

    /**
     * 机器ID位数
     */
    private static final long WORKER_ID_BITS = 5L;

    /**
     * 序列号位数
     */
    private static final long SEQUENCE_BITS = 12L;

    /**
     * 数据中心ID最大值（31）
     */
    private static final long MAX_DATA_CENTER_ID = ~(-1L << DATA_CENTER_ID_BITS);

    /**
     * 机器ID最大值（31）
     */
    private static final long MAX_WORKER_ID = ~(-1L << WORKER_ID_BITS);

    /**
     * 序列号最大值（4095）
     */
    private static final long MAX_SEQUENCE = ~(-1L << SEQUENCE_BITS);

    /**
     * 机器ID左移位数
     */
    private static final long WORKER_ID_SHIFT = SEQUENCE_BITS;

    /**
     * 数据中心ID左移位数
     */
    private static final long DATA_CENTER_ID_SHIFT = SEQUENCE_BITS + WORKER_ID_BITS;

    /**
     * 时间戳左移位数
     */
    private static final long TIMESTAMP_SHIFT = SEQUENCE_BITS + WORKER_ID_BITS + DATA_CENTER_ID_BITS;

    // ==================== 实例变量 ====================

    /**
     * 数据中心ID
     */
    private final long dataCenterId;

    /**
     * 机器ID
     */
    private final long workerId;

    /**
     * 序列号
     */
    private long sequence = 0L;

    /**
     * 上次生成ID的时间戳
     */
    private long lastTimestamp = -1L;

    // ==================== 单例 ====================

    private static volatile IdGenerator instance;

    /**
     * 获取单例实例
     */
    public static IdGenerator getInstance() {
        if (instance == null) {
            synchronized (IdGenerator.class) {
                if (instance == null) {
                    instance = new IdGenerator();
                }
            }
        }
        return instance;
    }

    /**
     * 私有构造函数（自动获取workerId）
     */
    private IdGenerator() {
        this(getDataCenterIdByIp(), getWorkerIdByIp());
    }

    /**
     * 构造函数
     *
     * @param dataCenterId 数据中心ID (0-31)
     * @param workerId     机器ID (0-31)
     */
    public IdGenerator(long dataCenterId, long workerId) {
        if (dataCenterId > MAX_DATA_CENTER_ID || dataCenterId < 0) {
            throw new IllegalArgumentException("数据中心ID必须在0-" + MAX_DATA_CENTER_ID + "之间");
        }
        if (workerId > MAX_WORKER_ID || workerId < 0) {
            throw new IllegalArgumentException("机器ID必须在0-" + MAX_WORKER_ID + "之间");
        }
        this.dataCenterId = dataCenterId;
        this.workerId = workerId;
    }

    // ==================== 核心方法 ====================

    /**
     * 生成下一个ID
     *
     * @return 分布式唯一ID
     */
    public synchronized long nextId() {
        long currentTimestamp = currentTimeMillis();

        // 时钟回拨检测
        if (currentTimestamp < lastTimestamp) {
            long offset = lastTimestamp - currentTimestamp;
            if (offset <= 5) {
                // 回拨时间较短，等待
                try {
                    Thread.sleep(offset << 1);
                    currentTimestamp = currentTimeMillis();
                    if (currentTimestamp < lastTimestamp) {
                        throw new RuntimeException("时钟回拨，无法生成ID");
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException("时钟回拨等待被中断", e);
                }
            } else {
                throw new RuntimeException("时钟回拨超过5ms，无法生成ID");
            }
        }

        // 同一毫秒内
        if (currentTimestamp == lastTimestamp) {
            sequence = (sequence + 1) & MAX_SEQUENCE;
            // 序列号溢出，等待下一毫秒
            if (sequence == 0) {
                currentTimestamp = waitNextMillis(lastTimestamp);
            }
        } else {
            // 不同毫秒，序列号重置（随机起始值避免偶数ID过多）
            sequence = ThreadLocalRandom.current().nextLong(0, 3);
        }

        lastTimestamp = currentTimestamp;

        // 组装ID
        return ((currentTimestamp - START_TIMESTAMP) << TIMESTAMP_SHIFT)
                | (dataCenterId << DATA_CENTER_ID_SHIFT)
                | (workerId << WORKER_ID_SHIFT)
                | sequence;
    }

    /**
     * 生成下一个ID（字符串形式）
     *
     * @return ID字符串
     */
    public String nextIdStr() {
        return String.valueOf(nextId());
    }

    // ==================== 静态便捷方法 ====================

    /**
     * 生成ID（静态方法）
     */
    public static long generateId() {
        return getInstance().nextId();
    }

    /**
     * 生成ID字符串（静态方法）
     */
    public static String generateIdStr() {
        return getInstance().nextIdStr();
    }

    // ==================== 辅助方法 ====================

    /**
     * 等待直到下一毫秒
     */
    private long waitNextMillis(long lastTimestamp) {
        long timestamp = currentTimeMillis();
        while (timestamp <= lastTimestamp) {
            timestamp = currentTimeMillis();
        }
        return timestamp;
    }

    /**
     * 获取当前时间戳
     */
    private long currentTimeMillis() {
        return System.currentTimeMillis();
    }

    /**
     * 根据IP获取数据中心ID
     */
    private static long getDataCenterIdByIp() {
        try {
            byte[] ip = InetAddress.getLocalHost().getAddress();
            return (ip[ip.length - 2] & 0xFF) % (MAX_DATA_CENTER_ID + 1);
        } catch (Exception e) {
            return ThreadLocalRandom.current().nextLong(0, MAX_DATA_CENTER_ID + 1);
        }
    }

    /**
     * 根据IP获取机器ID
     */
    private static long getWorkerIdByIp() {
        try {
            byte[] ip = InetAddress.getLocalHost().getAddress();
            return (ip[ip.length - 1] & 0xFF) % (MAX_WORKER_ID + 1);
        } catch (Exception e) {
            return ThreadLocalRandom.current().nextLong(0, MAX_WORKER_ID + 1);
        }
    }

    // ==================== ID解析 ====================

    /**
     * 解析ID获取时间戳
     *
     * @param id 分布式ID
     * @return 时间戳
     */
    public static long parseTimestamp(long id) {
        return (id >> TIMESTAMP_SHIFT) + START_TIMESTAMP;
    }

    /**
     * 解析ID获取数据中心ID
     *
     * @param id 分布式ID
     * @return 数据中心ID
     */
    public static long parseDataCenterId(long id) {
        return (id >> DATA_CENTER_ID_SHIFT) & MAX_DATA_CENTER_ID;
    }

    /**
     * 解析ID获取机器ID
     *
     * @param id 分布式ID
     * @return 机器ID
     */
    public static long parseWorkerId(long id) {
        return (id >> WORKER_ID_SHIFT) & MAX_WORKER_ID;
    }

    /**
     * 解析ID获取序列号
     *
     * @param id 分布式ID
     * @return 序列号
     */
    public static long parseSequence(long id) {
        return id & MAX_SEQUENCE;
    }
}
