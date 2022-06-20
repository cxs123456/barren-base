package org.barren.tools.sms.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author cxs
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SmsResponse implements Serializable {
    private static final long serialVersionUID = 1L;
    private boolean success;
    private Integer code;
    private String msg;
}
