package org.barren.tools.oss.model;

import io.minio.messages.Item;
import io.minio.messages.Owner;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZoneId;
import java.util.Date;

/**
 * @author cxs
 **/
@Data
@NoArgsConstructor
public class MinioItem {

    private String objectName;
    private Date lastModified;
    private String etag;
    private Long size;
    private String storageClass;
    private Owner owner;
    private boolean isDir;
    private String category;
    private Item item;

    public MinioItem(Item item) {
        this.objectName = item.objectName();
        this.lastModified = Date.from(item.lastModified().withZoneSameInstant(ZoneId.systemDefault()).toInstant());
        this.etag = item.etag();
        this.size = item.size();
        this.storageClass = item.storageClass();
        this.owner = item.owner();
        this.isDir = item.isDir();
        this.category = this.isDir ? "dir" : "file";
        this.item = item;
    }
}
