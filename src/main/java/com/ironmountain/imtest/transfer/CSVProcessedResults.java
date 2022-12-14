package com.ironmountain.imtest.transfer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Data
@Builder
public class CSVProcessedResults {
    long toSave;
    long omitted;
}
