package com.next.dynamo.persistance;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

import org.hibernate.validator.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "data_plugin")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "plugin_type")
@Getter 
@Setter
public class DataPlugin extends BaseEntity{

	
    @Column(name = "disabled")
    private boolean disabled;
    @Column(name = "plugin_name")
    @NotBlank(message="{dataplugin.pluginname.empty.error}")
    private String pluginName;

    @Override
    public String toString() {
        return "DataPlugin [id=" + id + ", ver=" + ver + ", dateCreated=" + dateCreated + ", dateModified=" + dateModified + ", creatorId=" + creatorId + ", modifierId=" + modifierId + ", disabled="
                + disabled + ", pluginName=" + pluginName + "]";
    }
	


	
}
