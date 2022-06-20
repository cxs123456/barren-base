package org.barren.core.tool.http;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 响应信息主体
 *
 * @author ruoyi
 */
@Data
@ApiModel(description = "返回信息")
@Schema(description = "返回信息")
public class R<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 成功
     */
    public static final int SUCCESS = 0;

    /**
     * 失败
     */
    public static final int FAIL = 500;

    @ApiModelProperty(value = "code值")
    private int code;

    @ApiModelProperty(value = "消息")
    private String msg;

    @ApiModelProperty("返回对象")
    private T data;

    public static <T> R<T> ok() {
        return restResult(null, SUCCESS, "操作成功");
    }

    public static <T> R<T> ok(T data) {
        return restResult(data, SUCCESS, "操作成功");
    }

    public static <T> R<T> ok(T data, String msg) {
        return restResult(data, SUCCESS, msg);
    }

    public static <T> R<T> fail() {
        return restResult(null, FAIL, "操作失败");
    }

    public static <T> R<T> fail(String msg) {
        return restResult(null, FAIL, msg);
    }

    public static <T> R<T> fail(T data) {
        return restResult(data, FAIL, "操作失败");
    }

    public static <T> R<T> fail(T data, String msg) {
        return restResult(data, FAIL, msg);
    }

    public static <T> R<T> fail(int code, String msg) {
        return restResult(null, code, msg);
    }

    private static <T> R<T> restResult(T data, int code, String msg) {
        R<T> apiResult = new R<>();
        apiResult.setCode(code);
        apiResult.setData(data);
        apiResult.setMsg(msg);
        return apiResult;
    }
}
