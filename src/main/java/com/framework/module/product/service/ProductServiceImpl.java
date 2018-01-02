package com.framework.module.product.service;

import com.framework.module.product.domain.*;
import com.kratos.common.AbstractCrudService;
import com.kratos.common.PageRepository;
import com.kratos.common.utils.IteratorUtils;
import com.kratos.common.utils.SpringUtils;
import com.kratos.entity.BaseEntity;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.method.P;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

@Component
@Transactional
public class ProductServiceImpl extends AbstractCrudService<Product> implements ProductService {
    private final ProductRepository productRepository;
    private final ProductProductStandardRepository productProductStandardRepository;
    private final SkuRepository skuRepository;
    @Override
    protected PageRepository<Product> getRepository() {
        return productRepository;
    }

    @Override
    public Product save(Product product) throws Exception {
        List<ProductProductStandard> productProductStandards = product.getProductProductStandards();
        Product old = productRepository.findOne(product.getId());
        if(productProductStandards != null) {
            if(StringUtils.isNotBlank(product.getId())) {
                productProductStandardRepository.delete(old.getProductProductStandards());
            }
            IteratorUtils.forEach(productProductStandards, (index, productProductStandard) -> {
                productProductStandard.setSortNumber(index);
                productProductStandard.setId(null);
                productProductStandard.setProduct(product);
            });
            productProductStandardRepository.save(productProductStandards);
        }
        List<Sku> skus = product.getSkus();
        // compare skus with old to judge whither has bean modified or not.
        // it must be newed if sku id is null.so remove them from the candidates .
        List<Sku> skuForCompare = new ArrayList<>();
        IteratorUtils.forEach(skus, (index, sku) -> {
            sku.setSortNumber(index);
            sku.setProduct(product);
            if(StringUtils.isNotBlank(sku.getId())) {
                skuForCompare.add(sku);
            }
        });
        if(StringUtils.isNotBlank(product.getId()) && !BaseEntity.compare(skuForCompare, old.getSkus())) {
            old.getSkus().forEach(sku -> sku.setLogicallyDeleted(true));
            skuRepository.save(old.getSkus());
            IteratorUtils.forEach(skus, (index, sku) -> sku.setId(null));
        }
        skuRepository.save(skus);
        return super.save(product);
    }

    @Override
    public Long count() throws Exception {
        return productRepository.count(
                (Root<Product> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder)-> {
                    List<Predicate> predicate = new ArrayList<>();
                    predicate.add(criteriaBuilder.equal(root.get("logicallyDeleted"), false));
                    return criteriaBuilder.and(predicate.toArray(new Predicate[]{}));
                }
        );
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
            predicate.add(criteriaBuilder.in(root.join("skus", JoinType.LEFT)).value(list));
            return criteriaBuilder.or(predicate.toArray(new Predicate[]{}));
        }, pageRequest);
    }

    @Autowired
    public ProductServiceImpl(
            ProductRepository productRepository,
            ProductProductStandardRepository productProductStandardRepository,
            SkuRepository skuRepository
    ) {
        this.productRepository = productRepository;
        this.productProductStandardRepository = productProductStandardRepository;
        this.skuRepository = skuRepository;
    }
}
