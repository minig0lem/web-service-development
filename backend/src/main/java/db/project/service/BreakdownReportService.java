package db.project.service;

import db.project.dto.*;
import db.project.exceptions.BreakdownReportException;
import db.project.exceptions.ErrorCode;
import db.project.repository.BreakdownReportRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class BreakdownReportService {
    private final BreakdownReportRepository breakdownReportRepository;

    public BreakdownReportService(BreakdownReportRepository breakdownReportRepository) {
        this.breakdownReportRepository = breakdownReportRepository;
    }

    @Transactional
    public String report(PostBreakdownReportDto postBreakdownReportDto){
        String user_id = SecurityContextHolder.getContext().getAuthentication().getName();

        breakdownReportRepository.insertReport(postBreakdownReportDto, user_id)
                .orElseThrow(() -> new BreakdownReportException("FAILURE TO RECEIVE FAULT REPORT", ErrorCode.FAIL_REPORT));

        breakdownReportRepository.updateBikeStatus(postBreakdownReportDto.getBike_id())
                .orElseThrow(() -> new BreakdownReportException("BIKE STATUS UPDATE FAILED", ErrorCode.FAIL_REPORT));

        return "고장신고가 접수되었습니다.";
    }

    @Transactional
    public BreakdownReportListResponseDto reportList(Optional<Integer> page) {
        int reportPage;
        if(page.isEmpty()) {
            reportPage = 0;
        } else {
            reportPage = (page.get() - 1) * 10;
        }

        Optional<List<ReturnGetBreakdownReportListDto>> reportListOptional = breakdownReportRepository.reportList(reportPage);
        if(reportListOptional.isEmpty()) {
            throw new BreakdownReportException("page not found", ErrorCode.NOT_FOUND_PAGE);
        }

        int reportCount = breakdownReportRepository.getReportCount();

        if(reportPage != 0 && reportCount <= reportPage) {
            throw new BreakdownReportException("page not found", ErrorCode.NOT_FOUND_PAGE);
        }

        List<ReturnGetBreakdownReportListDto> reportList = reportListOptional.get();
        BreakdownReportListResponseDto response = new BreakdownReportListResponseDto(reportCount);
        for (ReturnGetBreakdownReportListDto report : reportList) {
            response.getReportList().add(report);
        }
        return response;
    }

    public void reportRepair(PostBreakdownReportRepairDto postBreakdownReportRepairDto) {
        breakdownReportRepository.updateReportStatus(postBreakdownReportRepairDto);
    }
}
