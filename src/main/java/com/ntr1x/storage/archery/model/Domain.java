package com.ntr1x.storage.archery.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlElement;

import org.eclipse.persistence.annotations.CascadeOnDelete;

import com.ntr1x.storage.core.model.Resource;

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
	name = "domains"
)
@PrimaryKeyJoinColumn(name = "ResourceId", referencedColumnName = "Id")
@CascadeOnDelete
public class Domain extends Resource {

	public enum Type {
		API,
		ARCHERY
	}
	
	@XmlElement
    @ManyToOne
    @JoinColumn(name = "PortalId", nullable = false, updatable = false)
    private Portal portal;
	
	@Column(name = "Type", nullable = false)
	private Type type;
	
	@Column(name = "Name", nullable = false, unique = true)
	private String name;
}
