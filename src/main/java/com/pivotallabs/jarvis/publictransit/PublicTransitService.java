package com.pivotallabs.jarvis.publictransit;

import com.pivotallabs.jarvis.publictransit.cta.CtaTimeTableEntity;

public interface PublicTransitService {
    CtaTimeTableEntity loadPanelData();
}
