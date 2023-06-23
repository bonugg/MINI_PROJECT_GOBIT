package com.gobit.minipj_gobit.repository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface DatabaseChangeRepository {
    Optional<LocalDateTime> checkForDatabaseChange();
}