package edu.ssafy.punpun.service;

import edu.ssafy.punpun.dto.response.ShareResponseDTO;
import edu.ssafy.punpun.entity.Member;
import edu.ssafy.punpun.entity.Menu;
import edu.ssafy.punpun.entity.Support;
import edu.ssafy.punpun.entity.enumurate.SupportType;
import edu.ssafy.punpun.exception.PointLackException;
import edu.ssafy.punpun.repository.MemberRepository;
import edu.ssafy.punpun.repository.MenuRepository;
import edu.ssafy.punpun.repository.SupportRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class SupportServiceImpl implements SupportService{
    private final SupportRepository supportRepository;
    private final MemberRepository memberRepository;
    private final MenuRepository menuRepository;

    private final MenuService menuService;

    @Override
    public List<Support> findSupport(Member supporter) {
        return supportRepository.findBySupporter(supporter);
    }

    @Override
    public void saveSupport(List<Support> supportList, List<Long> menuId , List<Long> menuCount, Member member, Long usePoint) {
        // supporter use point
        Member supporter=memberRepository.findById(member.getId())
                .orElseThrow(IllegalArgumentException::new);

        if(supporter.getRemainPoint() < usePoint){
            throw new PointLackException("포인트가 부족합니다.");
        }
        supporter.support(usePoint);

        for(int i=0; i<supportList.size(); i++){
            // add menu sponsored count
            Menu menu=menuRepository.findById(menuId.get(i))
                    .orElseThrow(()->new IllegalArgumentException("없는 메뉴 입니다."));
            supportList.get(i).setMenu(menu);
            supportList.get(i).setStore(menu.getStore());

            menuService.addSponsoredCount(menuId.get(i), menuCount.get(i));
            // save support table
            for(int j=0; j<menuCount.get(i); j++) {
                supportRepository.save(supportList.get(i));
            }
        }
    }

    @Override
    public Page<ShareResponseDTO> findShareList(Long storeId, SupportType supportType, int page, LocalDate date) {
        return supportRepository.findShareList(storeId, supportType, page , date );
    }
}
