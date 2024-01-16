package ru.notivent.service;

import lombok.RequiredArgsConstructor;
import lombok.experimental.Delegate;
import org.springframework.stereotype.Service;
import ru.notivent.dao.GradeLogDao;

@Service
@RequiredArgsConstructor
public class GradeLogService {

    @Delegate
    final GradeLogDao gradeLogDao;
}
