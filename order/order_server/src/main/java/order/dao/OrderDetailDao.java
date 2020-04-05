package order.dao;

import order.entity.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderDetailDao extends JpaRepository<OrderDetail, String> {

    List<OrderDetail> findByOrderId(String orderId);

}
