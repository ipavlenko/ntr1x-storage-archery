package com.ntr1x.storage.archery.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlElement;

import org.eclipse.persistence.annotations.CascadeOnDelete;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.ntr1x.storage.core.model.Resource;
import com.ntr1x.storage.security.model.User;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
	name = "portals"
)
@PrimaryKeyJoinColumn(name = "ResourceId", referencedColumnName = "Id")
@CascadeOnDelete
public class Portal extends Resource {

	@Column(name = "Title", nullable = false)
	private String title;
	
	@Column(name = "Shared")
	private boolean shared;
	
	@Lob
	@ResourceExtra
	@Column(name = "Content", nullable = true)
	@ApiModelProperty(hidden = true)
	private String content;
	
	@XmlElement
	@JsonManagedReference
	@ManyToOne
	@JoinColumn(name = "UserId", nullable = false, updatable = false)
	private User user;
	
	@XmlElement
    @ManyToOne
	@JoinColumn(name = "ThumbnailId", nullable = true, updatable = true)
	private Resource thumbnail;
}
