package com.hurl.fundationusealipayopenchain.service;

import com.hurl.fundationusealipayopenchain.business.BlockchainClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class BlockchainService {

    @Autowired
    private BlockchainClient blockchainClient;

    public String deposit(String content) throws Exception {
        return blockchainClient.deposit(content);
    }

    public String queryTransaction(String hash) {
        return blockchainClient.queryTransaction(hash);
    }

}
