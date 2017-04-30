package com.ntr1x.storage.archery.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.xml.bind.annotation.XmlElement;

import org.eclipse.persistence.annotations.CascadeOnDelete;

import com.fasterxml.jackson.annotation.JsonManagedReference;
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
    name = "stores",
    uniqueConstraints = @UniqueConstraint(columnNames = { "PortalId", "Name" })
)
@PrimaryKeyJoinColumn(name = "ResourceId", referencedColumnName = "Id")
@CascadeOnDelete
public class Store extends Resource {
    
    public enum ParamType {
        PUBLIC,
        PRIVATE,
    }
    
    @XmlElement
    @JsonManagedReference
    @ManyToOne
    @JoinColumn(name = "PortalId", nullable = false, updatable = false)
    private Portal portal;
    
    @Column(name = "Name", nullable = false)
    private String name;
    
    @Column(name = "Title", nullable = true)
    private String title;
}
