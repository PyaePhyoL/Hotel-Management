package org.bytesync.hotelmanagement.audit;

import lombok.RequiredArgsConstructor;
import org.bytesync.hotelmanagement.repository.StaffRepository;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component("auditorAware")
@RequiredArgsConstructor
public class AuditAwareImpl implements AuditorAware<String> {

    private final StaffRepository staffRepository;

    @Override
    public Optional<String> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication != null && authentication.getPrincipal() != null) {
            var name = staffRepository.findUsernameByEmail(authentication.getPrincipal().toString()).orElse("System");
            return Optional.of(name);
        }
        return Optional.of("System");
    }
}
