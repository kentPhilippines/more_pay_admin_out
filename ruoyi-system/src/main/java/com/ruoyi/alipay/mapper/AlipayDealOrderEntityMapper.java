package com.ruoyi.alipay.mapper;

import com.ruoyi.alipay.domain.AlipayDealOrderEntity;
import com.ruoyi.common.core.domain.StatisticsEntity;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 交易订单Mapper接口
 *
 * @author kiwi
 * @date 2020-03-17
 */
public interface AlipayDealOrderEntityMapper {
    /**
     * 查询交易订单
     *
     * @param id 交易订单ID
     * @return 交易订单
     */
    AlipayDealOrderEntity selectAlipayDealOrderEntityById(Long id);

    /**
     * 查询交易订单列表
     *
     * @param alipayDealOrderEntity 交易订单
     * @return 交易订单集合
     */
    List<AlipayDealOrderEntity> selectAlipayDealOrderEntityList(AlipayDealOrderEntity alipayDealOrderEntity);

    List<AlipayDealOrderEntity> selectAlipayOrderList(AlipayDealOrderEntity alipayDealOrderEntity);

    /**
     * 修改交易订单
     *
     * @param alipayDealOrderEntity 交易订单
     * @return 结果
     */
    int updateAlipayDealOrderEntity(AlipayDealOrderEntity alipayDealOrderEntity);

    @Select("<script>" +
            "select '所有' userId, 'USDT' productName, " +
            "coalesce(sum(dealAmount),0) totalAmount," +
            "coalesce(sum(case orderStatus when 2 then dealAmount else 0 end),0) successAmount," +
            "coalesce(sum(case orderStatus when 2 then retain3 else 0 end),0) profit," +
            "count(*) totalCount," +
            "count(case orderStatus when 2 then id else null end) successCount " +
            "from alipay_deal_order where createTime between #{statisticsEntity.params.dayStart}" +
            " and #{statisticsEntity.params.dayEnd} and orderType = 1 " +
            "and currency = 'USDT' " +
            " union all " +
            "select '所有' userId, 'CNY' productName, " +
            "coalesce(sum(dealAmount),0) totalAmount," +
            "coalesce(sum(case orderStatus when 2 then dealAmount else 0 end),0) successAmount," +
            "coalesce(sum(case orderStatus when 2 then retain3 else 0 end),0) profit," +
            "count(*) totalCount," +
            "count(case orderStatus when 2 then id else null end) successCount " +
            "from alipay_deal_order where createTime between #{statisticsEntity.params.dayStart}" +
            " and #{statisticsEntity.params.dayEnd} and orderType = 1 " +
            "and currency = 'CNY' " +
            " union all " +
            "select o.orderQrUser userId, p.productName ," +
            "coalesce(sum(dealAmount),0.00) totalAmount," +
            "coalesce(sum(case orderStatus when 2 then dealAmount else 0 end),0) successAmount," +
            "coalesce(sum(case orderStatus when 2 then o.retain3 else 0 end),0) profit," +
            "count(*) totalCount," +
            "count(case orderStatus when 2 then o.id else null end) successCount " +
            "from alipay_deal_order o left join alipay_product p on o.retain1 = p.productId " +
            "where o.createTime between #{statisticsEntity.params.dayStart} and #{statisticsEntity.params.dayEnd} and orderType = 1 " +
            "<if test = \"statisticsEntity.userId != null and statisticsEntity.userId != ''\">" +
            "and o.orderQrUser = #{statisticsEntity.userId} " +
            "</if>" +
            "<if test = \"statisticsEntity.currency != null and statisticsEntity.currency != ''\">" +
            "and o.currency = #{statisticsEntity.currency} " +
            "</if>" +
            "group by o.orderQrUser, o.retain1 " +
            "</script>")
    List<StatisticsEntity> selectStatDateByDay(@Param("statisticsEntity") StatisticsEntity statisticsEntity, @Param("dayStart") String dayStart, @Param("dayEnd") String dayEnd);


    @Select("<script>" +
            "select o.orderQrUser userId, p.productName ," +
            "coalesce(sum(dealAmount),0.00) totalAmount," +
            "coalesce(sum(case orderStatus when 2 then dealAmount else 0 end),0) successAmount," +
            "coalesce(sum(case orderStatus when 2 then o.retain3 else 0 end),0) profit," +
            "count(*) totalCount," +
            "count(case orderStatus when 2 then o.id else null end) successCount ," +
            "DATE_FORMAT(o.createTime,'%Y%m%d%H') time " +
            "from alipay_deal_order o left join alipay_product p on o.retain1 = p.productId " +
            "where o.createTime between #{statisticsEntity.params.dayStart} and #{statisticsEntity.params.dayEnd} and orderType = 1 " +
            "<if test = \"statisticsEntity.userId != null and statisticsEntity.userId != ''\">" +
            "and o.orderQrUser = #{statisticsEntity.userId} " +
            "</if>" +
            "<if test = \"statisticsEntity.currency != null and statisticsEntity.currency != ''\">" +
            "and o.currency = #{statisticsEntity.currency} " +
            "</if>" +
            "group by o.orderQrUser, o.retain1 , time" +
            "</script>")
    List<StatisticsEntity> selectStatDateByHours(@Param("statisticsEntity") StatisticsEntity statisticsEntity, @Param("dayStart") String dayStart, @Param("dayEnd") String dayEnd);


    /**
     * 分页查询条件
     * @param starTime
     * @param endTime
     * @param page
     * @param size
     * @return
     */
    @Select("select * from alipay_deal_order where createTime between #{starTime} and #{endTime}  limit #{page} , #{size}")
    List<AlipayDealOrderEntity> findOrderLimit(@Param("starTime") String starTime, @Param("endTime") String endTime, @Param("page") Integer page, @Param("size") Integer size);
}
