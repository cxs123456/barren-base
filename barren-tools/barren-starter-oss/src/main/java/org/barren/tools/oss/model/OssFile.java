package org.barren.tools.oss.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author cxs
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OssFile {

    private String link;
    private String name;
    public String hash;
    private long length;
    private Date putTime;
    private String contentType;

    private String originalName;
    private String domain;
}
