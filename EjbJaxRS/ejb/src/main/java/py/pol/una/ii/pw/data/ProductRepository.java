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
package py.pol.una.ii.pw.data;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;


import java.util.List;

import py.pol.una.ii.pw.model.Product;
import py.pol.una.ii.pw.model.Provider;

@ApplicationScoped
public class ProductRepository {

    @Inject
    private EntityManager em;

    public Product findById(Long id) {
        return em.find(Product.class, id);
    }

    public List<Product> findAllOrderedByName() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Product> criteria = cb.createQuery(Product.class);
        Root<Product> member = criteria.from(Product.class);
        // Swap criteria statements if you would like to try out type-safe criteria queries, a new
        // feature in JPA 2.0
        // criteria.select(member).orderBy(cb.asc(member.get(Member_.name)));
        criteria.select(member).orderBy(cb.asc(member.get("name")));
        return em.createQuery(criteria).getResultList();
    }
    
    public List<Product> findAllbyProvider(Long idprov) {
        /*CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Product> criteria = cb.createQuery(Product.class);
        Root<Product> product = criteria.from(Product.class);
        // Swap criteria statements if you would like to try out type-safe criteria queries, a new
        // feature in JPA 2.0
        // criteria.select(member).orderBy(cb.asc(member.get(Member_.name)));
        criteria.select(product).orderBy(cb.asc(product.get("name")));*/

       /* Provider proveedor = em.find(Provider.class, (long) idprov);

        Query query = em.createQuery("SELECT p FROM Product  p WHERE p.proveedor = :proveedor");
        query.setParameter("proveedor", proveedor);

        List<Product> productos = query.getResultList();
        return productos;*/
        return em.createQuery("SELECT p FROM Product p, Provider d WHERE d.id = :idprov and p.provider = d ")
	    		.setParameter("idprov", idprov).getResultList();
    }
}
