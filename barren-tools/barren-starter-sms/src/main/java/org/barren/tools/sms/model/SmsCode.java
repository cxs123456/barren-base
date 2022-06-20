package org.barren.tools.sms.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
/**
 * @author cxs
 **/
@Builder
@Data
public class SmsCode implements Serializable {

    private static final long serialVersionUID = 1L;
    private boolean success;
    private String phone;
    private String id;

    @JsonIgnore
    private String value;
}
