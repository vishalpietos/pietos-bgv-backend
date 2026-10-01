package com.pietos.bgv.dto.request.cases;

import com.pietos.bgv.enums.ComponentSubStatus;
import com.pietos.bgv.enums.DispositionStatus;

public class CaseProcessingStatusUpdateRequest {

    private ComponentSubStatus status;
    
    private DispositionStatus dispositionStatus;

    private String activity;

    public ComponentSubStatus getStatus() {
        return status;
    }

    public void setStatus(ComponentSubStatus status) {
        this.status = status;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

	public DispositionStatus getDispositionStatus() {
		return dispositionStatus;
	}

	public void setDispositionStatus(DispositionStatus dispositionStatus) {
		this.dispositionStatus = dispositionStatus;
	}
    
    
}