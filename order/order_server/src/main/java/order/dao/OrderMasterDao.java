package order.dao;

import order.entity.OrderMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderMasterDao extends JpaRepository<OrderMaster, String> {
}
