package selfit.selfit.domain.body.service.Impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import selfit.selfit.domain.body.dto.BodySizeDto;
import selfit.selfit.domain.body.entity.Body;
import selfit.selfit.domain.body.repository.BodyRepository;
import selfit.selfit.domain.body.service.BodyService;
import selfit.selfit.domain.user.entity.User;
import selfit.selfit.domain.user.repository.UserRepository;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class BodyServiceImpl implements BodyService {

    private final BodyRepository bodyRepository;
    private final UserRepository userRepository;

    /**
     *  신체 정보 저장
     */
    @Override
    public Body saveSize(Long userId, BodySizeDto bodySizeDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다."));

        Body body = bodyRepository.findByUser(user)
                .map(existing -> {
                    existing.setHeight(bodySizeDto.getHeight());
                    existing.setWeight(bodySizeDto.getWeight());
                    existing.setWaist(bodySizeDto.getWaist());
                    return existing;
                })
                .orElseGet(() ->{
                        Body b = Body.builder()
                                .user(user)
                                .height(bodySizeDto.getHeight())
                                .weight(bodySizeDto.getWeight())
                                .waist(bodySizeDto.getWaist())
                                .build();
                        return b;
                });

        return bodyRepository.save(body);
    }

}
