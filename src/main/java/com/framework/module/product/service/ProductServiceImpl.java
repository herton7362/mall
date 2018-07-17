package com.framework.module.product.service;

import com.framework.module.product.domain.*;
import com.framework.module.product.web.vo.VoHomePage;
import com.framework.module.product.web.vo.VoProduct;
import com.kratos.common.AbstractCrudService;
import com.kratos.common.PageRepository;
import com.kratos.common.PageResult;
import com.kratos.common.utils.IteratorUtils;
import com.kratos.common.utils.SpringUtils;
import com.kratos.entity.BaseEntity;
import com.kratos.exceptions.BusinessException;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.*;
import java.util.*;
import java.util.stream.Collectors;

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
        params.put("product.id", new String[]{product.getId()});
        List<ProductProductStandard> oldProductProductStandards = productProductStandardService.findAll(params);
        List<ProductProductStandard> productProductStandards = product.getProductProductStandards();
        List<Sku> oldSkus = skuService.findAll(params);
        if (productProductStandards != null) {
            if (StringUtils.isNotBlank(product.getId())) {
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
            if (StringUtils.isNotBlank(sku.getId())) {
                skuForCompare.add(sku);
            }
        });
        if (StringUtils.isNotBlank(product.getId()) && !BaseEntity.compare(skuForCompare, oldSkus)) {
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
    public Page<Product> getStock(PageRequest pageRequest, String maxStockCount) {
        return productRepository.findAll((Root<Product> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) -> {
            List<Predicate> predicate = new ArrayList<>();
            Long count = 100L;
            if (StringUtils.isNotBlank(maxStockCount)) {
                count = Long.parseLong(maxStockCount);
            }
            predicate.add(criteriaBuilder.lessThanOrEqualTo(root.get("stockCount"), count));
            EntityManager entityManager = SpringUtils.getBean(EntityManager.class);
            Query query = entityManager.createQuery("select s from Sku s where s.stockCount<=:maxStockCount");
            query.setParameter("maxStockCount", count);
            List list = query.getResultList();
            criteriaQuery.distinct(true);
            if (!list.isEmpty()) {
                predicate.add(criteriaBuilder.in(root.join("skus", JoinType.LEFT)).value(list));
            }
            return criteriaBuilder.or(predicate.toArray(new Predicate[]{}));
        }, pageRequest);
    }

    @Override
    public Sku getSkuByProductStandardItemIds(String productId, String[] idArr) throws Exception {
        if (idArr == null || StringUtils.isBlank(idArr[0])) {
            throw new BusinessException("规格条目不能为空");
        }
        Product product = findOne(productId);
        List<String> idList = Arrays.asList(idArr);

        List<ProductProductStandard> productProductStandards = product.getProductProductStandards();
        if (productProductStandards.size() != idArr.length) {
            List<ProductStandard> unChecked = new ArrayList<>();
            for (ProductProductStandard productProductStandard : productProductStandards) {
                Boolean matched = false;
                for (ProductStandardItem productStandardItem : productProductStandard.getProductStandardItems()) {
                    if (idList.contains(productStandardItem.getId())) {
                        matched = true;
                    }
                }
                if (!matched) {
                    unChecked.add(productProductStandard.getProductStandard());
                }
            }

            throw new BusinessException("请选择" + String.join(",", unChecked.stream()
                    .map(ProductStandard::getName)
                    .collect(Collectors.toList())));
        }

        Optional<Sku> optionalSku = product.getSkus().stream().filter(sku ->
                sku.getProductStandardItems()
                        .stream()
                        .allMatch(productStandardItem -> idList.contains(productStandardItem.getId()))
        ).findFirst();


        if (!optionalSku.isPresent())
            throw new BusinessException("未找到商品sku");
        else
            return optionalSku.get();
    }

    @Override
    public VoHomePage homePage() throws Exception {
        VoHomePage voHomePage = new VoHomePage();
        Map<String, String[]> param = new HashMap<>();
        String[] newest = {"true"};
        String[] recommendArray = {"true"};
        List<Sort.Order> orders = new ArrayList<>();
        orders.add(new Sort.Order(Sort.Direction.ASC, "sortNumber"));
        orders.add(new Sort.Order(Sort.Direction.DESC, "updatedDate"));
        PageRequest pageRequest = new PageRequest(0, 10, new Sort(orders));
        PageResult<Product> allProductList = findAll(pageRequest, param);
        setResultParam(voHomePage, allProductList, 1);

        pageRequest = new PageRequest(0, 3, new Sort(orders));
        param.put("newest", newest);
        PageResult<Product> newestProductList = findAll(pageRequest, param);
        setResultParam(voHomePage, newestProductList, 2);

        param.remove("newest");
        param.put("recommend", recommendArray);
        PageResult<Product> recommendProductList = findAll(pageRequest, param);
        setResultParam(voHomePage, recommendProductList, 3);

        return voHomePage;
    }

    @Override
    public List<VoProduct> getProductsByCategoryId(Integer page, String categoryId) throws Exception {
        List<VoProduct> resultArray = new ArrayList<>();
        Map<String, String[]> param = new HashMap<>();
        String[] categoryIdArray = {categoryId};
        param.put("productCategory.id", categoryIdArray);
        List<Sort.Order> orders = new ArrayList<>();
        orders.add(new Sort.Order(Sort.Direction.ASC, "sortNumber"));
        orders.add(new Sort.Order(Sort.Direction.DESC, "updatedDate"));
        PageRequest pageRequest = new PageRequest(page - 1, 30, new Sort(orders));
        PageResult<Product> productList = findAll(pageRequest, param);
        if (productList == null || productList.getContent() == null) {
            return null;
        }
        for (Product product : productList.getContent()) {
            resultArray.add(getVoProduct(product));
        }
        return resultArray;
    }

    @Override
    public Long count() {
        return productRepository.count(
                (Root<Product> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) -> {
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

    private void setResultParam(VoHomePage voHomePage, PageResult<Product> allProductList, int productType) {
        for (Product p : allProductList.getContent()) {
            VoProduct voProduct = getVoProduct(p);
            if (productType == 1) {
                voHomePage.getAllProduct().add(voProduct);
            }
            if (productType == 2) {
                voHomePage.getNewestProduct().add(voProduct);
            }
            if (productType == 3) {
                voHomePage.getRecommendProduct().add(voProduct);
            }
        }
    }

    private VoProduct getVoProduct(Product p) {
        VoProduct voProduct = new VoProduct();
        voProduct.setId(p.getId());
        voProduct.setName(p.getName());
        voProduct.setCoverImageUrl("/attachment/download/" + p.getCoverImage().getId() + "." + p.getCoverImage().getFormat());
        voProduct.setPrice(p.getDisplayPrice());
        return voProduct;
    }
}
