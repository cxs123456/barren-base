
package org.barren.core.tool.api;

import java.io.Serializable;

/**
 * 业务代码接口
 *
 * @author cxs
 */
public interface IResultCode extends Serializable {

	/**
	 * 消息
	 *
	 * @return String
	 */
	String getMessage();

	/**
	 * 状态码
	 *
	 * @return int
	 */
	int getCode();

}
