1. log4j2由dips父工程统一配置，各个模块需要在部署环境config目录（存放环境application.yml的目录）下增加log.properties文件，配置logOutPath属性
logOutPath=/applog/模块日志目录

2. 本地调试，需在jvm参数增加-DlogOutPath=模块本地日志目录