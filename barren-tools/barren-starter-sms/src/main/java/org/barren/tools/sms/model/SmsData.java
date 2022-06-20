package org.barren.tools.sms.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Map;

/**
 * @author cxs
 **/
@Data
public class SmsData implements Serializable {

    private static final long serialVersionUID = 1L;
    private String key;
    private Map<String, String> params;
}
