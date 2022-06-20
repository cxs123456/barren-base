package org.barren.tools.oss.props;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * - 对象存储服务（Object Storage Service）
 * 对象存储是一种稳定、安全、高效、易用的云存储服务，具备标准Restful API接口，可存储任意数量和形式的非结构化数据。
 * - 存储空间（Bucket）
 * 存储空间是用于存储对象（Object）的容器，所有的对象都必须隶属于某个存储空间。存储空间具有各种配置属性，包括地域、访问权限、存储类型等。
 * 可以根据实际需求，创建不同类型的存储空间来存储不同的数据。
 * - 对象（Object）
 * 对象是OSS存储数据的基本单元，也被称为OSS的文件。对象由元信息（Object Meta）、用户数据（Data）和文件名（Key）组成。
 * 对象由存储空间内部唯一的Key来标识。对象元信息是一组键值对，表示了对象的一些属性，例如最后修改时间、大小等信息，同时您也可以在元信息中存储一些自定义的信息。
 * - 地域（Region）
 * 地域表示OSS的数据中心所在物理位置。可以根据费用、请求来源等选择合适的地域创建Bucket。
 * - 访问域名（Endpoint）
 * Endpoint表示OSS对外服务的访问域名。OSS以HTTP RESTful API的形式对外提供服务，当访问不同地域的时候，需要不同的域名。
 * 通过内网和外网访问同一个地域所需要的域名也是不同的。
 * - 访问密钥（AccessKey）
 * AccessKey简称AK，指的是访问身份验证中用到的AccessKey ID和AccessKey Secret。
 * OSS通过使用AccessKey ID和AccessKey Secret对称加密的方法来验证某个请求的发送者身份。
 * AccessKey ID用于标识用户；AccessKey Secret是用户用于加密签名字符串和OSS用来验证签名字符串的密钥，必须保密。关于获取AccessKey的方法
 *
 * @author cxs
 **/
@Data
@ConfigurationProperties(prefix = OssProperties.PREFIX)
public class OssProperties {
    public static final String PREFIX = "oss";

    /**
     * 是否开启oss配置
     */
    private Boolean enabled;
    /**
     * 具体开启的oss类型，minio为minio，阿里云oss为alioss，七牛oss为qiniu，腾讯oss为tencentcos
     */
    private String name;

    /**
     * oss对外开放的地址
     */
    private String endpoint;

    private String appId;
    private String region;

    /**
     * oss提供的accesskey
     */
    private String accessKey;
    /**
     * oss提供的secretkey
     */
    private String secretKey;

    /**
     * 存储桶名，有些oss服务需要手动先创建，有些可以自动创建
     */
    private String bucketName = "barren";
}
