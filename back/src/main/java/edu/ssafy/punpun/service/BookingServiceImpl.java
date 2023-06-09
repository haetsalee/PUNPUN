package edu.ssafy.punpun.service;

import edu.ssafy.punpun.dto.ApproveState;
import edu.ssafy.punpun.dto.BookingStoreSearchParamDTO;
import edu.ssafy.punpun.entity.enumurate.SupportType;
import edu.ssafy.punpun.event.EventType;
import edu.ssafy.punpun.entity.*;
import edu.ssafy.punpun.entity.enumurate.ReservationState;
import edu.ssafy.punpun.entity.enumurate.SupportReservationState;
import edu.ssafy.punpun.entity.enumurate.SupportState;
import edu.ssafy.punpun.exception.AlreadyEndException;
import edu.ssafy.punpun.exception.NotStoreOwnerException;
import edu.ssafy.punpun.kafka.ReservationEventPublisher;
import edu.ssafy.punpun.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final SupportReservationRepository supportReservationRepository;
    private final ReservationRepository reservationRepository;
    private final SupportRepository supportRepository;
    private final MenuRepository menuRepository;
    private final ReservationEventPublisher publisher;
    private final MemberRepository memberRepository;

    @Override
    public Reservation reservation(Child child, Long menuId, LocalDateTime reservationTime) {
        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new IllegalArgumentException("없는 메뉴입니다."));
        List<Support> supports = supportRepository.findAllByMenu(menu);
        //SHARE 먼저 검색
        Support support = supports.stream()
                .filter(each -> each.getSupportType() == SupportType.SHARE)
                .filter(each -> each.getSupportState() == SupportState.SUPPORT)
                .findFirst()
                .orElseGet(() -> supports.stream()
                        .filter(each -> each.getSupportState() == SupportState.SUPPORT)
                        .findFirst()
                        .orElseThrow(() -> new AlreadyEndException("이미 모두 예약되었습니다.")));

        Reservation reservation = Reservation.builder()
                .reservationTime(reservationTime)
                .state(ReservationState.BOOKING)
                .child(child)
                .menu(support.getMenu())
                .build();

        reservationRepository.save(reservation);

        SupportReservation supportReservation = SupportReservation.builder()
                .reservation(reservation)
                .support(support)
                .state(SupportReservationState.BOOKING)
                .build();
        supportReservationRepository.save(supportReservation);
        reservation.setSupportReservation(supportReservation);

        publisher.publish(reservation, EventType.RESERVATION);
        return reservation;
    }

    @Override
    public Page<Reservation> findReservations(Child child, LocalDateTime localDateTime, int page) {
        return reservationRepository.findAllByDate(child, localDateTime, page);
    }

    @Override
    public Page<Reservation> findAllByStore(Member owner, BookingStoreSearchParamDTO params) {
        memberRepository.findById(owner.getId())
                //TODO : 로딩을 위한 검색인데 필요한가?
                .orElseThrow(() -> new IllegalArgumentException("없는 유저입니다."))
                .getStores().stream()
                .filter(store -> store.getId().equals(params.getStoreId()))
                .findFirst()
                .orElseThrow(() -> new NotStoreOwnerException("가게의 주인이 아닙니다."));
        return reservationRepository.findAllByStore(params);
    }

    @Override
    public void reservationApprove(Long reservationId, Member owner, ApproveState state) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("없는 예약 번호입니다."));

        if (!reservation.getMenu().getStore().getOwner().getId().equals(owner.getId())) {
            throw new NotStoreOwnerException("가게의 주인이 아닙니다.");
        }

        if (state == ApproveState.OK) {
            reservation.changeState(ReservationState.END);
            reservation.getMenu().reservationApprove();
        } else if (state == ApproveState.NO) {
            reservation.changeState(ReservationState.CANCEL);
        }

        publisher.publish(reservation, EventType.APPROVE);
    }
}
