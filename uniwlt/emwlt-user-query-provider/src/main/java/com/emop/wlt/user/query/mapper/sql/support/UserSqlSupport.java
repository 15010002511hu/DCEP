package com.emop.wlt.user.query.mapper.sql.support;

import com.emop.data.security.encrypt.Sensitive;
import com.emop.data.security.encrypt.enums.SensitiveFieldTypeEnum;
import org.mybatis.dynamic.sql.SqlColumn;
import org.mybatis.dynamic.sql.SqlTable;

import java.time.LocalDateTime;

public final class UserSqlSupport {

    public static final UserSqlTable user = new UserSqlTable();

    public static final SqlColumn<String> userId = user.userId;
    public static final SqlColumn<String> pwd = user.pwd;
    public static final SqlColumn<String> pwdSalt = user.pwdSalt;
    public static final SqlColumn<String> phone = user.phone;
    public static final SqlColumn<String> countryRegionCode = user.countryRegionCode;
    public static final SqlColumn<String> deviceId = user.deviceId;
    public static final SqlColumn<String> deviceType = user.deviceType;
    public static final SqlColumn<String> status = user.status;
    public static final SqlColumn<Short> pwdRetryTimes = user.pwdRetryTimes;
    public static final SqlColumn<LocalDateTime> lockDate = user.lockDate;
    public static final SqlColumn<String> loginStatus = user.loginStatus;
    public static final SqlColumn<String> lastLoginTime = user.lastLoginTime;
    public static final SqlColumn<String> pushFlag = user.pushFlag;
    public static final SqlColumn<String> appInnerVersion = user.appInnerVersion;
    public static final SqlColumn<LocalDateTime> createDatetime = user.createDatetime;
    public static final SqlColumn<LocalDateTime> updateDatetime = user.updateDatetime;

    private static final class UserSqlTable extends SqlTable {

        private final SqlColumn<String> userId = column("user_id");

        private final SqlColumn<String> pwd = column("pwd");

        private final SqlColumn<String> pwdSalt = column("pwd_salt");

        @Sensitive(fieldType = SensitiveFieldTypeEnum.PLAIN, srcField = "phoneCipher")
        private final SqlColumn<String> phone = column("phone");

        @Sensitive(fieldType = SensitiveFieldTypeEnum.CIPHER, srcField = "phone")
        private final SqlColumn<String> phoneCipher = column("phone_cipher");

        private final SqlColumn<String> countryRegionCode = column("country_region_code");

        private final SqlColumn<String> deviceId = column("device_id");

        private final SqlColumn<String> deviceType = column("device_type");

        private final SqlColumn<String> status = column("status");

        private final SqlColumn<Short> pwdRetryTimes = column("pwd_retry_times");

        private final SqlColumn<LocalDateTime> lockDate = column("lock_date");

        private final SqlColumn<String> loginStatus = column("login_status");

        private final SqlColumn<String> lastLoginTime = column("last_login_time");

        private final SqlColumn<String> pushFlag = column("push_flag");

        private final SqlColumn<String> appInnerVersion = column("app_inner_version");

        private final SqlColumn<LocalDateTime> createDatetime = column("create_datetime");

        private final SqlColumn<LocalDateTime> updateDatetime = column("update_datetime");

        public UserSqlTable() {
            super("wltsvc_user");
        }
    }

}
