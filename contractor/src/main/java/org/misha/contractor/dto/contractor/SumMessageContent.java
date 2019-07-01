package org.misha.contractor.dto.contractor;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SumMessageContent {
    private int sum;
    private int left;
    private int right;
}
