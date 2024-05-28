package com.patternknife.pxbsample.domain.exceldbimpl.cache;


import com.patternknife.pxb.domain.exceldbprocessor.cache.IExcelDBReadInMemoryData;
import com.patternknife.pxbsample.domain.clinic.dto.ClinicResDTO;
import com.patternknife.pxbsample.domain.customer.dto.CustomerResDTO;

import java.util.ArrayList;
import java.util.List;

/*
*   "대기 중인 작업"
* */
public class ExcelDBReadInMemoryData implements IExcelDBReadInMemoryData {


    private List<CustomerResDTO.OneWithCountsWithAdmin> customerWithCountsWithAdmins;
    private List<ClinicResDTO.OneDetails> clinicDetails;


    public static ExcelDBReadInMemoryData of() {
        return new ExcelDBReadInMemoryData();
    }

    private ExcelDBReadInMemoryData() {
        this.customerWithCountsWithAdmins = new ArrayList<>();
        this.clinicDetails = new ArrayList<>();
    }

    public void addOneWithCountsWithAdmins(List<CustomerResDTO.OneWithCountsWithAdmin> oneWithCountsWithAdmins) {
        this.customerWithCountsWithAdmins.addAll(oneWithCountsWithAdmins);
    }


    public void addClinicDetails(List<ClinicResDTO.OneDetails> oneDetails) {
        this.clinicDetails.addAll(oneDetails);
    }



    public List<CustomerResDTO.OneWithCountsWithAdmin> getCustomerWithCountsWithAdmins() {
        return customerWithCountsWithAdmins;
    }

    public List<ClinicResDTO.OneDetails> getClinicDetails() {
        return clinicDetails;
    }


    // 초기화 메서드 추가
    public Boolean hasDataForId(Long excelGroupTaskId) {
        if(excelGroupTaskId.equals(50L)){
            return !customerWithCountsWithAdmins.isEmpty();
        }else if (excelGroupTaskId.equals(55L)) {
            return !clinicDetails.isEmpty();
        }else{
            throw new IllegalStateException("hasDataForId Not valid ID :: " + excelGroupTaskId);
        }
    }

    public void clearDataForId(Long excelGroupTaskId) {
        if(excelGroupTaskId.equals(50L)){
            this.customerWithCountsWithAdmins.clear();
        }else if (excelGroupTaskId.equals(55L)) {
            this.clinicDetails.clear();
        } else{
            throw new IllegalStateException("clearDataForId Not valid ID :: " + excelGroupTaskId);
        }
    }
}
