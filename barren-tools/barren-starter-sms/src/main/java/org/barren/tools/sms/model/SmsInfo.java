package org.barren.tools.sms.model;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author cxs
 **/
@Data
public class SmsInfo implements Serializable {
    private static final long serialVersionUID = 1L;
    private SmsData smsData;
    private List<String> phones;
}
