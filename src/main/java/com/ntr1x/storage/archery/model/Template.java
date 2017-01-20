package com.ntr1x.storage.archery.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

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
@Table(name = "templates")
@PrimaryKeyJoinColumn(name = "ResourceId", referencedColumnName = "Id")
@CascadeOnDelete
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Template extends Resource {

    @XmlElement
    @ManyToOne
    @JoinColumn(name = "PortalId", nullable = false, updatable = false)
    private Portal portal;
    
    @Column(name = "Name", nullable = false, length = 511)
    private String name;
    
    @Column(name = "Subject", nullable = false)
    private String subject;
    
    @Column(name = "Sender", nullable = false)
    private String sender;
    
    @Lob
    @Column(name = "Content")
    private String content;
}