package edu.ssafy.punpun.service;

import edu.ssafy.punpun.dto.ApproveState;
import edu.ssafy.punpun.dto.response.FavoriteMenuDTO;
import edu.ssafy.punpun.entity.*;
import edu.ssafy.punpun.entity.enumurate.UserRole;
import edu.ssafy.punpun.exception.NotStoreOwnerException;
import edu.ssafy.punpun.repository.FavoriteMenuRepository;
import edu.ssafy.punpun.repository.MenuRepository;
import edu.ssafy.punpun.repository.StoreRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("가게 서비스 테스트")
public class StoreServiceImplTest {
    @Mock
    private StoreRepository storeRepository;
    @Mock
    private MenuRepository menuRepository;
    @Mock
    private FavoriteMenuRepository favoriteMenuRepository;

    @InjectMocks
    private StoreServiceImpl storeService;

    @Test
    @DisplayName("가게 상세 정보 보기")
    void findById() {
        // given
        Image image1 = Image.builder()
                .name("가게 1 이미지")
                .url("https://www.hsd.co.kr/assets/images/brand/brand_img_02.jpg")
                .build();
        Store store1 = Store.builder()
                .name("store1")
                .openState(true)
                .info("가게 1 테스트용")
                .image(image1)
                .openTime("24시 운영")
                .address("상북도 구미시 옥계북로 27, 삼구트리니엔 108동 1층 108호 (옥계동)")
                .lon(128.41848477014165)
                .lat(36.13917919014956)
                .alwaysShare(true)
                .build();
//        storeRepository.save(store1);

        doReturn(Optional.of(store1)).when(storeRepository).findById(0L);

        // when
        Store result = storeService.findById(0L);

        // then
        Assertions.assertThat(result.getId()).isEqualTo(store1.getId());
        Assertions.assertThat(result.getName()).isEqualTo(store1.getName());
        Assertions.assertThat(result.getOpenTime()).isEqualTo(store1.getOpenTime());
        Assertions.assertThat(result.getInfo()).isEqualTo(store1.getInfo());
        Assertions.assertThat(result.getAddress()).isEqualTo(store1.getAddress());
        Assertions.assertThat(result.getLon()).isEqualTo(store1.getLon());
        Assertions.assertThat(result.getLat()).isEqualTo(store1.getLat());
        Assertions.assertThat(result.getImage()).isEqualTo(store1.getImage());
        Assertions.assertThat(result.isAlwaysShare()).isEqualTo(store1.isAlwaysShare());
        Assertions.assertThat(result.isOpenState()).isEqualTo(store1.isOpenState());
    }

    @Test
    @DisplayName("가게 상세 정보 보기 - 아동")
    void getStoreDetailChild() {
        // given
        Child child = Child.builder().build();
        Store store = Store.builder()
                .id(1L)
                .name("store1")
                .build();
        Menu menu = Menu.builder()
                .id(1L)
                .name("menu1")
                .price(10000L)
                .store(store)
                .build();
        FavoriteMenu favoriteMenu = FavoriteMenu.builder()
                .child(child)
                .menu(menu)
                .build();

        doReturn(List.of(menu)).when(menuRepository).findByStore(store);
        doReturn(Optional.of(favoriteMenu)).when(favoriteMenuRepository).findByChildAndMenu(child, menu);

        // when
        List<FavoriteMenuDTO> results = storeService.getStoreDetailChild(store, child);

        // then
        Assertions.assertThat(results.get(0).getMenuId()).isEqualTo(menu.getId());
        Assertions.assertThat(results.get(0).getMenuPrice()).isEqualTo(menu.getPrice());
        Assertions.assertThat(results.get(0).getMenuName()).isEqualTo(menu.getName());
    }

