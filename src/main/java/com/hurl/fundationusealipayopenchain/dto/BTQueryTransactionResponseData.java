package com.hurl.fundationusealipayopenchain.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BTQueryTransactionResponseData {
    private Integer blockNumber;
    private Integer gasUsed;
    private String hash;
    private TransactionDO transactionDO;

    @Getter @Setter
    public static class TransactionDO {
        private String data;
        private long timestamp;
    }
}
