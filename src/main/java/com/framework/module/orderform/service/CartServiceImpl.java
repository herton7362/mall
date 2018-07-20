package com.framework.module.orderform.service;

import com.framework.module.orderform.domain.Cart;
import com.framework.module.orderform.domain.CartItem;
import com.framework.module.orderform.domain.CartItemRepository;
import com.framework.module.orderform.domain.CartRepository;
import com.framework.module.orderform.dto.CartDTO;
import com.framework.module.orderform.dto.CartItemDTO;
import com.framework.module.orderform.web.param.EditCountParam;
import com.kratos.common.AbstractCrudService;
import com.kratos.common.PageRepository;
import com.kratos.dto.CascadePersistHelper;
import com.kratos.exceptions.BusinessException;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Transactional
public class CartServiceImpl extends AbstractCrudService<Cart> implements CartService {
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final CartDTO cartDTO;

    @Override
    protected PageRepository<Cart> getRepository() {
        return cartRepository;
    }

    @Override
    public void addProduct(final Cart cart) {
        if (StringUtils.isBlank(cart.getMemberId())) {
            throw new BusinessException("会员不能为空");
        }
        List<Cart> carts = cartRepository.findAll((Root<Cart> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) -> {
            List<Predicate> predicate = new ArrayList<>();
            predicate.add(criteriaBuilder.equal(root.get("memberId"), cart.getMemberId()));
            return criteriaBuilder.and(predicate.toArray(new Predicate[]{}));
        });
        if (carts == null || carts.isEmpty()) {
            cartRepository.save(cart);
            List<CartItem> cartItems = cart.getItems();
            CartItem cartItem = cartItems.get(0);
            cartItem.setCart(cart);
            cartItemRepository.save(cartItem);
        } else {
            Cart oldCart = carts.get(0);
            List<CartItem> oldCartItems = oldCart.getItems();
            List<CartItem> cartItems = cart.getItems();
            CartItem cartItem = cartItems.get(0);
            oldCartItems = oldCartItems
                    .stream()
                    .filter(item -> item.getProduct().getId().equals(cartItem.getProduct().getId()))
                    .collect(Collectors.toList());

            if (oldCartItems != null && !oldCartItems.isEmpty()) {
                CartItem oldCartItem = oldCartItems.get(0);
                oldCartItem.setCount(oldCartItem.getCount() + cartItem.getCount());
            } else {
                cartItem.setCart(oldCart);
                cartItemRepository.save(cartItem);
            }
        }
    }

    @Override
    public void deleteItem(String id) {
        cartItemRepository.delete(id);
    }

    @Override
    public void increaseItemCount(String id) {
        CartItem cartItem = cartItemRepository.findOne(id);
        cartItem.setCount(cartItem.getCount() + 1);
        cartItemRepository.save(cartItem);
    }

    @Override
    public void reduceItemCount(String id) {
        CartItem cartItem = cartItemRepository.findOne(id);
        if (cartItem.getCount() > 0) {
            cartItem.setCount(cartItem.getCount() - 1);
            cartItemRepository.save(cartItem);
        } else {
            cartItemRepository.delete(id);
        }

    }

    @Override
    public void addProduct(CartDTO cartDTO) throws Exception {
        List<CartItemDTO> cartItemDTOS = cartDTO.getItems();
        CartItem cartItem;
        for (CartItemDTO cartItemDTO : cartItemDTOS) {
            cartItem = cartItemDTO.convert();
            if (cartItem.getProduct() == null) {
                throw new BusinessException("您购买商品未找到，或已下架");
            }
            if (cartItem.getProduct().getSkus() != null
                    && !cartItem.getProduct().getSkus().isEmpty()
                    && cartItem.getSku() == null) {
                throw new BusinessException("您没有选中具体的商品规格");
            }
        }
        Cart cart = super.save(cartDTO.convert());
        cartDTO.setId(cart.getId());
        CascadePersistHelper.saveChildren(cartDTO);
    }

    @Override
    public void editCount(String id, EditCountParam param) {
        if (param.getCount() == null || param.getCount() <= 0) {
            throw new BusinessException("请设置数量");
        }
        CartItem cartItem = cartItemRepository.findOne(id);
        cartItem.setCount(param.getCount());
        cartItemRepository.save(cartItem);
    }

    @Override
    public void checkItem(String id) {
        if(StringUtils.isBlank(id)) {
            return;
        }
        CartItem cartItem = cartItemRepository.findOne(id);
        cartItem.setChecked(true);
        cartItemRepository.save(cartItem);
    }

    @Override
    public void unCheckItem(String id) {
        if(StringUtils.isBlank(id)) {
            return;
        }
        CartItem cartItem = cartItemRepository.findOne(id);
        cartItem.setChecked(false);
        cartItemRepository.save(cartItem);
    }

    @Override
    public CartDTO getCartList(String memberId) {
        List<Cart> carts = cartRepository.findAll((Root<Cart> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) -> {
            List<Predicate> predicate = new ArrayList<>();
            predicate.add(criteriaBuilder.equal(root.get("memberId"), memberId));
            return criteriaBuilder.and(predicate.toArray(new Predicate[]{}));
        });
        if (CollectionUtils.isEmpty(carts)) {
            Cart cart = new Cart();
            cart.setMemberId(memberId);
            cartRepository.save(cart);
            return cartDTO.convertFor(cart);
        }
        return cartDTO.convertFor(carts.get(0));
    }

    @Override
    public void delete(String id) {
        cartRepository.delete(id);
    }

    @Autowired
    public CartServiceImpl(
            CartRepository cartRepository,
            CartItemRepository cartItemRepository,
            CartDTO cartDTO) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.cartDTO = cartDTO;
    }
}
