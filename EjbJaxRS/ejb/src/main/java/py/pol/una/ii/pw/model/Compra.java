/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package py.pol.una.ii.pw.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;





@Entity
@XmlRootElement
@Table(name = "Compra")
public class Compra implements Serializable {
    /** Default value included to remove warning. Remove or modify at will. **/
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;

    @Basic
    @NotNull
    @Column(name = "fecha", insertable = true, updatable = true)
    private String fecha;
    
    @Basic
    @Column(name = "monto", nullable = true, insertable = true, updatable = true)
    private String monto;
    
    @ManyToOne(optional = false)
    @JoinColumn(name = "proveedor_id")
    private Provider proveedor;
    
    @OneToMany(mappedBy = "compra", cascade = CascadeType.ALL)
    private List<CompraDetalle> detalles = new ArrayList<>();
    
    public Compra() {
    }

    public Compra(Provider proveedor, String fecha, String monto) {
        this.fecha = fecha;
        this.proveedor = proveedor;
        this.monto = monto;
    }

    public Compra(Long id, Provider proveedor, String fecha, String monto) {
        this.id = id;
        this.fecha = fecha;
        this.proveedor = proveedor;
        this.monto = monto;
    }

    public Compra(String fecha, Provider proveedor, String monto, List<CompraDetalle> detalles) {
        this.fecha = fecha;
        this.proveedor = proveedor;
        this.monto = monto;
        this.detalles = detalles;
    }
	
    public void setId(Long id){
    	this.id = id;
    }
    
    public Long getId(){
    	return this.id;
    }
    
    public void setFecha(String fecha){
    	this.fecha = fecha;
    }
    
    public String getFecha(){
    	return this.fecha;
    }
    
    public void setProveedor(Provider proveedor){
    	this.proveedor = proveedor;
    }
    
    public Provider getProveedor(){
    	return this.proveedor;
    }
    
    public void setMonto(String monto){
    	this.monto = monto;
    }
    
    public String getMonto(){
    	return this.monto;
    }
    
    public void setDetalleCompra(CompraDetalle detalles){
    	this.detalles.add(detalles);
    }
    
    public List<CompraDetalle> getDetalleCompra(){
    	return this.detalles;
    }
    
}