    @Test
    @DisplayName("검색어(가게 이름)가 포함된 가게 찾기")
    void findByNameContaining() {
        // given
        Store store1 = Store.builder()
                .name("store1")
                .openState(true)
                .info("가게 1 테스트용")
                .openTime("24시 운영")
                .address("상북도 구미시 옥계북로 27, 삼구트리니엔 108동 1층 108호 (옥계동)")
                .lon(128.41848477014165)
                .lat(36.13917919014956)
                .alwaysShare(true)
                .build();
        Store store2 = Store.builder()
                .name("store2")
                .openState(true)
                .info("가게 2 테스트용")
                .openTime("24시 운영")
                .address("경북 구미시 인동중앙로1길 12")
                .lon(128.419606)
                .lat(36.107291)
                .alwaysShare(true)
                .build();

        doReturn(List.of(store1, store2)).when(storeRepository).findByNameContaining("store");

        // when
        List<Store> results = storeService.findByNameContaining("store");
        // then 1
        Assertions.assertThat(results.get(0).getId()).isEqualTo(store1.getId());
        Assertions.assertThat(results.get(0).getName()).isEqualTo(store1.getName());
        Assertions.assertThat(results.get(0).getOpenTime()).isEqualTo(store1.getOpenTime());
        Assertions.assertThat(results.get(0).getInfo()).isEqualTo(store1.getInfo());
        Assertions.assertThat(results.get(0).getAddress()).isEqualTo(store1.getAddress());
        Assertions.assertThat(results.get(0).getLon()).isEqualTo(store1.getLon());
        Assertions.assertThat(results.get(0).getLat()).isEqualTo(store1.getLat());
        Assertions.assertThat(results.get(0).getImage()).isEqualTo(store1.getImage());
        Assertions.assertThat(results.get(0).isAlwaysShare()).isEqualTo(store1.isAlwaysShare());
        Assertions.assertThat(results.get(0).isOpenState()).isEqualTo(store1.isOpenState());
        // then 2
        Assertions.assertThat(results.get(1).getId()).isEqualTo(store2.getId());
        Assertions.assertThat(results.get(1).getName()).isEqualTo(store2.getName());
        Assertions.assertThat(results.get(1).getOpenTime()).isEqualTo(store2.getOpenTime());
        Assertions.assertThat(results.get(1).getInfo()).isEqualTo(store2.getInfo());
        Assertions.assertThat(results.get(1).getAddress()).isEqualTo(store2.getAddress());
        Assertions.assertThat(results.get(1).getLon()).isEqualTo(store2.getLon());
        Assertions.assertThat(results.get(1).getLat()).isEqualTo(store2.getLat());
        Assertions.assertThat(results.get(1).getImage()).isEqualTo(store2.getImage());
        Assertions.assertThat(results.get(1).isAlwaysShare()).isEqualTo(store2.isAlwaysShare());
        Assertions.assertThat(results.get(1).isOpenState()).isEqualTo(store2.isOpenState());
    }

    @Test
    @DisplayName("사장님 기준으로 가게 찾기")
    void findByOwner() {
        // given
        Member member = Member.builder()
                .name("memberTest")
                .email("memberTest@email.com")
                .phoneNumber("01000000000")
                .role(UserRole.OWNER)
                .build();
        Store store1 = Store.builder()
                .name("store1")
                .openState(true)
                .info("가게 1 테스트용")
                .openTime("24시 운영")
                .address("상북도 구미시 옥계북로 27, 삼구트리니엔 108동 1층 108호 (옥계동)")
                .lon(128.41848477014165)
                .lat(36.13917919014956)
                .alwaysShare(true)
                .owner(member)
                .build();
        Store store2 = Store.builder()
                .name("store2")
                .openState(true)
                .info("가게 2 테스트용")
                .openTime("24시 운영")
                .address("경북 구미시 인동중앙로1길 12")
                .lon(128.419606)
                .lat(36.107291)
                .alwaysShare(true)
                .owner(member)
                .build();

        doReturn(List.of(store1, store2)).when(storeRepository).findByOwner(member);

        // when
        List<Store> results = storeService.findByOwner(member);
        // then 1
        Assertions.assertThat(results.get(0).getId()).isEqualTo(store1.getId());
        Assertions.assertThat(results.get(0).getName()).isEqualTo(store1.getName());
        Assertions.assertThat(results.get(0).getOpenTime()).isEqualTo(store1.getOpenTime());
        Assertions.assertThat(results.get(0).getInfo()).isEqualTo(store1.getInfo());
        Assertions.assertThat(results.get(0).getAddress()).isEqualTo(store1.getAddress());
        Assertions.assertThat(results.get(0).getLon()).isEqualTo(store1.getLon());
        Assertions.assertThat(results.get(0).getLat()).isEqualTo(store1.getLat());
        Assertions.assertThat(results.get(0).getImage()).isEqualTo(store1.getImage());
        Assertions.assertThat(results.get(0).isAlwaysShare()).isEqualTo(store1.isAlwaysShare());
        Assertions.assertThat(results.get(0).isOpenState()).isEqualTo(store1.isOpenState());
        // then 2
        Assertions.assertThat(results.get(1).getId()).isEqualTo(store2.getId());
        Assertions.assertThat(results.get(1).getName()).isEqualTo(store2.getName());
        Assertions.assertThat(results.get(1).getOpenTime()).isEqualTo(store2.getOpenTime());
        Assertions.assertThat(results.get(1).getInfo()).isEqualTo(store2.getInfo());
        Assertions.assertThat(results.get(1).getAddress()).isEqualTo(store2.getAddress());
        Assertions.assertThat(results.get(1).getLon()).isEqualTo(store2.getLon());
        Assertions.assertThat(results.get(1).getLat()).isEqualTo(store2.getLat());
        Assertions.assertThat(results.get(1).getImage()).isEqualTo(store2.getImage());
        Assertions.assertThat(results.get(1).isAlwaysShare()).isEqualTo(store2.isAlwaysShare());
        Assertions.assertThat(results.get(1).isOpenState()).isEqualTo(store2.isOpenState());
    }

