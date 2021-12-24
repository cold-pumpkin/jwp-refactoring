package kitchenpos.application.tablegroup;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.OrderTables;
import kitchenpos.domain.tablegroup.TableGroup;
import kitchenpos.domain.tablegroup.TableGroupRepository;
import kitchenpos.dto.table.OrderTableDto;
import kitchenpos.dto.tablegroup.TableGroupDto;
import kitchenpos.event.tablegroup.GroupingOrderTableEvent;
import kitchenpos.event.tablegroup.UngroupOrderTableEvent;

@ExtendWith(MockitoExtension.class)
public class TableGroupServiceTest {
    @Mock
    private TableGroupRepository tableGroupRepository;
    
    @Mock
    private TableGroupValidator tableGroupValidator;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @InjectMocks
    private TableGroupService tableGroupService;

    @DisplayName("단체지정이 저장된다.")
    @Test
    void create_tableGroup() {
        // given
        OrderTable 치킨_주문_단체테이블 = OrderTable.of(0, true);
        OrderTable 치킨2_주문_단체테이블 = OrderTable.of(0, true);

        List<OrderTable> 조회된_주문테이블_리스트 = List.of(치킨_주문_단체테이블, 치킨2_주문_단체테이블);

        when(tableGroupValidator.getValidatedOrderTables(any(TableGroupDto.class))).thenReturn(OrderTables.of(조회된_주문테이블_리스트));
        when(tableGroupRepository.save(any(TableGroup.class))).thenReturn(TableGroup.of());

        TableGroupDto 단체지정_요청전문 = TableGroupDto.of(List.of(OrderTableDto.of(치킨_주문_단체테이블), OrderTableDto.of(치킨2_주문_단체테이블)));

        // when
        tableGroupService.create(단체지정_요청전문);

        // then
        verify(eventPublisher).publishEvent(any(GroupingOrderTableEvent.class));
    }

    @DisplayName("단체지정이 해제된다.")
    @Test
    void update_tableUnGroup() {
        // given
        OrderTable 치킨_주문_단체테이블 = OrderTable.of(10, false);
        OrderTable 치킨2_주문_단체테이블 = OrderTable.of(10, false);

        치킨_주문_단체테이블.changeEmpty(true);
        치킨2_주문_단체테이블.changeEmpty(true);

        TableGroup 단체주문테이블 = TableGroup.of();

        when(tableGroupValidator.getComplateOrderTable(nullable(Long.class))).thenReturn(OrderTables.of(Lists.newArrayList(치킨_주문_단체테이블, 치킨2_주문_단체테이블)));
        
        // when
        tableGroupService.ungroup(단체주문테이블.getId());

        // then
        verify(eventPublisher).publishEvent(any(UngroupOrderTableEvent.class));
    }
}