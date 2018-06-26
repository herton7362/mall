package com.framework.module.product.service;

import com.framework.module.product.domain.*;
import com.kratos.common.AbstractCrudService;
import com.kratos.common.PageRepository;
import com.kratos.common.utils.IteratorUtils;
import com.kratos.common.utils.SpringUtils;
import com.kratos.entity.BaseEntity;
import com.mysql.fabric.xmlrpc.base.Param;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Transactional
public class ProductServiceImpl extends AbstractCrudService<Product> implements ProductService {
    private final ProductRepository productRepository;
    private final ProductProductStandardService productProductStandardService;
    private final SkuService skuService;
    @Override
    protected PageRepository<Product> getRepository() {
        return productRepository;
    }

    @Override
    public Product save(Product product) throws Exception {
        Product newProduct = productRepository.save(product);
        Map<String, String[]> params = new HashMap<>();
        params.put("product.id", new String[]{ product.getId() });
        List<ProductProductStandard> oldProductProductStandards = productProductStandardService.findAll(params);
        List<ProductProductStandard> productProductStandards = product.getProductProductStandards();
        List<Sku> oldSkus = skuService.findAll(params);
        if(productProductStandards != null) {
            if(StringUtils.isNotBlank(product.getId())) {
                for (ProductProductStandard productProductStandard : oldProductProductStandards) {
                    productProductStandardService.delete(productProductStandard.getId());
                }
            }
            IteratorUtils.forEach(productProductStandards, (index, productProductStandard) -> {
                productProductStandard.setSortNumber(index);
                productProductStandard.setId(null);
                productProductStandard.setProduct(newProduct);
                try {
                    productProductStandardService.save(productProductStandard);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

        }
        List<Sku> skus = product.getSkus();
        // compare skus with old to judge whither has bean modified or not.
        // it must be newed if sku id is null.so remove them from the candidates .
        List<Sku> skuForCompare = new ArrayList<>();
        IteratorUtils.forEach(skus, (index, sku) -> {
            sku.setSortNumber(index);
            sku.setProduct(newProduct);
            if(StringUtils.isNotBlank(sku.getId())) {
                skuForCompare.add(sku);
            }
        });
        if(StringUtils.isNotBlank(product.getId()) && !BaseEntity.compare(skuForCompare, oldSkus)) {
            for (Sku sku : oldSkus) {
                sku.setLogicallyDeleted(true);
                skuService.save(sku);
            }
            IteratorUtils.forEach(skus, (index, sku) -> sku.setId(null));
        }
        for (Sku sku : skus) {
            skuService.save(sku);
        }

        return product;
    }

    @Override
    public Page<Product> getStock(PageRequest pageRequest, String maxStockCount) throws Exception {
        return productRepository.findAll((Root<Product> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder)->{
            List<Predicate> predicate = new ArrayList<>();
            Long count = 100L;
            if(StringUtils.isNotBlank(maxStockCount)) {
                count = Long.parseLong(maxStockCount);
            }
            predicate.add(criteriaBuilder.lessThanOrEqualTo(root.get("stockCount"), count));
            EntityManager entityManager = SpringUtils.getBean(EntityManager.class);
            Query query = entityManager.createQuery("select s from Sku s where s.stockCount<=:maxStockCount");
            query.setParameter("maxStockCount", count);
            List list = query.getResultList();
            criteriaQuery.distinct(true);
            if(!list.isEmpty()) {
                predicate.add(criteriaBuilder.in(root.join("skus", JoinType.LEFT)).value(list));
            }
            return criteriaBuilder.or(predicate.toArray(new Predicate[]{}));
        }, pageRequest);
    }

    @Override
    public Long count() {
        return productRepository.count(
                (Root<Product> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder)-> {
                    List<Predicate> predicate = new ArrayList<>();
                    predicate.add(criteriaBuilder.equal(root.get("logicallyDeleted"), false));
                    return criteriaBuilder.and(predicate.toArray(new Predicate[]{}));
                }
        );
    }

    @Autowired
    public ProductServiceImpl(
            ProductRepository productRepository,
            ProductProductStandardService productProductStandardService,
            SkuService skuService
    ) {
        this.productRepository = productRepository;
        this.productProductStandardService = productProductStandardService;
        this.skuService = skuService;
    }
}