    @Nested
    @DisplayName("가게 사장이 가게 삭제 하기 _ 가게 등록 해제")
    public class deleteStoreByMember {
        @Test
        @DisplayName("가게 등록 해제 - 정상 동작")
        void deleteStoreByMember1() {
            // given
            Member member = Member.builder()
                    .name("memberTest")
                    .email("memberTest@email.com")
                    .phoneNumber("01000000000")
                    .role(UserRole.OWNER)
                    .build();
            Store store1 = Store.builder()
                    .id(1L)
                    .name("store1")
                    .openState(true)
                    .info("가게 1 테스트용")
                    .openTime("24시 운영")
                    .address("상북도 구미시 옥계북로 27, 삼구트리니엔 108동 1층 108호 (옥계동)")
                    .lon(128.41848477014165)
                    .lat(36.13917919014956)
                    .alwaysShare(true)
                    .owner(member)
                    .build();

            doReturn(Optional.of(store1)).when(storeRepository).findById(1L);

            // when
            storeService.deleteStoreByMember(member, 1L);
            // then
            Assertions.assertThat(store1.getId()).isEqualTo(1L);
            Assertions.assertThat(store1.getOwner()).isEqualTo(null);
        }

        @Test
        @DisplayName("가게 등록 해제 - 가게가 없는 경우")
        void deleteStoreByMember2() {
            // given
            Member member = Member.builder()
                    .name("memberTest")
                    .role(UserRole.OWNER)
                    .build();
            Store store1 = Store.builder()
                    .id(1L)
                    .name("store1")
                    .owner(member)
                    .build();

            doReturn(Optional.empty()).when(storeRepository).findById(2L);

            // when
            // then
            assertThatThrownBy(() -> storeService.deleteStoreByMember(member, 2L))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("가게 등록 해제 - 가게에 주인이 등록되어 있지 않은 경우")
        void deleteStoreByMember3() {
            // given
            Member member1 = Member.builder()
                    .name("memberTest")
                    .role(UserRole.OWNER)
                    .build();
            Store store1 = Store.builder()
                    .id(1L)
                    .name("store1")
                    .build();

            doReturn(Optional.of(store1)).when(storeRepository).findById(1L);

            // when
            // then
            assertThatThrownBy(() -> storeService.deleteStoreByMember(member1, 1L))
                    .isInstanceOf(NotStoreOwnerException.class);
        }

        @Test
        @DisplayName("가게 등록 해제 - 가게 주인이 아닌 경우")
        void deleteStoreByMember4() {
            // given
            Member member1 = Member.builder()
                    .id(1L)
                    .name("memberTest")
                    .role(UserRole.OWNER)
                    .build();
            Member member2 = Member.builder()
                    .id(2L)
                    .name("memberTest")
                    .role(UserRole.OWNER)
                    .build();
            Store store1 = Store.builder()
                    .id(1L)
                    .name("store1")
                    .owner(member2)
                    .build();

            doReturn(Optional.of(store1)).when(storeRepository).findById(eq(1L));

            // when
            // then
            assertThatCode(() -> storeService.deleteStoreByMember(member1, 1L))
                    .isInstanceOf(NotStoreOwnerException.class);
        }
    }
}
