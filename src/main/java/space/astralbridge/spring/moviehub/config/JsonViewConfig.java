package space.astralbridge.spring.moviehub.config;

/**
 * 定义JSON视图配置，用于控制实体类字段在不同场景下的可见性
 */
public class JsonViewConfig {
    /**
     * 基本视图，包含大多数字段
     */
    public static interface BaseView {
    }

    /**
     * 访客视图，不包含敏感数据
     */
    public static interface GuestView extends BaseView {
    }

    /**
     * 管理员视图，包含所有字段
     */
    public static interface AdminView extends BaseView {
    }
}
