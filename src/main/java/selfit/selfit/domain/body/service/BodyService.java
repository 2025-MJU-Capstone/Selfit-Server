package selfit.selfit.domain.body.service;

import org.springframework.stereotype.Service;
import selfit.selfit.domain.body.dto.BodySizeDto;
import selfit.selfit.domain.body.entity.Body;
import selfit.selfit.domain.user.entity.User;

@Service
public interface BodyService {
    Body saveSize(Long userId, BodySizeDto bodySizeDto);
}
